package com.Common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Array lists of:
 *
 * 1) Food Categories
 * 2) Beer Categories
 * 3) Drink Categories
 * 4) Event Categories
 * 5) Food Tags
 * 6) Beer Tags
 * 7) Drink Tags
 *
 * These are going to be HashMaps keyed by display order for later possible
 * implementation.
 */
public class VendorDropdownContainer {
    // Table dropdowns.
    public HashMap<Integer, VendorFoodCategory> foodCategories;
    public HashMap<Integer, BeerCategory> beerCategories;
    public HashMap<Integer, VendorDrinkCategory> drinkCategories;
    public HashMap<Integer, EventCategory> eventCategories;
    public HashMap<Integer, VendorFoodTag> foodTags;
    public HashMap<Integer, BeerTag> beerTags;
    public HashMap<Integer, VendorDrinkTag> drinkTags;
    public HashMap<Integer, VendorNutritionalFact> nutritionalFacts;
    public HashMap<Integer, Spirit> spirits;
    public HashMap<String, ArrayList<String>> enumerations;
    public VendorDropdownContainer() {
        this.foodCategories = new HashMap<Integer, VendorFoodCategory>();
        this.beerCategories = new HashMap<Integer, BeerCategory>();
        this.drinkCategories = new HashMap<Integer, VendorDrinkCategory>();
        this.eventCategories = new HashMap<Integer, EventCategory>();
        this.foodTags = new HashMap<Integer, VendorFoodTag>();
        this.beerTags = new HashMap<Integer, BeerTag>();
        this.drinkTags = new HashMap<Integer, VendorDrinkTag>();
        this.nutritionalFacts = new HashMap<Integer, VendorNutritionalFact>();
        this.spirits = new HashMap<Integer, Spirit>();
        this.enumerations = new HashMap<String, ArrayList<String>>();
    }
}
