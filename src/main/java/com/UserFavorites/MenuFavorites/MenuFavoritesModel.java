package com.UserFavorites.MenuFavorites;

import com.Common.AbstractModel;
import java.sql.*;

public class MenuFavoritesModel extends AbstractModel {

    public MenuFavoritesModel() throws Exception {}

    private String createFoodFavorite_sql =
            "INSERT INTO " +
                    "vendor_food_favorites (" +
                    "   account_id, " +
                    "   account_type, " +
                    "   vendor_food_id, " +
                    "   item_name" +
                    ") VALUES (?,'user'::account_type,?," +
                    "   (SELECT name FROM vendor_foods WHERE id = ?))";

    public boolean createFoodFavorite(
            String cookie,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.createFoodFavorite_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, vendor_food_id);
            preparedStatement.setInt(3, vendor_food_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("vendor_food_favorites_account_id_vendor_food_id_idx")) {
                // Already added to favorites.
                return true;
            }
            throw new Exception("Unable to add food to favorites list.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteFoodFavorite_sql =
            "DELETE FROM " +
                    "   vendor_food_favorites " +
                    "WHERE " +
                    "   account_id = ?" +
                    "AND " +
                    "   vendor_food_id = ?";

    public boolean deleteFoodFavorite(
            String cookie,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.deleteFoodFavorite_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, vendor_food_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete from food favorites list");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String createDrinkFavorite_sql =
            "INSERT INTO " +
                    "vendor_drink_favorites (" +
                    "   account_id, " +
                    "   account_type, " +
                    "   vendor_drink_id, " +
                    "   item_name" +
                    ") VALUES (?,'user'::account_type,?," +
                    "   (SELECT name FROM vendor_drinks WHERE id = ?))";

    public boolean createDrinkFavorite(
            String cookie,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.createDrinkFavorite_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, vendor_drink_id);
            preparedStatement.setInt(3, vendor_drink_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("vendor_drink_favorites_account_id_vendor_drink_id_idx")) {
                // Already added to favorites.
                return true;
            }
            throw new Exception("Unable to add drink to favorites list.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteDrinkFavorite_sql =
            "DELETE FROM " +
                    "   vendor_drink_favorites " +
                    "WHERE " +
                    "   account_id = ? " +
                    "AND " +
                    "   vendor_drink_id = ?";

    public boolean deleteDrinkFavorite(
            String cookie,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.deleteDrinkFavorite_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, vendor_drink_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete from drink favorites list");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String createBeerFavorite_sql =
            "INSERT INTO " +
                    "beer_favorites (" +
                    "   account_id, " +
                    "   account_type, " +
                    "   beer_id, " +
                    "   item_name" +
                    ") VALUES (?,'user'::account_type,?," +
                    "   (SELECT name FROM beers WHERE id = ?))";

    public boolean createBeerFavorite(
            String cookie,
            int beer_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.createBeerFavorite_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, beer_id);
            preparedStatement.setInt(3, beer_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("beer_favorites_account_id_beer_id_idx")) {
                // Already added to favorites.
                return true;
            }
            throw new Exception("Unable to add beer to favorites list.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteBeerFavorite_sql =
            "DELETE FROM " +
                    "   beer_favorites " +
                    "WHERE " +
                    "   account_id = ? " +
                    "AND " +
                    "   beer_id = ?";

    public boolean deleteBeerFavorite(
            String cookie,
            int beer_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.deleteBeerFavorite_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, beer_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete from beer favorites list");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
