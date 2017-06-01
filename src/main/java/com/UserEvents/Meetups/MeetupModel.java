package com.UserEvents.Meetups;

import com.Common.AbstractModel;
import com.Common.CookiePair;
import jnr.posix.Times;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Array;

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
                    "?,?,?,?::weekday,?,?,?::meetup_category[])";

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
        CookiePair cookiePair = this.validateCookiePermission(cookie, "organize_meetups");
        PreparedStatement preparedStatement =  this.DAO.prepareStatement(this.createMeetupsSQL);
        preparedStatement.setInt(1, cookiePair.userID);
        preparedStatement.setInt(2, cookiePair.userPermissionID);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(start_time));
        preparedStatement.setString(4, weekday);
        preparedStatement.setString(5, name);
        preparedStatement.setString(6, description);
        preparedStatement.setArray(7, this.DAO.createArrayOf("meetup_category", categories));
        System.out.println(preparedStatement);
        preparedStatement.executeUpdate();
        return 0;
    }
}
