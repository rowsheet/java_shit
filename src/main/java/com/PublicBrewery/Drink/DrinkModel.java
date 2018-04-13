package com.PublicBrewery.Drink;

import com.Common.*;
import com.Common.PublicVendor.DrinkMenu;
import com.PublicBrewery.VendorMedia.VendorMediaModel;
import jnr.ffi.annotations.In;
import sun.security.provider.ConfigFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class DrinkModel extends AbstractModel {
    private String loadDrinkMenuSQL_stage1 =
            "SELECT" +
                    "   vd.id AS vendor_drink_id," +
                    "   vd.vendor_id AS vendor_id," +
                    "   vd.name AS vendor_drink_name," +
                    "   vd.description AS vendor_drink_description," +
                    "   vd.price AS vendor_drink_price," +
                    "   vd.hex_one AS vendor_drink_hex_one, " +
                    "   vd.hex_two AS vendor_drink_hex_two, " +
                    "   vd.hex_three AS vendor_drink_hex_three, " +
                    "   vd.hex_background AS vendor_drink_hex_background, " +
                    "   vd.drink_serve_temp AS vendor_drink_serve_temp, " +
                    "   vd.servings AS vendor_drink_servings," +
                    "   vd.icon_enum AS vendor_drink_icon_enum, " +
                    "   vd.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vd.creation_timestamp::date)) AS creation_days_ago, " +
                    "   vdc.id AS vendor_drink_category_id, " +
                    "   vdc.name AS vendor_drink_category_name, " +
                    "   vdc.hex_color AS vendor_drink_category_hex_color, " +
                    "   vdc.icon_enum AS vendor_drink_category_icon_enum, " +
                    "   vdc.description AS vendor_drink_category_description, " +
                    "   vdt1.id AS tag_one_id, " +
                    "   vdt1.name AS tag_one_name, " +
                    "   vdt1.hex_color AS tag_one_hex_color, " +
                    "   vdt1.tag_type AS tag_one_tag_type, " +
                    "   vdt2.id AS tag_two_id, " +
                    "   vdt2.name AS tag_two_name, " +
                    "   vdt2.hex_color AS tag_two_hex_color, " +
                    "   vdt2.tag_type AS tag_two_tag_type, " +
                    "   vdt3.id AS tag_three_id, " +
                    "   vdt3.name AS tag_three_name, " +
                    "   vdt3.hex_color AS tag_three_hex_color, " +
                    "   vdt3.tag_type AS tag_three_tag_type, " +
                    "   vdt4.id AS tag_four_id, " +
                    "   vdt4.name AS tag_four_name, " +
                    "   vdt4.hex_color AS tag_four_hex_color, " +
                    "   vdt4.tag_type AS tag_four_tag_type, " +
                    "   vdt5.id AS tag_five_id, " +
                    "   vdt5.name AS tag_five_name, " +
                    "   vdt5.hex_color AS tag_five_hex_color, " +
                    "   vdt5.tag_type AS tag_five_tag_type," +
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
                    "FROM " +
                    "   vendor_drinks vd " +
                    "LEFT JOIN " +
                    "   vendor_drink_categories vdc " +
                    "ON " +
                    "   vd.vendor_drink_category_id = vdc.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt1 " +
                    "ON " +
                    "   vd.tag_one = vdt1.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt2 " +
                    "ON " +
                    "   vd.tag_two = vdt2.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt3 " +
                    "ON " +
                    "   vd.tag_three = vdt3.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt4 " +
                    "ON " +
                    "   vd.tag_four = vdt4.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt5 " +
                    "ON " +
                    "   vd.tag_five = vdt5.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   vd.nutritional_facts_id = vnf.id " +
                    "WHERE" +
                    "   vd.vendor_id = ?";

    /**
     * Bottom line, we need all the reviews for all the drinks for this vendor where they exist.
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
    private String loadDrinkMenuSQL_stage2 =
            "SELECT " +
                    "   vdr.id AS vendor_drink_review_id," +
                    "   v.id AS vendor_id," +
                    "   vd.id AS vendor_drink_id," +
                    "   vd.name AS vendor_drink_name," +
                    "   vdr.content AS vendor_drink_review_content," +
                    "   vdr.stars AS vendor_drink_review_stars," +
                    "   a.id AS account_id," +
                    "   vdr.review_image_one, " +
                    "   vdr.review_image_two, " +
                    "   vdr.review_image_three, " +
                    "   vdr.review_image_four, " +
                    "   vdr.review_image_five, " +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', vdr.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   vendor_drinks vd " +
                    "LEFT JOIN " +
                    "   vendor_drink_reviews vdr " +
                    "ON " +
                    "   vd.id = vdr.vendor_drink_id " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   vdr.account_id = a.id " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   v.id = vd.vendor_id " +
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
    private String loadDrinkMenuSQL_stage3 =
            "SELECT " +
                    "   vdi.id, " +
                    "   vdi.vendor_drink_id, " +
                    "   vdi.display_order, " +
                    "   vdi.filename, " +
                    "   vdi.creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vdi.creation_timestamp::date)) AS creation_days_ago " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_drinks vd " +
                    "ON " +
                    "   v.id = vd.vendor_id " +
                    "LEFT JOIN " +
                    "   vendor_drink_images vdi " +
                    "ON " +
                    "   vd.id = vdi.vendor_drink_id " +
                    "WHERE " +
                    "   v.id = ? " +
                    "AND " +
                    "   vdi.filename IS NOT NULL";

    private String loadDrinkMenuSQL_stage4 =
            "SELECT " +
                    "   vd.id AS vendor_drink_id, " +
                    "   s.spirit_type AS spirit_type, " +
                    "   s.company_name AS spirit_company_name, " +
                    "   s.brand_name AS spirit_brand_name, " +
                    "   s.id AS spirit_id " +
                    "FROM " +
                    "   vendor_drinks vd " +
                    "LEFT JOIN " +
                    "   spirit_drink_associations sda " +
                    "ON " +
                    "   vd.id = sda.vendor_drink_id " +
                    "LEFT JOIN " +
                    "   spirits s " +
                    "ON " +
                    "   sda.spirit_id = s.id " +
                    "WHERE " +
                    "   vd.id = ?";

    private String loadDrinkMenuSQL_stage5 =
            "SELECT " +
                    "   vd.id AS vd_id, " +
                    "   vdi.id AS vdi_id, " +
                    "   vdi.vendor_id AS vdi_vendor_id, " +
                    "   vdi.feature_id AS vdi_feature_id, " +
                    "   vdi.name AS vdi_name, " +
                    "   vdi.description AS vdi_description, " +
                    "   vdi.source AS vdi_source, " +
                    "   vdi.tag_one AS vdi_tag_one, " +
                    "   vdi.tag_two AS vdi_tag_two, " +
                    "   vdi.tag_three AS vdi_tag_three, " +
                    "   vdi.tag_four AS vdi_tag_four, " +
                    "   vdi.tag_five AS vdi_tag_five, " +
                    "   vdi.nutritional_facts_id AS vdi_nutritional_facts_id, " +
                    "   vdi.verified AS vdi_verified, " +
                    "   vdi.creation_timestamp AS vdi_creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vdi.creation_timestamp::date)) AS vdi_creation_days_ago, " +
                    "   vnf.profile_name AS vnf_profile_name, " +
                    "   vnf.id AS vnf_id, " +
                    "   vnf.vendor_id AS vnf_vendor_id, " +
                    "   vnf.serving_size AS vnf_serving_size, " +
                    "   vnf.calories AS vnf_calories, " +
                    "   vnf.calories_from_fat AS vnf_calories_from_fat, " +
                    "   vnf.total_fat AS vnf_total_fat, " +
                    "   vnf.saturated_fat AS vnf_saturated_fat, " +
                    "   vnf.trans_fat AS vnf_trans_fat, " +
                    "   vnf.cholesterol AS vnf_cholesterol, " +
                    "   vnf.sodium AS vnf_sodium, " +
                    "   vnf.total_carbs AS vnf_total_carbs, " +
                    "   vnf.dietary_fiber AS vnf_dietary_fiber, " +
                    "   vnf.sugar AS vnf_sugar, " +
                    "   vnf.vitamin_a AS vnf_vitamin_a, " +
                    "   vnf.vitamin_b AS vnf_vitamin_b, " +
                    "   vnf.vitamin_c AS vnf_vitamin_c, " +
                    "   vnf.vitamin_d AS vnf_vitamin_d, " +
                    "   vnf.calcium AS vnf_calcium, " +
                    "   vnf.iron AS vnf_iron, " +
                    "   vnf.protein AS vnf_protein, " +
                    "   vnf.creation_timestamp vnf_creation_timestamp," +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vnf.creation_timestamp::date)) AS vnf_creation_days_ago," +
                    "   vdt1.id AS vdt1_id, " +
                    "   vdt1.name AS vdt1_name, " +
                    "   vdt1.hex_color AS vdt1_hex_color, " +
                    "   vdt1.tag_type AS vdt1_tag_type, " +
                    "   vdt2.id AS vdt2_id, " +
                    "   vdt2.name AS vdt2_name, " +
                    "   vdt2.hex_color AS vdt2_hex_color, " +
                    "   vdt2.tag_type AS vdt2_tag_type, " +
                    "   vdt3.id AS vdt3_id, " +
                    "   vdt3.name AS vdt3_name, " +
                    "   vdt3.hex_color AS vdt3_hex_color, " +
                    "   vdt3.tag_type AS vdt3_tag_type, " +
                    "   vdt4.id AS vdt4_id, " +
                    "   vdt4.name AS vdt4_name, " +
                    "   vdt4.hex_color AS vdt4_hex_color, " +
                    "   vdt4.tag_type AS vdt4_tag_type, " +
                    "   vdt5.id AS vdt5_id, " +
                    "   vdt5.name AS vdt5_name, " +
                    "   vdt5.hex_color AS vdt5_hex_color, " +
                    "   vdt5.tag_type AS vdt5_tag_type " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_drinks vd " +
                    "ON " +
                    "   v.id = vd.vendor_id " +
                    "LEFT JOIN" +
                    "   vendor_drink_ingredient_associations vdia " +
                    "ON " +
                    "   vd.id = vdia.vendor_drink_id " +
                    "LEFT JOIN " +
                    "   vendor_drink_ingredients vdi " +
                    "ON " +
                    "   vdia.vendor_drink_ingredient_id = vdi.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   vdi.nutritional_facts_id = vnf.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt1 " +
                    "ON " +
                    "   vdi.tag_one = vdt1.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt2 " +
                    "ON " +
                    "   vdi.tag_two = vdt2.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt3 " +
                    "ON " +
                    "   vdi.tag_three = vdt3.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt4 " +
                    "ON " +
                    "   vdi.tag_four = vdt4.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt5 " +
                    "ON " +
                    "   vdi.tag_five = vdt5.id " +
                    "WHERE " +
                    "   v.id = ?";

    public String loadDrinkMenuSQL_stage8 =
            "SELECT " +
                    "   main_drink_gallery_id " +
                    "FROM " +
                    "   vendor_info " +
                    "WHERE " +
                    "   vendor_id = ?";

    public DrinkModel() throws Exception {
    }

    /**
     * @TODO Move this to materialized views (or distributed data stores).
     * Loads all drinks + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all drinks (as map by id).
     * 2) Load all review for all drinks.
     * 3) Load all image urls (has map by display order).
     * 4) Get all spirits-associations for drink.
     * 5) Load all ingredients.
     * 6) Calculate review averages for all the drinks.
     * 7) Load all drop-downs.
     * 8) Load main gallery.
     *
     * @param brewery_id
     * @return HashMap<vendor_drink_id, vendor_drink_data_structure>
     * @throws Exception
     */
    public DrinkMenu loadDrinkMenu(
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        PreparedStatement stage4 = null;
        ResultSet stage4Result = null;
        PreparedStatement stage5 = null;
        ResultSet stage5Result = null;
        PreparedStatement stage8 = null;
        ResultSet stage8Result = null;
        try {
            // Initialize variables.
            DrinkMenu drinkMenu = new DrinkMenu();
            HashMap<Integer, VendorDrink> menuItems = new HashMap<Integer, VendorDrink>();
            VendorDropdownContainer dropDowns = new VendorDropdownContainer();
            // Prepare the statements.
            stage1 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage3);
            /*
            Stage 1
             */
            stage1.setInt(1, brewery_id);
            stage1Result = stage1.executeQuery();
            // Hash map because when we fetch reviews, they need to be added to each respective drink
            // the moment they are pulled from the result set.
            while (stage1Result.next()) {
                Color color = new Color();
                VendorDrink vendorDrink = new VendorDrink();
                vendorDrink.vendor_drink_id = stage1Result.getInt("vendor_drink_id");
                vendorDrink.name = stage1Result.getString("vendor_drink_name");
                vendorDrink.vendor_id = stage1Result.getInt("vendor_id");
                vendorDrink.description = stage1Result.getString("vendor_drink_description");
                vendorDrink.price = stage1Result.getFloat("vendor_drink_price");
                vendorDrink.hex_one = stage1Result.getString("vendor_drink_hex_one");
                vendorDrink.hex_two = stage1Result.getString("vendor_drink_hex_two");
                vendorDrink.hex_three = stage1Result.getString("vendor_drink_hex_three");
                vendorDrink.hex_background = stage1Result.getString("vendor_drink_hex_background");
                vendorDrink.hex_icon_text = color.getInverseBW(vendorDrink.hex_background);
                vendorDrink.drink_serve_temp = stage1Result.getString("vendor_drink_serve_temp");
                vendorDrink.servings = stage1Result.getString("vendor_drink_servings");
                vendorDrink.icon_enum = stage1Result.getString("vendor_drink_icon_enum");
                vendorDrink.creation_timestamp = stage1Result.getString("creation_timestamp");
                vendorDrink.creation_days_ago = stage1Result.getString("creation_days_ago");
                vendorDrink.vendor_drink_category.vendor_id = vendorDrink.vendor_id;
                vendorDrink.vendor_drink_category.name = stage1Result.getString("vendor_drink_category_name");
                vendorDrink.vendor_drink_category.hex_color = stage1Result.getString("vendor_drink_category_hex_color");
                vendorDrink.vendor_drink_category.id = stage1Result.getInt("vendor_drink_category_id");
                vendorDrink.vendor_drink_category.description = stage1Result.getString("vendor_drink_category_description");
                vendorDrink.vendor_drink_category.text_color = color.getInverseBW(vendorDrink.vendor_drink_category.hex_color);
                VendorDrinkTag tag_one = new VendorDrinkTag();
                VendorDrinkTag tag_two = new VendorDrinkTag();
                VendorDrinkTag tag_three = new VendorDrinkTag();
                VendorDrinkTag tag_four = new VendorDrinkTag();
                VendorDrinkTag tag_five = new VendorDrinkTag();
                tag_one.id = stage1Result.getInt("tag_one_id");
                tag_one.name = stage1Result.getString("tag_one_name");
                tag_one.hex_color = stage1Result.getString("tag_one_hex_color");
                tag_one.tag_type = stage1Result.getString("tag_one_tag_type");
                tag_one.vendor_id = vendorDrink.vendor_id;
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                vendorDrink.tag_one = tag_one;
                tag_two.id = stage1Result.getInt("tag_two_id");
                tag_two.name = stage1Result.getString("tag_two_name");
                tag_two.hex_color = stage1Result.getString("tag_two_hex_color");
                tag_two.tag_type = stage1Result.getString("tag_two_tag_type");
                tag_two.vendor_id = vendorDrink.vendor_id;
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                vendorDrink.tag_two = tag_two;
                tag_three.id = stage1Result.getInt("tag_three_id");
                tag_three.name = stage1Result.getString("tag_three_name");
                tag_three.hex_color = stage1Result.getString("tag_three_hex_color");
                tag_three.tag_type = stage1Result.getString("tag_three_tag_type");
                tag_three.vendor_id = vendorDrink.vendor_id;
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                vendorDrink.tag_three = tag_three;
                tag_four.id = stage1Result.getInt("tag_four_id");
                tag_four.name = stage1Result.getString("tag_four_name");
                tag_four.hex_color = stage1Result.getString("tag_four_hex_color");
                tag_four.tag_type = stage1Result.getString("tag_four_tag_type");
                tag_four.vendor_id = vendorDrink.vendor_id;
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                vendorDrink.tag_four = tag_four;
                tag_five.id = stage1Result.getInt("tag_five_id");
                tag_five.name = stage1Result.getString("tag_five_name");
                tag_five.hex_color = stage1Result.getString("tag_five_hex_color");
                tag_five.tag_type = stage1Result.getString("tag_five_tag_type");
                tag_five.vendor_id = vendorDrink.vendor_id;
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                vendorDrink.tag_five = tag_five;
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = stage1Result.getInt("nutritional_fact_id");
                vendorNutritionalFact.vendor_id = vendorDrink.vendor_id;
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
                vendorDrink.nutritional_facts = vendorNutritionalFact;
                menuItems.put(vendorDrink.vendor_drink_id, vendorDrink);
            }
            /*
            Stage 2
             */
            stage2.setInt(1, brewery_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                VendorDrinkReview vendorDrinkReview = new VendorDrinkReview();
                vendorDrinkReview.review_id = stage2Result.getInt("vendor_drink_review_id");
                vendorDrinkReview.account_id = stage2Result.getInt("account_id");
                vendorDrinkReview.vendor_drink_id = stage2Result.getInt("vendor_drink_id");
                vendorDrinkReview.stars = stage2Result.getInt("vendor_drink_review_stars");
                vendorDrinkReview.content = stage2Result.getString("vendor_drink_review_content");
                vendorDrinkReview.days_ago = stage2Result.getInt("days_ago");
                vendorDrinkReview.username = stage2Result.getString("username");
                vendorDrinkReview.review_image_one = stage2Result.getString("review_image_one");
                vendorDrinkReview.review_image_two = stage2Result.getString("review_image_two");
                vendorDrinkReview.review_image_three = stage2Result.getString("review_image_three");
                vendorDrinkReview.review_image_four = stage2Result.getString("review_image_four");
                vendorDrinkReview.review_image_five = stage2Result.getString("review_image_five");
                // Add the drink review to the appropriate drink.
                menuItems.get(vendorDrinkReview.vendor_drink_id).reviews.add(vendorDrinkReview);
            }
            /*
            Stage 3
             */
            stage3.setInt(1, brewery_id);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                VendorDrinkImage vendorDrinkImage = new VendorDrinkImage();
                vendorDrinkImage.drink_image_id = stage3Result.getInt("id");
                vendorDrinkImage.drink_id = stage3Result.getInt("vendor_drink_id");
                vendorDrinkImage.display_order = stage3Result.getInt("display_order");
                vendorDrinkImage.filename = stage3Result.getString("filename");
                vendorDrinkImage.creation_timestamp = stage3Result.getString("creation_timestamp");
                vendorDrinkImage.creation_days_ago = stage3Result.getString("creation_days_ago");
                menuItems.get(vendorDrinkImage.drink_id).images.put(vendorDrinkImage.display_order, vendorDrinkImage);
            }
            /*
            Stage 4
             */
            for (int vendor_drink_id: menuItems.keySet()) {
                stage4 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage4);
                stage4.setInt(1, vendor_drink_id);
                stage4Result = stage4.executeQuery();
                while (stage4Result.next()) {
                    Spirit spirit = new Spirit();
                    spirit.id = stage4Result.getInt("spirit_id");
                    spirit.brand_name = stage4Result.getString("spirit_brand_name");
                    spirit.company_name = stage4Result.getString("spirit_company_name");
                    spirit.spirit_type = stage4Result.getString("spirit_type");
                    // Mark the drink as alcoholic and add the spirits.
                    if (spirit.id != 0) {
                        menuItems.get(vendor_drink_id).is_alcoholic = true;
                        menuItems.get(vendor_drink_id).spirits.add(spirit);
                    }
                }
            }
            /*
            Stage 5
            Load all ingredients.
             */
            stage5 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage5);
            stage5.setInt(1, brewery_id);
            stage5Result = stage5.executeQuery();
            while (stage5Result.next()) {
                Color color = new Color();
                VendorDrinkIngredient vendorDrinkIngredient = new VendorDrinkIngredient();
                VendorDrinkTag tag_one = new VendorDrinkTag();
                VendorDrinkTag tag_two = new VendorDrinkTag();
                VendorDrinkTag tag_three = new VendorDrinkTag();
                VendorDrinkTag tag_four = new VendorDrinkTag();
                VendorDrinkTag tag_five = new VendorDrinkTag();
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                int vendor_drink_id = stage5Result.getInt("vd_id");
                vendorDrinkIngredient.vendor_id = this.vendorCookie.vendorID;
                vendorDrinkIngredient.id = stage5Result.getInt("vdi_id");
                vendorDrinkIngredient.feature_id = stage5Result.getInt("vdi_feature_id");
                vendorDrinkIngredient.name = stage5Result.getString("vdi_name");
                vendorDrinkIngredient.description = stage5Result.getString("vdi_description");
                vendorDrinkIngredient.source = stage5Result.getString("vdi_source");
                vendorDrinkIngredient.verified = stage5Result.getBoolean("vdi_verified");
                vendorDrinkIngredient.creation_timestamp = stage5Result.getString("vdi_creation_timestamp");
                vendorDrinkIngredient.creation_days_ago = stage5Result.getString("vdi_creation_days_ago");
                vendorNutritionalFact.profile_name = stage5Result.getString("vnf_profile_name");
                vendorNutritionalFact.id = stage5Result.getInt("vnf_id");
                vendorNutritionalFact.vendor_id = stage5Result.getInt("vnf_vendor_id");
                vendorNutritionalFact.serving_size = stage5Result.getInt("vnf_serving_size");
                vendorNutritionalFact.calories = stage5Result.getInt("vnf_calories");
                vendorNutritionalFact.calories_from_fat = stage5Result.getInt("vnf_calories_from_fat");
                vendorNutritionalFact.total_fat = stage5Result.getInt("vnf_total_fat");
                vendorNutritionalFact.saturated_fat = stage5Result.getInt("vnf_saturated_fat");
                vendorNutritionalFact.trans_fat = stage5Result.getInt("vnf_trans_fat");
                vendorNutritionalFact.cholesterol = stage5Result.getInt("vnf_cholesterol");
                vendorNutritionalFact.sodium = stage5Result.getInt("vnf_sodium");
                vendorNutritionalFact.total_carbs = stage5Result.getInt("vnf_total_carbs");
                vendorNutritionalFact.dietary_fiber = stage5Result.getInt("vnf_dietary_fiber");
                vendorNutritionalFact.sugar = stage5Result.getInt("vnf_sugar");
                vendorNutritionalFact.vitamin_a = stage5Result.getInt("vnf_vitamin_a");
                vendorNutritionalFact.vitamin_b = stage5Result.getInt("vnf_vitamin_b");
                vendorNutritionalFact.vitamin_c = stage5Result.getInt("vnf_vitamin_c");
                vendorNutritionalFact.vitamin_d = stage5Result.getInt("vnf_vitamin_d");
                vendorNutritionalFact.calcium = stage5Result.getInt("vnf_calcium");
                vendorNutritionalFact.iron = stage5Result.getInt("vnf_iron");
                vendorNutritionalFact.protein = stage5Result.getInt("vnf_protein");
                vendorNutritionalFact.profile_name = stage5Result.getString("vnf_profile_name");
                if (vendorNutritionalFact.id != 0) {
                    vendorDrinkIngredient.nutritional_fact_profile = vendorNutritionalFact;
                }
                tag_one.vendor_id = this.vendorCookie.vendorID;
                tag_one.id = stage5Result.getInt("vdt1_id");
                tag_one.name = stage5Result.getString("vdt1_name");
                tag_one.hex_color = stage5Result.getString("vdt1_hex_color");
                tag_one.tag_type = stage5Result.getString("vdt1_tag_type");
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                    vendorDrinkIngredient.tag_one = tag_one;
                }
                tag_two.vendor_id = this.vendorCookie.vendorID;
                tag_two.id = stage5Result.getInt("vdt2_id");
                tag_two.name = stage5Result.getString("vdt2_name");
                tag_two.hex_color = stage5Result.getString("vdt2_hex_color");
                tag_two.tag_type = stage5Result.getString("vdt2_tag_type");
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                    vendorDrinkIngredient.tag_two = tag_two;
                }
                tag_three.vendor_id = this.vendorCookie.vendorID;
                tag_three.id = stage5Result.getInt("vdt3_id");
                tag_three.name = stage5Result.getString("vdt3_name");
                tag_three.hex_color = stage5Result.getString("vdt3_hex_color");
                tag_three.tag_type = stage5Result.getString("vdt3_tag_type");
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                    vendorDrinkIngredient.tag_three = tag_three;
                }
                tag_four.vendor_id = this.vendorCookie.vendorID;
                tag_four.id = stage5Result.getInt("vdt4_id");
                tag_four.name = stage5Result.getString("vdt4_name");
                tag_four.hex_color = stage5Result.getString("vdt4_hex_color");
                tag_four.tag_type = stage5Result.getString("vdt4_tag_type");
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                    vendorDrinkIngredient.tag_four = tag_four;
                }
                tag_five.vendor_id = this.vendorCookie.vendorID;
                tag_five.id = stage5Result.getInt("vdt5_id");
                tag_five.name = stage5Result.getString("vdt5_name");
                tag_five.hex_color = stage5Result.getString("vdt5_hex_color");
                tag_five.tag_type = stage5Result.getString("vdt5_tag_type");
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                    vendorDrinkIngredient.tag_five = tag_five;
                }
                if (vendorDrinkIngredient.id != 0) {
                    menuItems.get(vendor_drink_id).vendor_drink_ingredients.add(vendorDrinkIngredient);
                }
            }
            /*
            Stage 6
            Go through each drink and calculate the review star averages.
             */
            for (VendorDrink vendorDrink : menuItems.values()) {
                if (vendorDrink.reviews.size() > 0) {
                    float total = 0;
                    for (VendorDrinkReview vendorDrinkReview: vendorDrink.reviews) {
                        total += (float) vendorDrinkReview.stars;
                    }
                    vendorDrink.review_average = total / (float) vendorDrink.reviews.size();
                    vendorDrink.review_count = vendorDrink.reviews.size();
                }
            }
            /*
            Stage 7
            Load drop-downs.
             */
            dropDowns = this.getVendorDropdowns(brewery_id, this.DAO);
            /*
            Stage 8
            Load main gallery.
             */
            stage8 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage8);
            stage8.setInt(1, brewery_id);
            stage8Result = stage8.executeQuery();
            int main_gallery_id = 0;
            while (stage8Result.next()) {
                main_gallery_id = stage8Result.getInt("main_drink_gallery_id");
            }
            if (main_gallery_id != 0) {
                VendorMediaModel vendorMediaModel = new VendorMediaModel();
                drinkMenu.mainGallery = vendorMediaModel.loadVendorPageImageGallery(main_gallery_id);
            }
            /*
            Assemble final component.
             */
            drinkMenu.dropDowns = dropDowns;
            drinkMenu.menuItems = menuItems;
            return drinkMenu;
        } catch (Exception ex) {
            System.out.print(ex);
            throw new Exception("Unable to load drink menu.");
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
            if (stage4 != null) {
                stage4.close();
            }
            if (stage4Result != null) {
                stage4Result.close();
            }
            if (stage5 != null) {
                stage5.close();
            }
            if (stage5Result != null) {
                stage5Result.close();
            }
            if (stage8 != null) {
                stage8.close();
            }
            if (stage8Result != null) {
                stage8Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /*
    private String loadDrinkMenuPaginatedSQL_stage1 =
            "SELECT " +
                    "   id AS drink_id," +
                    "   vendor_id," +
                    "   name," +
                    "   color," +
                    "   bitterness," +
                    "   abv," +
                    "   drink_style," +
                    "   drink_tastes," +
                    "   description," +
                    "   price," +
                    "   drink_sizes, " +
                    "   hop_score " +
                    "FROM" +
                    "   drinks " +
                    "WHERE" +
                    "   vendor_id = ? " +
                    "ORDER BY <%order_by%> ASC " +
                    "LIMIT ? " +
                    "OFFSET ?";

    private String loadDrinkMenuPaginatedSQL_stage2 =
            "SELECT " +
                    "   br.id as review_id, " +
                    "   br.account_id, " +
                    "   br.drink_id, " +
                    "   br.stars, " +
                    "   br.content, " +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', br.creation_timestamp::date) as days_ago " +
                    "FROM " +
                    "   drink_reviews br " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   br.account_id = a.id " +
                    "WHERE " +
                    "   br.drink_id IN (";

    private String loadDrinkMenuPaginatedSQL_stage3 =
            "SELECT " +
                    "   drink_id, " +
                    "   display_order, " +
                    "   filename " +
                    "FROM " +
                    "   drink_images " +
                    "WHERE " +
                    "   drink_id in (";
    */

    /**
     * Returns limit-offset drinks for a given brewery_id ordered by a column (of possible options).
     *
     *      1) Fetch drinks where vendor_id matches with limit-offset, ordered by column (generating
     *         a list of drink_ids).
     *      2) Fetch all reviews (with drink_id in clause).
     *      3) Fetch all images (with drink_id in clause).
     *      4) Calculate review averages for all the drinks.
     *
     * @param brewery_id
     * @param limit
     * @param offset
     * @return
     * @throws Exception
     */
    public HashMap<Integer, VendorDrink> loadDrinkMenuPaginated(
            int brewery_id,
            int limit,
            int offset,
            boolean descending
    ) throws Exception {
        return new HashMap<Integer, VendorDrink>();
/*
I'm not doing this right now!
 */
//        PreparedStatement stage1 = null;
//        ResultSet stage1Result = null;
//        PreparedStatement stage2 = null;
//        ResultSet stage2Result = null;
//        PreparedStatement stage3 = null;
//        ResultSet stage3Result = null;
//        try {
//            /*
//            Stage 1
//            Get all the drinks as specified.
//             */
//            // Don't worry, this is validated like an enum in the controller
//            // so there's no possible SQL injection.
//            //
//            // This is not typically done in a prepared statement.
//            //
//            // You should be able to tell this from the API, so this is a good
//            // honeypot.
//            // @TODO Make this a honeypot.
//            this.loadDrinkMenuPaginatedSQL_stage1 = this.loadDrinkMenuPaginatedSQL_stage1.replace("<%order_by%>", order_by);
//            if (descending) {
//                this.loadDrinkMenuPaginatedSQL_stage1 = this.loadDrinkMenuPaginatedSQL_stage1.replace("ASC", "DESC");
//            }
//            stage1 = this.DAO.prepareStatement(loadDrinkMenuPaginatedSQL_stage1);
//            stage1.setInt(1, brewery_id);
//            stage1.setInt(2, limit);
//            stage1.setInt(3, offset);
//            HashMap<Integer, Drink> drinkHashMap = new HashMap<Integer, Drink>();
//            // Create the where clause for the other searches.
//            String drink_ids = "";
//            stage1Result = stage1.executeQuery();
//            while (stage1Result.next()) {
//                Drink drink = new Drink();
//                drink.drink_id = stage1Result.getInt("drink_id");
//                drink.vendor_id = stage1Result.getInt("vendor_id");
//                drink.name = stage1Result.getString("name");
//                drink.color = stage1Result.getInt("color");
//                drink.bitterness = stage1Result.getInt("bitterness");
//                drink.abv = stage1Result.getInt("abv");
//                drink.drink_style = stage1Result.getString("drink_style");
//                drink.description = stage1Result.getString("description");
//                drink.price = stage1Result.getFloat("price");
//                drink.hop_score = stage1Result.getString("hop_score");
//                // Drink tastes are an array of enums in postgres.
//                Array drink_tastes = stage1Result.getArray("drink_tastes");
//                String[] str_drink_tastes = (String[]) drink_tastes.getArray();
//                drink.drink_tastes = str_drink_tastes;
//                // Drink sizes are an array of enums in postgres.
//                Array drink_sizes = stage1Result.getArray("drink_sizes");
//                String[] str_drink_sizes = (String[]) drink_sizes.getArray();
//                drink.drink_sizes = str_drink_sizes;
//                drinkHashMap.put(drink.drink_id, drink);
//                // Create the brewery_ids where clause.
//                drink_ids += String.valueOf(drink.drink_id) + ",";
//            }
//            // If there are no drinks, return the empty hash map, else continue.
//            if (drink_ids == "") {
//                return drinkHashMap;
//            }
//            drink_ids += ")";
//            // Remove trailing comma.
//            drink_ids = drink_ids.replace(",)", ")");
//            /*
//            Stage 2
//            Get all the drink reviews where drink_ids are in as where clause.
//             */
//            this.loadDrinkMenuPaginatedSQL_stage2 += drink_ids;
//            stage2 = this.DAO.prepareStatement(this.loadDrinkMenuPaginatedSQL_stage2);
//            stage2Result = stage2.executeQuery();
//            while (stage2Result.next()) {
//                DrinkReview drinkReview = new DrinkReview();
//                drinkReview.review_id = stage2Result.getInt("review_id");
//                drinkReview.account_id = stage2Result.getInt("account_id");
//                drinkReview.drink_id = stage2Result.getInt("drink_id");
//                drinkReview.stars = stage2Result.getInt("stars");
//                drinkReview.content = stage2Result.getString("content");
//                drinkReview.username = stage2Result.getString("username");
//                drinkReview.days_ago = stage2Result.getInt("days_ago");
//                // Add the drink review to the appropriate drink.
//                drinkHashMap.get(drinkReview.drink_id).reviews.add(drinkReview);
//            }
//            /*
//            Stage 3
//            Get all the images where drink_ids in where clause.
//             */
//            this.loadDrinkMenuPaginatedSQL_stage3 += drink_ids;
//            stage3 = this.DAO.prepareStatement(this.loadDrinkMenuPaginatedSQL_stage3);
//            stage3Result = stage3.executeQuery();
//            while (stage3Result.next()) {
//                DrinkImage drinkImage = new DrinkImage();
//                drinkImage.drink_id = stage3Result.getInt("drink_id");
//                drinkImage.display_order = stage3Result.getInt("display_order");
//                drinkImage.filename = stage3Result.getString("filename");
//                drinkHashMap.get(drinkImage.drink_id).images.put(drinkImage.display_order, drinkImage);
//            }
//            /*
//            Stage 4
//             */
//            // Go through each drink and calculate the review star averages.
//            for (Drink drink : drinkHashMap.values()) {
//                if (drink.reviews.size() > 0) {
//                    float total = 0;
//                    for (DrinkReview drinkReview : drink.reviews) {
//                        total += (float) drinkReview.stars;
//                    }
//                    drink.review_average = total / (float) drink.reviews.size();
//                    drink.review_count = drink.reviews.size();
//                }
//            }
//            return drinkHashMap;
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            // Unknown error.
//            throw new Exception("Unable to load drinks.");
//        } finally {
//            // Clean up your shit.
//            if (stage1 != null) {
//                stage1.close();
//            }
//            if (stage1Result != null) {
//                stage1Result.close();
//            }
//            if (stage2 != null) {
//                stage2.close();
//            }
//            if (stage2Result != null) {
//                stage2Result.close();
//            }
//            if (stage3 != null) {
//                stage3.close();
//            }
//            if (stage3Result != null) {
//                stage3Result.close();
//            }
//            if (this.DAO != null) {
//                this.DAO.close();
//            }
//        }
    }
}
