package com.VendorAccounts;

import com.VendorAccounts.BreweryRegistration.BreweryRegistrationController;
import com.VendorAccounts.VendorAuthentication.VendorAuthenticationController;
import com.VendorAccounts.VendorAuthentication.VendorAuthenticationModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class VendorAccountsHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    /**
     * Creates a "Request" for a vendor account.
     *
     * 1) Creates a vendor account with status "pending".
     * 2) Creates and account owning that vendor with status "email_verification_pending".
     * 3) Creates a UUID for confirmation.
     *
     * The UUID will be emailed to the email address and used in the confimration function.
     *
     * @param official_business_name
     * @param primary_contact_first_name
     * @param primary_contact_last_name
     * @param primary_email
     * @param street_address
     * @param city
     * @param state
     * @param zip
     * @return vendor_id
     * @throws Exception
     */

    @WebMethod
    public int registerBreweryAccount(
            @WebParam(name = "official_business_name") String official_business_name,
            @WebParam(name = "primary_contact_first_name") String primary_contact_first_name,
            @WebParam(name = "primary_contact_last_name") String primary_contact_last_name,
            @WebParam(name = "primary_email") String primary_email,
            @WebParam(name = "password") String password,
            @WebParam(name = "confirm_password") String confirm_password,
            @WebParam(name = "street_address") String street_address,
            @WebParam(name = "city") String city,
            @WebParam(name = "state") String state,
            @WebParam(name = "zip") String zip
    ) throws Exception {
        BreweryRegistrationController breweryRegistrationController = new BreweryRegistrationController();
        return breweryRegistrationController.registerBreweryAccount(
                official_business_name,
                primary_contact_first_name,
                primary_contact_last_name,
                primary_email,
                password,
                confirm_password,
                street_address,
                city,
                state,
                zip
        );
    }

    /**
     * Confirms the vendor account with the confirmation code (UUID).
     *
     * 1) Sets vendor to status "preview".
     * 2) Sets account owning that vendor to status "email_verified".
     *
     * @param confimation_code
     * @return account_id
     * @throws Exception
     */

    @WebMethod
    public int confirmBreweryAccount(
            @WebParam(name = "confirmation_code") String confimation_code
    ) throws Exception {
        BreweryRegistrationController breweryRegistrationController = new BreweryRegistrationController();
        return breweryRegistrationController.confirmBreweryAccount(
                confimation_code
        );
    }

    /**
     * Logs vendor in where email_address and password are valid, returning cookie of new session.
     *
     * @param email_address
     * @param password
     * @return cookie
     * @throws Exception
     */

    @WebMethod
    public String vendorLogin(
            @WebParam(name = "email_address") String email_address,
            @WebParam(name = "password") String password
    ) throws Exception {
        VendorAuthenticationController vendorAuthenticationController = new VendorAuthenticationController();
        return vendorAuthenticationController.vendorLogin(
                email_address,
                password
        );
    }

    /**
     * Logs a vendor out assuming the seesion_key is correct.
     *
     * @param cookie
     * @return
     * @throws Exception
     */

    @WebMethod
    public String vendorLogout(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        VendorAuthenticationController vendorAuthenticationController = new VendorAuthenticationController();
        return vendorAuthenticationController.vendorLogout(
                cookie
        );
    }

    @WebMethod
    public int createVendorAccountAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "email_address") String email_address,
            @WebParam(name = "vendor_id") int vendor_id
    ) throws Exception {
        return 1;
    }
}
