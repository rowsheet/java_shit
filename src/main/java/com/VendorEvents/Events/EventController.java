package com.VendorEvents.Events;

import com.Common.AbstractController;
import com.Common.Event;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventController extends AbstractController {

    public boolean deleteEvent(
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "event_id");
        // Initilaize model and create data.
        EventModel eventModel = new EventModel();
        return eventModel.deleteEvent(
                cookie,
                id
        );
    }

    public boolean updateEvent (
            String cookie,
            int id,
            String name,
            String start_date,
            String end_date,
            boolean rsvp_required,
            String description,
            int initial_est_occupancy,
            String[] weekdays,
            int event_category_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "id");
        this.validateString(name, "name");
        this.validateDate(start_date, "start date");
        this.validateDate(end_date, "end date");
        this.validateText(description, "description");
        if (weekdays.length == 0) {
            throw new Exception("Must have at least one \"weekday\".");
        }
        for (String weekday : weekdays) {
            this.validateWeekday(weekday);
        }
        // Initialize model and create the data.
        EventModel eventModel = new EventModel();
        return eventModel.updateEvent(
                cookie,
                id,
                name,
                start_date,
                end_date,
                rsvp_required,
                description,
                initial_est_occupancy,
                weekdays,
                event_category_id
        );
    }

    public int createEvent(
            String cookie,
            String name,
            String start_date,
            String end_date,
            boolean rsvp_required,
            String description,
            int initial_est_occupancy,
            String[] weekdays,
            int event_category_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateDate(start_date, "start date");
        this.validateDate(end_date, "end date");
        this.validateText(description, "description");
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
                initial_est_occupancy,
                weekdays,
                event_category_id
        );
    }

    public int createEventCategory(
            String cookie,
            String category_name,
            String hex_color
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        // Initialize model and create data.
        EventModel eventModel = new EventModel();
        return eventModel.createEventCategory(
                cookie,
                category_name,
                hex_color
        );
    }

    public boolean updateEventCategory(
            String cookie,
            int id,
            String new_category_name,
            String new_hex_color
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(new_category_name, "new_category_name.");
        this.validateHexColor(new_hex_color);
        // Initialize model and create data.
        EventModel eventModel = new EventModel();
        return eventModel.updateEventCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color
        );
    }

    public boolean deleteEventCategory(
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "event_category_id");
        // Initialize mode and create data.
        EventModel eventModel = new EventModel();
        return eventModel.deleteEventCategory(
                cookie,
                id
        );
    }

    public String uploadEventImage(
            String cookie,
            String filename,
            int event_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Initialize model and create data.
        EventModel eventModel = new EventModel();
        return eventModel.uploadEventImage(
                cookie,
                filename,
                event_id
        );
    }

    public boolean updateEventImage(
            String cookie,
            int event_image_id,
            int display_order
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(event_image_id, "event_image_id");
        // Initialize model and create data.
        EventModel eventModel = new EventModel();
        return eventModel.updateEventImage(
                cookie,
                event_image_id,
                display_order
        );
    }

    public String deleteEventImage(
            String cookie,
            int event_image_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(event_image_id, "event_image_id");
        // Initialize model and create data.
        EventModel eventModel = new EventModel();
        return eventModel.deleteEventImage(
                cookie,
                event_image_id
        );
    }

}
