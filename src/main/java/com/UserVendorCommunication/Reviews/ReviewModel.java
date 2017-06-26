package com.UserVendorCommunication.Reviews;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class ReviewModel extends AbstractModel {
    private String createBeerReviewSQL =
            "INSERT INTO" +
                    "   vendor_reviews" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   vendor_id," +
                    "   feature_id," +
                    "   stars," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?,?,?)" +
                    "RETURNING id";

    public ReviewModel() throws Exception {
    }

    public int createVendorReview(
            String cookie,
            int vendor_id,
            int stars,
            String content
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "vendor_review" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookiePermission(cookie, "vendor_review");
            preparedStatement = this.DAO.prepareStatement(this.createBeerReviewSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, this.userCookie.requestPermissionID);
            preparedStatement.setInt(3, vendor_id);
            // You are making the assumption that the vendor will have a feature_id of 14 enabled and attempting to
            // insert the record. If it fails, it fails due to a constraint failure on the ACL.
            preparedStatement.setInt(4, 9);
            preparedStatement.setInt(5, stars);
            preparedStatement.setString(6, content);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int vendor_reivew_id = 0;
            while (resultSet.next()) {
                vendor_reivew_id = resultSet.getInt("id");
            }
            if (vendor_reivew_id == 0) {
                throw new Exception("Unable to create vendor review.");
            }
            return vendor_reivew_id;
        } catch (Exception ex) {
            System.out.println(ex);
            // Try to parse error message.
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("You've already created a review for this brewery!");
            }
            if (ex.getMessage().contains("user_account_permissions")) {
                throw new Exception("Sorry! You don't have permissions to post brewery reviews.");
            }
            // Unknown reason.
            throw new Exception("Unable to create vendor review.");
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

