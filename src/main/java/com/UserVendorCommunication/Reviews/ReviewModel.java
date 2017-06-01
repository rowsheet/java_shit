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
        // We need to validate the vendor request and make sure "vendor_review" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookiePermission(cookie, "vendor_review");
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createBeerReviewSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setInt(3, vendor_id);
        // You are making the assumption that the vendor will have a feature_id of 14 enabled and attempting to
        // insert the record. If it fails, it fails due to a constraint failure on the ACL.
        preparedStatement.setInt(4, 9);
        preparedStatement.setInt(5, stars);
        preparedStatement.setString(6, content);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int vendor_reivew_id = 0;
        while (resultSet.next()) {
            vendor_reivew_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) {
            preparedStatement = null;
        }
        if (resultSet != null) {
            resultSet = null;
        }
        if (vendor_reivew_id != 0) { return vendor_reivew_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create vendor review.");
    }
}

