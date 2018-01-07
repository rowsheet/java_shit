package com.Common;

public class VendorDrinkReview {
    public int review_id;
    public int account_id;
    public int vendor_drink_id;
    public int stars;
    public String content;
    public int days_ago; // Queried as creation timestamp minus now in days.
    public String username;
    public String review_image_one;
    public String review_image_two;
    public String review_image_three;
    public String review_image_four;
    public String review_image_five;

    public VendorDrinkReview() {
        this.review_id = 0;
        this.account_id = 0;
        this.vendor_drink_id = 0;
        this.stars = 0;
        this.content = null;
        this.days_ago = 0;
        this.username = null;
        this.review_image_one = "";
        this.review_image_two = "";
        this.review_image_three = "";
        this.review_image_four = "";
        this.review_image_five = "";
    }
}
