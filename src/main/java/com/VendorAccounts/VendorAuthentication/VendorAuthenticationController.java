package com.VendorAccounts.VendorAuthentication;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/3/17.
 */
public class VendorAuthenticationController extends AbstractController {

        public String vendorLogin(
                String email_address,
                String password
        ) throws Exception {
                // Validate input parameters.
                this.validateEmailAddress(email_address);
                this.validateString(password, "password");
                // Initialize model and create the data.
                VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
                return vendorAuthenticationModel.vendorLogin(
                        email_address,
                        password
                );
        }

}
