package com.VendorBlogging.Posts;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class PostModel extends AbstractModel {
    private String createPostSQL =
            "INSERT INTO" +
                    "   vendor_posts" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   title," +
                    "   account_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?,?)" +
                    "RETURNING id";

    public PostModel() throws Exception {}

    public int createPost(
            String cookie,
            String title,
            String content
    ) throws Exception {
        // We need to validate the vendor request and make sure "blog" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookieVendorFeature(cookie, "blog");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createPostSQL);
        preparedStatement.setInt(1, this.vendorCookie.vendorID);
        preparedStatement.setInt(2, this.vendorCookie.featureID);
        preparedStatement.setString(3, title);
        preparedStatement.setInt(4, this.vendorCookie.accountID);
        preparedStatement.setString(5, content);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int post_id = 0;
        while (resultSet.next()) {
            post_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (post_id != 0) { return post_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create post.");
    }
}

