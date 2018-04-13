package com.VendorAccounts.BreweryRegistration;

import com.Common.AbstractModel;

import java.util.Base64;

import java.sql.*;
import java.util.UUID;
import java.security.SecureRandom;

import com.Common.VendorCookie;
import com.Email.EmailTemplates;
import com.VendorAccounts.VendorAuthentication.VendorAuthenticationModel;
import com.sendgrid.*;

/**
 * Created by alexanderkleinhans on 6/2/17.
 */
public class BreweryRegistrationModel extends AbstractModel {

    /*
     Register the account.
      */
    private String registerBreweryAccountSQL_stage1 =
            "INSERT INTO" +
                    "   accounts" +
                    "(" +
                    "   email_address," +
                    "   pass_hash," +
                    "   salt," +
                    "   account_type," +
                    "   status" +
                    ") VALUES (" +
                    "?,?,?,?::account_type,?::account_status)" +
                    "RETURNING id";

    /*
     Register the vendor.
      */
    private String registerBreweryAccountSQL_stage2 =
            "INSERT INTO" +
                    "   vendors" +
                    "(" +
                    "   official_business_name," +
                    "   status," +
                    "   account_id," +
                    "   street_address," +
                    "   city," +
                    "   state," +
                    "   zip," +
                    "   primary_contact_first_name," +
                    "   primary_contact_last_name," +
                    "   confirmation_code" +
                    ") VALUES (" +
                    "?,?::vendor_status,?,?,?,?::us_state,?,?,?,?)" +
                    "RETURNING id";

    /*
     Register the vendor account association.
      */
    private String registerBreweryAccountSQL_stage3 =
            "INSERT INTO" +
                    "   vendor_account_associations" +
                    "(" +
                    "   account_id," +
                    "   account_type," +
                    "   vendor_id" +
                    ") VALUES (" +
                    "?,?::account_type,?)";

    private String registerBreweryAccountSQL_stage4_A =
            "INSERT INTO " +
                    "   beer_categories " +
                    "(" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color " +
                    ") VALUES (?,'default','#ffffff') " +
                    "RETURNING id";

    private String registerBreweryAccountSQL_stage4_B =
            "INSERT INTO " +
                    "   vendor_food_categories " +
                    "(" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color " +
                    ") VALUES (?,'default','#ffffff') " +
                    "RETURNING id";

    private String registerBreweryAccountSQL_stage4_C =
            "INSERT INTO " +
                    "   vendor_drink_categories " +
                    "(" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color " +
                    ") VALUES (?,'default','#ffffff') " +
                    "RETURNING id";

    private String registerBreweryAccountSQL_stage4_D =
            "INSERT INTO " +
                    "   event_categories " +
                    "(" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color " +
                    ") VALUES (?,'default','#ffffff') " +
                    "RETURNING id";

    private String registerBreweryAccountSQL_stage4_E =
            "INSERT INTO " +
                    "   vendor_page_image_galleries " +
                    "(" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color " +
                    ") VALUES (?,'default','#000000') " +
                    "RETURNING id";

    /*
     Set the vendor status to "preview" returning the account_id
     associated with that vendor.
      */
    private String confirmBreweryAccountSQL_stage1 =
            "UPDATE" +
                    "   vendors " +
                    "SET" +
                    "   status = ?::vendor_status " +
                    "WHERE" +
                    "   confirmation_code = ? " +
                    "RETURNING account_id;";

    /*
     Set the account status to "email_verified" where account_id
     corresponds with vendor with confirmation code in stage 1.

     Return the vendor_id that is owned by that account.
      */
    private String confirmBreweryAccountSQL_stage2 =
            "UPDATE" +
                    "   accounts AS acc " +
                    "SET" +
                    "   status = ?::account_status " +
                    "FROM" +
                    "   vendors AS ven " +
                    "WHERE" +
                    "   acc.id = ven.account_id " +
                    "AND" +
                    "   acc.id = ? " +
                    "RETURNING ven.id AS vendor_id";

