package com.PublicBrewery.Beer;

import com.Common.AbstractModel;
import com.Common.Beer;
import com.Common.BeerImage;
import com.Common.BeerReview;
import com.sun.org.apache.regexp.internal.RE;
import org.omg.PortableInterceptor.INACTIVE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/10/17.
 */
public class BeerModel extends AbstractModel {

    private String loadBeerMenuSQL_stage1 =
            "SELECT" +
                    "   id AS beer_id," +
                    "   vendor_id," +
                    "   name," +
                    "   color," +
                    "   bitterness," +
                    "   abv," +
                    "   beer_style," +
                    "   beer_tastes," +
                    "   description," +
                    "   price," +
                    "   beer_sizes, " +
                    "   hop_score " +
                    "FROM" +
                    "   beers " +
                    "WHERE" +
                    "   vendor_id = ?";

    /**
     * Bottom line, we need all the reviews for all the beers for this vendor where they exist.
     *
     * The timestamp field we are concerned about is how many days ago the review
     * was posted. I want the most recent reviews first.
     *
     * Because usernames are null by default, users without usersnames will use the
     * convention:
     *
     * "user" + user_id
     *
     * For example, user_id = 31 would create the username "user31".
     */
    private String loadBeerMenuSQL_stage2 =
            "SELECT " +
                    "   br.id as review_id," +
                    "   v.id as vendor_id," +
                    "   b.id as beer_id," +
                    "   b.name as beer_name," +
                    "   br.content," +
                    "   br.stars," +
                    "   a.id as account_id," +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', br.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   beers b " +
                    "LEFT JOIN " +
                    "   beer_reviews br " +
                    "ON " +
                    "   b.id = br.beer_id " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   br.account_id = a.id " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   v.id = b.vendor_id " +
                    "WHERE " +
                    // Specify the vendor_id, this will probably eliminate the largest set (trying to optimize).
                    "   v.id = ? " +
                    "AND " +
                    // If there are no reviews, there will be no account, so ignore that record.
                    "   a.id IS NOT NULL " +
                    "ORDER BY " +
                    // Get the newest reviews first.
                    "   days_ago ASC";


    /**
     * The file paths for each image take advantage of the fact that you can't have two
     * images with the same path, therefore, the unique constraints in the DB will give you
     * paths that are unique and the worst case scenario is that an image is not found.
     *
     * Paths go like:
     *
     *      /image_resource/vendor_id/item_id/name
     *
     * This is because image_resource will cut out most of the images, vendor_id will cut
     * out the next most.
     *
     * This also allows multiple mages per item and multiple item_ids to exist for vendors.
     *
     */
    private String loadBeerMenuSQL_stage3 =
            "SELECT " +
                    "   bi.beer_id, " +
                    "   bi.display_order, " +
                    "   bi.filename " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   beers b " +
                    "ON " +
                    "   v.id = b.vendor_id " +
                    "LEFT JOIN " +
                    "   beer_images bi " +
                    "ON " +
                    "   b.id = bi.beer_id " +
                    "WHERE " +
                    "   v.id = ? " +
                    "AND " +
                    "   bi.filename IS NOT NULL";

    public BeerModel() throws Exception {
    }

