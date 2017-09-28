package com.UserAccounts;

import com.UserAccounts.NativeAuth.NativeAuthController;
import com.UserAccounts.PassportAuth.PassportController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class UserAccountsHandler {

    /**
     * Register a user and set the account_status to "email_verification_pending".
     *
     * Return the user_id.
     *
     * @param first_name
     * @param last_name
     * @param email_address
     * @param password
     * @param confirm_password
     * @param over_21
     * @return account_id
     * @throws Exception
     */
    @WebMethod
    public int registerUserAccount (
            @WebParam(name="first_name") String first_name,
            @WebParam(name="last_name") String last_name,
            @WebParam(name="email_address") String email_address,
            @WebParam(name="password") String password,
            @WebParam(name="confirm_password") String confirm_password,
            @WebParam(name="over_21") boolean over_21
    ) throws Exception {
        NativeAuthController nativeAuthController = new NativeAuthController();
        return nativeAuthController.registerUserAccount(
                first_name,
                last_name,
                email_address,
                password,
                confirm_password,
                over_21
        );
    }

    /**
     * Confirms the user account. Sets the account_status to "email_verified".
     *
     * @param confirmation_code
     * @return account_id
     * @throws Exception
     */
    @WebMethod
    public int confirmUserAccount (
            @WebParam(name="confirmation_code") String confirmation_code
    ) throws Exception {
        NativeAuthController nativeAuthController = new NativeAuthController();
        return nativeAuthController.confirmUserAccount(
                confirmation_code
        );
    }

    /**
     * Logs vendor in where email_address and password are valid, returning cookie of new session.
     *
     * @param email_address
     * @param password
     * @return userCookie
     */
    @WebMethod
    public String userLogin(
            @WebParam(name="email_address") String email_address,
            @WebParam(name="ip_address") String ip_address,
            @WebParam(name="password") String password
    ) throws Exception {
        NativeAuthController nativeAuthController = new NativeAuthController();
        return nativeAuthController.userLogin(
            email_address,
            ip_address,
            password
        );
    }

    /**
     * Checks if a user session is legit from a cookie. Returns timestamp of
     * when it was created if it's legit, otherwise throws exception.
     *
     * All state should be held in the cookie because this is a REST API.
     *
     * @param session_key
     * @return session_valid (true if valid, false if invalid)
     * @throws Exception
     */
    @WebMethod
    public String checkUserSession(
            @WebParam(name = "session_key") String session_key
    ) throws Exception {
        NativeAuthController nativeAuthController = new NativeAuthController();
        return nativeAuthController.checkUserSession(
                session_key
        );
    }

    /**
     * Log a user out where the session key matchs. Throw an exception if there
     * is a problem.
     *
     * @param session_key
     * @throws Exception
     */
    @WebMethod
    public void userLogout(
            @WebParam(name="session_key") String session_key
    ) throws Exception {
        NativeAuthController nativeAuthController = new NativeAuthController();
        nativeAuthController.userLogout(session_key);
    }

    /**---------------------------------------------------/
     *
     *      PASSPORT SHIT
     *
     * For passport authentication, there is no confirmation process that
     * needs to take palace. Instead, there is a single method which does one of
     * two things:
     *
     *      1) Registers the account if not account exists, then registers a session.
     *      2) Registers the session only if an account already exists.
     *
     * In either case, under the assumption of a correct response from the third-party
     * server, an account and session will be registered returning a session key.
     ----------------------------------------------------*/

    /**
     * Sign the user in via a Google email address.
     *
     * @param email_address
     * @param ip_address
     * @throws Exception
     */
    @WebMethod
    public String googleSignIn (
            @WebParam(name="email_address") String email_address,
            @WebParam(name="ip_address") String ip_address
    ) throws Exception {
        PassportController passportController = new PassportController();
        return passportController.passportSignIn(
                email_address,
                ip_address,
                "GooglePassport"
        );
    }

    /**
     * Sign the user in via a Twitter username.
     *
     * @param username
     * @param ip_address
     * @return
     * @throws Exception
     */
    @WebMethod
    public String twitterSignIn(
            @WebParam(name="username") String username,
            @WebParam(name="ip_address") String ip_address
    ) throws Exception {
        PassportController passportController = new PassportController();
        return passportController.passportSignIn(
                "@" + username, // Add the @ since it's twitter.
                ip_address,
                "TwitterPassport"
        );
    }

}
