package com.VendorEvents.Events;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventController extends AbstractController {

    public int createEvent(
            String cookie,
            String name,
            String start_date,
            String end_date,
            boolean rsvp_required,
            String description,
            String[] event_categories,
            int initial_est_occupancy,
            String[] weekdays
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateTimestamp(start_date, "start date");
        this.validateTimestamp(end_date, "end date");
        this.validateText(description, "description");
        if (event_categories.length == 0) {
            throw new Exception("Must have at least one \"event category\".");
        }
        for (String event_category : event_categories) {
            this.validateEventCategory(event_category);
        }
        if (weekdays.length == 0) {
            throw new Exception("Must have at least one \"weekday\".");
        }
        for (String weekday : weekdays) {
            this.validateWeekday(weekday);
        }
        // Initialize model and create the data.
        EventModel eventModel = new EventModel();
        return eventModel.createEvent(
                cookie,
                name,
                start_date,
                end_date,
                rsvp_required,
                description,
                event_categories,
                initial_est_occupancy,
                weekdays
        );
    }
}
