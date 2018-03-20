package com.Common;

public class BeerTag {
    public int vendor_id;
    public int id;
    public String name;
    public String hex_color;
    public String tag_type;
    public String text_color;
    // These values are only populated for drop-downs in privileged requests.
    // Don't use them in public, they won't be populated!
    public String creation_timestamp;
    public String creation_days_ago;
    public int count_star;

    public BeerTag() {
        this.vendor_id = 0;
        this.id = 0;
        this.name = null;
        this.hex_color = null;
        this.tag_type = null;
        this.text_color = null;
        // These values are only populated for drop-downs in privileged requests.
        // Don't use them in public, they won't be populated!
        this.creation_timestamp = "";
        this.creation_days_ago = "";
        this.count_star = 0;
    }
}

