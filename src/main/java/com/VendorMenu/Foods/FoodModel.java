package com.VendorMenu.Foods;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class FoodModel extends AbstractModel {

    private String createFoodSQL =
            "INSERT INTO" +
                    "   vendor_foods" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name," +
                    "   description," +
                    "   price," +
                    "   food_sizes" +
                    ") VALUES (" +
                    "?,?,?,?,?,?::food_size[])" +
                    "RETURNING id";

   public FoodModel() throws Exception {}

    public int createFood(
            String cookie,
            String name,
            String description,
            float price,
            String[] food_sizes
    ) throws Exception {
        // We need to validate the vendor request and make sure "food_menu" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookieVendorFeature(cookie, "food_menu");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createFoodSQL);
        preparedStatement.setInt(1, this.vendorCookiePair.vendorID);
        preparedStatement.setInt(2, this.vendorCookiePair.featureID);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, description);
        preparedStatement.setFloat(5, price);
        preparedStatement.setArray(6, this.DAO.createArrayOf("food_size", food_sizes));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int food_id = 0;
        while (resultSet.next()) {
            food_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (food_id != 0) { return food_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create food.");
    }

}
