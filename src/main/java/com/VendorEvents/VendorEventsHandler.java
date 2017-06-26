package com.VendorEvents;

import com.Common.Event;
import com.VendorEvents.Events.EventController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
@WebService
public class VendorEventsHandler {

    @WebMethod
    public boolean updateEvent(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="name") String name,
            @WebParam(name="start_date") String start_date,
            @WebParam(name="end_date") String end_date,
            @WebParam(name="rsvp_required") boolean rsvp_required,
            @WebParam(name="description") String description,
            @WebParam(name="event_categories") String[] event_categories,
            @WebParam(name="initial_est_occupancy") int initial_est_occupancy,
            @WebParam(name="weekdays") String[] weekdays
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.updateEvent(
                cookie,
                id,
                name,
                start_date,
                end_date,
                rsvp_required,
                description,
                event_categories,
                initial_est_occupancy,
                weekdays
        );
    }

    @WebMethod
    public int createEvent(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="start_date") String start_date,
            @WebParam(name="end_date") String end_date,
            @WebParam(name="rsvp_required") boolean rsvp_required,
            @WebParam(name="description") String description,
            @WebParam(name="event_categories") String[] event_categories,
            @WebParam(name="initial_est_occupancy") int initial_est_occupancy,
            @WebParam(name="weekdays") String[] weekdays
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.createEvent(
            cookie,
            name,
            start_date,
            end_date,
            rsvp_required,
            description,
            event_categories,
            initial_est_occupancy,
            weekdays
        );
    }

    @WebMethod
    public boolean deleteEvent (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.deleteEvent(
                cookie,
                id
        );
    }
}