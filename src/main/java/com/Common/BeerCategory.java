package com.Common;

public class BeerCategory {
    public int beer_category_id;
    public int vendor_id;
    public String name;
    public String hex_color;

    public BeerCategory() {
        this.vendor_id = 0;
        this.beer_category_id = 0;
        this.name = "";
        this.hex_color = "";
    }
}
