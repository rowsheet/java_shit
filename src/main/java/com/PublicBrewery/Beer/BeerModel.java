package com.PublicBrewery.Beer;

import com.Common.AbstractModel;
import com.Common.Beer;
import com.Common.BeerReview;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
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
    public String loadBeerMenuSQL_stage3 =
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
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        // Prepare the statements.
        stage1 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage1);
        stage2 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage2);
        stage3 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage3);
        /*
        Stage 1
         */
        stage1.setInt(1, brewery_id);
        ResultSet stage1Result = stage1.executeQuery();
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
        ResultSet stage2Result = stage2.executeQuery();
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
        ResultSet stage3Result = stage3.executeQuery();
        while (stage3Result.next()) {
            int beer_id = stage3Result.getInt("beer_id");
            int display_order = stage3Result.getInt("display_order");
            String filename = stage3Result.getString("filename");
            beerHashMap.get(beer_id).images.put(display_order, filename);
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
    }
}

