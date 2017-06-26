package com.UserEvents.EventRsvp;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventRsvpModel extends AbstractModel {
    private String createEventRsvpSQL =
            "INSERT INTO" +
                    "   event_rsvp" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   event_id" +
                    ") VALUES (" +
                    "?,?,?)" +
                    "RETURNING id";

    public EventRsvpModel() throws Exception {}

    public int createEventRsvpModel (
            String cookie,
            int event_id
    ) throws Exception {
        // We need to validate the request and make sure "event_rsvp" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "event_rsvp");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createEventRsvpSQL);
        preparedStatement.setInt(1, this.userCookie.userID);
        preparedStatement.setInt(2, this.userCookie.requestPermissionID);
        preparedStatement.setInt(3, event_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int event_rsvp_id = 0;
        while (resultSet.next()) {
            event_rsvp_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (event_rsvp_id != 0) { return event_rsvp_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create event rsvp.");
    }
}
