package com.Common;

import java.sql.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import com.google.gson.*;
import com.sun.org.apache.regexp.internal.RE;
import sun.security.provider.ConfigFile;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class AbstractModel {

    /*
    Start implementing limits.
     */
    protected int vendor_beer_image_limit = 10;
    protected int vendor_food_image_limit = 10;
    protected int vendor_drink_image_limit = 10;

    private Connection SessionDAO;
    protected Connection DAO;
    protected UserCookie userCookie;
    protected VendorCookie vendorCookie;
    private String db_username;
    private String db_password;

    public AbstractModel()
            throws Exception {
        // Get DB credentials from env vars.
        this.db_username = System.getenv("DBUSERNAME");
        this.db_password = System.getenv("DBPASSWORD");
        // Initialize data access object.
        Class.forName("org.postgresql.Driver");
        this.DAO = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/skiphopp?assumeMinServerVersion=9.0",
                        this.db_username, this.db_password);
        // @TODO only initialize on of these and put the account type in the constructor.
        this.userCookie = new UserCookie();
        this.vendorCookie = new VendorCookie();
    }

    // @TODO This is going to be a MAJOR performance hit, needs to be materialized elseehere (Like Redis).
    private String checkUserSessionSQL =
            "SELECT " +
                    "   account_id " +
                    "FROM " +
                    "   sessions " +
                    "WHERE " +
                    "   session_key = ?";

    // @TODO Dido on the performance hit. Also should probably get some other shit (like anomoly behavioural
    // @TODO (cont.) tracking for security).
    private String checkVendorSessionSQL =
            "SELECT " +
                    "   vaa.vendor_id " +
                    "FROM " +
                    "   sessions s " +
                    "LEFT JOIN " +
                    "   vendor_account_associations vaa " +
                    "ON " +
                    "   s.account_id = vaa.account_id " +
                    "WHERE " +
                    "   s.session_key = ?";

    protected void cleanupDatabase() {
        if (this.DAO != null) {
            this.DAO = null;
        }
    }

    protected void validateUserCookie(String cookie)
        throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // @TODO fetch the session key and look it up, then setting
            // the account_id to what it was from the session.
            Gson gson = new Gson();
            this.userCookie = gson.fromJson(cookie, UserCookie.class);
            preparedStatement = this.DAO.prepareStatement(this.checkUserSessionSQL);
            preparedStatement.setString(1, this.userCookie.sessionKey);
            resultSet = preparedStatement.executeQuery();
            int account_id = 0;
            while (resultSet.next()) {
                account_id = resultSet.getInt("account_id");
            }
            if (account_id == 0) {
                throw new AbstractModelException("Invalid session key.");
            }
            this.userCookie.userID = account_id;
        } catch (AbstractModelException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Unknown reason.
            throw new Exception("Unable to check users session.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /**
     * Takes a cookie as a JSON string, a certain permission
     * and returns the userID and requestPermissionID if the
     * user permission is there. If not, an insufficient
     * permissions exception will be raised.
     *
     * SECURITY NOTE!
     *
     *      Do not set this model's userID from the cookie! Get if from the Session ONLY!
     *
     *      If the userID is parsed right from the cookie, it will allow users to be
     *      able to spoof their userID which may be used in resource ownership!
     *
     * @param cookie
     * @param permission_name
     * @return UserCookie
     */

    protected void validateCookiePermission(String cookie, String permission_name)
        throws Exception {
        Gson gson = new Gson();
        this.userCookie = gson.fromJson(cookie, UserCookie.class);
        UserPermission userPermission = this.userCookie.userPermissions.get(permission_name);
        if (userPermission == null) {
            // This cookie has no permission with the key required by the model.
        } else {
            // This cookie has permission with the key required by the model, if it fails
            // on the check constraint after this, it means the cookie was intentionally
            // forged or permissions changed between the time the user logged in and made
            // this request.
            this.userCookie.requestPermissionID = userPermission.permission_id;
        }
    }

    /**
     * Takes a cookie as a JSON string and just retuns the
     * vendorID. If there is no userID, and exception will
     * be thrown.
     * @param cookie
     * @return
     */

    protected int validateVendorCookie(String cookie)
        throws Exception {
        // Parse the cookie string and set the internal vendorCookie object.
        // We will need to sessionKey from this data.
        Gson gson = new Gson();
        this.vendorCookie = gson.fromJson(cookie, VendorCookie.class);
        // Fetching session data.
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Initialize data access object for sesseions.
            // In the future, this will be ported to Redis.
            Class.forName("org.postgresql.Driver");
            this.SessionDAO = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/skiphopp?assumeMinServerVersion=9.0",
                            this.db_username, this.db_password);
            String session_key = this.vendorCookie.sessionKey;
            preparedStatement = this.SessionDAO.prepareStatement(this.checkVendorSessionSQL);
            preparedStatement.setString(1, session_key);
            resultSet = preparedStatement.executeQuery();
            int rows_of_session_found = 0;
            int vendor_id = 0;
            while (resultSet.next()) {
                vendor_id = resultSet.getInt("vendor_id");
                rows_of_session_found++;
            }
            if (rows_of_session_found == 0) {
                throw new Exception("Invalid session.");
            }
            // Return the vendor_id.
            return vendor_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to validate session.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.SessionDAO != null) {
                this.SessionDAO.close();
            }
        }
    }

    /**
     * Takes a cookie from a vendor request.
     *
     *      1) Validates session key (ensure requesting user is logged in).
     *      2) Parses cookie setting the requestFeatureID to the featureID in the
     *         cookies request if the cookie has the feature required by the model,
     *         otherwise tells the user to fuck off with that unauthorized request.
     *
     * Takes a cookie as a JSON string, a certain requestFeatureID and
     * returns the vendorID and requestFeatureID if the cookie has that
     * feature set. If not, insufficient permissions exception will
     * be raised.
     *
     * SECURITY NOTE!
     *
     *      Do not set this model's vendorID from the cookie! Get if from the Session ONLY!
     *
     *      Resource ownership is checked from this models vendorID in update and delete statements.
     *
     *      If the vendorID is parsed right from the cookie, it will allow users to be
     *      able to spoof resource ownership if they know resourceIDs and vendorIDs!
     *
     * @param cookie
     * @param feature_name
     * @throws Exception
     */

    protected void validateCookieVendorFeature(String cookie, String feature_name)
        throws Exception {
//        System.out.println("cookie");
//        System.out.println(cookie);
//        System.out.println("feature_name");
//        System.out.println(feature_name);
//        System.out.println("vendor_id");
        System.out.println(this.vendorCookie.vendorID);
        this.validateVendorCookie(cookie);
        VendorFeature vendorFeature = this.vendorCookie.vendorFeatures.get(feature_name);
        if (vendorFeature == null) {
            // No feature by that name exists in the request cookie, meaning this
            // request does not have permission for this request, according to what
            // the model is asking for.
            throw new Exception("You do not currently have permission to modify: " + feature_name + "."); // Fuck Off!
        } else {
            // If it is in the cookie, get the ID of the feature and set the
            // requestFeatureID to this.
            //
            // If the request gets through here but fails on a foreign key constraint
            // that means the user SPOOFED THEIR COOKIE. Record that shit, take down
            // any and all information and eventually blacklist that user and all related
            // accounts (or investigate). The user is manually setting permissions
            // maliciously.
            this.vendorCookie.requestFeatureID = vendorFeature.feature_id;
            // We should be good here.
        }
    }

    /**
     * Does the same thing as above, but resets the requestFeatureID to what it was originally.
     *
     * @param cookie
     * @param feature_name
     * @param extra
     * @throws Exception
     */
    protected void validateCookieVendorFeature(String cookie, String feature_name, boolean extra)
        throws Exception {
        if (extra) {
            if (this.vendorCookie.requestFeatureID == 0) {
                throw new Exception("You must validate cookie without extra before validating with extra.");
            }
            int originalRequestFeatureID = this.vendorCookie.requestFeatureID;
            this.validateCookieVendorFeature(cookie, feature_name);
            this.vendorCookie.requestFeatureID = originalRequestFeatureID;
        } else {
            this.validateCookieVendorFeature(cookie, feature_name);
        }
    }

    /**
     * Just parses a vendor JSON cookie and returns the session key.
     *
     * @param cookie
     * @return session_key
     * @throws Exception
     */

    protected String parseSessionKey(String cookie)
        throws Exception {
        VendorCookie vendorCookie = new Gson().fromJson(cookie, VendorCookie.class);
        return vendorCookie.sessionKey;
    }

    /**
     * Takes a password and salt and returns the hash.
     *
     * Uses nested SHA-256 numerous times to try to slow the process down a bit.
     * Salt is generated from the registration process but stored in the DB and is generated
     * from java.security.SecureRandom then Base64 encoded. It must be of sufficient length.
     *
     * Returning hash is a Base64 encoded bytes array.
     *
     * @param password
     * @param salt
     * @return password_hash
     * @throws Exception
     */

    protected String getHash(String password, String salt) throws Exception {
        if (salt.length() < 50) {
            System.out.println(salt);
            System.out.println(salt.length());
            throw new Exception("Salt is too short.");
        }
        String salt_pass = salt + password;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                digest.digest(
                        digest.digest(
                                digest.digest(
                                        digest.digest(
                                                digest.digest(
                                                        digest.digest(
                                                                digest.digest(
                                                                        digest.digest(
                                                                                digest.digest(
                                                                                        digest.digest(
                                                                                                digest.digest(
                                                                                                        digest.digest(
                                                                                                                digest.digest(
                                                                                                                        digest.digest(
                                                                                                                                digest.digest(
                                                                                                                                        digest.digest(salt_pass.getBytes())
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        byte[] encodedBytes = Base64.getEncoder().encode(hash);
        return new String(encodedBytes);
    }

    /**
     * VENDOR DROPDOWNS SQL
     */
    private String getDropdownSQL_stage1 =
            "SELECT " +
                    "   vfc.id AS id, " +
                    "   vfc.name AS name, " +
                    "   vfc.hex_color AS hex_color, " +
                    "   vfc.description AS description, " +
                    "   vfc.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vfc.creation_timestamp::date)) AS creation_days_ago, " +
                    "   ROUND(AVG(vf.price)::numeric, 2) AS price_average, " +
                    "   COALESCE(ROUND(AVG(vfr.stars)::numeric, 2), 0.0) AS review_average, " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   vendor_food_categories vfc " +
                    "LEFT JOIN " +
                    "   vendor_foods vf " +
                    "ON " +
                    "   vf.vendor_food_category_id = vfc.id " +
                    "LEFT JOIN " +
                    "   vendor_food_reviews vfr " +
                    "ON " +
                    "   vfr.vendor_food_id = vf.id " +
                    "WHERE " +
                    "   vfc.vendor_id = ? " +
                    "GROUP BY 1";

    private String getDropdownSQL_stage2 =
            "SELECT " +
                    "   bc.id AS id, " +
                    "   bc.name AS name, " +
                    "   bc.hex_color AS hex_color, " +
                    "   bc.description AS description, " +
                    "   bc.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', bc.creation_timestamp::date)) AS creation_days_ago, " +
                    "   ROUND(AVG(b.price)::numeric, 2) AS price_average, " +
                    "   COALESCE(ROUND(AVG(br.stars)::numeric, 2), 0.0) AS review_average, " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   beer_categories bc " +
                    "LEFT JOIN " +
                    "   beers b " +
                    "ON " +
                    "   b.beer_category_id = bc.id " +
                    "LEFT JOIN " +
                    "   beer_reviews br " +
                    "ON " +
                    "   br.beer_id = b.id " +
                    "WHERE " +
                    "   bc.vendor_id = ? " +
                    "GROUP BY 1";

    private String getDropdownSQL_stage3 =
            "SELECT " +
                    "   vdc.id AS id, " +
                    "   vdc.name AS name, " +
                    "   vdc.hex_color AS hex_color, " +
                    "   vdc.description AS description, " +
                    "   vdc.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vdc.creation_timestamp::date)) AS creation_days_ago, " +
                    "   ROUND(AVG(vd.price)::numeric, 2) AS price_average, " +
                    "   COALESCE(ROUND(AVG(vdr.stars)::numeric, 2), 0.0) AS review_average, " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   vendor_drink_categories vdc " +
                    "LEFT JOIN " +
                    "   vendor_drinks vd " +
                    "ON " +
                    "   vd.vendor_drink_category_id = vdc.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_reviews vdr " +
                    "ON " +
                    "   vdr.vendor_drink_id = vd.id " +
                    "WHERE " +
                    "   vdc.vendor_id = ? " +
                    "GROUP BY 1";

/*
    private String getDropdownSQL_stage3 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color," +
                    "   icon_enum " +
                    "FROM " +
                    "   vendor_drink_categories " +
                    "WHERE " +
                    "   vendor_id = ?";
*/

    private String getDropdownSQL_stage4 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color " +
                    "FROM " +
                    "   event_categories " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage5 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type," +
                    "   creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', creation_timestamp::date)) AS creation_days_ago " +
                    "FROM " +
                    "   vendor_food_tags " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage6 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type, " +
                    "   creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', creation_timestamp::date)) AS creation_days_ago " +
                    "FROM " +
                    "   beer_tags " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage7 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type " +
                    "FROM " +
                    "   vendor_drink_tags " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage8 =
            "SELECT " +
                    "   id, " +
                    "   vendor_id, " +
                    "   serving_size, " +
                    "   calories, " +
                    "   calories_from_fat, " +
                    "   total_fat, " +
                    "   saturated_fat, " +
                    "   trans_fat, " +
                    "   cholesterol, " +
                    "   sodium, " +
                    "   total_carbs, " +
                    "   dietary_fiber, " +
                    "   sugar, " +
                    "   vitamin_a, " +
                    "   vitamin_b, " +
                    "   vitamin_c, " +
                    "   vitamin_d, " +
                    "   calcium, " +
                    "   iron, " +
                    "   protein, " +
                    "   profile_name " +
                    "FROM " +
                    "   vendor_nutritional_facts " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage9 =
            "SELECT " +
                    "   pg_type.typname, " +
                    "   pg_enum.enumlabel " +
                    "FROM " +
                    "   pg_type " +
                    "JOIN " +
                    "   pg_enum " +
                    "ON " +
                    "   pg_enum.enumtypid = pg_type.oid";

    private String getDropdownSQL_stage10 =
            "SELECT " +
                    "   id, " +
                    "   spirit_type, " +
                    "   brand_name, " +
                    "   company_name " +
                    "FROM " +
                    "   spirits";

    /**
     * Validate Cookie... then...
     * Get drop-downs for
     * 1) Food Categories
     * 2) Beer Categories
     * 3) Drink Categories
     * 4) Event Categories
     * 5) Food Tags
     * 6) Beer Tags
     * 7) Drink Tags
     * 8) Nutritional facts.
     * 9) Get all Enums.
     * 10) Get all alcohol spirits.
     *
     * @param vendor_id
     * @return
     * @throws Exception
     */
    protected VendorDropdownContainer getVendorDropdowns(
            int vendor_id,
            Connection DAO
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result =  null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result =  null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result =  null;
        PreparedStatement stage4 = null;
        ResultSet stage4Result =  null;
        PreparedStatement stage5 = null;
        ResultSet stage5Result =  null;
        PreparedStatement stage6 = null;
        ResultSet stage6Result =  null;
        PreparedStatement stage7 = null;
        ResultSet stage7Result =  null;
        PreparedStatement stage8 = null;
        ResultSet stage8Result = null;
        PreparedStatement stage9 = null;
        ResultSet stage9Result = null;
        PreparedStatement stage10 = null;
        ResultSet stage10Result = null;
        VendorDropdownContainer vendorDropdownContainer = new VendorDropdownContainer();
        try {
            /*
            Color for utils.
             */
            Color color = new Color();
            /*
            Stage 1
            Food Categories
             */
            // Create the dropdown hash-map.
            HashMap<Integer, VendorFoodCategory> foodCategories = new HashMap<Integer, VendorFoodCategory>();
            // Set up the query.
            stage1 = DAO.prepareStatement(this.getDropdownSQL_stage1);
            stage1.setInt(1, vendor_id);
            stage1Result = stage1.executeQuery();
            int foodCategoryCount = 0;
            while (stage1Result.next()) {
                VendorFoodCategory vendorFoodCategory = new VendorFoodCategory();
                vendorFoodCategory.id = stage1Result.getInt("id");
                vendorFoodCategory.name = stage1Result.getString("name");
                vendorFoodCategory.hex_color = stage1Result.getString("hex_color");
                vendorFoodCategory.description = stage1Result.getString("description");
                vendorFoodCategory.vendor_id = vendor_id;
                vendorFoodCategory.text_color = color.getInverseBW(vendorFoodCategory.hex_color);
                vendorFoodCategory.price_average = stage1Result.getFloat("price_average");
                vendorFoodCategory.review_average = stage1Result.getFloat("review_average");
                vendorFoodCategory.creation_timestamp = stage1Result.getString("creation_timestamp");
                vendorFoodCategory.creation_days_ago = stage1Result.getString("creation_days_ago");
                vendorFoodCategory.count_star = stage1Result.getInt("count_star");
                // Add the object the hash-map of drop-downs.
                foodCategories.put(foodCategoryCount, vendorFoodCategory);
                foodCategoryCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.foodCategories = foodCategories;
            /*
            Stage 2
            Beer Categories
             */
            // Create the dropdown hash-map.
            HashMap<Integer, BeerCategory> beerCategories = new HashMap<Integer, BeerCategory>();
            // Set up the query.
            stage2 = DAO.prepareStatement(this.getDropdownSQL_stage2);
            stage2.setInt(1, vendor_id);
            stage2Result = stage2.executeQuery();
            int beerCategoriesCount = 0;
            while (stage2Result.next()) {
                BeerCategory beerCategory = new BeerCategory();
                beerCategory.id = stage2Result.getInt("id");
                beerCategory.name = stage2Result.getString("name");
                beerCategory.hex_color = stage2Result.getString("hex_color");
                beerCategory.description = stage2Result.getString("description");
                beerCategory.vendor_id = vendor_id;
                beerCategory.text_color = color.getInverseBW(beerCategory.hex_color);
                beerCategory.price_average = stage2Result.getFloat("price_average");
                beerCategory.review_average = stage2Result.getFloat("review_average");
                beerCategory.creation_timestamp = stage2Result.getString("creation_timestamp");
                beerCategory.creation_days_ago = stage2Result.getString("creation_days_ago");
                beerCategory.count_star = stage2Result.getInt("count_star");
                // Add the object the hash-map of drop-downs.
                beerCategories.put(beerCategoriesCount, beerCategory);
                beerCategoriesCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.beerCategories = beerCategories;
            /*
            Stage 3
            Drink Categories
             */
            // Create the dropdown hash-map.
            HashMap<Integer, VendorDrinkCategory> drinkCategories = new HashMap<Integer, VendorDrinkCategory>();
            // Set up the query.
            stage3 = DAO.prepareStatement(this.getDropdownSQL_stage3);
            stage3.setInt(1, vendor_id);
            stage3Result = stage3.executeQuery();
            int drinkCategoriesCount = 0;
            while (stage3Result.next()) {
                VendorDrinkCategory vendorDrinkCategory = new VendorDrinkCategory();
                vendorDrinkCategory.id = stage3Result.getInt("id");
                vendorDrinkCategory.name = stage3Result.getString("name");
                vendorDrinkCategory.hex_color = stage3Result.getString("hex_color");
                vendorDrinkCategory.description = stage3Result.getString("description");
                vendorDrinkCategory.vendor_id = vendor_id;
                vendorDrinkCategory.text_color = color.getInverseBW(vendorDrinkCategory.hex_color);
                vendorDrinkCategory.price_average = stage3Result.getFloat("price_average");
                vendorDrinkCategory.review_average = stage3Result.getFloat("review_average");
                vendorDrinkCategory.creation_timestamp = stage3Result.getString("creation_timestamp");
                vendorDrinkCategory.creation_days_ago = stage3Result.getString("creation_days_ago");
                vendorDrinkCategory.count_star = stage3Result.getInt("count_star");
                // Add the object the hash-map of drop-downs.
                drinkCategories.put(drinkCategoriesCount, vendorDrinkCategory);
                drinkCategoriesCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.drinkCategories = drinkCategories;
            /*
            Stage 4
            Event Categories
             */
            // Create the dropdown hash-map.
            HashMap<Integer, EventCategory> eventCategories = new HashMap<Integer, EventCategory>();
            // Set up the query.
            stage4 = DAO.prepareStatement(this.getDropdownSQL_stage4);
            stage4.setInt(1, vendor_id);
            stage4Result = stage4.executeQuery();
            int eventCategoriesCount = 0;
            while (stage4Result.next()) {
                EventCategory eventCategory = new EventCategory();
                eventCategory.event_category_id = stage4Result.getInt("id");
                eventCategory.name = stage4Result.getString("name");
                eventCategory.hex_color = stage4Result.getString("hex_color");
                eventCategory.vendor_id = vendor_id;
                // Add the object the hash-map of drop-downs.
                eventCategories.put(eventCategoriesCount, eventCategory);
                eventCategoriesCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.eventCategories = eventCategories;
            /*
            Stage 5
            Food Tags
             */
            // Create the dropdown hash-map.
            HashMap<Integer, VendorFoodTag> foodTags = new HashMap<Integer, VendorFoodTag>();
            // Set up the query.
            stage5 = DAO.prepareStatement(this.getDropdownSQL_stage5);
            stage5.setInt(1, vendor_id);
            stage5Result = stage5.executeQuery();
            int foodTagCount = 0;
            while (stage5Result.next()) {
                VendorFoodTag vendorFoodTag = new VendorFoodTag();
                vendorFoodTag.id = stage5Result.getInt("id");
                vendorFoodTag.name = stage5Result.getString("name");
                vendorFoodTag.hex_color = stage5Result.getString("hex_color");
                vendorFoodTag.text_color = color.getInverseBW(vendorFoodTag.hex_color);
                vendorFoodTag.tag_type = stage5Result.getString("tag_type");
                vendorFoodTag.creation_timestamp = stage5Result.getString("creation_timestamp");
                vendorFoodTag.creation_days_ago = stage5Result.getString("creation_days_ago");
                vendorFoodTag.vendor_id = vendor_id;
                // Add the object the hash-map of drop-downs.
                foodTags.put(foodTagCount, vendorFoodTag);
                foodTagCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.foodTags = foodTags;
            /*
            Stage 6
            Beer Tags
             */
            // Create the dropdown hash-map.
            HashMap<Integer, BeerTag> beerTags = new HashMap<Integer, BeerTag>();
            // Set up the query.
            stage6 = DAO.prepareStatement(this.getDropdownSQL_stage6);
            stage6.setInt(1, vendor_id);
            stage6Result = stage6.executeQuery();
            int beerTagCount = 0;
            while (stage6Result.next()) {
                BeerTag beerTag = new BeerTag();
                beerTag.id = stage6Result.getInt("id");
                beerTag.name = stage6Result.getString("name");
                beerTag.hex_color = stage6Result.getString("hex_color");
                beerTag.tag_type = stage6Result.getString("tag_type");
                beerTag.vendor_id = vendor_id;
                beerTag.text_color = color.getInverseBW(beerTag.hex_color);
                beerTag.creation_timestamp = stage6Result.getString("creation_timestamp");
                beerTag.creation_days_ago = stage6Result.getString("creation_days_ago");
                // Add the object the hash-map of drop-downs.
                beerTags.put(beerTagCount, beerTag);
                beerTagCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.beerTags = beerTags;
            /*
            Stage 7
            Drink Tags
             */
            // Create the dropdown hash-map.
            HashMap<Integer, VendorDrinkTag> drinkTags = new HashMap<Integer, VendorDrinkTag>();
            // Set up the query.
            stage7 = DAO.prepareStatement(this.getDropdownSQL_stage7);
            stage7.setInt(1, vendor_id);
            stage7Result = stage7.executeQuery();
            int drinkTagCount = 0;
            while (stage7Result.next()) {
                VendorDrinkTag vendorDrinkTag = new VendorDrinkTag();
                vendorDrinkTag.id = stage7Result.getInt("id");
                vendorDrinkTag.name = stage7Result.getString("name");
                vendorDrinkTag.hex_color = stage7Result.getString("hex_color");
                vendorDrinkTag.text_color = color.getInverseBW(vendorDrinkTag.hex_color);
                vendorDrinkTag.tag_type = stage7Result.getString("tag_type");
                vendorDrinkTag.vendor_id = this.vendorCookie.vendorID;
                // Add the object the hash-map of drop-downs.
                drinkTags.put(drinkTagCount, vendorDrinkTag);
                drinkTagCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.drinkTags = drinkTags;
            /*
            Stage 8
            Nutritional Facts
             */
            // Create the dropdown hash-map.
            HashMap<Integer, VendorNutritionalFact> nutritionalFacts = new HashMap<Integer, VendorNutritionalFact>();
            // Set up the query.
            stage8 = DAO.prepareStatement(this.getDropdownSQL_stage8);
            stage8.setInt(1, vendor_id);
            stage8Result = stage8.executeQuery();
            int nutritionalFactCount = 0;
            while (stage8Result.next()) {
                VendorNutritionalFact nutritionalFact = new VendorNutritionalFact();
                nutritionalFact.id = stage8Result.getInt("id");
                nutritionalFact.serving_size = stage8Result.getInt("serving_size");
                nutritionalFact.calories = stage8Result.getInt("calories");
                nutritionalFact.calories_from_fat = stage8Result.getInt("calories_from_fat");
                nutritionalFact.total_fat = stage8Result.getInt("total_fat");
                nutritionalFact.saturated_fat = stage8Result.getInt("saturated_fat");
                nutritionalFact.trans_fat = stage8Result.getInt("trans_fat");
                nutritionalFact.cholesterol = stage8Result.getInt("cholesterol");
                nutritionalFact.sodium = stage8Result.getInt("sodium");
                nutritionalFact.total_carbs = stage8Result.getInt("total_carbs");
                nutritionalFact.dietary_fiber = stage8Result.getInt("dietary_fiber");
                nutritionalFact.sugar = stage8Result.getInt("sugar");
                nutritionalFact.vitamin_a = stage8Result.getInt("vitamin_a");
                nutritionalFact.vitamin_b = stage8Result.getInt("vitamin_b");
                nutritionalFact.vitamin_c = stage8Result.getInt("vitamin_c");
                nutritionalFact.vitamin_d = stage8Result.getInt("vitamin_d");
                nutritionalFact.calcium = stage8Result.getInt("calcium");
                nutritionalFact.iron = stage8Result.getInt("iron");
                nutritionalFact.protein = stage8Result.getInt("protein");
                nutritionalFact.profile_name = stage8Result.getString("profile_name");
                // Add the object the hash-map of drop-downs.
                nutritionalFacts.put(nutritionalFactCount, nutritionalFact);
                nutritionalFactCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.nutritionalFacts = nutritionalFacts;
            /*
            Stage 9
            Enumerations
             */
            // Create enmuerations hash-map.
            HashMap<String, ArrayList<String >> enumerations_hash_map = new HashMap<String, ArrayList<String>>();
            // Set up the query.
            stage9 = DAO.prepareStatement(this.getDropdownSQL_stage9);
            stage9Result = stage9.executeQuery();
            String last_type_name = "";
            while (stage9Result.next()) {
                String type_name = stage9Result.getString("typname");
                String enum_label = stage9Result.getString("enumlabel");
                if (!type_name.equals(last_type_name)) {
                    ArrayList<String> enum_list = new ArrayList<String>();
                    enumerations_hash_map.put(type_name, enum_list);
                    enumerations_hash_map.get(type_name).add(enum_label);
                } else {
                    enumerations_hash_map.get(type_name).add(enum_label);
                }
                last_type_name = type_name;
            }
            // Add the enumeration_hash_map to the drop-down container.
            vendorDropdownContainer.enumerations = enumerations_hash_map;
            /*
            Stage 10
            Spirits
             */
            // Create spirits hash-map.
            HashMap<Integer, Spirit> spiritHashMap = new HashMap<Integer, Spirit>();
            // Set up query.
            stage10 = DAO.prepareStatement(this.getDropdownSQL_stage10);
            stage10Result = stage10.executeQuery();
            int spiritCount = 0;
            while (stage10Result.next()) {
                Spirit spirit = new Spirit();
                spirit.id = stage10Result.getInt("id");
                spirit.brand_name = stage10Result.getString("brand_name");
                spirit.company_name = stage10Result.getString("company_name");
                spirit.spirit_type = stage10Result.getString("spirit_type");
                // Add the object to the hash-map of spirits.
                spiritHashMap.put(spiritCount, spirit);
                spiritCount++;
            }
            // Add the drop-down to the drop-downs container.
            vendorDropdownContainer.spirits = spiritHashMap;
            // Done. We've gotten all the dropdown resources we wanted. Add more here later
            // if you want them Return the object.
            return vendorDropdownContainer;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
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
            if (stage6 != null) {
                stage6.close();
            }
            if (stage6Result != null) {
                stage6Result.close();
            }
            if (stage7 != null) {
                stage7.close();
            }
            if (stage7Result != null) {
                stage7Result.close();
            }
            if (stage8 != null) {
                stage8.close();
            }
            if (stage8Result != null) {
                stage8Result.close();
            }
            if (stage9 != null) {
                stage9.close();
            }
            if (stage9Result != null) {
                stage9Result.close();
            }
            if (stage10 != null) {
                stage10.close();
            }
            if (stage10Result != null) {
                stage10Result.close();
            }
            // We inject the DAO to all private subroutines, do not close
            // this or else the calling function will not be able to use it.
        }
    }
}
