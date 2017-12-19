package com.Common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/13/17.
 */
public class Event {
    /**
     * Values straight from the DB.
     */
    public int event_id;
    public int vendor_id;
    public int feature_id;
    public String name;
    public String start_date;
    public String end_date;
    public boolean rsvp_required;
    public String description;
    public int initial_est_occupancy;
    public String[] weekdays;
    public EventCategory event_category;

    /**
     * Images
     * These go HashMap<display_order, file_path>
     */
    public HashMap<Integer, EventImage> images;

    public Event() {
        this.event_id = 0;
        this.vendor_id = 0;
        this.feature_id = 0;
        this.name = null;
        this.start_date = null;
        this.end_date = null;
        this.rsvp_required = false;
        this.description = null;
        this.initial_est_occupancy = 0;
        this.weekdays = null;
        this.event_category = new EventCategory();

        this.images = new HashMap<Integer, EventImage>();
    }
}
