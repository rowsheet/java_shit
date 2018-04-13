package com.UserAccounts.NativeAuth;

import com.Common.AbstractModel;
import com.Common.UserCookie;
import com.Common.UserPermission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;
import java.security.SecureRandom;
import java.util.Base64;

import com.Email.EmailTemplates;
import com.sendgrid.*;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

/**
 * Created by alexanderkleinhans on 6/21/17.
 */
public class NativeAuthModel extends AbstractModel {

    /*
    Stage 1
    Get the password and hash where email matches.
     */
    private String userLoginSQL_stage1 =
            "SELECT " +
                    "   pass_hash, " +
                    "   salt, " +
                    "   id, " +
                    "   status " +
                    "FROM " +
                    "   accounts " +
                    "WHERE " +
                    "   email_address = ? " +
                    "AND " +
                    "   account_type = ?::account_type";

    /*
    Stage 2
    Create the session.
     */
    private String userLoginSQL_stage2 =
            "INSERT INTO" +
                    "   sessions " +
                    "(" +
                    "   session_key," +
                    "   account_id," +
                    "   ip_address" +
                    ") VALUES (" +
                    "?,?,?) " +
                    "ON CONFLICT (account_id, ip_address)" +
                    "DO UPDATE " +
                    "SET session_key = ?";

    /*
    Stage 3
    Fetch all user features where account_id matches.
     */
    private String userLoginSQL_stage3 =
            "SELECT " +
                    "   up.id, " +
                    "   up.name " +
                    "FROM " +
                    "   user_permissions up " +
                    "LEFT JOIN " +
                    "   user_account_permissions uap " +
                    "ON " +
                    "   up.id = uap.user_permission_id " +
                    "WHERE " +
                    "   uap.account_id = ?";

    private String confirmUserAccountSQL_stage1 =
            "UPDATE " +
                    "   accounts AS a " +
                    "SET " +
                    "   status = ?::account_status " +
                    "FROM " +
                    "   user_account_info AS uai " +
                    "WHERE " +
                    "   a.id = uai.account_id " +
                    "AND " +
                    "   uai.confirmation_code = ? " +
                    "RETURNING a.id";

