package com.Common;

import java.util.ArrayList;
import java.util.HashMap;

public class VendorDrink {
    /**
     * Values straight from the DB
     */
    public int vendor_id;
    public int vendor_drink_id;
    public String name;
    public String description;
    public float price;
    public String creation_timestamp;
    public String hex_one;
    public String hex_two;
    public String hex_three;
    public String hex_background;
    // Comes from other table but almost straight from DB.
    public VendorDrinkCategory vendor_drink_category;
    public ArrayList<Spirit> spirits;
    public boolean is_alcoholic;

    /**
     * These have to be calculated.
     */
    public int review_count;
    public float review_average;

    /**
     * Fetched from other resources.
     */
    public ArrayList<VendorDrinkReview> reviews;

    /**
     * Images
     * These go HashMap<display_order, file_path>
     */
    public HashMap<Integer,VendorDrinkImage> images;

    public VendorDrink() {
        this.vendor_id = 0;
        this.vendor_drink_id = 0;
        this.name = "";
        this.description = "";
        this.price = (float)0.0;
        this.creation_timestamp = "";
        this.hex_one = "";
        this.hex_two = "";
        this.hex_three = "";
        this.hex_background = "";
        this.vendor_drink_category = new VendorDrinkCategory();
        this.spirits = new ArrayList<Spirit>();
        this.is_alcoholic = false;

        this.review_count = 0;
        this.review_average = (float)0.0;

        this.reviews = new ArrayList<VendorDrinkReview>();

        this.images = new HashMap<Integer, VendorDrinkImage>();
    }
}
