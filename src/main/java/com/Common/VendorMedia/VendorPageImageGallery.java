package com.Common.VendorMedia;

import java.util.HashMap;

public class VendorPageImageGallery {
    public int id;
    public int display_order;
    public String hex_color;
    public String text_color;
    public String name;
    public int image_count;
    // Keyed by display order.
    public HashMap<Integer, VendorPageImage> vendorPageImages;

    public VendorPageImageGallery() {
        this.id = 0;
        this.image_count = 0;
        this.display_order = 0;
        this.hex_color = null;
        this.text_color = null;
        this.name = null;
        this.vendorPageImages = new HashMap<Integer, VendorPageImage>();
    }
}
