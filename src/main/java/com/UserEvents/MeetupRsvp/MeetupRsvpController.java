package com.UserEvents.MeetupRsvp;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class MeetupRsvpController extends AbstractController {
    public int createMeetupRsvp(
            String cookie,
            int meetup_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(meetup_id, "meetup_id");
        // Initialize model and create the data.
        MeetupRsvpModel meetupRsvpModel = new MeetupRsvpModel();
        return meetupRsvpModel.createMeetupRsvp(
            cookie,
            meetup_id
        );
    }
}
