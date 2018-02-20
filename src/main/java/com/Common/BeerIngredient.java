package com.Common;

public class BeerIngredient {
    public int vendor_id;
    public int feature_id;
    public String name;
    public String description;
    public String source;
    public String[] keywords;
    public String tag_one;
    public String tag_two;
    public String tag_three;
    public String tag_four;
    public String tag_five;
    public VendorNutritionalFact nutritional_fact_profile;
    public BeerIngredient() {
        this.vendor_id = 0;
        this.feature_id = 0;
        this.name = null;
        this.description = null;
        this.keywords = null;
        this.tag_one = null;
        this.tag_two = null;
        this.tag_three = null;
        this.tag_four = null;
        this.tag_five = null;
    }
}

