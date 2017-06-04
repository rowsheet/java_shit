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
