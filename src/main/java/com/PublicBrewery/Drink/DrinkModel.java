package com.PublicBrewery.Drink;

import com.Common.*;
import sun.security.provider.ConfigFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    "   vdc.id AS vendor_drink_category_id, " +
                    "   vdc.name AS vendor_drink_category_name, " +
                    "   vdc.hex_color AS vendor_drink_category_hex_color " +
                    "FROM " +
                    "   vendor_drinks vd " +
                    "LEFT JOIN " +
                    "   vendor_drink_categories vdc " +
                    "ON " +
                    "   vd.id = vdc.vendor_id " +
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
    private String loadDrinkMenuSQL_stage3 =
            "SELECT " +
                    "   vdi.vendor_drink_id AS vendor_drink_id, " +
                    "   vdi.display_order AS vendor_drink_display_order, " +
                    "   vdi.filename AS vendor_drink_filename " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_drinks vd " +
                    "ON " +
                    "   v.id = vd.vendor_id " +
                    "LEFT JOIN " +
                    "   vendor_drink_images vdi " +
                    "ON " +
                    "   v.id = vdi.vendor_drink_id " +
                    "WHERE " +
                    "   v.id = ? " +
                    "AND " +
                    "   vdi.filename IS NOT NULL";

    private String loadDrinkMenuSQL_stage4 =
            "SELECT " +
                    "   vd.id AS vendor_drink_id, " +
                    "   s.spirit_type AS spirit_type, " +
                    "   s.company_name AS spirit_company_name, " +
                    "   s.brand_name AS spirit_brand_name " +
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

    public DrinkModel() throws Exception {
    }

    /**
     * Loads all drinks + reviews + images for a given vendor_id (brewery_id).
     *
     * Does this in three stages:
     *
     * 1) Load all drinks (as map by id).
     * 2) Load all review for all drinks.
     * 3) Load all image urls (has map by display order).
     * 4) Get all spirits-associations for drink.
     * 5) Calculate review averages for all the drinks.
     *
     * @param brewery_id
     * @return HashMap<vendor_drink_id, vendor_drink_data_structure>
     * @throws Exception
     */
    public HashMap<Integer, VendorDrink> loadDrinkMenu(
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
        try {
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
            HashMap<Integer, VendorDrink> vendorDrinkHashMap = new HashMap<Integer, VendorDrink>();
            while (stage1Result.next()) {
                VendorDrink vendorDrink = new VendorDrink();
                vendorDrink.vendor_drink_id = stage1Result.getInt("vendor_drink_id");
                vendorDrink.vendor_id = stage1Result.getInt("vendor_id");
                vendorDrink.description = stage1Result.getString("vendor_drink_description");
                vendorDrink.price = stage1Result.getFloat("vendor_drink_price");
                vendorDrink.hex_one = stage1Result.getString("vendor_drink_hex_one");
                vendorDrink.hex_two = stage1Result.getString("vendor_drink_hex_two");
                vendorDrink.hex_three = stage1Result.getString("vendor_drink_hex_three");
                vendorDrink.hex_background = stage1Result.getString("vendor_drink_hex_background");
                vendorDrink.vendor_drink_category.vendor_id = vendorDrink.vendor_id;
                vendorDrink.vendor_drink_category.name = stage1Result.getString("vendor_drink_category_name");
                vendorDrink.vendor_drink_category.hex_color = stage1Result.getString("vendor_drink_category_hex_color");
                vendorDrink.vendor_drink_category.vendor_drink_category_id = stage1Result.getInt("vendor_drink_category_id");
                vendorDrinkHashMap.put(vendorDrink.vendor_drink_id, vendorDrink);
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
                // Add the drink review to the appropriate drink.
                vendorDrinkHashMap.get(vendorDrinkReview.vendor_drink_id).reviews.add(vendorDrinkReview);
            }
            /*
            Stage 3
             */
            stage3.setInt(1, brewery_id);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                VendorDrinkImage vendorDrinkImage = new VendorDrinkImage();
                vendorDrinkImage.drink_id = stage3Result.getInt("vendor_drink_id");
                vendorDrinkImage.display_order = stage3Result.getInt("vendor_drink_display_order");
                vendorDrinkImage.filename = stage3Result.getString("vendor_drink_filename");
                vendorDrinkHashMap.get(vendorDrinkImage.drink_id).images.put(vendorDrinkImage.display_order, vendorDrinkImage);
            }
            /*
            Stage 4
             */
            for (int vendor_drink_id: vendorDrinkHashMap.keySet()) {
                stage4 = this.DAO.prepareStatement(this.loadDrinkMenuSQL_stage4);
                stage4.setInt(1, vendor_drink_id);
                stage4Result = stage4.executeQuery();
                while (stage4Result.next()) {
                    Spirit spirit = new Spirit();
                    spirit.brand_name = stage4Result.getString("spirit_brand_name");
                    spirit.company_name = stage4Result.getString("spirit_company_name");
                    spirit.spirit_type = stage4Result.getString("spirit_type");
                    vendorDrinkHashMap.get(vendor_drink_id).spirits.add(spirit);
                }
            }
            /*
            Stage 5
             */
            // Go through each drink and calculate the review star averages.
            for (VendorDrink vendorDrink : vendorDrinkHashMap.values()) {
                if (vendorDrink.reviews.size() > 0) {
                    float total = 0;
                    for (VendorDrinkReview vendorDrinkReview: vendorDrink.reviews) {
                        total += (float) vendorDrinkReview.stars;
                    }
                    vendorDrink.review_average = total / (float) vendorDrink.reviews.size();
                    vendorDrink.review_count = vendorDrink.reviews.size();
                }
            }
            return vendorDrinkHashMap;
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
