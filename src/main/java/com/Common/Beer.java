package com.Common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/10/17.
 */
public class Beer {

    /**
     * Values straight from the DB.
     */
    public int beer_id;
    public int vendor_id;
    public String name;
    public int color;
    public int bitterness;
    public int abv;
    public String beer_style;
    public String[] beer_tastes;
    public String description;
    public float price;
    public String creation_timestamp;
    public String creation_days_ago;
    public String[] beer_sizes;
    public String hop_score;
    // From a join, but almost straight from DB (one-to-one) relationship.
    public BeerCategory beer_category;
    public VendorNutritionalFact nutritional_facts;
    // Ingredients and tags.
    public ArrayList<BeerIngredient> beer_ingredients;
    public BeerTag tag_one;
    public BeerTag tag_two;
    public BeerTag tag_three;
    public BeerTag tag_four;
    public BeerTag tag_five;

    /**
     * These have to be calculated.
     */
    public int review_count;
    public float review_average;

    /**
     * Veched from other resources.
     */
    public ArrayList<BeerReview> reviews;

    /**
     * Images
     * These go HashMap<display_order, file_path>
     */
    public HashMap<Integer, BeerImage> images;

    public Beer() {
        this.beer_id = 0;
        this.vendor_id = 0;
        this.name = "";
        this.color = 0;
        this.bitterness = 0;
        this.abv = 0;
        this.beer_style = "";
        this.beer_tastes = null;
        this.description = "";
        this.price = (float) 0.0;
        this.creation_timestamp = "";
        this.creation_days_ago = "";
        this.beer_sizes = null;
        this.hop_score = "";
        this.beer_category = new BeerCategory();
        this.nutritional_facts = new VendorNutritionalFact();

        this.review_count = 0;
        this.review_average = (float) 0.0;

        this.reviews = new ArrayList<BeerReview>();

        this.images = new HashMap<Integer, BeerImage>();

        this.beer_ingredients = new ArrayList<BeerIngredient>();
        this.tag_one = new BeerTag();
        this.tag_two = new BeerTag();
        this.tag_three = new BeerTag();
        this.tag_four = new BeerTag();
        this.tag_five = new BeerTag();
    }
}
