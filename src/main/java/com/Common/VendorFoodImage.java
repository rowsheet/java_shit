package com.Common;

/**
 * Created by alexanderkleinhans on 6/27/17.
 */
public class VendorFoodImage {
    public int food_image_id;
    public int food_id;
    public int display_order;
    public String filename;
    public String upload_date;

    public VendorFoodImage() {
        this.food_image_id = 0;
        this.food_id = 0;
        this.display_order = 0;
        this.filename = null;
        this.upload_date = null;
    }
}
