package com.VendorAccounts.BreweryRegistration;

import com.Common.AbstractModel;
import com.sun.org.apache.regexp.internal.RE;

import java.util.Base64;

import java.sql.*;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.SecureRandom;

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
      */
    private String confirmBreweryAccountSQL_stage2 =
            "UPDATE" +
                    "   accounts " +
                    "SET" +
                    "   status = ?::account_status " +
                    "WHERE" +
                    "   id = ?";

    public BreweryRegistrationModel() throws Exception {}

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
            // Create an association with both known ids, then commit the transaction.
            stage3.setInt(1, account_id);
            stage3.setString(2, "vendor");
            stage3.setInt(3, vendor_id);
            stage3.execute();
            this.DAO.commit();
            //@TODO Send a confimration email.
            // Return the new vendor_id.
            return vendor_id;
        } catch (Exception ex) {
            // Roll back the transaction if anything has cone wrong.
            // Clean up.
            if (this.DAO != null) {
                System.out.println("ROLLING BACK");
                this.DAO.rollback();
            }
            throw new Exception("Unable to register vendor.");
        } finally {
            if (stage1 != null) { stage1 = null; }
            if (stage2 != null) { stage2 = null; }
            if (stage3 != null) { stage3 = null; }
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
     * @param confirmation_code
     * @return account_id
     * @throws Exception
     */

    public int confirmBreweryAccount(
            String confirmation_code
    ) throws Exception {
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Two things need to happen, otherwise transaction is rolled back:
            1) Vendor with confirmation_code needs to have status set to "preview".
            2) Account owning that vendor needs to have status set to "email_verified".
             */
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Create the statements.
            stage1 = this.DAO.prepareStatement(this.confirmBreweryAccountSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.confirmBreweryAccountSQL_stage2);
            // Set status for vendor = "preview" returning account_id.
            stage1.setString(1, "preview");
            stage1.setString(2, confirmation_code);
            ResultSet stage1Result = stage1.executeQuery();
            int account_id = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("account_id");
            }
            // Set status for account = "email_verified".
            stage2.setString(1, "email_verified");
            stage2.setInt(2, account_id);
            stage2.execute();
            // Return the account_id.
            return account_id;
        } catch (Exception ex) {
            // Roll back the transaction if anything has gone wrong.
            // Clean up.
            if (this.DAO != null) {
                System.out.println(ex);
                this.DAO.rollback();
            }
            throw new Exception("Unable to confirm brewery account.");
        } finally {
            if (stage1 != null) { stage1 = null; }
            if (stage2 != null) { stage2 = null; }
            this.DAO.setAutoCommit(true);
        }
    }
}
