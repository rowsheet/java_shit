package com.Common.PublicVendor;

import com.Common.*;
import jnr.ffi.annotations.In;

import java.util.ArrayList;
import java.util.HashMap;
import com.Common.VendorMedia.VendorPageImage;
import com.Common.VendorMedia.VendorPageImageGallery;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
public class Brewery {
    public int vendor_id;
    /**
     * General Info
     */
    public String display_name;
    /**
     * About Info
     */
    public String about_text;
    /**
     * Hours Info
     */
    public String mon_open;
    public String mon_close;
    public String tue_open;
    public String tue_close;
    public String wed_open;
    public String wed_close;
    public String thu_open;
    public String thu_close;
    public String fri_open;
    public String fri_close;
    public String sat_open;
    public String sat_close;
    public String sun_open;
    public String sun_close;
    /**
     * Address Info
     */
    public String street_address;
    public String city;
    public String state;
    public String zip;
    public String public_phone;
    public String public_email;
    /**
     * Has and Friendly
     */
    public String[] brewery_has;
    public String[] brewery_friendly;
    /**
     * Google Maps Info
     */
    public String google_maps_address;
    public float latitude;
    public float longitude;
    public int google_maps_zoom;
    /**
     * Extra Info
     */
    public String official_business_name;

    /**
     * Nice formatting info.
     */
    public String short_type_description;
    public String short_text_description;
    public int main_gallery_id;
    public int main_image_id;
    public VendorPageImage main_image;
    public VendorPageImageGallery main_gallery;

    /**
     * Owned Info
     */
//    public HashMap<Integer, Beer> beerMenu;
    public BeerMenu beerMenu;
//    public HashMap<Integer, VendorFood> foodMenu;
    public FoodMenu foodMenu;
//    public HashMap<Integer, VendorDrink> drinkMenu;
    public DrinkMenu drinkMenu;
    public HashMap<Integer, Event> events;
    public HashMap<Integer, VendorReview> reviews;
    public String short_code;

    public int total_favorites;
    public Brewery() {
        this.short_text_description = null;
        this.short_type_description = null;
        this.main_gallery_id = 0;
        this.main_image_id = 0;
        this.main_image = new VendorPageImage();
        this.main_gallery = new VendorPageImageGallery();
        // nice format things.
        this.vendor_id = 0;
        this.display_name = null;
        this.about_text = null;
        this.mon_open = null;
        this.mon_close = null;
        this.tue_open = null;
        this.tue_close = null;
        this.wed_open = null;
        this.wed_close = null;
        this.thu_open = null;
        this.thu_close = null;
        this.fri_open = null;
        this.fri_close = null;
        this.sat_open = null;
        this.sat_close = null;
        this.sun_open = null;
        this.sun_close = null;
        this.street_address = null;
        this.city = null;
        this.state = null;
        this.zip = null;
        this.public_phone = null;
        this.public_email = null;
        this.brewery_has = new String[]{};
        this.brewery_friendly = new String[]{};
        this.google_maps_address = null;
        this.latitude = (float)0.0;
        this.longitude = (float)0.0;
        this.google_maps_zoom = 0;
        this.official_business_name = null;
//        this.beerMenu = new HashMap<Integer, Beer>();
        this.beerMenu = new BeerMenu();
//        this.foodMenu = new HashMap<Integer, VendorFood>();
        this.foodMenu = new FoodMenu();
//        this.drinkMenu = new HashMap<Integer, VendorDrink>();
        this.drinkMenu = new DrinkMenu();
        this.events = new HashMap<Integer, Event>();
        this.reviews = new HashMap<Integer, VendorReview>();
        this.short_code = "";
        this.total_favorites = 0;
    }
}
