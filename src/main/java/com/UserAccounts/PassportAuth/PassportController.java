package com.UserAccounts.PassportAuth;

import com.Common.AbstractController;
import com.Common.UserCookie;

/**
 * Created by alexanderkleinhans on 7/11/17.
 */
public class PassportController extends AbstractController {

    public String passportSignIn (
            String passport_uid,
            String ip_address,
            String passport_strategy
    ) throws Exception {
        // Validate input parameters.
        this.validateString(passport_uid, "passport_uid");
        //@TODO validate ip address?
        // this.validateString(ip_address, "ip_address");
        // Initialize model and create the data.
        PassportModel passportModel = new PassportModel();
        UserCookie userCookie = passportModel.passportSignIn(
                passport_uid,
                ip_address,
                passport_strategy
        );
        return this.returnJSON(userCookie);
    }
}
