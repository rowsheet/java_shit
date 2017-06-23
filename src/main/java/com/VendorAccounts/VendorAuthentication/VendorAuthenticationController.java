package com.VendorAccounts.VendorAuthentication;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/3/17.
 */
public class VendorAuthenticationController extends AbstractController {

        public String vendorLogin(
                String email_address,
                String password,
                String ip_address
        ) throws Exception {
                // Validate input parameters.
                this.validateEmailAddress(email_address);
                this.validateString(password, "password");
                // Initialize model and create the data.
                VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
                return vendorAuthenticationModel.vendorLogin(
                        email_address,
                        password,
                        ip_address
                );
        }

        public String checkVendorSession(
                String session_key
        ) throws Exception {
                // Validate input parameters.
                this.validateString(session_key, "session_key");
                // Initialize model and create the data.
                VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
                return vendorAuthenticationModel.checkVendorSession(
                        session_key
                );
        }

        public String vendorLogout(
                String cookie
        ) throws Exception {
                // Validate input parameters.
                this.validateString(cookie, "cookie");
                // Initialize model and create the data.
                VendorAuthenticationModel vendorAuthenticationModel = new VendorAuthenticationModel();
                return vendorAuthenticationModel.vendorLogout(
                        cookie
                );
        }
}
