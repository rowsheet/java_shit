package com.Common;

/**
 * Created by alexanderkleinhans on 6/27/17.
 */
public class VendorFoodImage {
    public int food_image_id;
    public int food_id;
    public String food_name;
    public int display_order;
    String filename;
    String upload_date;

    public VendorFoodImage() {
        this.food_image_id = 0;
        this.food_id = 0;
        this.food_name = null;
        this.display_order = 0;
        this.filename = null;
        this.upload_date = null;
    }
}
