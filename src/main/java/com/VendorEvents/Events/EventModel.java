package com.VendorEvents.Events;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventModel extends AbstractModel {
    private String createEventSQL =
            "INSERT INTO" +
                    "   events" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name," +
                    "   start_date," +
                    "   end_date," +
                    "   rsvp_required," +
                    "   description," +
                    "   event_categories," +
                    "   initial_est_occupancy," +
                    "   weekdays" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?::event_category[],?,?::weekday[])" +
                    "RETURNING id";

    public EventModel() throws Exception {}

    public int createEvent(
            String cookie,
            String name,
            String start_date,
            String end_date,
            boolean rsvp_required,
            String description,
            String[] event_categories,
            int initial_est_occupancy,
            String[] weekdays
    ) throws Exception {
        // We need to validate the vendor request and make sure "events" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookieVendorFeature(cookie, "events");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createEventSQL);
        preparedStatement.setInt(1, this.vendorCookie.vendorID);
        preparedStatement.setInt(2, this.vendorCookie.featureID);
        preparedStatement.setString(3, name);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(start_date));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(end_date));
        preparedStatement.setBoolean(6, rsvp_required);
        preparedStatement.setString(7, description);
        preparedStatement.setArray(8, this.DAO.createArrayOf("event_category", event_categories));
        preparedStatement.setInt(9, initial_est_occupancy);
        preparedStatement.setArray(10, this.DAO.createArrayOf("weekday", weekdays));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int event_id = 0;
        while (resultSet.next()) {
            event_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (event_id != 0) { return event_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create event.");
    }

}
