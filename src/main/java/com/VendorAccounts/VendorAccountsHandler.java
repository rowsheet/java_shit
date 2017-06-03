package com.VendorAccounts;

import com.VendorAccounts.BreweryRegistration.BreweryRegistrationController;
import com.VendorAccounts.BreweryRegistration.BreweryRegistrationModel;

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
     * Creates a "Request" for a new brewery by creating a vendor entity with the status "pending".
     * Will also create a random string for email confirmation.
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

    @WebMethod
    public int confirmBreweryAccount(
            @WebParam(name = "confirmation_code") String confimation_code
    ) throws Exception {
        return 1;
    }

    @WebMethod
    public int vendorLogin(
            @WebParam(name = "email_address") String email_address,
            @WebParam(name = "password") String password
    ) throws Exception {
        return 1;
    }

    @WebMethod
    public int readVendorSession(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        return 1;
    }

    @WebMethod
    public int updateVendorSession(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "session_string") String session_string
    ) throws Exception {
        return 1;
    }

    @WebMethod
    public int vendorLogout(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        return 1;
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
