package com.UserAccounts.NativeAuth;

import com.Common.AbstractController;
import com.Common.Account;
import com.Common.UserCookie;

/**
 * Created by alexanderkleinhans on 6/21/17.
 */
public class NativeAuthController extends AbstractController {

    public int registerUserAccount (
            String first_name,
            String last_name,
            String email_address,
            String password,
            String confirm_password,
            boolean over_21
    ) throws Exception {
        // Validate input parameters.
        this.validateString(first_name, "First Name");
        this.validateString(last_name, "Last Name");
        this.validateEmailAddress(email_address);
        this.validatePasswordPair(password, confirm_password);
        if (!over_21) {
            throw new Exception("You must be at least 21 to use this beer app!");
        }
        // Initialize model and create the data.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        return nativeAuthModel.registerUserAccount(
                first_name,
                last_name,
                email_address,
                password
        );
    }

    public int confirmUserAccount(
            String confirmation_code
    ) throws Exception {
        // Validate input parameters.
        this.validateUUID(confirmation_code);
        // Initialize model and create the data.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        return nativeAuthModel.confirmUserAccount(
                confirmation_code
        );
    }

    public String userLogin(
            String email_address,
            String ip_address,
            String password
    ) throws Exception {
        // Validate input parameters.
        this.validateEmailAddress(email_address);
        this.validateString(password, "password");
        this.validateString(ip_address, "ip_address");
        // Initialize model and create the data.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        UserCookie userCookie = nativeAuthModel.userLogin(
            email_address,
            ip_address,
            password
        );
        return this.returnJSON(userCookie);
    }

    public String checkUserSession(
            String session_key
    ) throws Exception {
        // Validate input parameters.
        this.validateString(session_key, "session_key");
        // Initialize model and create the data.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        return nativeAuthModel.checkUserSession(
                session_key
        );
    }

    public void userLogout(
            String session_key
    ) throws Exception {
        // Validate input parameters.
        this.validateString(session_key, "session_key");
        // Initialize model and create the data.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        nativeAuthModel.userLogout(session_key);
    }

    public String OAuthUserAuthorize(
            String oauth_guid,
            String oauth_provider
    ) throws Exception {
        // Validate input params.
        this.validateString(oauth_guid, "oauth_guid");
        this.validateString(oauth_provider, "oauth_provider");
        // Model things.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        UserCookie userCookie = nativeAuthModel.OAuthUserAuthorize(
                oauth_guid,
                oauth_provider
        );
        return this.returnJSON(userCookie);
    }

    public String loadUserAccountProfile(
            String cookie
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        // Fetch data.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        Account account = nativeAuthModel.loadUserAccountProfile(
                cookie
        );
        return this.returnJSON(account);
    }

    public boolean updateUserAccountInfo (
            String cookie,
            String public_first_name,
            String public_last_name,
            String about_me
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        this.validateString(public_first_name, "public_first_name");
        this.validateString(public_last_name, "public_last_name");
        this.validateString(about_me, "about_me");
        // Model shit.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        return nativeAuthModel.updateUserAccountInfo(
                cookie,
                public_first_name,
                public_last_name,
                about_me
        );
    }

    public boolean uploadUserAccountProfilePicture(
            String cookie,
            String filename
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Model shit.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        return nativeAuthModel.uploadUserAccountProfilePicture(
                cookie,
                filename
        );
    }

    public boolean deleteUserAccountProfilePicture(
            String cookie
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        // Model shit.
        NativeAuthModel nativeAuthModel = new NativeAuthModel();
        return nativeAuthModel.deleteUserAccountProfilePicture(
                cookie
        );
    }
}
