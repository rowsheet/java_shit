package com.UserAccounts.GoogleAuth;

import com.Common.AbstractController;
import com.Common.UserCookie;

/**
 * Created by alexanderkleinhans on 6/21/17.
 */
public class GoogleAuthController extends AbstractController {

    public String GoogleSignIn(
            String email_address,
            String ip_address
    ) throws Exception {
        // Validate input parameters.
        this.validateEmailAddress(email_address);
        //@TODO validate ip address?
        // this.validateString(ip_address, "ip_address");
        // Initialize model and create the data.
        GoogleAuthModel googleAuthModel = new GoogleAuthModel();
        UserCookie userCookie = googleAuthModel.GoogleSignIn(
                email_address,
                ip_address
        );
        return this.returnJSON(userCookie);
    }

}
