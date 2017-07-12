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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the user request and make sure "beer_reviews" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookiePermission(cookie, "beer_reviews");
            preparedStatement = this.DAO.prepareStatement(this.createBeerReviewSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, this.userCookie.requestPermissionID);
            preparedStatement.setInt(3, beer_id);
            preparedStatement.setInt(4, stars);
            preparedStatement.setString(5, content);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_reivew_id = 0;
            while (resultSet.next()) {
                beer_reivew_id = resultSet.getInt("id");
            }
            if (beer_reivew_id == 0) {
                // Could create it. Don't know why...
                throw new MenuException("Unable to create new beer review.");
            }
            return beer_reivew_id;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            // Unknown error due to null pointer exception.
            if (ex.getMessage() == null) {
                throw new Exception("Oops! Something went wrong...");
            }
            System.out.println(ex.getMessage());
            // Try to parse the exception.
            if (ex.getMessage().contains("user_account_permissions")) {
                // This is a serious event. It means the cookie permission was likely
                // manually tampered with.
                // You should probably blacklist this IP.
                //@TODO consider blacklisting this IP.
                throw new Exception("Sorry! You do not have permission to post beer reviews.");
            }
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("Sorry! You've already created a review for this beer!");
            }
            // Unkonwn reason.
            throw new Exception("Unable to create beer review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public int createFoodReview(
            String cookie,
            int vendor_food_id,
            int stars,
            String content
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "food_reviews" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookiePermission(cookie, "vendor_food_review");
            preparedStatement = this.DAO.prepareStatement(this.createFoodReviewSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, this.userCookie.requestPermissionID);
            preparedStatement.setInt(3, vendor_food_id);
            preparedStatement.setInt(4, stars);
            preparedStatement.setString(5, content);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_reivew_id = 0;
            while (resultSet.next()) {
                beer_reivew_id = resultSet.getInt("id");
            }
            if (beer_reivew_id == 0) {
                // Could create it. Don't know why...
                throw new MenuException("Unable to create new beer.");
            }
            return beer_reivew_id;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse the exception.
            if (ex.getMessage().contains("user_account_permissions")) {
                // This is a serious event. It means the cookie permission was likely
                // manually tampered with.
                // You should probably blacklist this IP.
                //@TODO consider blacklisting this IP.
                throw new Exception("Sorry! You do not have permission to post food reviews.");
            }
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("Sorry! You've already created a review for this food!");
            }
            // Unkonwn reason.
            throw new Exception("Unable to create food review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}

