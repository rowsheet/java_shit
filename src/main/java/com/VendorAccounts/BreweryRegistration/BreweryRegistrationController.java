package com.VendorAccounts.BreweryRegistration;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/2/17.
 */
public class BreweryRegistrationController extends AbstractController {

    public int registerBreweryAccount (
            String official_business_name,
            String primary_contact_first_name,
            String primary_contact_last_name,
            String primary_email,
            String password,
            String confirm_password,
            String street_address,
            String city,
            String state,
            String zip
    ) throws Exception {
        // Validate input parameters.
        this.validateString(official_business_name, "Official Business Name");
        this.validateString(primary_contact_first_name, "Primary Contact First Name");
        this.validateString(primary_contact_last_name, "Primary Contact Last Name");
        this.validateEmailAddress(primary_email);
        this.validatePasswordPair(password, confirm_password);
        this.validateString(street_address, "Street Address");
        this.validateString(city, "City");
        this.validateState(state);
        this.validateZipCode(zip);
        // Initialize model and create the data.
        BreweryRegistrationModel breweryRegistrationModel = new BreweryRegistrationModel();
        return breweryRegistrationModel.registerBreweryAccount(
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

    public int confirmBreweryAccount(
            String confirmation_code
    ) throws Exception {
        // Validate input parameters.
        this.validateUUID(confirmation_code);
        // Initialize model and process.
        BreweryRegistrationModel breweryRegistrationModel = new BreweryRegistrationModel();
        return breweryRegistrationModel.confirmBreweryAccount(
                confirmation_code
        );
    }

}
