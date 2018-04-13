package com.Common;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class VendorCookie {
    public String sessionKey;
    public int vendorID;
    public int requestFeatureID;
    public int accountID;
    public String first_name;
    public String last_name;
    public String about_me;
    public String profile_picture;
    public HashMap<String, VendorFeature> vendorFeatures;
    public VendorCookie() {
        this.sessionKey = "";
        this.vendorID = 0;
        this.requestFeatureID = 0;
        this.accountID = 0;
        this.vendorFeatures = new HashMap<String, VendorFeature>();
        this.first_name = "NA";
        this.last_name = "NA";
        this.about_me = "NA";
        this.profile_picture = "NA";
    }
}
