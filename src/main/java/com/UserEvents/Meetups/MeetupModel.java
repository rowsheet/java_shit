package com.UserEvents.Meetups;

import com.Common.AbstractModel;
import com.Common.CookiePair;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class MeetupModel extends AbstractModel {
    private String createMeetupsSQL =
            "INSERT INTO " +
                    "   meetups" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   start_time," +
                    "   weekday," +
                    "   name," +
                    "   description," +
                    "   meetup_categories" +
                    ") VALUES (" +
                    "?,?,?,?::weekday,?,?,?::meetup_category[])" +
                    "RETURNING id";

    public MeetupModel() throws Exception {
    }

    public int createMeetup(
            String cookie,
            String start_time,
            String weekday,
            String name,
            String description,
            String[] categories
    ) throws Exception {
        // We need to validate the request and make sure "organize_meetups" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "organize_meetups");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement =  this.DAO.prepareStatement(this.createMeetupsSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(start_time));
        preparedStatement.setString(4, weekday);
        preparedStatement.setString(5, name);
        preparedStatement.setString(6, description);
        preparedStatement.setArray(7, this.DAO.createArrayOf("meetup_category", categories));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int meetup_id = 0;
        while (resultSet.next()) {
            meetup_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (meetup_id != 0) { return meetup_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create meetup.");
    }
}
