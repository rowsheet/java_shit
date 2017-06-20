package com.Common;

/**
 * Created by alexanderkleinhans on 6/18/17.
 */
public class VendorReview {

    /**
     * Values straight from the DB.
     */
    public int review_id;
    public int account_id;
    public int vendor_id;
    public int stars;
    public String title;
    public String content;

    /**
     * Kind of come from the DB
     */
    public String username;
    public int days_ago;

    public VendorReview() {
        this.review_id = 0;
        this.account_id = 0;
        this.vendor_id = 0;
        this.stars = 0;
        this.title = null;
        this.content = null;

        this.username = null;
        this.days_ago = 0;
    }
}