    private String confirmUserAccountSQL_stage2 =
            "INSERT INTO " +
                    "   user_account_permissions" +
                    "( " +
                    "   account_id, " +
                    "   account_type, " +
                    "   user_permission_id" +
                    ") VALUES" +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?), " +
                    "(?,?::account_type,?) " +
                    // Although we already check the login count, still do nothing here.
                    "ON CONFLICT (account_id, user_permission_id) " +
                    "DO NOTHING";

    private String registerUserAccountSQL_stage1 =
            "INSERT INTO " +
                    "   accounts " +
                    "(" +
                    "   email_address, " +
                    "   pass_hash, " +
                    "   salt, " +
                    "   account_type, " +
                    "   status" +
                    ") VALUES (" +
                    "?,?,?,?::account_type,?::account_status) " +
                    "RETURNING id";

    private String registerUserAccountSQL_stage2 =
            "INSERT INTO " +
                    "   user_account_info " +
                    "(" +
                    "   account_id, " +
                    "   first_name, " +
                    "   last_name, " +
                    "   confirmation_code " +
                    ") VALUES (" +
                    "?,?,?,?)";

    private String userLogoutSQL_stage1 =
            "DELETE FROM" +
                    "   sessions " +
                    "WHERE " +
                    "   session_key = ?";

    private String checkUserSessionSQL_stage1 =
            "SELECT " +
                    "   creation_timestamp " +
                    "FROM " +
                    "   sessions " +
                    "WHERE " +
                    "   session_key = ? " +
                    "LIMIT 1";

    public NativeAuthModel() throws Exception {}

    private String OauthUserAuthorize_sql =
            "SELECT " +
                    "   confirmation_code " +
                    "FROM " +
                    "   user_account_info " +
                    "WHERE " +
                    "   account_id = ?";

    public UserCookie OAuthUserAuthorize(
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
            int account_id = this.registerUserAccount(
                    "Enter First Name",
                    "Enter Last Name",
                    oauth_guid, // for oauth, guid is email.
                    "", // Doesn't matter
                    true,
                    oauth_provider
            );
            // Step 2)
            // "Confirm"
            preparedStatement = this.DAO.prepareStatement(this.OauthUserAuthorize_sql);
            preparedStatement.setInt(1, account_id);
            resultSet = preparedStatement.executeQuery();
            String confirmation_code = "";
            while (resultSet.next()) {
                confirmation_code = resultSet.getString("confirmation_code");
            }
            account_id = this.confirmUserAccount(confirmation_code);
            // Step 3)
            // Login
            // This is a bad hack, but confirmation will close the DAO. Just create a new instance
            // of yourself and login.
            NativeAuthModel nativeAuthModel = new NativeAuthModel();
            return nativeAuthModel.userLogin(
                    oauth_guid,
                    "", // Doesn't matter.
                    "OAUTH", // Can't get it because it's from ouath callback, also doesn't matter because
                    // brute force will hit the ouath provider first. Not our problem.
                    true
            );
        } catch (Exception ex) {
            // Check for error about email address collision. This means it just needs to be
            // logged in.
            if (ex.getMessage().contains("email address already exists")) {
                return this.userLogin(
                        oauth_guid,
                        "", // Doesn't matter.
                        "OAUTH", // Can't get it because it's from ouath callback, also doesn't matter because
                        // brute force will hit the ouath provider first. Not our problem.
                        true
                );
            }
            throw new Exception("Unable to authenticate user oauth account.");
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
     * Register the user account, then register it's info with a confirmation code.
     * Ensure when first registering, the account has a status "email_verification_pending".
     *
     *      1) Register the account.
     *      2) Register the account info.
     *
     * Do this all in a transaction or roll back.
     *
     * @param first_name
     * @param last_name
     * @param email_address
     * @param password
     * @return
     * @throws Exception
     */

    public int registerUserAccount(
            String first_name,
            String last_name,
            String email_address,
            String password
    ) throws Exception {
        boolean is_oauth = false; // Set this default as false. Maybe true if called from overloaded function.
        String provider = "NO OAUTH"; // Doesn't matter.
        return this.registerUserAccount(
                first_name,
                last_name,
                email_address,
                password,
                is_oauth,
                provider
        );
    }

    public int registerUserAccount(
            String first_name,
            String last_name,
            String email_address,
            String password,
            boolean is_oauth,
            String oauth_provider
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
        // Do both stages in a transaction or roll back.
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Two things need to happen:
                1) Account needs to be registered.
                2) Account info needs to be registered (with confimration_code).
             */
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Create the statements.
            /*
            Stage 1
            Create the account.
             */
            if (is_oauth) {
                pash_hash = "OAUTH_" + oauth_provider;
                salt = "OAUTH_" + oauth_provider;
            }
            stage1 = this.DAO.prepareStatement(this.registerUserAccountSQL_stage1);
            stage1.setString(1, email_address);
            stage1.setString(2, pash_hash);
            stage1.setString(3, salt);
            stage1.setString(4, "user");
            // Note: If this was done with oauth, set the status to "email_verified".
            if (is_oauth) {
                stage1.setString(5, "email_verified");
            } else {
                stage1.setString(5, "email_verification_pending");
            }
            stage1Result = stage1.executeQuery();
            int account_id = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("id");
            }
            /*
            Stage 2
            Create the account info.
             */
            stage2 = this.DAO.prepareStatement(this.registerUserAccountSQL_stage2);
            stage2.setInt(1, account_id);
            stage2.setString(2, first_name);
            stage2.setString(3, last_name);
            stage2.setString(4, confimration_code);
            stage2.execute();
            /*
             Send email.
             */
