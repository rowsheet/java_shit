package com.PublicBrewery.Beer;

import com.Common.*;
import com.Common.PublicVendor.BeerMenu;
import com.Common.VendorMedia.VendorMedia;
import com.PublicBrewery.VendorMedia.VendorMediaModel;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

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
                    "   b.id AS beer_id," +
                    "   b.vendor_id AS beer_vendor_id," +
                    "   b.name AS beer_name," +
                    "   b.color AS beer_color," +
                    "   b.bitterness AS beer_bitterness," +
                    "   b.abv AS beer_abv," +
                    "   b.beer_style AS beer_style," +
                    "   b.beer_tastes AS beer_tastes," +
                    "   b.description AS beer_description," +
                    "   b.price AS beer_price," +
                    "   b.beer_sizes AS beer_sizes, " +
                    "   b.hop_score AS beer_hop_score, " +
                    "   b.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', b.creation_timestamp::date)) AS creation_days_ago, " +
                    "   bc.name AS beer_category_name, " +
                    "   bc.hex_color AS beer_category_hex_color, " +
                    "   bc.id AS beer_category_id, " +
                    "   bc.description AS beer_category_description, " +
                    "   vbt1.id AS tag_one_id, " +
                    "   vbt1.name AS tag_one_name, " +
                    "   vbt1.hex_color AS tag_one_hex_color, " +
                    "   vbt1.tag_type AS tag_one_tag_type, " +
                    "   vbt2.id AS tag_two_id, " +
                    "   vbt2.name AS tag_two_name, " +
                    "   vbt2.hex_color AS tag_two_hex_color, " +
                    "   vbt2.tag_type AS tag_two_tag_type, " +
                    "   vbt3.id AS tag_three_id, " +
                    "   vbt3.name AS tag_three_name, " +
                    "   vbt3.hex_color AS tag_three_hex_color, " +
                    "   vbt3.tag_type AS tag_three_tag_type, " +
                    "   vbt4.id AS tag_four_id, " +
                    "   vbt4.name AS tag_four_name, " +
                    "   vbt4.hex_color AS tag_four_hex_color, " +
                    "   vbt4.tag_type AS tag_four_tag_type, " +
                    "   vbt5.id AS tag_five_id, " +
                    "   vbt5.name AS tag_five_name, " +
                    "   vbt5.hex_color AS tag_five_hex_color, " +
                    "   vbt5.tag_type AS tag_five_tag_type, " +
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
                    "   beers b " +
                    "LEFT JOIN " +
                    "   beer_tags vbt1 " +
                    "ON " +
                    "   b.tag_one = vbt1.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt2 " +
                    "ON " +
                    "   b.tag_two = vbt2.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt3 " +
                    "ON " +
                    "   b.tag_three = vbt3.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt4 " +
                    "ON " +
                    "   b.tag_four = vbt4.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt5 " +
                    "ON " +
                    "   b.tag_five = vbt5.id " +
                    "LEFT JOIN" +
                    "   beer_categories bc " +
                    "ON " +
                    "   b.beer_category_id = bc.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   b.nutritional_facts_id = vnf.id " +
                    "WHERE" +
                    "   b.vendor_id = ?";

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
    /*
    DEPRECIATED
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
                    "   DATE_PART('day', now()::date) - DATE_PART('day', br.creation_timestamp::date) as days_ago," +
                    "   br.review_image_one, " +
                    "   br.review_image_two, " +
                    "   br.review_image_three, " +
                    "   br.review_image_four, " +
                    "   br.review_image_five " +
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
    */

    /**
     * The file paths for each image are full URLS to S3.
     */
    private String loadBeerMenuSQL_stage3 =
            "SELECT " +
                    "   bi.id, " +
                    "   bi.beer_id, " +
                    "   bi.display_order, " +
                    "   bi.filename, " +
                    "   bi.creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', bi.creation_timestamp::date)) AS creation_days_ago " +
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

    public String loadBeerMenuSQL_stage4 =
            "SELECT " +
                    "   b.id AS b_id, " +
                    "   bi.id AS bi_id, " +
                    "   bi.vendor_id AS bi_vendor_id, " +
                    "   bi.feature_id AS bi_feature_id, " +
                    "   bi.name AS bi_name, " +
                    "   bi.description AS bi_description, " +
                    "   bi.source AS bi_source, " +
                    "   bi.tag_one AS bi_tag_one, " +
                    "   bi.tag_two AS bi_tag_two, " +
                    "   bi.tag_three AS bi_tag_three, " +
                    "   bi.tag_four AS bi_tag_four, " +
                    "   bi.tag_five AS bi_tag_five, " +
                    "   bi.nutritional_facts_id AS bi_nutritional_facts_id, " +
                    "   bi.verified AS bi_verified, " +
                    "   bi.creation_timestamp AS bi_creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', bi.creation_timestamp::date)) AS bi_creation_days_ago, " +
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
                    "   bt1.id AS bt1_id, " +
                    "   bt1.name AS bt1_name, " +
                    "   bt1.hex_color AS bt1_hex_color, " +
                    "   bt1.tag_type AS bt1_tag_type, " +
                    "   bt2.id AS bt2_id, " +
                    "   bt2.name AS bt2_name, " +
                    "   bt2.hex_color AS bt2_hex_color, " +
                    "   bt2.tag_type AS bt2_tag_type, " +
                    "   bt3.id AS bt3_id, " +
                    "   bt3.name AS bt3_name, " +
                    "   bt3.hex_color AS bt3_hex_color, " +
                    "   bt3.tag_type AS bt3_tag_type, " +
                    "   bt4.id AS bt4_id, " +
                    "   bt4.name AS bt4_name, " +
                    "   bt4.hex_color AS bt4_hex_color, " +
                    "   bt4.tag_type AS bt4_tag_type, " +
                    "   bt5.id AS bt5_id, " +
                    "   bt5.name AS bt5_name, " +
                    "   bt5.hex_color AS bt5_hex_color, " +
                    "   bt5.tag_type AS bt5_tag_type " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   beers b " +
                    "ON " +
                    "   v.id = b.vendor_id " +
                    "LEFT JOIN" +
                    "   beer_ingredient_associations bia " +
                    "ON " +
                    "   b.id = bia.beer_id " +
                    "LEFT JOIN " +
                    "   beer_ingredients bi " +
                    "ON " +
                    "   bia.beer_ingredient_id = bi.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   bi.nutritional_facts_id = vnf.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt1 " +
                    "ON " +
                    "   bi.tag_one = bt1.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt2 " +
                    "ON " +
                    "   bi.tag_two = bt2.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt3 " +
                    "ON " +
                    "   bi.tag_three = bt3.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt4 " +
                    "ON " +
                    "   bi.tag_four = bt4.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt5 " +
                    "ON " +
                    "   bi.tag_five = bt5.id " +
                    "WHERE " +
                    "   v.id = ?";

    public String loadBeerMenuSQL_stage5 =
            "SELECT DISTINCT " +
                    "   b.id, " +
                    "   COUNT(bf.*) AS count_star " +
                    "FROM " +
                    "   beers b " +
                    "LEFT JOIN " +
                    "   beer_favorites bf " +
                    "ON " +
                    "   b.id = bf.beer_id " +
                    "WHERE " +
                    "   b.vendor_id = ? " +
                    "GROUP BY 1";

    public String loadBeerMenuSQL_stage7 =
            "SELECT " +
                    "   main_beer_gallery_id " +
                    "FROM " +
                    "   vendor_info " +
                    "WHERE " +
                    "   vendor_id = ?";

    public BeerModel() throws Exception {
    }

    /**
     * @TODO Move this to materialized views (or distributed data stores).
     * Loads all beers + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all beers (as map by id).
     * 2) [DEPRECIATED (now lazy loaded)] Load all review for all beers.
     * 3) Load all image urls (has map by display order).
     * 4) Load all ingredients (and associated nutritional facts).
     * 5) [DEPRECIATED] Calculate review averages for all the beers.
     * 5) Fetch total favorites.
     * 6) Load all drop-downs.
     * 7) Load main gallery.
     *
     * @param brewery_id
     * @return HashMap<beer_id, beer_data_structure>
     * @throws Exception
     */
    public BeerMenu loadBeerMenu(
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        // PreparedStatement stage2 = null;
        // ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        PreparedStatement stage4 = null;
        ResultSet stage4Result = null;
        PreparedStatement stage5 = null;
        ResultSet stage5Result = null;
        PreparedStatement stage7 = null;
        ResultSet stage7Result = null;
        try {
            // Initialize variables.
            BeerMenu beerMenu = new BeerMenu();
            HashMap<Integer, Beer> beerHashMap = new HashMap<Integer, Beer>();
            VendorDropdownContainer dropDowns = new VendorDropdownContainer();
            // Prepare the statements.
            stage1 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage1);
            // stage2 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage3);
            stage5 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage5);
            /*
            Stage 1
             */
            stage1.setInt(1, brewery_id);
            stage1Result = stage1.executeQuery();
            // Hash map because when we fetch reviews, they need to be added to each respective beer
            // the moment they are pulled from the result set.
            while (stage1Result.next()) {
                Color color = new Color();
                Beer beer = new Beer();
                beer.beer_id = stage1Result.getInt("beer_id");
                beer.vendor_id = stage1Result.getInt("beer_vendor_id");
                beer.name = stage1Result.getString("beer_name");
                beer.color = stage1Result.getInt("beer_color");
                beer.bitterness = stage1Result.getInt("beer_bitterness");
                beer.abv = stage1Result.getInt("beer_abv");
                beer.beer_style = stage1Result.getString("beer_style");
                // Beer tastes are an array of enums in postgres.
                Array beer_tastes = stage1Result.getArray("beer_tastes");
                String[] str_beer_tastes = (String[]) beer_tastes.getArray();
                beer.beer_tastes = str_beer_tastes;
                beer.description = stage1Result.getString("beer_description");
                beer.price = stage1Result.getFloat("beer_price");
                // Beer sizes are an array of enums in postgres.
                Array beer_sizes = stage1Result.getArray("beer_sizes");
                String[] str_beer_sizes = (String[]) beer_sizes.getArray();
                beer.beer_sizes = str_beer_sizes;
                beer.hop_score = stage1Result.getString("beer_hop_score");
                beer.beer_category.name = stage1Result.getString("beer_category_name");
                beer.beer_category.hex_color = stage1Result.getString("beer_category_hex_color");
                beer.beer_category.vendor_id = beer.vendor_id;
                beer.beer_category.id = stage1Result.getInt("beer_category_id");
                beer.beer_category.description = stage1Result.getString("beer_category_description");
                beer.beer_category.text_color = color.getInverseBW(beer.beer_category.hex_color);
                beer.creation_timestamp = stage1Result.getString("creation_timestamp");
                beer.creation_days_ago = stage1Result.getString("creation_days_ago");
                BeerTag tag_one = new BeerTag();
                BeerTag tag_two = new BeerTag();
                BeerTag tag_three = new BeerTag();
                BeerTag tag_four = new BeerTag();
                BeerTag tag_five = new BeerTag();
                tag_one.id = stage1Result.getInt("tag_one_id");
                tag_one.name = stage1Result.getString("tag_one_name");
                tag_one.hex_color = stage1Result.getString("tag_one_hex_color");
                tag_one.tag_type = stage1Result.getString("tag_one_tag_type");
                tag_one.vendor_id = beer.vendor_id;
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                beer.tag_one = tag_one;
                tag_two.id = stage1Result.getInt("tag_two_id");
                tag_two.name = stage1Result.getString("tag_two_name");
                tag_two.hex_color = stage1Result.getString("tag_two_hex_color");
                tag_two.tag_type = stage1Result.getString("tag_two_tag_type");
                tag_two.vendor_id = beer.vendor_id;
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                beer.tag_two = tag_two;
                tag_three.id = stage1Result.getInt("tag_three_id");
                tag_three.name = stage1Result.getString("tag_three_name");
                tag_three.hex_color = stage1Result.getString("tag_three_hex_color");
                tag_three.tag_type = stage1Result.getString("tag_three_tag_type");
                tag_three.vendor_id = beer.vendor_id;
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                beer.tag_three = tag_three;
                tag_four.id = stage1Result.getInt("tag_four_id");
                tag_four.name = stage1Result.getString("tag_four_name");
                tag_four.hex_color = stage1Result.getString("tag_four_hex_color");
                tag_four.tag_type = stage1Result.getString("tag_four_tag_type");
                tag_four.vendor_id = beer.vendor_id;
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                beer.tag_four = tag_four;
                tag_five.id = stage1Result.getInt("tag_five_id");
                tag_five.name = stage1Result.getString("tag_five_name");
                tag_five.hex_color = stage1Result.getString("tag_five_hex_color");
                tag_five.tag_type = stage1Result.getString("tag_five_tag_type");
                tag_five.vendor_id = beer.vendor_id;
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                beer.tag_five = tag_five;
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = stage1Result.getInt("nutritional_fact_id");
                vendorNutritionalFact.vendor_id = beer.vendor_id;
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
                beer.nutritional_facts = vendorNutritionalFact;
                beerHashMap.put(beer.beer_id, beer);
            }
            /*
            Stage 2
             */
            /*
            DEPRECIATED
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
                beerReview.review_image_one = stage2Result.getString("review_image_one");
                beerReview.review_image_two = stage2Result.getString("review_image_two");
                beerReview.review_image_three = stage2Result.getString("review_image_three");
                beerReview.review_image_four = stage2Result.getString("review_image_four");
                beerReview.review_image_five = stage2Result.getString("review_image_five");
                // Add the beer review to the appropriate beer.
                beerHashMap.get(beerReview.beer_id).reviews.add(beerReview);
            }
             */
            /*
            Stage 3
             */
            stage3.setInt(1, brewery_id);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                BeerImage beerImage = new BeerImage();
                beerImage.beer_image_id = stage3Result.getInt("id");
                beerImage.beer_id = stage3Result.getInt("beer_id");
                beerImage.display_order = stage3Result.getInt("display_order");
                beerImage.filename = stage3Result.getString("filename");
                beerImage.creation_timestamp = stage3Result.getString("creation_timestamp");
                beerImage.creation_days_ago = stage3Result.getString("creation_days_ago");
                beerHashMap.get(beerImage.beer_id).images.put(beerImage.display_order, beerImage);
            }
             /*
            Stage 4
            Load all ingredients.
             */
            stage4 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage4);
            stage4.setInt(1, brewery_id);
            stage4Result = stage4.executeQuery();
            while (stage4Result.next()) {
                Color color = new Color();
                BeerIngredient beerIngredient = new BeerIngredient();
                BeerTag tag_one = new BeerTag();
                BeerTag tag_two = new BeerTag();
                BeerTag tag_three = new BeerTag();
                BeerTag tag_four = new BeerTag();
                BeerTag tag_five = new BeerTag();
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                int beer_id = stage4Result.getInt("b_id");
                beerIngredient.vendor_id = this.vendorCookie.vendorID;
                beerIngredient.id = stage4Result.getInt("bi_id");
                beerIngredient.feature_id = stage4Result.getInt("bi_feature_id");
                beerIngredient.name = stage4Result.getString("bi_name");
                beerIngredient.description = stage4Result.getString("bi_description");
                beerIngredient.source = stage4Result.getString("bi_source");
                beerIngredient.verified = stage4Result.getBoolean("bi_verified");
                beerIngredient.creation_timestamp = stage4Result.getString("bi_creation_timestamp");
                beerIngredient.creation_days_ago = stage4Result.getString("bi_creation_days_ago");
                vendorNutritionalFact.profile_name = stage4Result.getString("vnf_profile_name");
                vendorNutritionalFact.id = stage4Result.getInt("vnf_id");
                vendorNutritionalFact.vendor_id = stage4Result.getInt("vnf_vendor_id");
                vendorNutritionalFact.serving_size = stage4Result.getInt("vnf_serving_size");
                vendorNutritionalFact.calories = stage4Result.getInt("vnf_calories");
                vendorNutritionalFact.calories_from_fat = stage4Result.getInt("vnf_calories_from_fat");
                vendorNutritionalFact.total_fat = stage4Result.getInt("vnf_total_fat");
                vendorNutritionalFact.saturated_fat = stage4Result.getInt("vnf_saturated_fat");
                vendorNutritionalFact.trans_fat = stage4Result.getInt("vnf_trans_fat");
                vendorNutritionalFact.cholesterol = stage4Result.getInt("vnf_cholesterol");
                vendorNutritionalFact.sodium = stage4Result.getInt("vnf_sodium");
                vendorNutritionalFact.total_carbs = stage4Result.getInt("vnf_total_carbs");
                vendorNutritionalFact.dietary_fiber = stage4Result.getInt("vnf_dietary_fiber");
                vendorNutritionalFact.sugar = stage4Result.getInt("vnf_sugar");
                vendorNutritionalFact.vitamin_a = stage4Result.getInt("vnf_vitamin_a");
                vendorNutritionalFact.vitamin_b = stage4Result.getInt("vnf_vitamin_b");
                vendorNutritionalFact.vitamin_c = stage4Result.getInt("vnf_vitamin_c");
                vendorNutritionalFact.vitamin_d = stage4Result.getInt("vnf_vitamin_d");
                vendorNutritionalFact.calcium = stage4Result.getInt("vnf_calcium");
                vendorNutritionalFact.iron = stage4Result.getInt("vnf_iron");
                vendorNutritionalFact.protein = stage4Result.getInt("vnf_protein");
                vendorNutritionalFact.profile_name = stage4Result.getString("vnf_profile_name");
                if (vendorNutritionalFact.id != 0) {
                    beerIngredient.nutritional_fact_profile = vendorNutritionalFact;
                }
                tag_one.vendor_id = this.vendorCookie.vendorID;
                tag_one.id = stage4Result.getInt("bt1_id");
                tag_one.name = stage4Result.getString("bt1_name");
                tag_one.hex_color = stage4Result.getString("bt1_hex_color");
                tag_one.tag_type = stage4Result.getString("bt1_tag_type");
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                    beerIngredient.tag_one = tag_one;
                }
                tag_two.vendor_id = this.vendorCookie.vendorID;
                tag_two.id = stage4Result.getInt("bt2_id");
                tag_two.name = stage4Result.getString("bt2_name");
                tag_two.hex_color = stage4Result.getString("bt2_hex_color");
                tag_two.tag_type = stage4Result.getString("bt2_tag_type");
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                    beerIngredient.tag_two = tag_two;
                }
                tag_three.vendor_id = this.vendorCookie.vendorID;
                tag_three.id = stage4Result.getInt("bt3_id");
                tag_three.name = stage4Result.getString("bt3_name");
                tag_three.hex_color = stage4Result.getString("bt3_hex_color");
                tag_three.tag_type = stage4Result.getString("bt3_tag_type");
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                    beerIngredient.tag_three = tag_three;
                }
                tag_four.vendor_id = this.vendorCookie.vendorID;
                tag_four.id = stage4Result.getInt("bt4_id");
                tag_four.name = stage4Result.getString("bt4_name");
                tag_four.hex_color = stage4Result.getString("bt4_hex_color");
                tag_four.tag_type = stage4Result.getString("bt4_tag_type");
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                    beerIngredient.tag_four = tag_four;
                }
                tag_five.vendor_id = this.vendorCookie.vendorID;
                tag_five.id = stage4Result.getInt("bt5_id");
                tag_five.name = stage4Result.getString("bt5_name");
                tag_five.hex_color = stage4Result.getString("bt5_hex_color");
                tag_five.tag_type = stage4Result.getString("bt5_tag_type");
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                    beerIngredient.tag_five = tag_five;
                }
                if (beerIngredient.id != 0) {
                    beerHashMap.get(beer_id).beer_ingredients.add(beerIngredient);
                }
            }

            /*
            Stage 5
             */
            // Go through each beer and calculate the review star averages.
            /* DEPRECIDATED
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
            */
            /*
            New Stage 5
            Fetch total favorites.
             */
            stage5.setInt(1, brewery_id);
            stage5Result = stage5.executeQuery();
            while(stage5Result.next()) {
                int beer_id = stage5Result.getInt("id");
                int count_star = stage5Result.getInt("count_star");
                beerHashMap.get(beer_id).total_favorites = count_star;
            }
            /*
            Stage 6
            Load all drop-downs.
             */
            dropDowns = this.getVendorDropdowns(brewery_id, this.DAO);
            /*
            Stage 7
            Load all galleries.
             */
            stage7 = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage7);
            stage7.setInt(1, brewery_id);
            int main_gallery_id = 0;
            stage7Result = stage7.executeQuery();
            while (stage7Result.next()) {
                main_gallery_id = stage7Result.getInt("main_beer_gallery_id");
            }
            if (main_gallery_id != 0) {
                VendorMediaModel vendorMediaModel = new VendorMediaModel();
                beerMenu.mainGallery = vendorMediaModel.loadVendorPageImageGallery(main_gallery_id);
            }
            /*
            Assemble main return object and be done.
             */
            beerMenu.menuItems = beerHashMap;
            beerMenu.dropDowns = dropDowns;
            return beerMenu;
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
//            if (stage2 != null) {
//                stage2.close();
//            }
//            if (stage2Result != null) {
//                stage2Result.close();
//            }
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
            if (stage7 != null) {
                stage7.close();
            }
            if (stage7Result != null) {
                stage7Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /*
    DEPRECIATED
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
     */

    /**
     * @TODO STOP USING THIS!
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
    /*
    DEPRECIATED
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
            Stage 1
            Get all the beers as specified.
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
            Stage 2
            Get all the beer reviews where beer_ids are in as where clause.
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
            Stage 3
            Get all the images where beer_ids in where clause.
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
            Stage 4
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
    */
}

