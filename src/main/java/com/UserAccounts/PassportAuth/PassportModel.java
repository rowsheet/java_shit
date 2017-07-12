package com.UserAccounts.PassportAuth;

import com.UserAccounts.NativeAuth.NativeAuthModel;
import com.Common.UserCookie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by alexanderkleinhans on 7/11/17.
 */
public class PassportModel extends NativeAuthModel {

    public PassportModel() throws Exception {}

    private String passportSignInSQL =
            "INSERT INTO " +
                    "   accounts " +
                    "(" +
                    // Email address is actually passport UID.
                    "   email_address, " +
                    "   pass_hash, " +
                    "   salt, " +
                    "   account_type, " +
                    "   status, " +
                    "   username," +
                    "   login_count " +
                    ") VALUES (" +
                    // If the user is logging in for the first time, set login count = 1
                    "?,?,?,'user','email_verified',?, 1) " +
                    "ON CONFLICT (email_address) " +
                    "DO UPDATE SET " +
                    "   email_address = ? ," +
                    // Increment the login count.
                    "   login_count = accounts.login_count + 1 " +
                    // Need to return id, so just update something...
                    "RETURNING id, login_count";

    public UserCookie passportSignIn (
            String passport_uid,
            String ip_address,
            String passport_strategy
    ) throws Exception {
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        ResultSet stage1Result = null;
        try {
            /*
            Stage 1
            Ensure registration of email address.
             */
            stage1 = this.DAO.prepareStatement(this.passportSignInSQL);
            stage1.setString(1, passport_uid);
            stage1.setString(2, passport_strategy);
            stage1.setString(3, passport_strategy);
            stage1.setString(4, passport_uid);
            stage1.setString(5, passport_uid);
            stage1Result = stage1.executeQuery();
            int account_id = 0;
            int login_count = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("id");
                login_count = stage1Result.getInt("login_count");
            }
            /*
            Stage 2
            We only need to do this if the user is a new user. If they are not (login_count != 1), then
            we skip this step.

            "Confirm" user account (meaning insert all records and use base class
            "confirmation" method to legitimize this account with permissions by inserting
            records into the user_account_permissions table while using the "skip_confirmaion_code"
            flag.
             */
            if (login_count == 1) {
                this.confirmUserAccount(account_id);
            }
            /*
            Stage 3
            Create user session, returning the cookie.
             */
            return this.userLogin(
                    passport_uid,
                    ip_address,
                    account_id
            );
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to sign in " + passport_strategy + " user.");
        } finally {
            if (this.DAO != null) {
                this.DAO.close();
            }
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
        }
    }
}
