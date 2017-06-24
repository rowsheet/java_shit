package com.VendorMenu.Beers;

import com.Common.AbstractModel;
import com.Common.Beer;
import com.sun.org.apache.regexp.internal.RE;

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
                    "   beer_sizes," +
                    "   hop_score" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?::beer_style,?::beer_taste[],?,?,?::beer_size[],?::hop_score)" +
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
        String[] beer_sizes,
        String hop_score
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "beer_menu" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookieVendorFeature(cookie, "beer_menu");
            // After we insert the record, we need to get the ID of the record back.
            preparedStatement = this.DAO.prepareStatement(this.createBeerSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, this.vendorCookie.requestFeatureID);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, color);
            preparedStatement.setInt(5, bitterness);
            preparedStatement.setInt(6, abv);
            preparedStatement.setString(7, beer_style);
            preparedStatement.setArray(8, this.DAO.createArrayOf("beer_taste", beer_tastes));
            preparedStatement.setString(9, description);
            preparedStatement.setFloat(10, price);
            preparedStatement.setArray(11, this.DAO.createArrayOf("beer_size", beer_sizes));
            preparedStatement.setString(12, hop_score);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_id = 0;
            while (resultSet.next()) {
                beer_id = resultSet.getInt("id");
            }
            if (beer_id == 0) {
                throw new BeerException("Unable to create beer.");
            }
            return beer_id;
            // Return unable exception if no id found.
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Try to parse exception.
            if (ex.getMessage().contains("beers_vendor_id_name_idx") &&
                    ex.getMessage().contains("already exists")) {
                // Name for beer taken with this vendor_id (unique constraint).
                throw new Exception("This brewery already as a beer with that name!");
            }
            throw new Exception("Unable to create beer."); // Unknown reason.
        } finally {
            // Clean up and return.
            if (preparedStatement != null) {
                preparedStatement = null;
            }
            if (resultSet != null) {
                resultSet = null;
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

}
