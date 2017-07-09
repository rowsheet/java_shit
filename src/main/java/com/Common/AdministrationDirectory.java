package com.Common;

/**
 * Created by alexanderkleinhans on 7/3/17.
 */
public class AdministrationDirectory {

    public final String confirmation_email_source;

    public AdministrationDirectory() {
        String domain_name = "skiphopp.com";
        this.confirmation_email_source = "brewery_email_confirmation@" + domain_name;
    }

}
