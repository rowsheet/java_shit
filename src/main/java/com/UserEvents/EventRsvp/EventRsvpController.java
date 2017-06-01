package com.UserEvents.EventRsvp;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventRsvpController extends AbstractController {
    public int createEventRsvp(
            String cookie,
            int event_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(event_id, "event_id");
        // Initialize model and create the data.
        EventRsvpModel eventRsvpModel = new EventRsvpModel();
        return eventRsvpModel.createEventRsvpModel(
                cookie,
                event_id
        );
    }
}
