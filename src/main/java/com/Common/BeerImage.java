package com.Common;

/**
 * Created by alexanderkleinhans on 6/28/17.
 */
public class BeerImage {
    public int beer_image_id;
    public int beer_id;
    public String beer_name;
    public int display_order;
    public String filename;
    public String creation_timestamp;
    public String creation_days_ago;

    public BeerImage() {
        this.beer_image_id = 0;
        this.beer_id = 0;
        this.beer_name = null;
        this.display_order = 0;
        this.filename = null;
        this.creation_timestamp = null;
        this.creation_days_ago = null;
    }
}
