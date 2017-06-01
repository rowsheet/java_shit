package com.UserSocial.UserMessaging;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class UserMessagingModel extends AbstractModel {
    private String createUserMessageSQL =
            "INSERT INTO" +
                    "   user_messages" +
                    "(" +
                    "   sender_account_id," +
                    "   sender_user_permission_id," +
                    "   reciever_account_id," +
                    "   reciever_user_permission_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?,?)" +
                    "RETURNING id";

    public UserMessagingModel() throws Exception {}

    public int createUserMessage(
            String cookie,
            int reciever_account_id,
            String content
    ) throws Exception {
        // We need to validate the request and make sure "user_messages" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "user_messages");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createUserMessageSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, this.cookiePair.userPermissionID);
        preparedStatement.setInt(3, reciever_account_id);
        // Under the assumption that recieving messages has not been disabled for the user.
        preparedStatement.setInt(4, 5);
        preparedStatement.setString(5, content);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int user_message_id = 0;
        while (resultSet.next()) {
            user_message_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (user_message_id != 0) { return user_message_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create user message.");
    }

}

