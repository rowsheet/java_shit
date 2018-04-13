package com.Common;

public class Account {
    public String email_address;
    public String account_type;
    public String account_status;
    public String first_name;
    public String last_name;
    public String creation_timestamp;
    public int creation_days_ago;
    public String profile_picture;
    public String about_me;
    public int account_id;
    public Account() {
        this.email_address = "";
        this.account_type = "";
        this.account_status = "";
        this.first_name = "";
        this.last_name = "";
        this.creation_timestamp = "";
        this.creation_days_ago = 0;
        this.profile_picture = "";
        this.about_me = "";
        this.account_id = 0;
    }
}
