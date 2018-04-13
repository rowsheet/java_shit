package com.Common.VendorMedia;

import java.util.HashMap;

public class VendorMedia {
    // Keyed by display order.
    public HashMap<Integer, VendorPageImageGallery> pageImageGalleries;
    public VendorMedia() {
        this.pageImageGalleries = new HashMap<Integer, VendorPageImageGallery>();
    }
}
