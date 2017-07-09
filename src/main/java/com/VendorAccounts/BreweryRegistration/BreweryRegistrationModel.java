package com.VendorAccounts.BreweryRegistration;

import com.Common.AbstractModel;

import java.util.Base64;

import java.sql.*;
import java.util.UUID;
import java.security.SecureRandom;

import com.Email.EmailTemplates;
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
                    "(?,?,?::feature_status)";

    public BreweryRegistrationModel() throws Exception {
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
     * 4)   Send confirmation email.
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
            /*
            Stage 1)
             */
            // Prepare and execute the first statement to get the account_id.
            stage1.setString(1, primary_email);
            stage1.setString(2, pash_hash);
            stage1.setString(3, salt);
            stage1.setString(4, "vendor");
            stage1.setString(5, "email_verification_pending");
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
             Send email.
             */
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
            /*
            Commmit and return
             */
            this.DAO.commit();
            // Return the new vendor_id.
            return vendor_id;
        } catch (Exception ex) {
            // Roll back the transaction if anything has cone wrong.
            // Clean up.
            if (this.DAO != null) {
                System.out.println(ex);
                System.out.println("ROLLING BACK");
                this.DAO.rollback();
            }
            // Try to parse error.
            if (ex.getMessage().contains("accounts_email_address_key") &&
                    ex.getMessage().contains("already exists.")) {
                throw new Exception("An account with that email already exists.");
            } else {
                throw new Exception("Unable to register brewery at this time.");
            }
        } finally {
            if (stage1 != null) {
                stage1 = null;
            }
            if (stage2 != null) {
                stage2 = null;
            }
            if (stage3 != null) {
                stage3 = null;
            }
            this.DAO.setAutoCommit(true);
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
            stage3.execute();
            /*
            Commit and return)
             */
            this.DAO.commit();
            // Return the vendor_id.
            return vendor_id;
        } catch (BreweryRegistrationException ex) {
            // Roll back the transaction if anything has gone wrong.
            System.out.println(ex);
            if (this.DAO != null) {
                this.DAO.rollback();
            }
            // This exception needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex);
            // Roll back the transaction if anything has gone wrong.
            // Clean up.
            if (this.DAO != null) {
                System.out.println(ex);
                this.DAO.rollback();
            }
            // Try to parse exception.
            if (ex.getMessage().contains("vendor_feature_associations_venodr_id_feature_id_idx") &&
                ex.getMessage().contains("already exists.")) {
                throw new Exception("Account with this confirmation code has already been confirmed. Please log in.");
            }
            throw new Exception("Unable to confirm brewery account.");
        } finally {
            if (stage1 != null) {
                stage1 = null;
            }
            if (stage2 != null) {
                stage2 = null;
            }
            this.DAO.setAutoCommit(true);
        }
    }
}
