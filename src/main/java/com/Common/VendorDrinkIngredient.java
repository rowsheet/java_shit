package com.Common;

public class VendorDrinkIngredient {
    public int id;
    public int vendor_id;
    public int feature_id;
    public String name;
    public String description;
    public String source;
    public String[] keywords;
    public VendorDrinkTag tag_one;
    public VendorDrinkTag tag_two;
    public VendorDrinkTag tag_three;
    public VendorDrinkTag tag_four;
    public VendorDrinkTag tag_five;
    public VendorNutritionalFact nutritional_fact_profile;
    public boolean verified;
    public String creation_timestamp;
    public String creation_days_ago;
    public VendorDrinkIngredient() {
        this.id = 0;
        this.vendor_id = 0;
        this.feature_id = 0;
        this.name = null;
        this.description = null;
        this.source = null;
        this.keywords = null;
        this.tag_one = new VendorDrinkTag();
        this.tag_two = new VendorDrinkTag();
        this.tag_three = new VendorDrinkTag();
        this.tag_four = new VendorDrinkTag();
        this.tag_five = new VendorDrinkTag();
        this.nutritional_fact_profile = new VendorNutritionalFact();
        this.verified = false;
        this.creation_timestamp = null;
        this.creation_days_ago = null;
    }
}