/*
            EmailTemplates emailTemplates = new EmailTemplates();
            Email from = new Email("confirm_email@addesyn.com");
            from.setName("Email Confirmation");
            String subject = "Skiphopp Email Confirmation";
            Email to = new Email(email_address);
            Content content = new Content("text/html", emailTemplates.getUserConfirmationHtml(confimration_code));
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode()); // Should be 202.
*/
            /*
            Done. Commit it.
             */
            if (!is_oauth) { // Don't commit if oauth because confimration also needs to be done.
                this.DAO.commit();
            }
            return account_id;
        } catch (Exception ex) {
            // Roll back if something goes wrong.
            System.out.println(ex);
            if (this.DAO != null) {
                System.out.println("ROLLING BACK");
                this.DAO.rollback();
            }
            // Try to parse the exception.
            if (ex.getMessage().contains("duplicate key") &&
                    ex.getMessage().contains("accounts_email_address_key")) {
                throw new Exception("An account with that email address already exists!");
            }
            // Unknown error.
            throw new Exception("Unable to register user account.");
        } finally {
            // Clean up.
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (!is_oauth) { // Don't close this if oauth because there are other steps that will need it.
                if (this.DAO != null) {
                    this.DAO.close();
                }
            }
        }
    }

    /**
     * Fetch the password hash and salt where email matches, make sure that
     * it's correct. Create a session and return a cookie with all user permissions
     * for that account.
     *
     *      1) Unsure correct email and password.
     *      2) Create session.
     *      3) Return cookie with all permissions.
     *
     * Do this all in a transaction and roll back if anything goes wrong.
     * @param email_address
     * @param password
     * @return cookie
     * @throws Exception
     */
    public UserCookie userLogin(
            String email_address,
            String ip_address,
            String password
    ) throws Exception {
        return this.userLogin(
                email_address,
                ip_address,
                password,
                false
        );
    }

    private String oauth_stage1_sql =
        "SELECT " +
            "   id " +
            "FROM " +
            "   accounts " +
            "WHERE " +
            "   email_address = ?";

    /**
     * Logs the user in if they are a Native Auth  user or Passport user.
     * In the case they are a passport user, skip password is passed as true.
     * If this happens, the account_id MUST already be known.
     *
     * Exception is thrown if is_oauth is true and account_id = 0
     *
     * @param email_address
     * @param ip_address
     * @param password
     * @param is_oauth
     * @return
     * @throws Exception
     */
    private UserCookie userLogin(
            String email_address,
            String ip_address,
            String password,
            boolean is_oauth
    ) throws Exception {
        PreparedStatement stage1 = null;
        PreparedStatement oauth_stage1 = null;
        ResultSet stage1Result = null;
        ResultSet oauth_stage1_result = null;
        PreparedStatement stage2 = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            int account_id = 0;
            if (!is_oauth) {
                /*
                Stage 1
                Fetch pass-hasn and salt and ensure correct email and password.
                 */
                stage1 = this.DAO.prepareStatement(this.userLoginSQL_stage1);
                stage1.setString(1, email_address);
                stage1.setString(2, "user");
                stage1Result = stage1.executeQuery();
                String pass_hash = null;
                String salt = null;
                String status = null;
                while (stage1Result.next()) {
                    pass_hash = stage1Result.getString("pass_hash");
                    salt = stage1Result.getString("salt");
                    account_id = stage1Result.getInt("id");
                    status = stage1Result.getString("status");
                }
                // Make sure id is legit (if zero, email didn't match).
                if (account_id == 0) {
                    throw new NativeAuthException("Unrecognized email address.");
                }
                // Make sure status is valid.
                if (!status.equals("email_verified")) {
                    throw new NativeAuthException("Account not yet verified. Please check email for confirmation code.");
                }
                // Now make sure password is correct.
                String hash_from_provided_password = this.getHash(password, salt);
                if (!hash_from_provided_password.equals(pass_hash)) {
                    throw new NativeAuthException("Invalid password!");
                }
            } else {
                oauth_stage1 = this.DAO.prepareStatement(this.oauth_stage1_sql);
                oauth_stage1.setString(1, email_address);
                oauth_stage1_result = oauth_stage1.executeQuery();
                while (oauth_stage1_result.next()) {
                    account_id = oauth_stage1_result.getInt("id");
                }
                if (account_id == 0) {
                    throw new Exception("Unable to log in oauth user.");
                }
            }
            /*
            Stage 2
            Create the session.
             */
            // Generate a session_key for thew new session.
            SecureRandom random = new SecureRandom();
            byte random_bytes[] = new byte[50];
            random.nextBytes(random_bytes);
            String session_key = new String(Base64.getEncoder().encode(random_bytes));
            // Prepare statement.
            stage2 = this.DAO.prepareStatement(this.userLoginSQL_stage2);
            stage2.setString(1, session_key);
            stage2.setInt(2, account_id);
            stage2.setString(3, ip_address);
            stage2.setString(4, session_key);
            stage2.execute();
            /*
            Stage (before 3)
            Create a cookie because we're going to start adding to it.
             */
            UserCookie userCookie = new UserCookie();
            userCookie.userID = account_id;
            userCookie.sessionKey = session_key;
            userCookie.emailAddress = email_address;
            userCookie.userName = email_address; // Yeah I know.
            userCookie.userPermissions = new HashMap<String, UserPermission>();
            /*
            Stage 3
            Fetch all user permissions.
             */
            stage3 = this.DAO.prepareStatement(this.userLoginSQL_stage3);
            stage3.setInt(1, account_id);
            stage3Result = stage3.executeQuery();
            while (stage3Result.next()) {
                UserPermission userPermission = new UserPermission();
                userPermission.permission_id = stage3Result.getInt("id");
                userPermission.name = stage3Result.getString("name");
                userCookie.userPermissions.put(userPermission.name, userPermission);
            }
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return userCookie;
        } catch (NativeAuthException ex) {
            System.out.print(ex);
            // Just roll back and throw back up the chain.
            if (this.DAO != null) {
                System.out.println("ROLLING BACK!");
                this.DAO.rollback();
            }
            throw new NativeAuthException(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex);
            if (this.DAO != null) {
                System.out.println("ROLLING BACK");
                this.DAO.rollback();
            }
            throw new Exception("Unable to login user.");
        } finally {
            // Clean up.
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (oauth_stage1 != null)  {
                oauth_stage1.close();
            }
            if (oauth_stage1_result != null) {
                oauth_stage1_result.close();
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /**
     * Confirms a user account, usually with a confirmation code, but with the option of skipping this
     * for Passport Stratagey accounts where no confirmation code exists.
     *
     * In either case, finally inserts all permissions for this account.
     * @param confirmation_code
     * @return
     * @throws Exception
     */
    public int confirmUserAccount(
            String confirmation_code
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Set status for account = "email_verified".
             */
            stage1 = this.DAO.prepareStatement(this.confirmUserAccountSQL_stage1);
            stage1.setString(1, "email_verified");
            stage1.setString(2, confirmation_code);
            stage1Result = stage1.executeQuery();
            int account_id = 0;
            while (stage1Result.next()) {
                account_id = stage1Result.getInt("id");
            }
            if (account_id == 0) {
                throw new NativeAuthException("Unknown confirmation code.");
            }
            /*
            Stage 2
            Set all permissions for the account.
             */
            stage2 = this.DAO.prepareStatement(this.confirmUserAccountSQL_stage2);
            // @TODO Make this a map somewhere, probably in abstract model.
            // meetup_notifications
            stage2.setInt(1, account_id);
            stage2.setString(2, "user");
            stage2.setInt(3, 1);
            // event_notifications
            stage2.setInt(4, account_id);
            stage2.setString(5, "user");
            stage2.setInt(6, 2);
            // beer_notifications
            stage2.setInt(7, account_id);
            stage2.setString(8, "user");
            stage2.setInt(9, 3);
            // friends
            stage2.setInt(10, account_id);
            stage2.setString(11, "user");
            stage2.setInt(12, 4);
            // recieve_user_messages
            stage2.setInt(13, account_id);
            stage2.setString(14, "user");
            stage2.setInt(15, 5);
            // send_user_messages
            stage2.setInt(16, account_id);
            stage2.setString(17, "user");
            stage2.setInt(18, 6);
            // organize_meetups
            stage2.setInt(19, account_id);
            stage2.setString(20, "user");
            stage2.setInt(21, 7);
            // meetup_rsvp
            stage2.setInt(22, account_id);
            stage2.setString(23, "user");
            stage2.setInt(24, 8);
            // event_rsvp
            stage2.setInt(25, account_id);
            stage2.setString(26, "user");
            stage2.setInt(27, 9);
            // vendor_memberships
            stage2.setInt(28, account_id);
            stage2.setString(29, "user");
            stage2.setInt(30, 10);
            // vendor_blog_comment
            stage2.setInt(31, account_id);
            stage2.setString(32, "user");
            stage2.setInt(33, 11);
            // third_party_food_review
            stage2.setInt(34, account_id);
            stage2.setString(35, "user");
            stage2.setInt(36, 12);
            // third_party_reviews
            stage2.setInt(37, account_id);
            stage2.setString(38, "user");
            stage2.setInt(39, 13);
            // vendor_review
            stage2.setInt(40, account_id);
            stage2.setString(41, "user");
            stage2.setInt(42, 14);
            // beer_reviews
            stage2.setInt(43, account_id);
            stage2.setString(44, "user");
            stage2.setInt(45, 15);
            // vendor_food_review
            stage2.setInt(46, account_id);
            stage2.setString(47, "user");
            stage2.setInt(48, 16);
            // vendor_drink_review
            stage2.setInt(49, account_id);
            stage2.setString(50, "user");
            stage2.setInt(51, 17);
            stage2.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return account_id;
        } catch (NativeAuthException ex) {
            System.out.print(ex);
            throw new NativeAuthException(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex);
            if (ex.getMessage().contains("duplicate key") &&
                    ex.getMessage().contains("user_account_permissions_account_id_user_permission_id_idx")) {
                throw new Exception("The email address for this account has already been verified!");
            }
            throw new Exception("Unable to confirm user account.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /**
     * Just delete the key from the sesion table where the session key is specified.
     * If there is a problem, an exception is thrown. Return nothing
     *
     *      1) Set the account status to "email_verified".
     *      2) Insert all user_permissions into user_account_permissions.
     *
     * @param session_key
     * @throws Exception
     */

    public void userLogout(
            String session_key
    ) throws Exception {
        PreparedStatement stage1 = null;
        try {
            stage1 = this.DAO.prepareStatement(this.userLogoutSQL_stage1);
            stage1.setString(1, session_key);
            stage1.execute();
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to logout user.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
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
    public String checkUserSession(
            String session_key
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        try {
            stage1 = this.DAO.prepareStatement(checkUserSessionSQL_stage1);
            stage1.setString(1, session_key);
            stage1Result = stage1.executeQuery();
            String creation_timestamp = null;
            while (stage1Result.next()) {
                creation_timestamp = stage1Result.getString("creation_timestamp");
            }
            if (creation_timestamp == null) {
                throw new NativeAuthException("Invalid session key. No session found.");
            }
            return creation_timestamp;
        } catch (NativeAuthException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to check user session."); // unknown reason.
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
