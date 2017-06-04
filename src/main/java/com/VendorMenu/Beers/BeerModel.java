package com.VendorMenu.Beers;

import com.Common.AbstractModel;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerModel extends AbstractModel {
    private String createBeerSQL =
            "INSERT INTO" +
                    "   beers" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name," +
                    "   color," +
                    "   bitterness," +
                    "   abv," +
                    "   beer_style," +
                    "   beer_tastes," +
                    "   description," +
                    "   price," +
                    "   beer_sizes" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?::beer_style,?::beer_taste[],?,?,?::beer_size[])" +
                    "RETURNING id";

    public BeerModel() throws Exception {}

    public int createBeer(
        String cookie,
        String name,
        int color,
        int bitterness,
        int abv,
        String beer_style,
        String[] beer_tastes,
        String description,
        float price,
        String[] beer_sizes
    ) throws Exception {
        // We need to validate the vendor request and make sure "beer_menu" is one of the vendor features
        // and is in the cookie before we insert a new record.
        this.validateCookieVendorFeature(cookie, "beer_menu");
        // After we insert the record, we need to get the ID of the record back.
        PreparedStatement preparedStatement = this.DAO.prepareStatement(this.createBeerSQL);
        preparedStatement.setInt(1, this.vendorCookie.vendorID);
        preparedStatement.setInt(2, this.vendorCookie.featureID);
        preparedStatement.setString(3, name);
        preparedStatement.setInt(4, color);
        preparedStatement.setInt(5, bitterness);
        preparedStatement.setInt(6, abv);
        preparedStatement.setString(7, beer_style);
        preparedStatement.setArray(8, this.DAO.createArrayOf("beer_taste", beer_tastes));
        preparedStatement.setString(9, description);
        preparedStatement.setFloat(10, price);
        preparedStatement.setArray(11, this.DAO.createArrayOf("beer_size", beer_sizes));
        ResultSet resultSet = preparedStatement.executeQuery();
        // Get the id of the new entry.
        int beer_id = 0;
        while (resultSet.next()) {
            beer_id = resultSet.getInt("id");
        }
        // Clean up and return.
        if (preparedStatement != null) { preparedStatement = null; }
        if (resultSet != null) { resultSet = null; }
        if (beer_id != 0) { return beer_id; }
        // Return unable exception if no id found.
        throw new Exception("Unable to create beer.");
    }

}
