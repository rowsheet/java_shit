package com.Common;

/**
 * Created by alexanderkleinhans on 6/12/17.
 */
public class VendorFoodReview {

    public int review_id;
    public int account_id;
    public int vendor_food_id;
    public int stars;
    public String content;
    public int days_ago; // Queried as creation timestamp minus now in days.
    public String username;

    public VendorFoodReview() {
        this.review_id = 0;
        this.account_id = 0;
        this.vendor_food_id = 0;
        this.stars = 0;
        this.content = null;
        this.days_ago = 0;
        this.username = null;
    }

}
