package com.UserSocial.UserBlacklist;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class UserBlacklistModel extends AbstractModel {
    private String createUserBlacklistSQL =
            "INSERT INTO" +
                    "   user_blacklists" +
                    "(" +
                    "   primary_account_id," +
                    "   secondary_account_id" +
                    ") VALUES (" +
                    "?,?)" +
                    "RETURNING id";

    public UserBlacklistModel() throws Exception {}

    public int createUserBlacklist(
            String cookie,
            int secondary_account_id
    ) throws Exception {
        // We need to validate the request and make sure "user_blacklists" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "user_blacklists");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createUserBlacklistSQL);
        preparedStatement.setInt(1, this.cookiePair.userID);
        preparedStatement.setInt(2, secondary_account_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int user_blacklist_id = 0;
        while (resultSet.next()) {
            user_blacklist_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (user_blacklist_id != 0) { return user_blacklist_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create user blacklist.");
    }

}

