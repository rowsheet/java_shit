package com.UserAccounts.NativeAuth;

import com.Common.AbstractController;
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
}
