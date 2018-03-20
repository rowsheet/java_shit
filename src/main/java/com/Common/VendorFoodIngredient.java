package com.Common;

import java.util.ArrayList;

public class VendorFoodIngredient {
    public int id;
    public int vendor_id;
    public int feature_id;
    public String name;
    public String description;
    public String source;
    public String[] keywords;
    public VendorFoodTag tag_one;
    public VendorFoodTag tag_two;
    public VendorFoodTag tag_three;
    public VendorFoodTag tag_four;
    public VendorFoodTag tag_five;
    public VendorNutritionalFact nutritional_fact_profile;
    public boolean verified;
    public String creation_timestamp;
    public String creation_days_ago;

    public VendorFoodIngredient() {
        this.id = 0;
        this.vendor_id = 0;
        this.feature_id = 0;
        this.name = null;
        this.description = null;
        this.source = null;
        this.keywords = null;
        this.tag_one = new VendorFoodTag();
        this.tag_two = new VendorFoodTag();
        this.tag_three = new VendorFoodTag();
        this.tag_four = new VendorFoodTag();
        this.tag_five = new VendorFoodTag();
        this.nutritional_fact_profile = new VendorNutritionalFact();
        this.verified = false;
        this.creation_timestamp = null;
        this.creation_days_ago = null;
    }
}

