package com.UserNotifications.Meetups;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MeetupNotificationController extends AbstractController {
    public int createMeetupNotificationRsvp(
            String cookie,
            String name,
            int expected_occupancy,
            String[] weekdays,
            String[] meetup_categories
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        for (String weekday: weekdays) {
            this.validateWeekday(weekday);
        }
        for (String meetup_category: meetup_categories) {
            this.validateMeetupCategory(meetup_category);
        }
        // Initialize model and create the data.
        MeetupNotificationModel beerNotificationModel = new MeetupNotificationModel();
        return beerNotificationModel.createMeetupNotification(
                cookie,
                name,
                expected_occupancy,
                weekdays,
                meetup_categories
        );
    }
}

