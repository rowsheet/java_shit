package com.Common;

import java.sql.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.ConcurrentModificationException;
import java.util.Map;

import com.google.gson.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.security.jgss.GSSCaller;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class AbstractModel {

    private Connection SessionDAO;
    protected Connection DAO;
    protected UserCookie userCookie;
    protected VendorCookie vendorCookie;

    public AbstractModel()
            throws Exception {
        // Get DB credentials from env vars.
        String db_username = System.getenv("DBUSERNAME");
        String db_password = System.getenv("DBPASSWORD");
        // Initialize data access object.
        Class.forName("org.postgresql.Driver");
        this.DAO = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/skiphopp",
                        db_username, db_password);
        // Initialize data access object for sesseions.
        // In the future, this will be ported to Redis.
        Class.forName("org.postgresql.Driver");
        this.SessionDAO = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/skiphopp",
                        db_username, db_password);
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
}
