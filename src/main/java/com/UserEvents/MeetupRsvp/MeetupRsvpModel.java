package com.UserEvents.MeetupRsvp;

import com.Common.AbstractModel;
import com.Common.CookiePair;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 5/31/17.
 */
public class MeetupRsvpModel extends AbstractModel {
    private String createMeetupRsvpSQL =
            "INSERT INTO " +
                    "   meetup_rsvp" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   meetup_id" +
                    ") VALUES (" +
                    "?, ?, ?)" +
                    "RETURNING id";

    public int createMeetupRsvp(
            String cookie,
            int meetup_id
    ) throws Exception {
        // We need to validate the request and make sure "meetup_rsvp" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "meetup_rsvp");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement =  this.DAO.prepareStatement(this.createMeetupRsvpSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setInt(3, meetup_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int meetup_rsvp_id = 0;
        while (resultSet.next()) {
            meetup_rsvp_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (meetup_rsvp_id != 0) { return meetup_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create meetup rsvp.");
    }

}
