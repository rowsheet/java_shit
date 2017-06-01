package com.UserNotifications.Events;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventNotificationController extends AbstractController {
    public int createEventNotificationRsvp(
            String cookie,
            String name,
            int expected_occupancy,
            String[] weekdays,
            String[] event_categories
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        for (String weekday: weekdays) {
            this.validateWeekday(weekday);
        }
        for (String event_category: event_categories) {
            this.validateEventCategory(event_category);
        }
        // Initialize model and create the data.
        EventNotificationModel beerNotificationModel = new EventNotificationModel();
        return beerNotificationModel.createEventNotification(
            cookie,
            name,
            expected_occupancy,
            weekdays,
            event_categories
        );
    }
}

