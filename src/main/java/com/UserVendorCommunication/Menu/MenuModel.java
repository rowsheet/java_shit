package com.UserVendorCommunication.Menu;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MenuModel extends AbstractModel {
    private String createBeerReviewSQL =
            "INSERT INTO" +
                    "   beer_reviews" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   beer_id," +
                    "   stars," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?,?)" +
                    "RETURNING id";

    private String createFoodReviewSQL =
            "INSERT INTO" +
                    "   vendor_food_reviews" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   vendor_food_id," +
                    "   stars," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?,?)" +
                    "RETURNING id";

    public MenuModel() throws Exception {}

    public int createBeerReview(
            String cookie,
            int beer_id,
            int stars,
            String content
    ) throws Exception {
        // We need to validate the vendor request and make sure "beer_reviews" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookiePermission(cookie, "beer_reviews");
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createBeerReviewSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setInt(3, beer_id);
        preparedStatement.setInt(4, stars);
        preparedStatement.setString(5, content);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int beer_reivew_id = 0;
        while (resultSet.next()) {
            beer_reivew_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (beer_reivew_id != 0) { return beer_reivew_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create beer review.");
    }

    public int createFoodReview(
            String cookie,
            int vendor_food_id,
            int stars,
            String content
    ) throws Exception {
        // We need to validate the vendor request and make sure "beer_reviews" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookiePermission(cookie, "beer_reviews");
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createFoodReviewSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setInt(3, vendor_food_id);
        preparedStatement.setInt(4, stars);
        preparedStatement.setString(5, content);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int vendor_food_reivew_id = 0;
        while (resultSet.next()) {
            vendor_food_reivew_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (vendor_food_reivew_id != 0) { return vendor_food_reivew_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create food review.");
    }
}

