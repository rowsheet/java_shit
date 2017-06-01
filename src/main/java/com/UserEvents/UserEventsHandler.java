package com.UserEvents;

import com.UserEvents.EventRsvp.EventRsvpController;
import com.UserEvents.EventRsvp.EventRsvpModel;
import com.UserEvents.Meetups.MeetupsController;
import com.UserEvents.MeetupRsvp.MeetupRsvpController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class UserEventsHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    /**
     * Create a new meetup entry assuming user has "organize_meetup" permisison.
     * Returns the ID of the new meetup entry if successful.
     *
     * @param cookie
     * @param start_time
     * @param weekday
     * @param name
     * @param description
     * @param categories
     * @return meetup_id
     * @throws Exception
     */

    @WebMethod
    public int createMeetup(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="start_time") String start_time,
            @WebParam(name="weekday") String weekday,
            @WebParam(name="name") String name,
            @WebParam(name="description") String description,
            @WebParam(name="categories") String[] categories
    ) throws Exception {
        MeetupsController meetupsController = new MeetupsController();
        return meetupsController.createMeetup(
                cookie,
                start_time,
                weekday,
                name,
                description,
                categories
        );
    }

    /**
     * Create a new meetup rsvp entry, assuming user has "meetup_rsvp" permission.
     * Returns the ID of the new meetup rsvp entry if successful.
     *
     * @param cookie
     * @param meetup_id
     * @return meetup_rsvp_id
     * @throws Exception
     */

    @WebMethod
    public int createMeetupRsvp(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="meetup_id") int meetup_id
    ) throws Exception {
        MeetupRsvpController meetupRsvpController = new MeetupRsvpController();
        return meetupRsvpController.createMeetupRsvp(
                cookie,
                meetup_id
        );
    }

    /**
     * Create a new event rsvp entry, assuming user has "event_rsvp" permission.
     * Returns the ID of the new event rsvp entry if successful.
      * @param cookie
     * @param event_id
     * @return event_rsvp_id
     * @throws Exception
     */

    @WebMethod
    public int createEventRsvp(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="event_id") int event_id
    ) throws Exception {
        EventRsvpController eventRsvpController = new EventRsvpController();
        return eventRsvpController.createEventRsvp(
                cookie,
                event_id
        );
    }

    @WebMethod
    public String readMeetups(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="categories") String[] categories,
            @WebParam(name="offset") int offset,
            @WebParam(name="limit") int limit,
            @WebParam(name="chronological") boolean chronological
    ) {
        return "Something";
    }

    @WebMethod
    public String deleteMeetup(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="start_time") String start_time,
            @WebParam(name="weekday") String weekday,
            @WebParam(name="name") String name,
            @WebParam(name="description") String description,
            @WebParam(name="categories") String[] categories
    ) {
        return "Something";
    }
}
