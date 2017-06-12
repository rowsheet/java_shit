package com.PublicBrewery.Food;

import com.Common.AbstractModel;
import com.Common.VendorFood;
import com.Common.VendorFoodReview;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;

/**
 * Created by alexanderkleinhans on 6/11/17.
 */
public class FoodModel extends AbstractModel {

    private String loadFoodMenuSQL_stage1 =
            "SELECT" +
                    "   id AS vendor_food_id," +
                    "   vendor_id," +
                    "   name," +
                    "   description," +
                    "   price," +
                    "   food_sizes " +
                    "FROM" +
                    "   vendor_foods " +
                    "WHERE" +
                    "   vendor_id = ?";

    /**
     * Bottom line, we need all the reviews for all the foods for this vendor where they exist.
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
    private String loadFoodMenuSQL_stage2 =
            "SELECT " +
                    "   vfr.id as review_id," +
                    "   v.id as vendor_id," +
                    "   vf.id as food_id," +
                    "   vf.name as food_name," +
                    "   vfr.content," +
                    "   vfr.stars," +
                    "   a.id as account_id," +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', vfr.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   vendor_foods vf " +
                    "LEFT JOIN " +
                    "   vendor_food_reviews vfr " +
                    "ON " +
                    "   vf.id = vfr.vendor_food_id " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   vfr.account_id = a.id " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   v.id = vf.vendor_id " +
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
                    "   vfi.vendor_food_id, " +
                    "   vfi.display_order, " +
                    "   vfi.filename " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_foods vf " +
                    "ON " +
                    "   v.id = vf.vendor_id " +
                    "LEFT JOIN " +
                    "   vendor_food_images vfi " +
                    "ON " +
                    "   vf.id = vfi.vendor_food_id " +
                    "WHERE " +
                    "   v.id = ? " +
                    "AND " +
                    "   vfi.filename IS NOT NULL";

    public FoodModel() throws Exception {}

    /**
     * Loads all foods + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all foods (as map by id).
     * 2) Load all review for all foods.
     * 3) Load all image urls (has map by display order).
     * 4) Calculate review averages for all the beers.
     *
     * @param brewery_id
     * @return HashMap<beer_id, beer_data_structure>
     * @throws Exception
     */
    public HashMap<Integer, VendorFood> loadFoodMenu(
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        // Prepare the statements.
        stage1 = this.DAO.prepareStatement(this.loadFoodMenuSQL_stage1);
        stage2 = this.DAO.prepareStatement(this.loadFoodMenuSQL_stage2);
        stage3 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage3);
        /*
        Stage 1
         */
        stage1.setInt(1, brewery_id);
        ResultSet stage1Result = stage1.executeQuery();
        HashMap<Integer, VendorFood> vendorFoodHashMap = new HashMap<Integer, VendorFood>();
        while (stage1Result.next()) {
            VendorFood vendorFood = new VendorFood();
            vendorFood.vendor_id = stage1Result.getInt("vendor_id");
            vendorFood.vendor_food_id = stage1Result.getInt("vendor_food_id");
            vendorFood.name = stage1Result.getString("name");
            vendorFood.description = stage1Result.getString("description");
            vendorFood.price = stage1Result.getFloat("price");
            Array food_sizes_array = stage1Result.getArray("food_sizes");
            String[] str_food_sizes = (String[]) food_sizes_array.getArray();
            vendorFood.food_sizes = str_food_sizes;
            vendorFoodHashMap.put(vendorFood.vendor_food_id, vendorFood);
        }
        /*
        Stage 2
         */
        stage2.setInt(1, brewery_id);
        ResultSet stage2Result = stage2.executeQuery();
        while (stage2Result.next()) {
            VendorFoodReview vendorFoodReview = new VendorFoodReview();
            vendorFoodReview.review_id = stage2Result.getInt("review_id");
            vendorFoodReview.account_id = stage2Result.getInt("account_id");
            vendorFoodReview.vendor_food_id = stage2Result.getInt("food_id");
            vendorFoodReview.stars = stage2Result.getInt("stars");
            vendorFoodReview.content = stage2Result.getString("content");
            vendorFoodReview.days_ago = stage2Result.getInt("days_ago");
            vendorFoodReview.username = stage2Result.getString("username");
            // Add the beer review to the appropriate beer.
            vendorFoodHashMap.get(vendorFoodReview.vendor_food_id).reviews.add(vendorFoodReview);
        }
        /*
        Stage 3
         */
        stage3.setInt(1, brewery_id);
        ResultSet stage3Result = stage3.executeQuery();
        while (stage3Result.next()) {
            int vendor_food_id = stage3Result.getInt("vendor_food_id");
            int display_order = stage3Result.getInt("display_order");
            String filename = stage3Result.getString("filename");
            vendorFoodHashMap.get(vendor_food_id).images.put(display_order, filename);
        }
        /*
        Stage 4
         */
        // Go through each food and calcualte the review star averages.
        for (VendorFood vendorFood : vendorFoodHashMap.values()) {
            if (vendorFood.reviews.size() > 0) {
                float total = 0;
                for (VendorFoodReview vendorFoodReview : vendorFood.reviews) {
                    total += (float) vendorFoodReview.stars;
                }
                vendorFood.review_average = total / (float) vendorFood.reviews.size();
                vendorFood.review_count = vendorFood.reviews.size();
            }
        }
        return vendorFoodHashMap;
    }
}
