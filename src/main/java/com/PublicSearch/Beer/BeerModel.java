package com.PublicSearch.Beer;

import com.Common.AbstractModel;
import com.Common.BeerSearchResult;
import com.Common.BeerReview;
import com.Common.BeerImage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
public class BeerModel extends AbstractModel {

    public BeerModel() throws Exception {}

    private String searchBeersSQL_stage1 =
        "SELECT " +
                "   b.id as beer_id, " +
                "   b.vendor_id, " +
                "   b.name, " +
                "   b.color, " +
                "   b.bitterness, " +
                "   b.abv, " +
                "   b.beer_style, " +
                "   b.beer_tastes, " +
                "   b.description, " +
                "   b.price, " +
                "   b.beer_sizes, " +
                "   b.hop_score, " +
                "   vi.display_name AS vendor_display_name, " +
                "   vi.public_phone, " +
                "   vi.public_email, " +
                "   v.street_address, " +
                "   v.city, " +
                "   v.state, " +
                "   v.zip, " +
                // Calcualte how many days ago the beer was posted so
                // we can put a "new" tag on it if we want.
                "   DATE_PART('day', now()::date) - DATE_PART('day', b.creation_timestamp::date) as days_ago " +
                "FROM " +
                "   beers b " +
                "LEFT JOIN " +
                "   vendors v " +
                "ON " +
                "   b.vendor_id = v.id " +
                "LEFT JOIN " +
                "   vendor_info vi " +
                "ON " +
                "   vi.vendor_id = v.id " +
                "WHERE " +
                "   b.color >= ? AND " +
                "   b.color <= ? AND " +
                "   b.bitterness >= ? AND " +
                "   b.bitterness <= ? AND " +
                "   b.abv >= ? AND " +
                "   b.abv <= ? " +
                "<%beer_style_clause%> " +
                "<%beer_taste_clause%> " +
                "LIMIT ? OFFSET ?";

    /*
    Note:
        fetching of reviews and images copy-pasted from PublicBrewery beer model.
        sorry! I was short on time!
     */

    private String searchBeersSQL_stage2 =
            "SELECT " +
                    "   br.id as review_id, " +
                    "   br.account_id, " +
                    "   br.beer_id, " +
                    "   br.stars, " +
                    "   br.content, " +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', br.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   beer_reviews br " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   br.account_id = a.id " +
                    "WHERE " +
                    "   br.beer_id IN (";

    private String searchBeersSQL_stage3 =
            "SELECT " +
                    "   beer_id, " +
                    "   display_order, " +
                    "   filename " +
                    "FROM " +
                    "   beer_images " +
                    "WHERE " +
                    "   beer_id in (";

