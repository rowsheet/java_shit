package com.VendorAccounts.VendorAuthentication;

import com.Common.*;

import com.VendorAccounts.General.GeneralModel;
import com.google.gson.*;
import com.sun.org.apache.regexp.internal.RE;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/3/17.
 */
public class VendorAuthenticationModel extends AbstractModel {

    /*
    Stage 1
    Fetch pass and hash where email address match
     */
    private String vendorLoginSQL_stage1 =
            "SELECT " +
                    "   pass_hash," +
                    "   salt," +
                    "   id," +
                    "   status " +
                    "FROM " +
                    "   accounts " +
                    "WHERE " +
                    "   email_address = ? " +
                    "AND" +
                    "   account_type = ?::account_type " +
                    "LIMIT 1";

    /*
    Stage 2
    Create session for vendor.
     */
    private String vendorLoginSQL_stage2 =
            "INSERT INTO" +
                    "   sessions " +
                    "(" +
                    "   session_key," +
                    "   account_id," +
                    "   ip_address " +
                    ") VALUES (" +
                    "?,?,?) " +
                    "ON CONFLICT (account_id, ip_address)" +
                    "DO UPDATE " +
                    "SET session_key = ?";

    /*
    Stage 3
    Fetch all features for vendor.
     */
    private String vendorLoginSQL_stage3 =
            "SELECT " +
                    "   vaa.vendor_id, " +
                    "   vf.name, " +
                    "   vfa.feature_id, " +
                    "   vfa.feature_status " +
                    "FROM " +
                    "   vendor_feature_associations vfa " +
                    "INNER JOIN " +
                    "   vendor_account_associations vaa " +
                    "ON " +
                    "   vfa.vendor_id = vaa.vendor_id " +
                    "INNER JOIN " +
                    "   vendor_features vf " +
                    "ON " +
                    "   vfa.feature_id = vf.id " +
                    "WHERE " +
                    "   vaa.account_id = ?";


    private String vendorLogoutSQL_stage1 =
            "DELETE FROM" +
                    "   sessions " +
                    "WHERE" +
                    "   session_key = ?";

    private String checkVendorSessionSQL_stage1 =
            "SELECT" +
                    " creation_timestamp" +
                    "   FROM " +
                    "sessions" +
                    "   WHERE " +
                    "session_key = ?";

    public VendorAuthenticationModel () throws Exception {}

