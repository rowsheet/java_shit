package com.Common.VendorMedia;

import java.util.HashMap;

/**
 * "Privilaged"
 */
public class P_VendorPageImageTable {
    // Later I will add cool stats and anlytics to this.
    public HashMap<Integer, P_VendorPageImage> page_images;
    public String gallery_name;
    public int gallery_id;
    public String creation_timestamp;
    public String creation_days_ago;
    public String gallery_hex_color;
    public String gallery_text_color;
    public int image_count;
    public P_VendorPageImageTable() {
        this.page_images = new HashMap<Integer, P_VendorPageImage>();
        this.gallery_name = null;
        this.gallery_id = 0;
        this.creation_days_ago = null;
        this.creation_days_ago = null;
        this.gallery_hex_color = null;
        this.gallery_text_color = null;
        this.image_count = 0;
    }
}
