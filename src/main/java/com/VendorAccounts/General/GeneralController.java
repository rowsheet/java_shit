package com.VendorAccounts.General;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/24/17.
 */
public class GeneralController extends AbstractController {
    public boolean updateBreweryInfo(
        String cookie,
        String display_name,
        String about_text,
        String mon_open,
        String mon_close,
        String tue_open,
        String tue_close,
        String wed_open,
        String wed_close,
        String thu_open,
        String thu_close,
        String fri_open,
        String fri_close,
        String sat_open,
        String sat_close,
        String sun_open,
        String sun_close,
        String street_address,
        String city,
        String state,
        String zip,
        int public_phone,
        String public_email
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(display_name, "display_name");
        this.validateText(about_text, "about_text");
        this.validateTime(mon_open, "monday open");
        this.validateTime(mon_close, "monday close");
        this.validateTime(mon_open, "tuesday open");
        this.validateTime(mon_close, "tuesday close");
        this.validateTime(mon_open, "wednesday open");
        this.validateTime(mon_close, "wednesday close");
        this.validateTime(mon_open, "thursday open");
        this.validateTime(mon_close, "thursday close");
        this.validateTime(mon_open, "friday open");
        this.validateTime(mon_close, "friday close");
        this.validateTime(mon_open, "saturday open");
        this.validateTime(mon_close, "saturday close");
        this.validateTime(mon_open, "sunday open");
        this.validateTime(mon_close, "sunday close");
        this.validateString(street_address, "street_address");
        this.validateString(city, "city");
        this.validateState(state);
        this.validateZipCode(zip);
        this.validatePhone(public_phone);
        this.validateEmailAddress(public_email);
        // Initialize model and create the data.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.updateBreweryInfo(
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