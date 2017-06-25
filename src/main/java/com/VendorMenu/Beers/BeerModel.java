package com.VendorMenu.Beers;

import com.Common.AbstractModel;
import com.Common.Beer;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerModel extends AbstractModel {

    private String updateBeerSQL_stage2 =
            "SELECT " +
                    "   b.id " +
                    "FROM" +
                    "   beers b " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   b.vendor_id = v.id " +
                    "WHERE " +
                    "   b.id = ? " +
                    "AND " +
                    "   v.id = ? " +
                    "LIMIT 1";

    private String updateBeerSQL_stage3 =
            "UPDATE " +
                    "   beers " +
                    "SET" +
                    "   name = ?, " +
                    "   color = ?, " +
                    "   bitterness = ?, " +
                    "   abv = ?, " +
                    "   beer_style = ?::beer_style, " +
                    "   beer_tastes = ?::beer_taste[], " +
                    "   description = ?, " +
                    "   price = ?, " +
                    "   beer_sizes = ?::beer_size[], " +
                    "   hop_score = ?::hop_score " +
                    "WHERE " +
                    "   id = ?";

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

    /**
     * Updates beer as per request as long as session and user valid and beer is
     * ownded by user. Does three things:
     *
     *      1) Validates session and permissions.
     *      2) Ensures resource ownership.
     *      3) Updates data.
     *
     * Do all three steps in a transaction and roll back on exception.
     *
     * @param cookie
     * @param id
     * @param name
     * @param color
     * @param bitterness
     * @param abv
     * @param beer_style
     * @param beer_tastes
     * @param description
     * @param price
     * @param beer_sizes
     * @param hop_score
     * @return
     * @throws Exception
     */
    public boolean updateBeer(
        String cookie,
        int id,
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
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
             */
            this.validateCookieVendorFeature(cookie, "beer_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.updateBeerSQL_stage2);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int beer_id = 0;
            while (stage2Result.next()) {
                beer_id = stage2Result.getInt("id");
            }
            if (beer_id == 0) {
                // Beer is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new BeerException("This account does not have have permission to change this food."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateBeerSQL_stage3);
            stage3.setString(1, name);
            stage3.setInt(2, color);
            stage3.setInt(3, bitterness);
            stage3.setInt(4, abv);
            stage3.setString(5, beer_style);
            stage3.setArray(6, this.DAO.createArrayOf("beer_taste", beer_tastes));
            stage3.setString(7, description);
            stage3.setFloat(8, price);
            stage3.setArray(9, this.DAO.createArrayOf("beer_size", beer_sizes));
            stage3.setString(10, hop_score);
            stage3.setInt(11, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // Unknown error.
            throw new Exception("Unable to create beer.");
        } finally {
            /*
            Reset auto-commit to true.
             */
            this.DAO.setAutoCommit(true);
            /*
            Clean up.
             */
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

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