    /**
     * Try to delete rows from the session table and return a status "success" of any rows affected.
     * Throw and exception if now rows are affected.
     *
     * 1) Parse the cookie.
     * 2) Delete the record where session_key matches.
     *
     * @param cookie
     * @return logout_status
     * @throws Exception
     */
    public String vendorLogout(
        String cookie
    ) throws Exception {
        String session_key = this.parseSessionKey(cookie);
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.vendorLogoutSQL_stage1);
        try {
            preparedStatement.setString(1, session_key);
            if (preparedStatement.executeUpdate() < 1) {
                throw new Exception("Unable to log out, unknown session key.");
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            throw new Exception("Unable to log out."); // unknown reason.
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
        return "success";
    }

    private String getDropdownSQL_stage1 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color " +
                    "FROM " +
                    "   vendor_food_categories " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage2 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color " +
                    "FROM " +
                    "   beer_categories " +
                    "WHERE " +
                    "   vendor_id = ?";

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
                    "   tag_type " +
                    "FROM " +
                    "   vendor_food_tags " +
                    "WHERE " +
                    "   vendor_id = ?";

    private String getDropdownSQL_stage6 =
            "SELECT " +
                    "   id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type " +
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
     *
     * @param vendor_id
     * @return
     * @throws Exception
     */
    private VendorDropdownContainer getDropdowns(
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
        VendorDropdownContainer vendorDropdownContainer = new VendorDropdownContainer();
        try {
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
                System.out.println(stage1Result.getString("name"));
                VendorFoodCategory vendorFoodCategory = new VendorFoodCategory();
                vendorFoodCategory.vendor_food_category_id = stage1Result.getInt("id");
                vendorFoodCategory.name = stage1Result.getString("name");
                vendorFoodCategory.hex_color = stage1Result.getString("hex_color");
                vendorFoodCategory.vendor_id = vendor_id;
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
                beerCategory.beer_category_id = stage2Result.getInt("id");
                beerCategory.name = stage2Result.getString("name");
                beerCategory.hex_color = stage2Result.getString("hex_color");
                beerCategory.vendor_id = vendor_id;
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
            while (stage2Result.next()) {
                VendorDrinkCategory vendorDrinkCategory = new VendorDrinkCategory();
                vendorDrinkCategory.vendor_drink_category_id = stage3Result.getInt("id");
                vendorDrinkCategory.name = stage3Result.getString("name");
                vendorDrinkCategory.hex_color = stage3Result.getString("hex_color");
                vendorDrinkCategory.icon_enum = stage3Result.getString("icon_enum");
                vendorDrinkCategory.vendor_id = vendor_id;
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
                vendorFoodTag.vendor_food_tag_id = stage5Result.getInt("id");
                vendorFoodTag.name = stage5Result.getString("name");
                vendorFoodTag.hex_color = stage5Result.getString("hex_color");
                vendorFoodTag.tag_type = stage5Result.getString("tag_type");
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
                beerTag.beer_tag_id = stage6Result.getInt("id");
                beerTag.name = stage6Result.getString("name");
                beerTag.hex_color = stage6Result.getString("hex_color");
                beerTag.tag_type = stage6Result.getString("tag_type");
                beerTag.vendor_id = vendor_id;
                // Add the object the hash-map of drop-downs.
                beerTags.put(beerTagCount, beerTag);
                beerCategoriesCount++;
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
                vendorDrinkTag.vendor_drink_tag_id = stage7Result.getInt("id");
                vendorDrinkTag.name = stage7Result.getString("name");
                vendorDrinkTag.hex_color = stage7Result.getString("hex_color");
                vendorDrinkTag.tag_type = stage7Result.getString("tag_type");
                vendorDrinkTag.vendor_id = this.vendorCookie.vendorID;
                // Add the object the hash-map of drop-downs.
                drinkTags.put(drinkTagCount, vendorDrinkTag);
                drinkTagCount++;
            }
            // Add the dropdown to drop-downs container.
            vendorDropdownContainer.drinkTags = drinkTags;
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
            // We inject the DAO to all private subroutines, do not close
            // this or else the calling function will not be able to use it.
        }
    }

    /**
     * Log the vendor account in and return the cookie for that new session.
     *
     * 1A) Fetches the pass_hash and salt for account where email_address matches and where account_type = "vendor".
     *
     * 1B) Compares the hash to hash generated by password and salt found.
     *
     * 1C) Make sure the account status is verified.
     *
     * 2) Creates a session for that account with the randomly generated session_key.
     *
     * 3) Fetch all the features for the vendor associated with that account.
     *
     * 4) Create a REST cookie with all features, account_id, vendor_id, and drop-downs and return it.
     *
     * 5) Add the drop-downs to the cookie (I know this is a little awkward).
     *
     * @param email_address
     * @param password
     * @return cookie
     * @throws Exception
     */

    public String vendorLogin(
            String email_address,
            String password,
            String ip_address
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Create the statements.
            stage1 = this.DAO.prepareStatement(this.vendorLoginSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.vendorLoginSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.vendorLoginSQL_stage3);
            /*
            Stage 1A)
             */
            // Fetches the pass_hash and salt for account where email_address matches and account_type = "vendor".
            stage1.setString(1, email_address);
            stage1.setString(2, "vendor");
            stage1Result = stage1.executeQuery();
            String pass_hash = null;
            String salt = null;
            int account_id = 0;
            String account_status = null;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("id");
                salt = stage1Result.getString("salt");
                pass_hash = stage1Result.getString("pass_hash");
                account_status = stage1Result.getString("status");
            }
            if (account_id == 0) {
                throw new VendorAuthenticationException("No matching vendor account found.");
            }
            /*
            Stage 1B)
             */
            String hash_from_provided_password = this.getHash(password, salt);
            if (!hash_from_provided_password.equals(pass_hash)) {
                throw new VendorAuthenticationException("Valid account found but invalid password match.");
            }
            /*
            Stage 1C)
             */
            if (account_status.equals("email_verification_pending")) {
                throw new VendorAuthenticationException("Sorry! This email address hasn't been verified yet.");
            }
            /*
            Stage 2)
             */
            // Generate a session_key for thew new session.
            SecureRandom random = new SecureRandom();
            byte random_bytes[] = new byte[50];
            random.nextBytes(random_bytes);
            String session_key = new String(Base64.getEncoder().encode(random_bytes));
            // Set statement variables.
            stage2.setString(1, session_key);
            stage2.setInt(2, account_id);
            stage2.setString(3, ip_address);
            stage2.setString(4, session_key);
            stage2.execute();
            /*
            Stage 3)
             */
            stage3.setInt(1, account_id);
            stage3Result = stage3.executeQuery();
            int vendor_id = 0;
            HashMap<String , VendorFeature> stringVendorFeatureHashMap = new HashMap<String, VendorFeature>();
            while (stage3Result.next()) {
                VendorFeature vendorFeature = new VendorFeature();
                String name = null;
                name = stage3Result.getString("name");
                vendor_id = stage3Result.getInt("vendor_id");
                vendorFeature.feature_id = stage3Result.getInt("feature_id");
                vendorFeature.feature_status = stage3Result.getString("feature_status");
                vendorFeature.name = name;
                stringVendorFeatureHashMap.put(name, vendorFeature);
            }
            /*
            Stage 4)
             */
            VendorCookie vendorCookie = new VendorCookie();
            vendorCookie.sessionKey = session_key;
            vendorCookie.accountID = account_id;
            vendorCookie.vendorID = vendor_id;
            vendorCookie.vendorFeatures = stringVendorFeatureHashMap;
            Gson gson = new Gson();
            String cookie = gson.toJson(vendorCookie);
            /*
            Stage 5)
             */
            // This is a little awkward, but we will need to re-JSONify the cookie since
            // we need to get the drop-downs (using the cookie string).
            GeneralModel generalModel = new GeneralModel();
            vendorCookie.dropDowns = this.getDropdowns(vendorCookie.vendorID, this.DAO);
            // Now re-JSONify the cookie.
            cookie = gson.toJson(vendorCookie);
            // Done.
            return cookie;
        } catch (VendorAuthenticationException ex) {
            // Roll back the transaction if anything has cone wrong.
            // Clean up.
            if (this.DAO != null) {
                System.out.println(ex);
                System.out.println("ROLLING BACK");
                this.DAO.rollback();
            }
            // Re-throw the exception, pass all the way back up.
            throw new VendorAuthenticationException(ex.getMessage());
        } catch (Exception ex) {
            // Roll back the transaction if anything has cone wrong.
            // Clean up.
            if (this.DAO != null) {
                System.out.println(ex);
                System.out.println("ROLLING BACK");
                this.DAO.rollback();
            }
            throw new Exception("Unable to log in vendor.");
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
            if (stage3 != null) {
                stage3.close();
            }
            if (stage3Result != null) {
                stage3Result.close();
            }
            this.DAO.setAutoCommit(true);
        }
    }

    /**
     * Checks the session and returns the timestamp of the session based on a
     * session-key if it exists. If not, throws an exception.
     *
     * @param session_key
     * @return timestamp
     * @throws Exception
     */
    public String checkVendorSession (
            String session_key
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet resultSet = null;
        try {
            stage1 = this.DAO.prepareStatement(this.checkVendorSessionSQL_stage1);
            stage1.setString(1, session_key);
            resultSet = stage1.executeQuery();
            String creation_timestamp = null;
            while (resultSet.next()) {
                creation_timestamp = resultSet.getString("creation_timestamp");
            }
            if (creation_timestamp == null) {
                // No results found.
                throw new Exception("No session found.");
            }
            return creation_timestamp;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to check session.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

}
