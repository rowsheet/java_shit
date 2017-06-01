package com.UserNotifications.Events;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class EventNotificationModel extends AbstractModel {
    private String createEventNotificationSQL =
            "INSERT INTO" +
                    "   event_notifications" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   name," +
                    "   expected_occupancy," +
                    "   weekdays," +
                    "   event_categories" +
                    ") VALUES (" +
                    "?,?,?,?,?::weekday[],?::event_category[])" +
                    "RETURNING id";

    public EventNotificationModel() throws Exception {}

    public int createEventNotification(
            String cookie,
            String name,
            int expected_occupancy,
            String[] weekdays,
            String[] event_categories
    ) throws Exception {
        // We need to validate the request and make sure "event_notifications" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "event_notifications");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createEventNotificationSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setString(3, name);
        preparedStatement.setInt(4, expected_occupancy);
        preparedStatement.setArray(5, this.DAO.createArrayOf("weekday", weekdays));
        preparedStatement.setArray(6, this.DAO.createArrayOf("event_category", event_categories));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int event_notification_id = 0;
        while (resultSet.next()) {
            event_notification_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (event_notification_id != 0) { return event_notification_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create event notification.");
    }

}