    /**
     * First get the beer_ids with a limit offset, then take IDs and fetch
     * coresponding info with join such as reviews, and vendor info, then
     * calculate review information.
     *
     *      1) Get all beer_ids matching info with limit offset.
     *      2) Get all reviews. (note: copy-pasted from PublicBrewery)
     *      3) Get all images. (note: copy-pasted from PublicBrewery)
     *      4) Calculate review averages. (note: copy-pasted from PublicBrewery)
     *
     * For color, bitterness, and abv ranges, they are all AND conditions.
     *
     * For style and taste conditions, they are OR conditions for each unless
     * the style or taste "Any" is in their (which is set in the controller). In
     * this case, this extra where clause is not appended.
     *
     * The reason to fetch all beers first is because it will give us a lighter
     * query for when we do the joins.
     *
     * @param min_color
     * @param max_color
     * @param min_bitterness
     * @param max_bitterness
     * @param min_abv
     * @param max_abv
     * @param styles
     * @param tastes
     * @return
     * @throws Exception
     */
    public HashMap<Integer, BeerSearchResult> searchBeers(
            int min_color,
            int max_color,
            int min_bitterness,
            int max_bitterness,
            int min_abv,
            int max_abv,
            String[] styles,
            String[] tastes,
            int limit,
            int offset
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
            /*
             Stage 1
             Fetch all the beers.
            */
            // We need to build the query where styles and tastes are in the
            // array, and there are two scenarios for each we have to preempt.
            //
            //      1) All styles were selected.
            //      2) No styles were selected.
            //
            // If either all or none were selected, the where clause will not
            // be added because either the user checked "All styles" which means
            // they didn't care or they checked no styles which also means they
            // didn't care.
            //
            // If this is not the case, you need to append the query in the
            // where clause.
            String beer_style_clause = "";
            if (styles.length > 0) {
                if (!styles[0].equals("Any")) {
                    beer_style_clause += " AND b.beer_style IN (";
                    for (int i = 0; i < styles.length; i++) {
                        beer_style_clause += "'" + styles[i] + "'";
                        if (i < (styles.length - 1)) {
                            beer_style_clause += ",";
                        }
                    }
                    beer_style_clause += ")";
                } // If not, don't add any conditions for style because the user picked them all.
            } //If not, don't add any conditions for styles because they weren't specified.
            this.searchBeersSQL_stage1 = this.searchBeersSQL_stage1.replace(
                    "<%beer_style_clause%>",
                    beer_style_clause);
            String beer_taste_clause = "";
            if (tastes.length > 0) {
                if (!tastes[0].equals("Any")) {
                    beer_taste_clause += " AND (";
                    for (int i = 0; i < tastes.length; i++) {
                        beer_taste_clause += "'" + tastes[i] + "' = ANY(b.beer_tastes) ";
                        if (i < (tastes.length - 1)) {
                            beer_taste_clause += " OR ";
                        }
                    }
                    beer_taste_clause += ")";
                } // If not, don't add any conditions for tastes because the user picked them all.
            } // If not, don't add any conditions for tastes because they weren't specified.
            this.searchBeersSQL_stage1 = this.searchBeersSQL_stage1.replace(
                    "<%beer_taste_clause%>",
                    beer_taste_clause);
            stage1 = this.DAO.prepareStatement(this.searchBeersSQL_stage1);
            stage1.setInt(1, min_color);
            stage1.setInt(2, max_color);
            stage1.setInt(3, min_bitterness);
            stage1.setInt(4, max_bitterness);
            stage1.setInt(5, min_abv);
            stage1.setInt(6, max_abv);
            stage1.setInt(7, limit);
            stage1.setInt(8, offset);
            String beer_ids = "";
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                BeerSearchResult beerSearchResult = new BeerSearchResult();
                beerSearchResult.beer_id = stage1Result.getInt("beer_id");
                beerSearchResult.vendor_id = stage1Result.getInt("vendor_id");
                beerSearchResult.name = stage1Result.getString("name");
                beerSearchResult.color = stage1Result.getInt("color");
                beerSearchResult.bitterness = stage1Result.getInt("bitterness");
                beerSearchResult.abv = stage1Result.getInt("abv");
                beerSearchResult.beer_style = stage1Result.getString("beer_style");
                // Beer tastes are an array of enums in postgres.
                Array beer_tastes = stage1Result.getArray("beer_tastes");
                String[] str_beer_tastes = (String[]) beer_tastes.getArray();
                beerSearchResult.beer_tastes = str_beer_tastes;
                beerSearchResult.description = stage1Result.getString("description");
                beerSearchResult.price = stage1Result.getFloat("price");
                // Beer sizes are an array of enums in postgres.
                Array beer_sizes = stage1Result.getArray("beer_sizes");
                String[] str_beer_sizes = (String[]) beer_sizes.getArray();
                beerSearchResult.beer_sizes = str_beer_sizes;
                beerSearchResult.hop_score = stage1Result.getString("hop_score");
                beerSearchResult.days_ago = stage1Result.getInt("days_ago");
                // Get vendor info here too since this doesn't expand to one-to-many from the beer entity.
                beerSearchResult.vendor_display_name = stage1Result.getString("vendor_display_name");
                beerSearchResult.vendor_public_phone = stage1Result.getString("public_phone");
                beerSearchResult.vendor_public_email = stage1Result.getString("public_email");
                beerSearchResult.vendor_street_address = stage1Result.getString("street_address");
                beerSearchResult.vendor_city = stage1Result.getString("city");
                beerSearchResult.vendor_state = stage1Result.getString("state");
                beerSearchResult.vendor_zip = stage1Result.getString("zip");
                beerSearchResultHashMap.put(beerSearchResult.beer_id, beerSearchResult);
                // Create the brewery_ids where clause.
                beer_ids += String.valueOf(beerSearchResult.beer_id) + ",";
            }
            /*
            Note:
                Last three stages are copy-pasted from PublicBrewery beer model.
                sorry! I was short on time!
             */
            // If there are no beers, return the empty hash map, else continue.
            if (beer_ids == "") {
                return beerSearchResultHashMap;
            }
            beer_ids += ")";
            // Remove trailing comma.
            beer_ids = beer_ids.replace(",)", ")");
            /*
            Stage 2
            Get all the beer reviews where beer_ids are in as where clause.
             */
            this.searchBeersSQL_stage2 += beer_ids;
            stage2 = this.DAO.prepareStatement(this.searchBeersSQL_stage2);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                BeerReview beerReview = new BeerReview();
                beerReview.review_id = stage2Result.getInt("review_id");
                beerReview.account_id = stage2Result.getInt("account_id");
                beerReview.beer_id = stage2Result.getInt("beer_id");
                beerReview.stars = stage2Result.getInt("stars");
                beerReview.content = stage2Result.getString("content");
                beerReview.username = stage2Result.getString("username");
                beerReview.days_ago = stage2Result.getInt("days_ago");
                // Add the beer review to the appropriate beer.
                beerSearchResultHashMap.get(beerReview.beer_id).reviews.add(beerReview);
            }
            /*
            Stage 3
            Get all the images where beer_ids in where clause.
             */
            this.searchBeersSQL_stage3 += beer_ids;
            stage3 = this.DAO.prepareStatement(this.searchBeersSQL_stage3);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                BeerImage beerImage = new BeerImage();
                beerImage.beer_id = stage3Result.getInt("beer_id");
                beerImage.display_order = stage3Result.getInt("display_order");
                beerImage.filename = stage3Result.getString("filename");
                beerSearchResultHashMap.get(beerImage.beer_id).images.put(beerImage.display_order, beerImage);
            }
            /*
            Stage 4
             */
            // Go through each beer and calculate the review star averages.
            for (BeerSearchResult beer : beerSearchResultHashMap.values()) {
                if (beer.reviews.size() > 0) {
                    float total = 0;
                    for (BeerReview beerReview : beer.reviews) {
                        total += (float) beerReview.stars;
                    }
                    beer.review_average = total / (float) beer.reviews.size();
                    beer.review_count = beer.reviews.size();
                }
            }
            return beerSearchResultHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (stage3Result != null) {
                stage3Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
