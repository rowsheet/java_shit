package com.VendorMemberships.Promotions;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class PromotionModel extends AbstractModel {
    private String createPromotionSQL =
            "INSERT INTO" +
                    "   vendor_membership_promotions" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   membership_id," +
                    "   title," +
                    "   description," +
                    "   start_date," +
                    "   end_date," +
                    "   max_accounts," +
                    "   promotion_categories" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?,?::promotion_category[])" +
                    "RETURNING id";

    public PromotionModel() throws Exception {}

    public int createPromotion(
        String cookie,
        int membership_id,
        String title,
        String description,
        String start_date,
        String end_date,
        int max_accounts,
        String[] promotion_categories
    ) throws Exception {
        // We need to validate the vendor request and make sure "promotions" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookieVendorFeature(cookie, "promotions");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createPromotionSQL);
        preparedStatement.setInt(1, this.vendorCookiePair.vendorID);
        preparedStatement.setInt(2, this.vendorCookiePair.featureID);
        preparedStatement.setInt(3, membership_id);
        preparedStatement.setString(4, title);
        preparedStatement.setString(5, description);
        preparedStatement.setTimestamp(6, Timestamp.valueOf(start_date));
        preparedStatement.setTimestamp(7, Timestamp.valueOf(end_date));
        preparedStatement.setInt(8, max_accounts);
        preparedStatement.setArray(9, this.DAO.createArrayOf("promotion_category", promotion_categories));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int promotion_id = 0;
        while (resultSet.next()) {
            promotion_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (promotion_id != 0) { return promotion_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create promotion.");
    }

}
