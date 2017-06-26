package com.UserVendorCommunication.Blogs;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BlogModel extends AbstractModel {
    private String createBlogPostCommentSQL =
            "INSERT INTO" +
                    "   vendor_post_comments" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   vendor_post_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?)" +
                    "RETURNING id";

    private String createBlogPostCommentReplySQL =
            "INSERT INTO" +
                    "   vendor_post_comment_replies" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   vendor_post_comment_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?)" +
                    "RETURNING id";

    public BlogModel() throws Exception {}

    public int createBlogPostComment(
            String cookie,
            int vendor_post_id,
            String content
    ) throws Exception {
        // We need to validate the vendor request and make sure "vendor_blog_comment" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookiePermission(cookie, "vendor_blog_comment");
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createBlogPostCommentSQL);
        preparedStatement.setInt(1, this.userCookie.userID);
        preparedStatement.setInt(2, this.userCookie.requestPermissionID);
        preparedStatement.setInt(3, vendor_post_id);
        preparedStatement.setString(4, content);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int comment_id = 0;
        while (resultSet.next()) {
            comment_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (comment_id != 0) { return comment_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create comment.");
    }

    public int createBlogPostCommentReply(
            String cookie,
            int vendor_post_comment_id,
            String content
    ) throws Exception {
        // We need to validate the vendor request and make sure "vendor_blog_comment" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookiePermission(cookie, "vendor_blog_comment");
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createBlogPostCommentReplySQL);
        preparedStatement.setInt(1, this.userCookie.userID);
        preparedStatement.setInt(2, this.userCookie.requestPermissionID);
        preparedStatement.setInt(3, vendor_post_comment_id);
        preparedStatement.setString(4, content);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int comment_reply_id = 0;
        while (resultSet.next()) {
            comment_reply_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (comment_reply_id != 0) { return comment_reply_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create comment reply.");
    }
}

