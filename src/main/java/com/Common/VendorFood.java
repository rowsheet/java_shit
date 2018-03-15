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
    public String creation_days_ago;
    // Comes from other table but basically straight from the DB.
    public VendorFoodCategory vendor_food_category;
    public VendorNutritionalFact nutritional_facts;
    // Ingredients and tags.
    public ArrayList<VendorFoodIngredient> vendor_food_ingredients;
    public VendorFoodTag tag_one;
    public VendorFoodTag tag_two;
    public VendorFoodTag tag_three;
    public VendorFoodTag tag_four;
    public VendorFoodTag tag_five;

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
        this.creation_days_ago = null;
        this.vendor_food_category = new VendorFoodCategory();
        this.nutritional_facts = new VendorNutritionalFact();

        this.review_count = 0;
        this.review_average = (float) 0.0;

        this.reviews = new ArrayList<VendorFoodReview>();

        this.images = new HashMap<Integer, VendorFoodImage>();

        this.vendor_food_ingredients = new ArrayList<VendorFoodIngredient>();
        this.tag_one = new VendorFoodTag();
        this.tag_two = new VendorFoodTag();
        this.tag_three = new VendorFoodTag();
        this.tag_four = new VendorFoodTag();
        this.tag_five = new VendorFoodTag();
    }

}
