package com.VendorAccounts;

import com.VendorAccounts.BreweryRegistration.BreweryRegistrationController;
import com.VendorAccounts.VendorAuthentication.VendorAuthenticationController;
import com.VendorAccounts.General.GeneralController;

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
            @WebParam(name = "password") String password,
            @WebParam(name = "ip_address") String ip_address
    ) throws Exception {
        VendorAuthenticationController vendorAuthenticationController = new VendorAuthenticationController();
        return vendorAuthenticationController.vendorLogin(
                email_address,
                password,
                ip_address
        );
    }

    /**
     * Checks if a vendor session is legit from a cookie. Returns timestamp of
     * when it was created if it's legit, otherwise throws exception.
     *
     * All state should be held in the cookie because this is a REST API.
     *
     * @param session_key
     * @return session_valid (true if valid, false if invalid)
     * @throws Exception
     */
    @WebMethod
    public String checkVendorSession(
            @WebParam(name = "session_key") String session_key
    ) throws Exception {
        VendorAuthenticationController vendorAuthenticationController = new VendorAuthenticationController();
        return vendorAuthenticationController.checkVendorSession(
            session_key
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
    public boolean updateBreweryInfo (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "display_name") String display_name,
            @WebParam(name = "about_text") String about_text,
            @WebParam(name = "mon_open") String mon_open,
            @WebParam(name = "mon_close") String mon_close,
            @WebParam(name = "tue_open") String tue_open,
            @WebParam(name = "tue_close") String tue_close,
            @WebParam(name = "wed_open") String wed_open,
            @WebParam(name = "wed_close") String wed_close,
            @WebParam(name = "thu_open") String thu_open,
            @WebParam(name = "thu_close") String thu_close,
            @WebParam(name = "fri_open") String fri_open,
            @WebParam(name = "fri_close") String fri_close,
            @WebParam(name = "sat_open") String sat_open,
            @WebParam(name = "sat_close") String sat_close,
            @WebParam(name = "sun_open") String sun_open,
            @WebParam(name = "sun_close") String sun_close,
            @WebParam(name = "street_address") String street_address,
            @WebParam(name = "city") String city,
            @WebParam(name = "state") String state,
            @WebParam(name = "zip") String zip,
            @WebParam(name = "public_phone") int public_phone,
            @WebParam(name = "public_email") String public_email
    ) throws Exception {
        GeneralController generalController = new GeneralController();
        return generalController.updateBreweryInfo(
                cookie,
                display_name,
                about_text,
                mon_open,
                mon_close,
                tue_open,
                tue_close,
                wed_open,
                wed_close,
                thu_open,
                thu_close,
                fri_open,
                fri_close,
                sat_open,
                sat_close,
                sun_open,
                sun_close,
                street_address,
                city,
                state,
                zip,
                public_phone,
                public_email
        );
    }
}
