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
    public HashMap<String, VendorFeature> vendorFeatures;
}
