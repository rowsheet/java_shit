package com.UserSocial.UserAssociations;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class UserAssociationModel extends AbstractModel {
    private String createUserAssociationSQL =
            "INSERT INTO" +
                    "   user_associations" +
                    "(" +
                    "   primary_account_id," +
                    "   primary_user_permission_id," +
                    "   secondary_account_id," +
                    "   secondary_user_permission_id," +
                    "   status" +
                    ") VALUES (" +
                    "?,?,?,?,?::user_association_status)" +
                    "RETURNING id";

    public UserAssociationModel() throws Exception {}

    public int createUserAssociation(
        String cookie,
        int secondary_account_id,
        String status
    ) throws Exception {
        // We need to validate the request and make sure "user_associations" is in the user permission
        // cookie before we insert a new record.
        this.validateCookiePermission(cookie, "user_associations");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createUserAssociationSQL);
        preparedStatement.setInt(1, this.userCookie.userID);
        preparedStatement.setInt(2, this.userCookie.userPermissionID);
        preparedStatement.setInt(3, secondary_account_id);
        // Under the assumption that the requested friend is allowed to have friends, we assume their
        // user permission ID is valid, otherwise this will fail on the foreign key constraint which
        // is a corner case.
        preparedStatement.setInt(4, 4);
        preparedStatement.setString(5, status);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int user_association_id = 0;
        while (resultSet.next()) {
            user_association_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (user_association_id != 0) { return user_association_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create user association.");
    }

}

