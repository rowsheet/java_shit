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
        this.validateString(cookie);
        this.validateTimestamp(start_time);
        this.validateWeekday(weekday);
        this.validateString(name);
        this.validateText(description);
        for (String category: categories) {
            this.validateString(category);
        }
        // Initialize model and create the data.
        try {
            MeetupModel meetupModel = new MeetupModel();
            meetupModel.createMeetup(
                    cookie, start_time, weekday, name, description, categories
            );
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 1;
    }
}
