package com.Common;

public class VendorAccount {
    public Account account;
    public boolean verified;
    public boolean is_primary_account;
    public String primary_contact_first_name;
    public String primary_contact_last_name;
    public int vendor_id;
    public VendorAccount() {
        this.account = new Account();
        this.verified = false;
        this.is_primary_account = false;
        this.primary_contact_first_name = "";
        this.primary_contact_last_name = "";
        this.vendor_id = 0;
     }
}
