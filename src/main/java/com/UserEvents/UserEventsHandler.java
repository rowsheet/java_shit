package com.UserEvents;

import com.UserEvents.Meetups.MeetupsController;

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
        int meetupID = meetupsController.createMeetup(
                cookie,
                start_time,
                weekday,
                name,
                description,
                categories
        );
        meetupsController = null;
        return meetupID;
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
