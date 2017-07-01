package com.Common;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */

/**
 * Extends beer, but also has information such as
 * vendor info, i.e. display name and phone number
 * for each entry.
 */
public class BeerSearchResult extends Beer {

    public String vendor_display_name;
    public String vendor_public_phone;
    public String vendor_public_email;
    public String vendor_street_address;
    public String vendor_city;
    public String vendor_state;
    public String vendor_zip;
    // Since this is search, I want to know if this is
    // a "new" beer or not.
    public int days_ago;

    public BeerSearchResult() {
        super();
        this.vendor_display_name = null;
        this.vendor_public_phone = null;
        this.vendor_public_email = null;
        this.vendor_street_address = null;
        this.vendor_city = null;
        this.vendor_state = null;
        this.vendor_zip = null;
        this.days_ago = 0;
    }
}
