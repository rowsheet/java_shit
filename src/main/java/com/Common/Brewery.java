package com.Common;

import jnr.ffi.annotations.In;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
public class Brewery {
    public int vendor_id;
    /**
     * General Info
     */
    public String display_name;
    public HashMap<Integer, String> page_images;
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
     * Extra Info
     */
    public String official_business_name;

    /**
     * Owned Info
     */
    public HashMap<Integer, Beer> beerMenu;
    public HashMap<Integer, VendorFood> foodMenu;
    public HashMap<Integer, Event> events;
    public HashMap<Integer, VendorReview> reviews;

    public Brewery() {
        this.vendor_id = 0;
        this.display_name = null;
        this.page_images = new HashMap<Integer, String>();
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
        this.official_business_name = null;
        this.beerMenu = new HashMap<Integer, Beer>();
        this.foodMenu = new HashMap<Integer, VendorFood>();
        this.events = new HashMap<Integer, Event>();
        this.reviews = new HashMap<Integer, VendorReview>();
    }
}
