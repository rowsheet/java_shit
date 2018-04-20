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
    public String first_name;
    public String last_name;
    public String review_image_one;
    public String review_image_two;
    public String review_image_three;
    public String review_image_four;
    public String review_image_five;

    public VendorFoodReview() {
        this.review_id = 0;
        this.account_id = 0;
        this.vendor_food_id = 0;
        this.stars = 0;
        this.content = null;
        this.days_ago = 0;
        this.first_name = "";
        this.last_name = "";
        this.review_image_one = "";
        this.review_image_two = "";
        this.review_image_three = "";
        this.review_image_four = "";
        this.review_image_five = "";
    }

}
