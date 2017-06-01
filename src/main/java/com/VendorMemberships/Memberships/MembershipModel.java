package com.VendorMemberships.Memberships;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MembershipModel extends AbstractModel {
    private String createMembershipSQL =
            "INSERT INTO" +
                    "   vendor_memberships" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name," +
                    "   description," +
                    "   membership_categories" +
                    ") VALUES (" +
                    "?,?,?,?,?::membership_category[])" +
                    "RETURNING id";

    public MembershipModel() throws Exception {}

    public int createMembership(
            String cookie,
            String name,
            String description,
            String[] membership_categories
    ) throws Exception {
        // We need to validate the vendor request and make sure "memberships" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookieVendorFeature(cookie, "memberships");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createMembershipSQL);
        preparedStatement.setInt(1, this.vendorCookiePair.vendorID);
        preparedStatement.setInt(2, this.vendorCookiePair.featureID);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, description);
        preparedStatement.setArray(5, this.DAO.createArrayOf("membership_category", membership_categories));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int membership_id = 0;
        while (resultSet.next()) {
            membership_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (membership_id != 0) { return membership_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create membership.");
    }

}

