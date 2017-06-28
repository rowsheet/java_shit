package com.VendorEvents;

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

    /*
    Images
     */

    /**
     * Inserts record into event_images with default
     * display order and a null event_id.
     *
     * Record must belong to a vendor with the feature "event_images".
     *
     * Although event_id can be null, it has a comopound foreign key
     * to events alongside the vendor_id, so the image must be owned
     * by a event item owned by the same vendor_id.
     *
     * Returns filepath of new image.
     *
     * @param cookie
     * @param filename
     * @return filepath
     * @throws Exception
     */
    @WebMethod
    public String uploadEventImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "filename") String filename,
            @WebParam(name = "event_id") int event_id
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.uploadEventImage(
                cookie,
                filename,
                event_id
        );
    }

    /**
     * Updates image. Returns true or throws exception message.
     *
     * @param cookie
     * @param event_image_id
     * @param display_order
     * @return success
     * @throws Exception
     */
    @WebMethod
    public boolean updateEventImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "event_image_id") int event_image_id,
            @WebParam(name = "display_order") int display_order
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.updateEventImage(
                cookie,
                event_image_id,
                display_order
        );
    }

    /**
     * Deletes image. Returns filename or throws exception.
     *
     * @param cookie
     * @param event_image_id
     * @return
     * @throws Exception
     */
    @WebMethod
    public String deleteEventImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "event_image_id") int event_image_id
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.deleteEventImage(
                cookie,
                event_image_id
        );
    }

}