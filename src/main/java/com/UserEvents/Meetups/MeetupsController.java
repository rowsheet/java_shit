package com.UserEvents.Meetups;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class MeetupsController extends AbstractController {
    public int createMeetup(
            String cookie,
            String start_time,
            String weekday,
            String name,
            String description,
            String[] categories
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateTimestamp(start_time, "start_time");
        this.validateWeekday(weekday);
        this.validateString(name, "meetup_name");
        this.validateText(description, "description");
        for (String category : categories) {
            this.validateMeetupCategory(category);
        }
        // Initialize model and create the data.
        MeetupModel meetupModel = new MeetupModel();
        return meetupModel.createMeetup(
                cookie,
                start_time,
                weekday,
                name,
                description,
                categories
        );
    }
}
