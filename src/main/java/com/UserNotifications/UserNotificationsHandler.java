package com.UserNotifications;

import com.UserNotifications.Events.EventNotificationController;
import com.UserNotifications.Beers.BeerNotificationController;
import com.UserNotifications.Meetups.MeetupNotificationController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class UserNotificationsHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    @WebMethod
    public int createBeerNotification(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="min_color") int min_color,
            @WebParam(name="max_color") int max_color,
            @WebParam(name="min_bitterness") int min_bitterness,
            @WebParam(name="max_bitterness") int max_bitterness,
            @WebParam(name="min_abv") int min_abv,
            @WebParam(name="max_abv") int max_abv,
            @WebParam(name="beer_styles") String[] beer_styles,
            @WebParam(name="beer_tastes") String[] beer_tastes
    ) throws Exception {
        BeerNotificationController beerNotificationController = new BeerNotificationController();
        return beerNotificationController.createBeerNotification(
            cookie,
            name,
            min_color,
            max_color,
            min_bitterness,
            max_bitterness,
            min_abv,
            max_abv,
            beer_styles,
            beer_tastes
        );
    }

    @WebMethod
    public int createEventNotification(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="expected_occupancy") int expected_occupancy,
            @WebParam(name="weekdays") String[] weekdays,
            @WebParam(name="event_categories") String[] event_categories
    ) throws Exception {
        System.out.println(event_categories.length);
        EventNotificationController eventNotificationController = new EventNotificationController();
        return eventNotificationController.createEventNotificationRsvp(
            cookie,
            name,
            expected_occupancy,
            weekdays,
            event_categories
        );
    }

    @WebMethod
    public int createMeetupNotification(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="expected_occupancy") int expected_occupancy,
            @WebParam(name="weekdays") String[] weekdays,
            @WebParam(name="meetup_categories") String[] meetup_categories
    ) throws Exception {
        MeetupNotificationController meetupNotificationController = new MeetupNotificationController();
        return meetupNotificationController.createMeetupNotificationRsvp(
            cookie,
            name,
            expected_occupancy,
            weekdays,
            meetup_categories
        );
    }
}