    /*
    Insert all of the vendor features for this vendor with status "preview".
     */
    private String confirmBreweryAccountSQL_stage3 =
            "INSERT INTO" +
                    "   vendor_feature_associations " +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   feature_status" +
                    ") VALUES " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status), " +
                    "(?,?,?::feature_status)";

    public BreweryRegistrationModel() throws Exception {
    }

    private String OauthVendorAuthorize_sql =
    "SELECT " +
            "   confirmation_code " +
            "FROM " +
            "   vendors " +
            "WHERE " +
            "   id = ?";

    /**
     * Register a brewery account like normal (with fake values and blank passwords).
     * Confirm the brewery account.
     *
     * This will be almost like registering an account normally, except that primary_email_address
     * of the vendor (also used as account email address and used in login) will be the oauth_guid.
     *
     * Instead of something like a registration for ExampleBrewing@gmail.com => password:1234
     *
     * accounts:
     *      email: example_brewing@gmail.com
     *      password: 1234
     * vendors:
     *      primary_email: example_brewing@gmail.com
     *
     * You will use oauth_guid instead.
     *
     * accounts:
     *      email: twitter_oauth_123241242342342
     *      password: [doesn't matter, never used, so never hashed, and none will match]
     * vendors:
     *      primary_email: twitter_oauth_123241242342342
     *
     * Note that the account record will never change the oauth_guid as email since that is used to authenticate (and
     * should only be called from the web-server, never as a publically open API), but it is possible for the
     * vendor table record for "primary_email" to be changed (although this is never used in authentication).
     * @param oauth_guid
     * @return
     * @throws Exception
     */
    public String OauthVendorAuthorize(
        String oauth_guid,
        String oauth_provider
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /*
            1) Register
            2) "Confirm"
            3) Login
             */
            // Step 1)
            // Register
            int vendor_id = this.registerBreweryAccount(
                    "Enter Official Business Name",
                    "Enter First Name",
                    "Enter Last Name",
                    oauth_guid, // for oauth, guid is email.
                    "", // Doesn't matter.
                    "", // Doesn't matter.
                    "Enter Street Address",
                    "Enter City",
                    "CO", // will have to do something about this.
                    "Enter ZIP",
                    true, // is oauth.
                    oauth_provider);
            // Step 2)
            // "Confirm"
            preparedStatement = this.DAO.prepareStatement(OauthVendorAuthorize_sql);
            preparedStatement.setInt(1, vendor_id);
            resultSet = preparedStatement.executeQuery();
            String confirmation_code = "";
            while (resultSet.next()) {
                confirmation_code = resultSet.getString("confirmation_code");
            }
            vendor_id = this.confirmBreweryAccount(confirmation_code);
            // Step 3)
            // Login
            VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
            return vendorAuthenticationModel.vendorLogin(
                    oauth_guid,
                    "", // Doesn't matter.
                    "OAUTH", // Can't get it because it's from ouath callback, also doesn't matter because
                    // brute force will hit the ouath provider first. Not our problem.
                    true
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Check for error about email address collision. This means it just needs to be
            // logged in.
            if (ex.getMessage().contains("email already exists")) {
                VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
                return vendorAuthenticationModel.vendorLogin(
                        oauth_guid,
                        "", // Doesn't matter.
                        "OAUTH", // Can't get it because it's from ouath callback, also doesn't matter because
                        // brute force will hit the ouath provider first. Not our problem.
                        true
                );
            }
            throw new Exception("Unable to authenticate vendor oauth account.");
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
     * Register a new brewery vendor account (or a request for one).
     *
     * 1)   Registers an account, set the status to "email_verification_pending".
     *
     *      note:
     *
     *          1) Account already exists, wrong type.
     *          2) Account already exists, status pending, please check email.
     *          3) Account already exists, status active, go to login.
     *
     * 2)   Registers a vendor, set the status to "pending".
     *
     *      note:
     *
     *          vendors have a unique constraint on primary_email_address and official_busienss_name.
     *          primary_email_address is constraint to accounts, so it's impossible for an email to be
     *          used already unless it's already registered with another account.
     *
     * 3)   Registers a vendor account association.
     *
     *      note:
     *
     *          It's impossible for the account to be associated to another
     *          vendor because the account would have to exist before this procedure
     *          started and the vendor would have to exist before this procedure started.
     *
     * 4)   Insert default categories into three major resources (Beer, Food, Drinks).
     *
     * 5)   Send confirmation email.
     *
     * @param official_business_name
     * @param primary_contact_first_name
     * @param primary_contact_last_name
     * @param primary_email
     * @param password
     * @param confirm_password
     * @param street_address
     * @param city
     * @param state
     * @param zip
     * @return vendor_id
     * @throws Exception
     */

    public int registerBreweryAccount(
            String official_business_name,
            String primary_contact_first_name,
            String primary_contact_last_name,
            String primary_email,
            String password,
            String confirm_password,
            String street_address,
            String city,
            String state,
            String zip
    ) throws Exception {
        boolean is_oauth = false; // Set this default as false. Maybe true if called from overloaded function.
        String provider = "NO OAUTH"; // Doesn't matter.
        return this.registerBreweryAccount(
                official_business_name,
                primary_contact_first_name,
                primary_contact_last_name,
                primary_email,
                password,
                confirm_password,
                street_address,
                city,
                state,
                zip,
                is_oauth,
                provider
        );
    }


    public int registerBreweryAccount(
            String official_business_name,
            String primary_contact_first_name,
            String primary_contact_last_name,
            String primary_email,
            String password,
            String confirm_password,
            String street_address,
            String city,
            String state,
            String zip,
            boolean is_oauth,
            String oauth_provider // defaults to false in non-overloaded function.
    ) throws Exception {
        // Generate a new salt for this new account.
        SecureRandom random = new SecureRandom();
        byte random_bytes[] = new byte[50];
        random.nextBytes(random_bytes);
        String salt = new String(Base64.getEncoder().encode(random_bytes));
        // Generate the hash in combination with the salt and password.
        // The underlying function does nested SHA-256.
        String pash_hash = this.getHash(password, salt);
        // Generate a random confimration code.
        String confimration_code = UUID.randomUUID().toString();
        // Do a transaction to commit all three stages and rollback on exception.
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        // Insert default categories.
        PreparedStatement stage4_A = null;
        ResultSet stage4_A_result_set = null;
        PreparedStatement stage4_B = null;
        ResultSet stage4_B_result_set = null;
        PreparedStatement stage4_C = null;
        ResultSet stage4_C_result_set = null;
        PreparedStatement stage4_D = null;
        ResultSet stage4_D_result_set = null;
        PreparedStatement stage4_E = null;
        ResultSet stage4_E_result_set = null;
        try {
            /*
            Four things need to happen in a transaction, otherwise the entire
            procedure is rolled back.
            1) An account with a "vendor" type needs to be created and an account_id returned.
            2) A vendor constrained to that account_id needs to be created and a vendor_id returned.
            3) A vendor-account association needs to be created with those ids.
            4) A confirmation email needs to be sent with the confimration_code for that vendor.
             */
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Create the statements.
            stage1 = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage3);
            stage4_A = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage4_A);
            stage4_B = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage4_B);
            stage4_C = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage4_C);
            stage4_D = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage4_D);
            stage4_E = this.DAO.prepareStatement(this.registerBreweryAccountSQL_stage4_E);
            /*
            Stage 1)
             */
            // Prepare and execute the first statement to get the account_id.
            // Note: If this was done with oauth, passwords are not a thing. Do not hash anything. Just enter
            // plaintext "OAUTH" for hash and salt so hash will never match. It won't be able to authetnicate.
            if (is_oauth) {
                pash_hash = "OAUTH_" + oauth_provider;
                salt = "OAUTH_" + oauth_provider;
            }
            stage1.setString(1, primary_email);
            stage1.setString(2, pash_hash);
            stage1.setString(3, salt);
            stage1.setString(4, "vendor");
            // Note: If this was done with oauth, set the status to "email_verified".
            if (is_oauth) {
                stage1.setString(5, "email_verified");
            } else {
                stage1.setString(5, "email_verification_pending");
            }
            ResultSet stage1Result = stage1.executeQuery();
            int account_id = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("id");
            }
            /*
            Stage 2)
             */
            // Prepare and execute the second statement using that account_id to get a vendor_id.
            stage2.setString(1, official_business_name);
            stage2.setString(2, "pending");
            stage2.setInt(3, account_id);
            stage2.setString(4, street_address);
            stage2.setString(5, city);
            stage2.setString(6, state);
            stage2.setString(7, zip);
            stage2.setString(8, primary_contact_first_name);
            stage2.setString(9, primary_contact_last_name);
            stage2.setString(10, confimration_code);
            ResultSet stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("id");
            }
            /*
            Stage 3)
             */
            // Create an association with both known ids, then commit the transaction.
            stage3.setInt(1, account_id);
            stage3.setString(2, "vendor");
            stage3.setInt(3, vendor_id);
            stage3.execute();
            /*
            Stage 4)
             */
            // Insert default categories into major resources (Beers, Foods, Events, Drinks).
            // Default beer category.
            int beer_category_id = 0;
            stage4_A.setInt(1, vendor_id);
            stage4_A_result_set = stage4_A.executeQuery();
            while(stage4_A_result_set.next()) {
                beer_category_id = stage4_A_result_set.getInt("id");
            }
            if (beer_category_id == 0) {
                throw new BreweryRegistrationException("Registration failure: Unable to create default beer category. Aborting.");
            }
            // Default food category.
            int food_category_id = 0;
            stage4_B.setInt(1, vendor_id);
            stage4_B_result_set = stage4_B.executeQuery();
            while(stage4_B_result_set.next()) {
                food_category_id = stage4_B_result_set.getInt("id");
            }
            if (food_category_id == 0) {
                throw new BreweryRegistrationException("Registration failure: Unable to create default food category. Aborting.");
            }
            // Default drink category.
            int drink_category_id = 0;
            stage4_C.setInt(1, vendor_id);
            stage4_C_result_set = stage4_C.executeQuery();
            while(stage4_C_result_set.next()) {
                drink_category_id = stage4_C_result_set.getInt("id");
            }
            if (drink_category_id == 0) {
                throw new BreweryRegistrationException("Registration failure: Unable to create default drink category. Aborting.");
            }
            // Default event category.
            int event_category_id = 0;
            stage4_D.setInt(1, vendor_id);
            stage4_D_result_set = stage4_D.executeQuery();
            while(stage4_D_result_set.next()) {
                event_category_id = stage4_D_result_set.getInt("id");
            }
            if (event_category_id == 0) {
                throw new BreweryRegistrationException("Registration failure: Unable to create default event category. Aborting.");
            }
            // Default image gallery.
            int image_gallery_id = 0;
            stage4_E.setInt(1, vendor_id);
            stage4_E_result_set = stage4_E.executeQuery();
            while(stage4_E_result_set.next()) {
                image_gallery_id = stage4_E_result_set.getInt("id");
            }
            if (image_gallery_id == 0) {
                throw new BreweryRegistrationException("Registration failure: Unable to create default image gallery. Aborting.");
            }
            /*
            Stage 5)
            Send email.
             */
