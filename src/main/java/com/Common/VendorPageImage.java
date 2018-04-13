package com.Common;

/**
 * Created by alexanderkleinhans on 6/27/17.
 *
 * DEPRECIATED!!!!!!!!
 *
 */
public class VendorPageImage {
    public String filename;
    public String gallery;
    public String upload_date;
    public int image_id;
    public int display_order;
    public boolean show_in_main_gallery;
    public boolean show_in_main_slider;

    public VendorPageImage() {
        this.filename = null;
        this.gallery = null;
        this.upload_date = null;
        this.image_id = 0;
        this.display_order = 0;
        this.show_in_main_gallery = false;
        this.show_in_main_slider = false;
    }
}
