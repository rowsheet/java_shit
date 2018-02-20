package com.PublicBrewery.Beer;

import com.Common.AbstractModel;
import com.Common.Beer;
import com.Common.BeerImage;
import com.Common.BeerReview;
import com.Common.BeerIngredient;
import com.Common.VendorNutritionalFact;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
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
                    "   bc.name AS beer_category_name, " +
                    "   bc.hex_color AS beer_category_hex_color, " +
                    "   bc.id AS beer_category_id " +
                    "FROM" +
                    "   beers b " +
                    "LEFT JOIN" +
                    "   beer_categories bc " +
                    "ON " +
                    "   b.beer_category_id = bc.id " +
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

    private String loadBeerMenuSQL_stage5 =
            "SELECT " +
                    "    bi.vendor_id AS beer_vendor_id, " +
                    "    bi.feature_id AS beer_feature_id, " +
                    "    bi.name AS beer_name, " +
                    "    bi.description AS beer_description, " +
                    "    bi.source AS beer_source, " +
                    "    bi.keywords AS beer_keywords, " +
                    "    bi.tag_one AS beer_tag_one, " +
                    "    bi.tag_two AS beer_tag_two, " +
                    "    bi.tag_three AS beer_tag_three, " +
                    "    bi.tag_four AS beer_tag_four, " +
                    "    bi.tag_five AS beer_tag_five, " +
                    "    vnf.id AS nf_nutritional_facts_id, " +
                    "    vnf.vendor_id AS nf_vendor_id, " +
                    "    vnf.serving_size AS nf_serving_size, " +
                    "    vnf.calories AS nf_calories, " +
                    "    vnf.calories_from_fat AS nf_calories_from_fat, " +
                    "    vnf.total_fat AS nf_total_fat, " +
                    "    vnf.saturated_fat AS nf_saturated_fat, " +
                    "    vnf.trans_fat AS nf_trans_fat, " +
                    "    vnf.cholesterol AS nf_cholesterol, " +
                    "    vnf.sodium AS nf_sodium, " +
                    "    vnf.total_carbs AS nf_total_carbs, " +
                    "    vnf.dietary_fiber AS nf_dietary_fiber, " +
                    "    vnf.sugar AS nf_sugar, " +
                    "    vnf.vitamin_a AS nf_vitamin_a, " +
                    "    vnf.vitamin_b AS nf_vitamin_b, " +
                    "    vnf.vitamin_c AS nf_vitamin_c, " +
                    "    vnf.vitamin_d AS nf_vitamin_d, " +
                    "    vnf.calcium AS nf_calcium, " +
                    "    vnf.iron AS nf_iron, " +
                    "    vnf.protein AS nf_protein, " +
                    "    vnf.profile_name AS nf_profile_name " +
                    "FROM " +
                    "    beers b " +
                    "LEFT JOIN " +
                    "    beer_ingredient_associations bia " +
                    "ON " +
                    "    bia.beer_id = b.id " +
                    "LEFT JOIN " +
                    "    beer_ingredients bi " +
                    "ON " +
                    "    bia.beer_ingredient_id = bi.id " +
                    "LEFT JOIN " +
                    "    vendor_nutritional_facts vnf " +
                    "ON " +
                    "    bi.nutritional_facts_id = vnf.id " +
                    "WHERE " +
                    "    b.id = ?";

    public BeerModel() throws Exception {
    }

    /**
     * @TODO Move this to materialized views (or distributed data stores).
     * Loads all beers + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all beers (as map by id).
     * 2) Load all review for all beers.
     * 3) Load all image urls (has map by display order).
     * 4) Calculate review averages for all the beers.
     * 5) Load all ingredients (and associated nutritional facts).
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
                beer.beer_category.beer_category_id = stage1Result.getInt("beer_category_id");
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
                beerReview.review_image_one = stage2Result.getString("review_image_one");
                beerReview.review_image_two = stage2Result.getString("review_image_two");
                beerReview.review_image_three = stage2Result.getString("review_image_three");
                beerReview.review_image_four = stage2Result.getString("review_image_four");
                beerReview.review_image_five = stage2Result.getString("review_image_five");
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
            /*
            Stage 5
             */
            // Load all ingredients and associated nutritional facts.
            // Go through the beer_hash_map. It has all the ids for the vendor_drinks.
            for (Beer beer : beerHashMap.values()) {
                PreparedStatement preparedStatement = this.DAO.prepareStatement(this.loadBeerMenuSQL_stage5);
                preparedStatement.setInt(1, beer.beer_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                ArrayList ingredients_array = new ArrayList<BeerIngredient>();
                while(resultSet.next()) {
                    // Initialize for beer ingredient and nutritional fact profile.
                    BeerIngredient beerIngredient = new BeerIngredient();
                    VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                    // Assign all beer ingredient values.
                    beerIngredient.vendor_id = resultSet.getInt("beer_vendor_id");
                    beerIngredient.feature_id = resultSet.getInt("beer_feature_id");
                    beerIngredient.name = resultSet.getString("beer_name");
                    beerIngredient.description = resultSet.getString("beer_description");
                    beerIngredient.source = resultSet.getString("beer_source");
                    Array keywords_array = resultSet.getArray("beer_keywords");
                    // This conversion will throw a null pointer if keywords_array is null,
                    // which will occur if there are no ingredients associated with this beer.
                    if (keywords_array != null) {
                        beerIngredient.keywords = (String[]) keywords_array.getArray();
                    }
                    beerIngredient.tag_one = resultSet.getString("beer_tag_one");
                    beerIngredient.tag_two = resultSet.getString("beer_tag_two");
                    beerIngredient.tag_three = resultSet.getString("beer_tag_three");
                    beerIngredient.tag_four = resultSet.getString("beer_tag_four");
                    beerIngredient.tag_five = resultSet.getString("beer_tag_five");
                    // Assign all nutritional fact values.
                    vendorNutritionalFact.vendor_id = resultSet.getInt("nf_vendor_id");
                    vendorNutritionalFact.nutritional_fact_id = resultSet.getShort("nf_nutritional_facts_id");
                    vendorNutritionalFact.vendor_id = resultSet.getInt("nf_vendor_id");
                    vendorNutritionalFact.serving_size = resultSet.getInt("nf_serving_size");
                    vendorNutritionalFact.calories = resultSet.getInt("nf_calories");
                    vendorNutritionalFact.calories_from_fat = resultSet.getInt("nf_calories_from_fat");
                    vendorNutritionalFact.total_fat = resultSet.getInt("nf_total_fat");
                    vendorNutritionalFact.saturated_fat = resultSet.getInt("nf_saturated_fat");
                    vendorNutritionalFact.trans_fat = resultSet.getInt("nf_trans_fat");
                    vendorNutritionalFact.cholesterol = resultSet.getInt("nf_cholesterol");
                    vendorNutritionalFact.sodium = resultSet.getInt("nf_sodium");
                    vendorNutritionalFact.total_carbs = resultSet.getInt("nf_total_carbs");
                    vendorNutritionalFact.dietary_fiber = resultSet.getInt("nf_dietary_fiber");
                    vendorNutritionalFact.sugar = resultSet.getInt("nf_sugar");
                    vendorNutritionalFact.vitamin_a = resultSet.getInt("nf_vitamin_a");
                    vendorNutritionalFact.vitamin_b = resultSet.getInt("nf_vitamin_b");
                    vendorNutritionalFact.vitamin_c = resultSet.getInt("nf_vitamin_c");
                    vendorNutritionalFact.vitamin_d = resultSet.getInt("nf_vitamin_d");
                    vendorNutritionalFact.calcium = resultSet.getInt("nf_calcium");
                    vendorNutritionalFact.iron = resultSet.getInt("nf_iron");
                    vendorNutritionalFact.protein = resultSet.getInt("nf_protein");
                    vendorNutritionalFact.profile_name = resultSet.getString("nf_profile_name");
                    // Link the nutritional fact to the to the ingredient data-structure.
                    System.out.println(vendorNutritionalFact);
                    beerIngredient.nutritional_fact_profile = vendorNutritionalFact;
                    // Finally add the ingredients to the total array list.
                    ingredients_array.add(beerIngredient);
                }
                // Associate the ingredients array to the drink.
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
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
    // @TODO STOP USING THIS!
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
            Get all the beer reviews where beer_ids are in as where clause.
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

