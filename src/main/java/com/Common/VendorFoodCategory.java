package com.Common;

public class VendorFoodCategory {
    public int id;
    public int vendor_id;
    public String name;
    public String hex_color;
    public String text_color;
    public String description;
    // These values are only populated for drop-downs in privileged requests.
    // Don't use them in public, they won't be populated!
    public float price_average;
    public float review_average;
    public String creation_timestamp;
    public String creation_days_ago;
    public int count_star;  // count(*)

    public VendorFoodCategory() {
        this.vendor_id = 0;
        this.id = 0;
        this.name = "";
        this.hex_color = "";
        this.text_color = "";
        this.description = "";
        // These values are only populated for drop-downs in privileged requests.
        // Don't use them in public, they won't be populated!
        this.price_average = (float)0.0;
        this.review_average = (float)0.0;
        this.creation_timestamp = "";
        this.creation_days_ago = "";
        this.count_star = 0;
    }
}
