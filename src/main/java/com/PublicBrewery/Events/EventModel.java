package com.PublicBrewery.Events;

import com.Common.AbstractModel;
import com.Common.Event;
import com.Common.EventImage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import java.sql.Array;

/**
 * Created by alexanderkleinhans on 6/13/17.
 */
public class EventModel extends AbstractModel {

    private String loadEventsSQL_stage1 =
            "SELECT" +
                    "   e.id as event_id," +
                    "   e.vendor_id as event_vendor_id, " +
                    "   e.name as event_name, " +
                    "   TO_CHAR(e.start_date, 'YYYY-MM-DD') AS event_start_date, " +
                    "   TO_CHAR(e.end_date, 'YYYY-MM-DD') AS event_end_date, " +
                    "   e.description as event_description, " +
                    "   e.initial_est_occupancy as event_initial_est_occupancy, " +
                    "   e.weekdays as event_weekdays, " +
                    "   ec.name as event_category_name, " +
                    "   ec.id as event_category_id, " +
                    "   ec.hex_color as event_category_hex_color, " +
                    "   ec.vendor_id as event_category_vendor_id " + // I know, it's constraint by FK, whatever.
                    "FROM " +
                    "   events AS e " +
                    "LEFT JOIN " +
                    "   event_categories AS ec " +
                    "ON " +
                    "   e.event_category_id = ec.id " +
                    "WHERE " +
                    "   e.vendor_id = ? " +
                    "ORDER BY " +
                    "   e.start_date ASC " +
                    "LIMIT ? OFFSET ?";

    private String loadEventsSQL_stage2 =
            "SELECT " +
                    "   event_id, " +
                    "   display_order, " +
                    "   filename " +
                    "FROM " +
                    "   event_images " +
                    "WHERE " +
                    "   event_id " +
                    "IN (" +
                    "   SELECT id FROM events WHERE vendor_id = ? ORDER BY start_date ASC LIMIT ? OFFSET ? )";

    public EventModel() throws Exception {}

    /**
     * Get all events and event images for a brewery_id ordered by
     * star_date with limit offsets based on pagination and count.
     *
     *      1) Select all the events.
     *      2) Select all the event images.
     *
     * @param brewery_id
     * @param limit
     * @param offset
     * @return
     * @throws Exception
     */
    public HashMap<Integer, Event> loadEvents(
            int brewery_id,
            int limit,
            int offset
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        try {
            stage1 = this.DAO.prepareStatement(this.loadEventsSQL_stage1);
            stage2 = this.DAO.prepareStatement(this.loadEventsSQL_stage2);
            // Create a hash map of events keyed by their event_id.
            HashMap<Integer, Event> eventHashMap = new HashMap<Integer, Event>();
            /*
            Stage 1
            Select all events.
             */
            stage1.setInt(1, brewery_id);
            stage1.setInt(2, limit);
            stage1.setInt(3, offset);
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                Event event = new Event();
                event.event_id = stage1Result.getInt("event_id");
                event.vendor_id = stage1Result.getInt("event_vendor_id");
                event.name = stage1Result.getString("event_name");
                event.start_date = stage1Result.getString("event_start_date");
                event.end_date = stage1Result.getString("event_end_date");
                event.description = stage1Result.getString("event_description");
                event.initial_est_occupancy = stage1Result.getInt("event_initial_est_occupancy");
                Array weekdays = stage1Result.getArray("event_weekdays");
                String[] weekdays_str = (String[]) weekdays.getArray();
                event.weekdays = weekdays_str;
                event.event_category.name = stage1Result.getString("event_category_name");
                event.event_category.hex_color = stage1Result.getString("event_category_hex_color");
                event.event_category.event_category_id = stage1Result.getInt("event_category_id");
                event.event_category.vendor_id = stage1Result.getInt("event_category_vendor_id");
                eventHashMap.put(event.event_id, event);
            }
            /*
            Stage 2
            Select all event images.
             */
            stage2.setInt(1, brewery_id);
            stage2.setInt(2, limit);
            stage2.setInt(3, offset);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                EventImage eventImage = new EventImage();
                eventImage.display_order = stage2Result.getInt("display_order");
                eventImage.filename = stage2Result.getString("filename");
                eventImage.event_id = stage2Result.getInt("event_id");
                eventHashMap.get(eventImage.event_id).images.put(eventImage.display_order, eventImage);
            }
            return eventHashMap;
        } catch (Exception ex) {
            System.out.print(ex);
            throw new Exception("Unable to load events");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
