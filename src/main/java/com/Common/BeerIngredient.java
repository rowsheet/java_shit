package com.Common;

public class BeerIngredient {
    public int id;
    public int vendor_id;
    public int feature_id;
    public String name;
    public String description;
    public String source;
    public String[] keywords;
    public BeerTag tag_one;
    public BeerTag tag_two;
    public BeerTag tag_three;
    public BeerTag tag_four;
    public BeerTag tag_five;
    public VendorNutritionalFact nutritional_fact_profile;
    public boolean verified;
    public String creation_timestamp;
    public String creation_days_ago;

    public BeerIngredient() {
        this.id = 0;
        this.vendor_id = 0;
        this.feature_id = 0;
        this.name = null;
        this.description = null;
        this.keywords = null;
        this.tag_one = new BeerTag();
        this.tag_two = new BeerTag();
        this.tag_three = new BeerTag();
        this.tag_four = new BeerTag();
        this.tag_five = new BeerTag();
        this.nutritional_fact_profile = new VendorNutritionalFact();
        this.verified = false;
        this.creation_timestamp = null;
        this.creation_days_ago = null;
    }
}

