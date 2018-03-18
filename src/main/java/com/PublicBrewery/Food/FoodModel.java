package com.PublicBrewery.Food;

import com.Common.*;
import com.Common.PublicVendor.FoodMenu;

import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;

/**
 * Created by alexanderkleinhans on 6/11/17.
 */
public class FoodModel extends AbstractModel {

    private String loadFoodMenuSQL_stage1 = "SELECT" +
                    "   vf.id AS vendor_food_id," +
                    "   vf.vendor_id AS vendor_food_vendor_id," +
                    "   vf.name AS vendor_food_name," +
                    "   vf.description AS vendor_food_description," +
                    "   vf.price AS vendor_food_price," +
                    "   vf.food_sizes AS vendor_food_food_sizes," +
                    "   vf.id AS id," +
                    "   vf.creation_timestamp AS creation_timestamp," +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vf.creation_timestamp::date)) AS creation_days_ago, " +
                    "   vfc.name AS vendor_food_category_name," +
                    "   vfc.hex_color AS vendor_food_category_hex_color, " +
                    "   vfc.vendor_id AS vendor_food_category_vendor_id, " +
                    "   vfc.id AS vendor_food_category_id, " +
                    "   vfc.description AS vendor_food_category_description, " +
                    "   vft1.id AS tag_one_id, " +
                    "   vft1.name AS tag_one_name, " +
                    "   vft1.hex_color AS tag_one_hex_color, " +
                    "   vft1.tag_type AS tag_one_tag_type, " +
                    "   vft2.id AS tag_two_id, " +
                    "   vft2.name AS tag_two_name, " +
                    "   vft2.hex_color AS tag_two_hex_color, " +
                    "   vft2.tag_type AS tag_two_tag_type, " +
                    "   vft3.id AS tag_three_id, " +
                    "   vft3.name AS tag_three_name, " +
                    "   vft3.hex_color AS tag_three_hex_color, " +
                    "   vft3.tag_type AS tag_three_tag_type, " +
                    "   vft4.id AS tag_four_id, " +
                    "   vft4.name AS tag_four_name, " +
                    "   vft4.hex_color AS tag_four_hex_color, " +
                    "   vft4.tag_type AS tag_four_tag_type, " +
                    "   vft5.id AS tag_five_id, " +
                    "   vft5.name AS tag_five_name, " +
                    "   vft5.hex_color AS tag_five_hex_color, " +
                    "   vft5.tag_type AS tag_five_tag_type, " +
                    "   vnf.id AS nutritional_fact_id, " +
                    "   vnf.serving_size AS nutritional_fact_serving_size, " +
                    "   vnf.calories AS nutritional_fact_calories, " +
                    "   vnf.calories_from_fat  AS nutritional_fact_calories_from_fat, " +
                    "   vnf.total_fat  AS nutritional_fact_total_fat, " +
                    "   vnf.saturated_fat  AS nutritional_fact_saturated_fat, " +
                    "   vnf.trans_fat  AS nutritional_fact_trans_fat, " +
                    "   vnf.cholesterol  AS nutritional_fact_cholesterol, " +
                    "   vnf.sodium AS nutritional_fact_sodium, " +
                    "   vnf.total_carbs  AS nutritional_fact_total_carbs, " +
                    "   vnf.dietary_fiber  AS nutritional_fact_dietary_fiber, " +
                    "   vnf.sugar  AS nutritional_fact_sugar, " +
                    "   vnf.vitamin_a  AS nutritional_fact_vitamin_a, " +
                    "   vnf.vitamin_b  AS nutritional_fact_vitamin_b, " +
                    "   vnf.vitamin_c  AS nutritional_fact_vitamin_c, " +
                    "   vnf.vitamin_d  AS nutritional_fact_vitamin_d, " +
                    "   vnf.calcium  AS nutritional_fact_calcium, " +
                    "   vnf.iron AS nutritional_fact_iron, " +
                    "   vnf.protein  AS nutritional_fact_protein, " +
                    "   vnf.profile_name AS nutritional_fact_profile_name " +
                    "FROM" +
                    "   vendor_foods AS vf " +
                    "LEFT JOIN " +
                    "   vendor_food_tags vft1 " +
                    "ON " +
                    "   vf.tag_one = vft1.id " +
                    "LEFT JOIN " +
                    "   vendor_food_tags vft2 " +
                    "ON " +
                    "   vf.tag_two = vft2.id " +
                    "LEFT JOIN " +
                    "   vendor_food_tags vft3 " +
                    "ON " +
                    "   vf.tag_three = vft3.id " +
                    "LEFT JOIN " +
                    "   vendor_food_tags vft4 " +
                    "ON " +
                    "   vf.tag_four = vft4.id " +
                    "LEFT JOIN " +
                    "   vendor_food_tags vft5 " +
                    "ON " +
                    "   vf.tag_five = vft5.id " +
                    "LEFT JOIN" +
                    "   vendor_food_categories AS vfc " +
                    "ON " +
                    "   vf.vendor_food_category_id = vfc.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   vf.nutritional_facts_id = vnf.id " +
                    "WHERE" +
                    "   vf.vendor_id = ?";

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
                    "   vfr.review_image_one, " +
                    "   vfr.review_image_two, " +
                    "   vfr.review_image_three, " +
                    "   vfr.review_image_four, " +
                    "   vfr.review_image_five, " +
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
     * The file paths for each image are full URLS to S3.
     */
    public String loadFoodMenuSQL_stage3 =
            "SELECT " +
                    "   vfi.id, " +
                    "   vfi.vendor_food_id, " +
                    "   vfi.display_order, " +
                    "   vfi.filename, " +
                    "   vfi.creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vfi.creation_timestamp::date)) AS creation_days_ago " +
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
     * @TODO Move this to materialized views (or distributed data stores).
     * Loads all foods + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all foods (as map by id).
     * 2) Load all review for all foods.
     * 3) Load all image urls (has map by display order).
     * 4) Calculate review averages for all the vendor_foods.
     * 5) Load all ingredients.
     * 6) Load the drop-downs.
     *
     * @param brewery_id
     * @return HashMap<vendor_food_id, vendor_food_data_structure>
     * @throws Exception
     */
    public FoodMenu loadFoodMenu(
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            // Initialize variables.
            FoodMenu foodMenu = new FoodMenu();
            HashMap<Integer, VendorFood> vendorFoodHashMap = new HashMap<Integer, VendorFood>();
            VendorDropdownContainer dropDowns = new VendorDropdownContainer();
            // Prepare the statements.
            stage1 = this.DAO.prepareStatement(this.loadFoodMenuSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.loadFoodMenuSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.loadFoodMenuSQL_stage3);
            /*
            Stage 1
            Load all foods.
             */
            stage1.setInt(1, brewery_id);
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                Color color = new Color();
                VendorFood vendorFood = new VendorFood();
                vendorFood.vendor_id = stage1Result.getInt("vendor_food_vendor_id");
                vendorFood.vendor_food_id = stage1Result.getInt("vendor_food_id");
                vendorFood.name = stage1Result.getString("vendor_food_name");
                vendorFood.description = stage1Result.getString("vendor_food_description");
                vendorFood.price = stage1Result.getFloat("vendor_food_price");
                vendorFood.creation_timestamp = stage1Result.getString("creation_timestamp");
                vendorFood.creation_days_ago = stage1Result.getString("creation_days_ago");
                Array food_sizes_array = stage1Result.getArray("vendor_food_food_sizes");
                String[] str_food_sizes = (String[]) food_sizes_array.getArray();
                vendorFood.food_sizes = str_food_sizes;
                vendorFood.vendor_food_category.name = stage1Result.getString("vendor_food_category_name");
                vendorFood.vendor_food_category.vendor_id = stage1Result.getInt("vendor_food_category_vendor_id");
                vendorFood.vendor_food_category.id = stage1Result.getInt("vendor_food_category_id");
                vendorFood.vendor_food_category.description = stage1Result.getString("vendor_food_category_description");
                vendorFood.vendor_food_category.hex_color = stage1Result.getString("vendor_food_category_hex_color");
                vendorFood.vendor_food_category.text_color = color.getInverseBW(vendorFood.vendor_food_category.hex_color);
                VendorFoodTag tag_one = new VendorFoodTag();
                VendorFoodTag tag_two = new VendorFoodTag();
                VendorFoodTag tag_three = new VendorFoodTag();
                VendorFoodTag tag_four = new VendorFoodTag();
                VendorFoodTag tag_five = new VendorFoodTag();
                tag_one.id = stage1Result.getInt("tag_one_id");
                tag_one.name = stage1Result.getString("tag_one_name");
                tag_one.hex_color = stage1Result.getString("tag_one_hex_color");
                tag_one.tag_type = stage1Result.getString("tag_one_tag_type");
                tag_one.vendor_id = vendorFood.vendor_id;
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                vendorFood.tag_one = tag_one;
                tag_two.id = stage1Result.getInt("tag_two_id");
                tag_two.name = stage1Result.getString("tag_two_name");
                tag_two.hex_color = stage1Result.getString("tag_two_hex_color");
                tag_two.tag_type = stage1Result.getString("tag_two_tag_type");
                tag_two.vendor_id = vendorFood.vendor_id;
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                vendorFood.tag_two = tag_two;
                tag_three.id = stage1Result.getInt("tag_three_id");
                tag_three.name = stage1Result.getString("tag_three_name");
                tag_three.hex_color = stage1Result.getString("tag_three_hex_color");
                tag_three.tag_type = stage1Result.getString("tag_three_tag_type");
                tag_three.vendor_id = vendorFood.vendor_id;
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                vendorFood.tag_three = tag_three;
                tag_four.id = stage1Result.getInt("tag_four_id");
                tag_four.name = stage1Result.getString("tag_four_name");
                tag_four.hex_color = stage1Result.getString("tag_four_hex_color");
                tag_four.tag_type = stage1Result.getString("tag_four_tag_type");
                tag_four.vendor_id = vendorFood.vendor_id;
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                vendorFood.tag_four = tag_four;
                tag_five.id = stage1Result.getInt("tag_five_id");
                tag_five.name = stage1Result.getString("tag_five_name");
                tag_five.hex_color = stage1Result.getString("tag_five_hex_color");
                tag_five.tag_type = stage1Result.getString("tag_five_tag_type");
                tag_five.vendor_id = vendorFood.vendor_id;
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                vendorFood.tag_five = tag_five;
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = stage1Result.getInt("nutritional_fact_id");
                vendorNutritionalFact.vendor_id = vendorFood.vendor_id;
                vendorNutritionalFact.serving_size = stage1Result.getInt("nutritional_fact_serving_size");
                vendorNutritionalFact.calories = stage1Result.getInt("nutritional_fact_calories");
                vendorNutritionalFact.calories_from_fat = stage1Result.getInt("nutritional_fact_calories_from_fat");
                vendorNutritionalFact.total_fat = stage1Result.getInt("nutritional_fact_total_fat");
                vendorNutritionalFact.saturated_fat = stage1Result.getInt("nutritional_fact_saturated_fat");
                vendorNutritionalFact.trans_fat = stage1Result.getInt("nutritional_fact_trans_fat");
                vendorNutritionalFact.cholesterol = stage1Result.getInt("nutritional_fact_cholesterol");
                vendorNutritionalFact.sodium = stage1Result.getInt("nutritional_fact_sodium");
                vendorNutritionalFact.total_carbs = stage1Result.getInt("nutritional_fact_total_carbs");
                vendorNutritionalFact.dietary_fiber = stage1Result.getInt("nutritional_fact_dietary_fiber");
                vendorNutritionalFact.sugar = stage1Result.getInt("nutritional_fact_sugar");
                vendorNutritionalFact.vitamin_a = stage1Result.getInt("nutritional_fact_vitamin_a");
                vendorNutritionalFact.vitamin_b = stage1Result.getInt("nutritional_fact_vitamin_b");
                vendorNutritionalFact.vitamin_c = stage1Result.getInt("nutritional_fact_vitamin_c");
                vendorNutritionalFact.vitamin_d = stage1Result.getInt("nutritional_fact_vitamin_d");
                vendorNutritionalFact.calcium = stage1Result.getInt("nutritional_fact_calcium");
                vendorNutritionalFact.iron = stage1Result.getInt("nutritional_fact_iron");
                vendorNutritionalFact.protein = stage1Result.getInt("nutritional_fact_protein");
                vendorNutritionalFact.profile_name = stage1Result.getString("nutritional_fact_profile_name");
                vendorFood.nutritional_facts = vendorNutritionalFact;
                vendorFoodHashMap.put(vendorFood.vendor_food_id, vendorFood);
            }
            /*
            Stage 2
            Load all reviews.
             */
            stage2.setInt(1, brewery_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                VendorFoodReview vendorFoodReview = new VendorFoodReview();
                vendorFoodReview.review_id = stage2Result.getInt("review_id");
                vendorFoodReview.account_id = stage2Result.getInt("account_id");
                vendorFoodReview.vendor_food_id = stage2Result.getInt("food_id");
                vendorFoodReview.stars = stage2Result.getInt("stars");
                vendorFoodReview.content = stage2Result.getString("content");
                vendorFoodReview.days_ago = stage2Result.getInt("days_ago");
                vendorFoodReview.username = stage2Result.getString("username");
                vendorFoodReview.review_image_one = stage2Result.getString("review_image_one");
                vendorFoodReview.review_image_two = stage2Result.getString("review_image_two");
                vendorFoodReview.review_image_three = stage2Result.getString("review_image_three");
                vendorFoodReview.review_image_four = stage2Result.getString("review_image_four");
                vendorFoodReview.review_image_five = stage2Result.getString("review_image_five");
                // Add the vendor_food review to the appropriate vendor_food.
                vendorFoodHashMap.get(vendorFoodReview.vendor_food_id).reviews.add(vendorFoodReview);
            }
            /*
            Stage 3
            Load all images.
             */
            stage3.setInt(1, brewery_id);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                VendorFoodImage vendorFoodImage = new VendorFoodImage();
                vendorFoodImage.food_image_id = stage3Result.getInt("id");
                vendorFoodImage.food_id = stage3Result.getInt("vendor_food_id");
                vendorFoodImage.display_order = stage3Result.getInt("display_order");
                vendorFoodImage.filename = stage3Result.getString("filename");
                vendorFoodImage.creation_timestamp = stage3Result.getString("creation_timestamp");
                vendorFoodImage.creation_days_ago = stage3Result.getString("creation_days_ago");
                vendorFoodHashMap.get(vendorFoodImage.food_id).images.put(vendorFoodImage.display_order, vendorFoodImage);
            }
            /*
            Stage 4
            Calculate review averages.
             */
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
            /*
            Stage 5
            Load all ingredients.
            @TODO impelment
             */

            /*
            Stage 6
            Load all drop-downs.
             */
            dropDowns = this.getVendorDropdowns(brewery_id, this.DAO);
            /*
            Assemble main return object and be done.
             */
            foodMenu.menuItems = vendorFoodHashMap;
            foodMenu.dropDowns = dropDowns;
            return foodMenu;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to load food");
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

    private String loadFoodMenuPaginatedSQL_stage1 =
            "SELECT " +
                    "   id, " +
                    "   vendor_id," +
                    "   name," +
                    "   description," +
                    "   price," +
                    "   food_sizes " +
                    "FROM" +
                    "   vendor_foods " +
                    "WHERE" +
                    "   vendor_id = ? " +
                    "ORDER BY <%order_by%> ASC " +
                    "LIMIT ? " +
                    "OFFSET ?";

    private String loadFoodMenuPaginatedSQL_stage2 =
            "SELECT " +
                    "   vfr.id as review_id, " +
                    "   vfr.account_id, " +
                    "   vfr.vendor_food_id as food_id, " +
                    "   vfr.stars, " +
                    "   vfr.content, " +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', vfr.creation_timestamp::date) as days_ago, " +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username " +
                    "FROM " +
                    "   vendor_food_reviews vfr " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   vfr.account_id = a.id " +
                    "WHERE " +
                    "   vfr.vendor_food_id IN (";

    private String loadFoodMenuPaginatedSQL_stage3 =
            "SELECT " +
                    "   vendor_food_id, " +
                    "   display_order, " +
                    "   filename " +
                    "FROM " +
                    "   vendor_food_images " +
                    "WHERE " +
                    "   vendor_food_id IN (";

    /**
     * Returns limit-offset vendor_foods for a given brewery_id ordered by a column (of possible options).
     *
     *      1) Fetch vendor_foods where vendor_id matches with limit-offset, ordered by column (generating
     *         a list of vendor_food_ids).
     *      2) Fetch all reviews (with vendor_food_id in clause).
     *      3) Fetch all images (with vendor_food_id in clause).
     *      4) Calculate review averages for all the vendor_foods.
     *
     * @param brewery_id
     * @param limit
     * @param offset
     * @param order_by
     * @return
     * @throws Exception
     */
    public HashMap<Integer, VendorFood> loadFoodMenuPaginated(
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
            Get all the vendor_foods as specified.
             */
            // Don't worry, this is validated like an enum in the controller
            // so there's no possible SQL injection.
            //
            // This is not typically done in a prepared statement.
            //
            // You should be able to tell this from the API, so this is a good
            // honeypot.
            // @TODO Make this a honeypot.
            this.loadFoodMenuPaginatedSQL_stage1 = this.loadFoodMenuPaginatedSQL_stage1.replace("<%order_by%>", order_by);
            if (descending) {
                this.loadFoodMenuPaginatedSQL_stage1 = this.loadFoodMenuPaginatedSQL_stage1.replace("ASC", "DESC");
            }
            stage1 = this.DAO.prepareStatement(loadFoodMenuPaginatedSQL_stage1);
            stage1.setInt(1, brewery_id);
            stage1.setInt(2, limit);
            stage1.setInt(3, offset);
            HashMap<Integer, VendorFood> vendorFoodHashMap = new HashMap<Integer, VendorFood>();
            // Create the where clause for the other searches.
            String vendor_food_ids = "";
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                VendorFood vendorFood = new VendorFood();
                vendorFood.vendor_id = stage1Result.getInt("vendor_id");
                vendorFood.vendor_food_id = stage1Result.getInt("id");
                vendorFood.name = stage1Result.getString("name");
                vendorFood.description = stage1Result.getString("description");
                vendorFood.price = stage1Result.getFloat("price");
                Array food_sizes_array = stage1Result.getArray("food_sizes");
                String[] str_food_sizes = (String[]) food_sizes_array.getArray();
                vendorFood.food_sizes = str_food_sizes;
                vendorFoodHashMap.put(vendorFood.vendor_food_id, vendorFood);
                vendor_food_ids += String.valueOf(vendorFood.vendor_food_id) + ",";
            }
            // If there are no foods, return the empty hash map.
            if (vendor_food_ids == "") {
                return vendorFoodHashMap;
            }
            vendor_food_ids += ")";
            // Remove trailing comma.
            vendor_food_ids = vendor_food_ids.replace(",)", ")");
            /*
            Stage 2
            Get all the vendor_food reviews where vendor_food_ids are in as where clause.
             */
            this.loadFoodMenuPaginatedSQL_stage2 += vendor_food_ids;
            stage2 = this.DAO.prepareStatement(this.loadFoodMenuPaginatedSQL_stage2);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                VendorFoodReview vendorFoodReview = new VendorFoodReview();
                vendorFoodReview.review_id = stage2Result.getInt("review_id");
                vendorFoodReview.account_id = stage2Result.getInt("account_id");
                vendorFoodReview.vendor_food_id = stage2Result.getInt("food_id");
                vendorFoodReview.stars = stage2Result.getInt("stars");
                vendorFoodReview.content = stage2Result.getString("content");
                vendorFoodReview.days_ago = stage2Result.getInt("days_ago");
                vendorFoodReview.username = stage2Result.getString("username");
                // Add the vendor_food review to the appropriate vendor_food.
                vendorFoodHashMap.get(vendorFoodReview.vendor_food_id).reviews.add(vendorFoodReview);
            }
            /*
            Stage 3
            Get all the images where vendor_food_ids in where clause.
             */
            this.loadFoodMenuPaginatedSQL_stage3 += vendor_food_ids;
            stage3 = this.DAO.prepareStatement(this.loadFoodMenuPaginatedSQL_stage3);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                VendorFoodImage vendorFoodImage = new VendorFoodImage();
                vendorFoodImage.food_id = stage3Result.getInt("vendor_food_id");
                vendorFoodImage.display_order = stage3Result.getInt("display_order");
                vendorFoodImage.filename = stage3Result.getString("filename");
                vendorFoodHashMap.get(vendorFoodImage.food_id).images.put(vendorFoodImage.display_order, vendorFoodImage);
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Unknown error.
            throw new Exception("Unable to load foods.");
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
