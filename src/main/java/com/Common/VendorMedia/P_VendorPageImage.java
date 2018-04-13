package com.Common.VendorMedia;

/**
 * "Privilaged" Vendor Page Image
 */
public class P_VendorPageImage extends VendorPageImage {
    public String creation_timestamp;
    public String creation_days_ago;
    // Put other stats in here when necessary.
    public P_VendorPageImage() {
        super();
        this.creation_timestamp = null;
        this.creation_days_ago = null;
    }
}
