package com.Common;

/**
 * Created by alexanderkleinhans on 6/28/17.
 */
public class EventImage {
    public int event_image_id;
    public int event_id;
    public String event_name;
    public int display_order;
    public String filename;
    public String upload_date;

    public EventImage() {
        this.event_image_id = 0;
        this.event_id = 0;
        this.event_name = null;
        this.display_order = 0;
        this.filename = null;
        this.upload_date = null;
    }
}
