package com.UserNotifications.Meetups;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MeetupNotificationModel extends AbstractModel {
    private String createMeetupNotificationSQL =
            "INSERT INTO" +
                    "   meetup_notifications" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   name," +
                    "   expected_occupancy," +
                    "   weekdays," +
                    "   meetup_categories" +
                    ") VALUES (" +
                    "?,?,?,?,?::weekday[],?::meetup_category[])" +
                    "RETURNING id";

    public MeetupNotificationModel() throws Exception {}

    public int createMeetupNotification(
            String cookie,
            String name,
            int expected_occupancy,
            String[] weekdays,
            String[] meetup_categories
    ) throws Exception {
        // We need to validate the request and make sure "meetup_notifications" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "meetup_notifications");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createMeetupNotificationSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setString(3, name);
        preparedStatement.setInt(4, expected_occupancy);
        preparedStatement.setArray(5, this.DAO.createArrayOf("weekday", weekdays));
        preparedStatement.setArray(6, this.DAO.createArrayOf("meetup_category", meetup_categories));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int meetup_notification_id = 0;
        while (resultSet.next()) {
            meetup_notification_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (meetup_notification_id != 0) { return meetup_notification_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create meetup notification.");
    }

}

