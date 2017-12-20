package com.Common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/11/17.
 */
public class VendorFood {

    /**
     * Value straight from the DB.
     */
    public int vendor_id;
    public int vendor_food_id;
    public String name;
    public String description;
    public float price;
    public String[] food_sizes;
    public String creation_timestamp;
    // Comes from other table but basically straight from the DB.
    public VendorFoodCategory vendor_food_category;

    /**
     * These have to be calculated.
     */
    public int review_count;
    public float review_average;

    /**
     * Fetched from other resources.
     */
    public ArrayList<VendorFoodReview> reviews;

    /**
     * Images
     * These go HashMap<display_order, file_path>
     */
    public HashMap<Integer,VendorFoodImage> images;

    public VendorFood() {
        this.vendor_id = 0;
        this.vendor_food_id = 0;
        this.name = null;
        this.description = null;
        this.price = (float)0.0;
        this.food_sizes = null;
        this.creation_timestamp = null;
        this.vendor_food_category = new VendorFoodCategory();

        this.review_count = 0;
        this.review_average = (float) 0.0;

        this.reviews = new ArrayList<VendorFoodReview>();

        this.images = new HashMap<Integer, VendorFoodImage>();
    }

}
