package com.UserAccounts.GoogleAuth;

import com.Common.AbstractModel;
import com.Common.UserCookie;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

/**
 * Created by alexanderkleinhans on 6/21/17.
 */
public class GoogleAuthModel extends AbstractModel {

    private String googleSignInSQL_stage1 =
            "INSERT INTO " +
                "   accounts " +
                "(" +
                "   email_address, " +
                "   pass_hash, " +
                "   salt, " +
                "   account_type, " +
                "   status, " +
                "   username" +
                ") VALUES (" +
                "?,'NA','NA','user','email_verified',?) " +
                "ON CONFLICT (email_address) " +
                "DO UPDATE " +
                "   SET email_address = ? " + // Need to return id, so just update something...
                "RETURNING id";

    private String googleSignInQSL_stage2 =
            "INSERT INTO " +
                    "   sessions" +
                    "(" +
                    "   session_key, " +
                    "   account_id, " +
                    "   ip_address " +
                    ") VALUES (" +
                    "?,?,?) " +
                    "ON CONFLICT (account_id, ip_address) " +
                    "DO UPDATE " +
                    "   SET session_key = ?";

    public GoogleAuthModel() throws Exception {}

    /**
     * Attempt to create a new user. If a user with that email address already
     * exists, do nothing.
     *
     * Once it's know that a user exists, create a session for that user and return
     * the cookie.
     *
     *      1) Ensure registration.
     *      2) Create session.
     *
     * @param email_address
     * @param ip_address
     * @return userCookie
     * @throws Exception
     */
    public UserCookie GoogleSignIn(
            String email_address,
            String ip_address
    ) throws Exception {
        PreparedStatement stage1 = null;
        PreparedStatement stage2 = null;
        ResultSet stage1Result = null;
        try {
            /*
            Stage 1
            Ensure registration of email address.
             */
            stage1 = this.DAO.prepareStatement(this.googleSignInSQL_stage1);
            stage1.setString(1, email_address);
            stage1.setString(2, email_address);
            stage1.setString(3, email_address);
            stage1Result = stage1.executeQuery();
            int account_id = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("id");
            }
            /*
            Stage 2
            Create user session.
             */
            // Generate a new session key for this new account.
            SecureRandom random = new SecureRandom();
            byte random_bytes[] = new byte[50];
            random.nextBytes(random_bytes);
            String session_key = new String(Base64.getEncoder().encode(random_bytes));
            // Insert the record.
            stage2 = this.DAO.prepareStatement(this.googleSignInQSL_stage2);
            stage2.setString(1, session_key);
            stage2.setInt(2, account_id);
            stage2.setString(3, ip_address);
            stage2.setString(4, session_key);
            stage2.execute();
            // Create the user cookie to return
            UserCookie userCookie = new UserCookie();
            userCookie.emailAddress = email_address;
            userCookie.userID = account_id;
            userCookie.sessionKey = session_key;
            userCookie.userName = email_address; // Uh... take this up on oauth refactor.
            return userCookie;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to sign in Google user.");
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
            if (stage1Result != null) {
                stage1Result.close();
            }
        }
    }
}
