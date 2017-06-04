package com.Common;

import java.sql.*;
import java.security.MessageDigest;
import java.util.Base64;

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
     * Takes a cookie as a JSON string, a certain featureID and
     * returns the vendorID and featureID if the cookie has that
     * feature set. If not, insufficient permissions exception will
     * be raised.
     * @param cookie
     * @param feature_id
     * @throws Exception
     */

    protected void validateCookieVendorFeature(String cookie, String feature_id)
        throws Exception {
        /*
        When a vendor sends a request, it needs to be validated. The request will come in with a cookie. That cookie
        will be used to look up a session variable. That session variable will be parsed to check for weather or
        not it has a certain feature_id.

        Once this is done, three pieces of information need to be parsed and added to the vendorCookie.
        1) the vendor_id
        2) the feature_id
        3) the account_id of the request.

        If a cookie is not found and cannot look up a session, an VendorAuthenticationException will be thrown.
        If a feature is not found in the session, a VendorAuthenticationException will be thrown.

        This procedure will haven in N steps:

        1) An attempt to look up session, vendor_id, and account_id via the cookie will occur.
        2) An attempt to parse the feature_id from the session will occur.
        3) vendorCookie will have attributes assigned and it will be returned.
         */

        // 1) Attempt to look up session, vendor_id, and account_id via cookie.
        PreparedStatement preparedStatement = null;
        String lookup_sql =
                "SELECT " +
                        "   "
        this.vendorCookie.vendorID = 1;
        this.vendorCookie.featureID = 1;
        this.vendorCookie.accountID = 2;
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
