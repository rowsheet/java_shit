package com.UserNotifications.Beers;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerNotificationModel extends AbstractModel {
    private String createBeerNotificationSQL =
            "INSERT INTO" +
                    "   beer_notifications" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   name," +
                    "   min_color," +
                    "   max_color," +
                    "   min_bitterness," +
                    "   max_bitterness," +
                    "   min_abv," +
                    "   max_abv," +
                    "   beer_styles," +
                    "   beer_tastes" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?,?,?::beer_style[],?::beer_taste[])" +
                    "RETURNING id";

    public BeerNotificationModel() throws Exception {}

    public int createBeerNotification(
            String cookie,
            String name,
            int min_color,
            int max_color,
            int min_bitterness,
            int max_bitterness,
            int min_abv,
            int max_abv,
            String[] beer_styles,
            String[] beer_tastes
    ) throws Exception {
        // We need to validate the request and make sure "beer_notifications" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "beer_notifications");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createBeerNotificationSQL);
        preparedStatement.setInt(1, this.userCookie.userID);
        preparedStatement.setInt(2, this.userCookie.requestPermissionID);
        preparedStatement.setString(3, name);
        preparedStatement.setInt(4,min_color);
        preparedStatement.setInt(5,max_color);
        preparedStatement.setInt(6, min_bitterness);
        preparedStatement.setInt(7, max_bitterness);
        preparedStatement.setInt(8, min_abv);
        preparedStatement.setInt(9, max_abv);
        preparedStatement.setArray(10, this.DAO.createArrayOf("beer_style", beer_styles));
        preparedStatement.setArray(11, this.DAO.createArrayOf("beer_taste", beer_tastes));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int beer_notification_id = 0;
        while (resultSet.next()) {
            beer_notification_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (beer_notification_id != 0) { return beer_notification_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create beer notification.");
    }

}

