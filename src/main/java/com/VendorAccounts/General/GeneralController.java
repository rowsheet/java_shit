package com.VendorAccounts.General;

import com.Common.AbstractController;
import com.Common.VendorAccount;
import com.sun.tools.javah.Gen;

/**
 * Created by alexanderkleinhans on 6/24/17.
 */
public class GeneralController extends AbstractController {

    public boolean updateShortCode(
            String cookie,
            String short_code
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateString(short_code, "short_code");
        if (short_code.length() > 16) {
            throw new Exception("Short code too long.");
        }
        // This is in the regex, but I wan't a specific error message.
        if (short_code.charAt(0) == '-' || short_code.charAt(short_code.length() - 1) == '-') {
            throw new Exception("Short code cannot start or end in dash.");
        }
        this.validateShortCode(short_code);
        GeneralModel generalModel = new GeneralModel();
        return generalModel.updateShortCode(
                cookie,
                short_code
        );
    }

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
        String public_phone,
        String public_email,
        String[] brewery_has,
        String[] brewery_friendly,
        String short_type_description,
        String short_text_description
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
        this.validateString(short_type_description, "short_type_description");
        this.validateString(short_text_description, "short_text_description");
        if (short_text_description.length() > 64) {
            throw new Exception("Short text description too long.");
        }
        if (short_type_description.length() > 32) {
            throw new Exception("Short type description too long.");
        }
        for (int i = 0; i < brewery_has.length; i++) {
            this.validateBreweryHas(brewery_has[i]);
        }
        for (int i = 0; i < brewery_friendly.length; i++) {
            this.validateBreweryFriendly(brewery_friendly[i]);
        }
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
                public_email,
                brewery_has,
                brewery_friendly,
                short_type_description,
                short_text_description
        );
    }

    public String uploadVendorPageImage(
            String cookie,
            String filename,
            String gallery
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        this.validateString(gallery, "gallery");
        // Initialize model and return data.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.uploadVendorPageImage(
                cookie,
                filename,
                gallery
        );
    }

    public boolean updateVendorPageImage(
            String cookie,
            int image_id,
            boolean show_in_main_gallery,
            boolean show_in_main_slider,
            int display_order
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        // Initialize model and return data.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.updateVendorPageImage(
                cookie,
                image_id,
                show_in_main_gallery,
                show_in_main_slider,
                display_order
        );
    }

    public String deleteVendorPageImage(
            String cookie,
            int image_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        // Initialize model and return data.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.deleteVenodrPageImage(
                cookie,
                image_id
        );
    }

    public boolean setGoogleMapsAddress(
            String cookie,
            String googleMapsAddress,
            float latitude,
            float longitude,
            int googleMapsZoom
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateLatLong(latitude, longitude);
        // Initialize model and set data.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.setGoogleMapsAddress(
                cookie,
                googleMapsAddress,
                latitude,
                longitude,
                googleMapsZoom
        );
    }

    public String loadVendorAccountProfile(
            String cookie
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        // Load model shit.
        GeneralModel generalModel = new GeneralModel();
        VendorAccount vendorAccount = generalModel.loadVendorAccountProfile(
                cookie
        );
        return this.returnJSON(vendorAccount);
    }

    public boolean updateVendorAccountInfo (
            String cookie,
            String public_first_name,
            String public_last_name,
            String about_me
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        this.validateString(public_first_name, "public_first_name");
        this.validateString(public_last_name, "public_last_name");
        this.validateString(about_me, "about_me");
        // Model shit.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.updateVendorAccountInfo(
                cookie,
                public_first_name,
                public_last_name,
                about_me
        );
    }

    public boolean uploadVendorAccountProfilePicture(
            String cookie,
            String filename
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Model shit.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.uploadVendorAccountProfilePicture(
                cookie,
                filename
        );
    }

    public boolean deleteVendorAccountProfilePicture(
            String cookie
    ) throws Exception {
        // Validate shit.
        this.validateString(cookie, "cookie");
        // Model shit.
        GeneralModel generalModel = new GeneralModel();
        return generalModel.deleteVendorAccountProfilePicture(
                cookie
        );
    }
}