    /**
     * Loads all beers + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all beers (as map by id).
     * 2) Load all review for all beers.
     * 3) Load all image urls (has map by display order).
     * 4) Calculate review averages for all the beers.
     *
     * @param brewery_id
     * @return HashMap<beer_id, beer_data_structure>
     * @throws Exception
     */
    public HashMap<Integer, Beer> loadBeerMenu(
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            // Prepare the statements.
            stage1 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage3);
            /*
            Stage 1
             */
            stage1.setInt(1, brewery_id);
            stage1Result = stage1.executeQuery();
            // Hash map because when we fetch reviews, they need to be added to each respective beer
            // the moment they are pulled from the result set.
            HashMap<Integer, Beer> beerHashMap = new HashMap<Integer, Beer>();
            while (stage1Result.next()) {
                Beer beer = new Beer();
                beer.beer_id = stage1Result.getInt("beer_id");
                beer.vendor_id = stage1Result.getInt("vendor_id");
                beer.name = stage1Result.getString("name");
                beer.color = stage1Result.getInt("color");
                beer.bitterness = stage1Result.getInt("bitterness");
                beer.abv = stage1Result.getInt("abv");
                beer.beer_style = stage1Result.getString("beer_style");
                // Beer tastes are an array of enums in postgres.
                Array beer_tastes = stage1Result.getArray("beer_tastes");
                String[] str_beer_tastes = (String[]) beer_tastes.getArray();
                beer.beer_tastes = str_beer_tastes;
                beer.description = stage1Result.getString("description");
                beer.price = stage1Result.getFloat("price");
                // Beer sizes are an array of enums in postgres.
                Array beer_sizes = stage1Result.getArray("beer_sizes");
                String[] str_beer_sizes = (String[]) beer_sizes.getArray();
                beer.beer_sizes = str_beer_sizes;
                beer.hop_score = stage1Result.getString("hop_score");
                beerHashMap.put(beer.beer_id, beer);
            }
            /*
            Stage 2
             */
            stage2.setInt(1, brewery_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                BeerReview beerReview = new BeerReview();
                beerReview.review_id = stage2Result.getInt("review_id");
                beerReview.account_id = stage2Result.getInt("account_id");
                beerReview.beer_id = stage2Result.getInt("beer_id");
                beerReview.stars = stage2Result.getInt("stars");
                beerReview.content = stage2Result.getString("content");
                beerReview.days_ago = stage2Result.getInt("days_ago");
                beerReview.username = stage2Result.getString("username");
                // Add the beer review to the appropriate beer.
                beerHashMap.get(beerReview.beer_id).reviews.add(beerReview);
            }
            /*
            Stage 3
             */
            stage3.setInt(1, brewery_id);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                BeerImage beerImage = new BeerImage();
                beerImage.beer_id = stage3Result.getInt("beer_id");
                beerImage.display_order = stage3Result.getInt("display_order");
                beerImage.filename = stage3Result.getString("filename");
                beerHashMap.get(beerImage.beer_id).images.put(beerImage.display_order, beerImage);
            }
            /*
            Stage 4
             */
            // Go through each beer and calculate the review star averages.
            for (Beer beer : beerHashMap.values()) {
                if (beer.reviews.size() > 0) {
                    float total = 0;
                    for (BeerReview beerReview : beer.reviews) {
                        total += (float) beerReview.stars;
                    }
                    beer.review_average = total / (float) beer.reviews.size();
                    beer.review_count = beer.reviews.size();
                }
            }
            return beerHashMap;
        } catch (Exception ex) {
            System.out.print(ex);
            throw new Exception("Unable to load beer menu.");
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

    private String loadBeerMenuPaginatedSQL_stage1 =
            "SELECT " +
                    "   id AS beer_id," +
                    "   vendor_id," +
                    "   name," +
                    "   color," +
                    "   bitterness," +
                    "   abv," +
                    "   beer_style," +
                    "   beer_tastes," +
                    "   description," +
                    "   price," +
                    "   beer_sizes, " +
                    "   hop_score " +
                    "FROM" +
                    "   beers " +
                    "WHERE" +
                    "   vendor_id = ? " +
                    "ORDER BY <%order_by%> ASC " +
                    "LIMIT ? " +
                    "OFFSET ?";

    private String loadBeerMenuPaginatedSQL_stage2 =
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

    private String loadBeerMenuPaginatedSQL_stage3 =
            "SELECT " +
                    "   beer_id, " +
                    "   display_order, " +
                    "   filename " +
                    "FROM " +
                    "   beer_images " +
                    "WHERE " +
                    "   beer_id in (";

    /**
     * Returns limit-offset beers for a given brewery_id ordered by a column (of possible options).
     *
     *      1) Fetch beers where vendor_id matches with limit-offset, ordered by column (generating
     *         a list of beer_ids).
     *      2) Fetch all reviews (with beer_id in clause).
     *      3) Fetch all images (with beer_id in clause).
     *      4) Calculate review averages for all the beers.
     *
     * @param brewery_id
     * @param limit
     * @param offset
     * @param order_by
     * @return
     * @throws Exception
     */
    public HashMap<Integer, Beer> loadBeerMenuPaginated(
            int brewery_id,
            int limit,
            int offset,
            String order_by,
            boolean descending
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            /*
            Stage 1
            Get all the beers as specified.
             */
            // Don't worry, this is validated like an enum in the controller
            // so there's no possible SQL injection.
            //
            // This is not typically done in a prepared statement.
            //
            // You should be able to tell this from the API, so this is a good
            // honeypot.
            // @TODO Make this a honeypot.
            this.loadBeerMenuPaginatedSQL_stage1 = this.loadBeerMenuPaginatedSQL_stage1.replace("<%order_by%>", order_by);
            if (descending) {
                this.loadBeerMenuPaginatedSQL_stage1 = this.loadBeerMenuPaginatedSQL_stage1.replace("ASC", "DESC");
            }
            stage1 = this.DAO.prepareStatement(loadBeerMenuPaginatedSQL_stage1);
            stage1.setInt(1, brewery_id);
            stage1.setInt(2, limit);
            stage1.setInt(3, offset);
            HashMap<Integer, Beer> beerHashMap = new HashMap<Integer, Beer>();
            // Create the where clause for the other searches.
            String beer_ids = "";
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                Beer beer = new Beer();
                beer.beer_id = stage1Result.getInt("beer_id");
                beer.vendor_id = stage1Result.getInt("vendor_id");
                beer.name = stage1Result.getString("name");
                beer.color = stage1Result.getInt("color");
                beer.bitterness = stage1Result.getInt("bitterness");
                beer.abv = stage1Result.getInt("abv");
                beer.beer_style = stage1Result.getString("beer_style");
                beer.description = stage1Result.getString("description");
                beer.price = stage1Result.getFloat("price");
                beer.hop_score = stage1Result.getString("hop_score");
                // Beer tastes are an array of enums in postgres.
                Array beer_tastes = stage1Result.getArray("beer_tastes");
                String[] str_beer_tastes = (String[]) beer_tastes.getArray();
                beer.beer_tastes = str_beer_tastes;
                // Beer sizes are an array of enums in postgres.
                Array beer_sizes = stage1Result.getArray("beer_sizes");
                String[] str_beer_sizes = (String[]) beer_sizes.getArray();
                beer.beer_sizes = str_beer_sizes;
                beerHashMap.put(beer.beer_id, beer);
                // Create the brewery_ids where clause.
                beer_ids += String.valueOf(beer.beer_id) + ",";
            }
            // If there are no beers, return the empty hash map, else continue.
            if (beer_ids == "") {
                return beerHashMap;
            }
            beer_ids += ")";
            // Remove trailing comma.
            beer_ids = beer_ids.replace(",)", ")");
            /*
            Stage 2
            Get all the beers where beer_ids are in as where clause.
             */
            this.loadBeerMenuPaginatedSQL_stage2 += beer_ids;
            stage2 = this.DAO.prepareStatement(this.loadBeerMenuPaginatedSQL_stage2);
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
                beerHashMap.get(beerReview.beer_id).reviews.add(beerReview);
            }
            /*
            Stage 3
            Get all the images where beer_ids in where clause.
             */
            this.loadBeerMenuPaginatedSQL_stage3 += beer_ids;
            stage3 = this.DAO.prepareStatement(this.loadBeerMenuPaginatedSQL_stage3);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                BeerImage beerImage = new BeerImage();
                beerImage.beer_id = stage3Result.getInt("beer_id");
                beerImage.display_order = stage3Result.getInt("display_order");
                beerImage.filename = stage3Result.getString("filename");
                beerHashMap.get(beerImage.beer_id).images.put(beerImage.display_order, beerImage);
            }
            /*
            Stage 4
             */
            // Go through each beer and calculate the review star averages.
            for (Beer beer : beerHashMap.values()) {
                if (beer.reviews.size() > 0) {
                    float total = 0;
                    for (BeerReview beerReview : beer.reviews) {
                        total += (float) beerReview.stars;
                    }
                    beer.review_average = total / (float) beer.reviews.size();
                    beer.review_count = beer.reviews.size();
                }
            }
            return beerHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown error.
            throw new Exception("Unable to load beers.");
        } finally {
            // Clean up your shit.
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

