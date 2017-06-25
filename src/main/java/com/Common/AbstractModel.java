package com.Common;

import java.sql.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

import com.google.gson.*;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class AbstractModel {

    protected Connection DAO;
    protected UserCookie userCookie;
    protected VendorCookie vendorCookie;

    public AbstractModel()
            throws Exception {
        // Initialize data access object.
        Class.forName("org.postgresql.Driver");
        this.DAO = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/skiphopp",
                        "alexanderkleinhans", "");
        // @TODO only initialize on of these and put the account type in the constructor.
        this.userCookie = new UserCookie();
        this.vendorCookie = new VendorCookie();
    }

    protected void cleanupDatabase() {
        if (this.DAO != null) {
            this.DAO = null;
        }
    }

    /**
     * Takes a cookie as a JSON string, a certain permission
     * and returns the userID and userPermissionID if the
     * user permission is there. If not, an insufficient
     * permissions exception will be raised.
     * @param cookie
     * @param user_permission_id
     * @return UserCookie
     */

    protected void validateCookiePermission(String cookie, String user_permission_id)
        throws Exception {
        this.userCookie.userID = 1;
        this.userCookie.userPermissionID = 11;
    }

    /**
     * Takes a cookie as a JSON string and just retuns the
     * userID. If there is no userID, and exception will
     * be thrown.
     * @param cookie
     * @return
     */

    protected int validateCookie(String cookie)
        throws Exception {
        return 1;
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
        System.out.println("cookie from model");
        System.out.println(cookie);
        Gson gson = new Gson();
        this.vendorCookie = gson.fromJson(cookie, VendorCookie.class);
        System.out.println(this.vendorCookie.sessionKey);
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
