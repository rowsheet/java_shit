package com.Common;

public class VendorReviewReply {
    public int id;
    public int root_review_id;
    public int account_id;
    public String content;
    public int days_ago; // Queried as creation timestamp minus now in days.
    public String first_name;
    public String last_name;
    public VendorReviewReply() {
        this.id = 0;
        this.root_review_id = 0;
        this.account_id = 0;
        this.content = "";
        this.days_ago = 0;
        this.first_name = "";
        this.last_name = "";
    }
}