/*
            EmailTemplates emailTemplates = new EmailTemplates();
            Email from = new Email("confirm_brewery@addesyn.com");
            from.setName("Brewery Confirmation");
            String subject = "Skiphopp Brewery Email Confirmation";
            Email to = new Email(primary_email);
            Content content = new Content("text/html", emailTemplates.getBrewerConfirmationHtml(confimration_code));
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response);
*/
            /*
            Commmit and return
             */
            if (!is_oauth) { // Don't commit if oauth because confimration also needs to be done.
                this.DAO.commit();
            }
            // Return the new vendor_id.
            return vendor_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse error.
            if (ex.getMessage().contains("accounts_email_address_key") &&
                    ex.getMessage().contains("already exists.")) {
                throw new Exception("An account with that email already exists.");
            } else {
                throw new Exception("Unable to register brewery at this time.");
            }
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (stage4_A != null) {
                stage4_A.close();
            }
            if (stage4_A_result_set != null) {
                stage4_A_result_set.close();
            }
            if (stage4_B != null) {
                stage4_B.close();
            }
            if (stage4_B_result_set != null) {
                stage4_B_result_set.close();
            }
            if (stage4_C != null) {
                stage4_C.close();
            }
            if (stage4_C_result_set != null) {
                stage4_C_result_set.close();
            }
            if (stage4_D != null) {
                stage4_D.close();
            }
            if (stage4_D_result_set != null) {
                stage4_D_result_set.close();
            }
            if (stage4_E != null) {
                stage4_E.close();
            }
            if (stage4_E_result_set != null) {
                stage4_E_result_set.close();
            }
            if (!is_oauth) { // Don't close this if oauth because there are other steps that will need it.
                if (this.DAO != null) {
                    this.DAO.close();
                }
            }
        }
    }

    /**
     * Confirm the brewer account with the confirmation code and set the status of the
     * vendor to "preview" and the account associated to it to "email_verified".
     *
     * 1) Set the status of the vendor to "preview" returning the account_id that owns it.
     *
     * 2) Set the status of the account ot "email_verified" where account_id is returned above.
     *
     * 3) Insert all vendor_features into vendor_feature_associations with status "preview" for this vendor.
     *
     * @param confirmation_code
     * @return account_id
     * @throws Exception
     */

    public int confirmBreweryAccount(
            String confirmation_code
    ) throws Exception {
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Three things need to happen, otherwise transaction is rolled back:
            1) Vendor with confirmation_code needs to have status set to "preview".
            2) Account owning that vendor needs to have status set to "email_verified".
            3) All features need to be set into vendor_feature_associations with status "preview".
             */
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Create the statements.
            stage1 = this.DAO.prepareStatement(this.confirmBreweryAccountSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.confirmBreweryAccountSQL_stage2);
            stage3 = this.DAO.prepareStatement(this.confirmBreweryAccountSQL_stage3);
            /*
            Stage 1)
             */
            // Set status for vendor = "preview" returning account_id.
            stage1.setString(1, "preview");
            stage1.setString(2, confirmation_code);
            ResultSet stage1Result = stage1.executeQuery();
            int account_id = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("account_id");
            }
            if (account_id == 0) {
                // There was no account for that confirmation code.
                throw new BreweryRegistrationException("Unrecognized brewery confirmation code.");
            }
            /*
            Stage 2)
             */
            // Set status for account = "email_verified".
            stage2.setString(1, "email_verified");
            stage2.setInt(2, account_id);
            ResultSet stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            /*
            Stage 3)
             */
            // Set all features in vendor_feature_associations with status "preview".
            // @TODO Make this a map somewhere, probably in abstract model.
            // blog
            stage3.setInt(1, vendor_id);
            stage3.setInt(2, 1);
            stage3.setString(3, "preview");
            // memberships
            stage3.setInt(4, vendor_id);
            stage3.setInt(5, 2);
            stage3.setString(6, "preview");
            // promotions
            stage3.setInt(7, vendor_id);
            stage3.setInt(8, 3);
            stage3.setString(9, "preview");
            // food_menu
            stage3.setInt(10, vendor_id);
            stage3.setInt(11, 4);
            stage3.setString(12, "preview");
            // beer_menu
            stage3.setInt(13, vendor_id);
            stage3.setInt(14, 5);
            stage3.setString(15, "enabled");
            // events
            stage3.setInt(16, vendor_id);
            stage3.setInt(17, 6);
            stage3.setString(18, "preview");
            // @TODO Finish up 7 and 8 for third_party later.
            // vendor_reviews.
            stage3.setInt(19, vendor_id);
            stage3.setInt(20, 9);
            stage3.setString(21, "enabled");
            // vendor_page_images_20 (0-20 images)
            stage3.setInt(22, vendor_id);
            stage3.setInt(23, 10);
            stage3.setString(24, "enabled");
            // vendor_page_images
            stage3.setInt(25, vendor_id);
            stage3.setInt(26, 11);
            stage3.setString(27, "enabled");
            // vendor_beer_images
            stage3.setInt(28, vendor_id);
            stage3.setInt(29, 12);
            stage3.setString(30, "enabled");
            // vendor_food_images
            stage3.setInt(31, vendor_id);
            stage3.setInt(32, 13);
            stage3.setString(33, "enabled");
            // vendor_event_images
            stage3.setInt(34, vendor_id);
            stage3.setInt(35, 14);
            stage3.setString(36, "enabled");
            // drink_menu
            stage3.setInt(37, vendor_id);
            stage3.setInt(38, 15);
            stage3.setString(39, "enabled");
            // vendor_drink_images
            stage3.setInt(40, vendor_id);
            stage3.setInt(41, 16);
            stage3.setString(42, "enabled");
            // vendor_food_ingredients
            stage3.setInt(43, vendor_id);
            stage3.setInt(44, 17);
            stage3.setString(45, "enabled");
            // vendor_drink_ingredients
            stage3.setInt(46, vendor_id);
            stage3.setInt(47, 18);
            stage3.setString(48, "enabled");
            // vendor_beer_ingredients
            stage3.setInt(49, vendor_id);
            stage3.setInt(50, 19);
            stage3.setString(51, "enabled");
            // nutritional_facts
            stage3.setInt(52, vendor_id);
            stage3.setInt(53, 20);
            stage3.setString(54, "enabled");
            stage3.execute();
            /*
            Commit and return)
             */
            this.DAO.commit();
            // Return the vendor_id.
            return vendor_id;
        } catch (BreweryRegistrationException ex) {
            System.out.println(ex);
            // This exception needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex);
            // Try to parse exception.
            if (ex.getMessage().contains("vendor_feature_associations_venodr_id_feature_id_idx") &&
                ex.getMessage().contains("already exists.")) {
                throw new Exception("Account with this confirmation code has already been confirmed. Please log in.");
            }
            throw new Exception("Unable to confirm brewery account.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
