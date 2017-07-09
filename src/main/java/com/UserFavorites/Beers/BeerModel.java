package com.UserFavorites.Beers;

import com.Common.AbstractModel;
import com.Common.Beer;

import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
public class BeerModel extends AbstractModel {

    public BeerModel() throws Exception {}

    private String createUserBeerFavoriteSQL_stage1 =
            "INSERT INTO " +
                    "   user_beer_favorites (" +
                    "   account_id, " +
                    "   vendor_id" +
                    ") VALUES (?,?) " +
                    "RETURNING id";

    public int createUserBeerFavorite (
            String cookie,
            int beer_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        try {
            // Just check the session, no permissions here becaues all users can
            // have favorites (it doesn't affect any other accounts) and all breweries
            // can be favorited.
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.createUserBeerFavoriteSQL_stage1);
            stage1.setInt(1, this.userCookie.userID);
            stage1.setInt(2, beer_id);
            stage1Result = stage1.executeQuery();
            int user_favorite_id = 0;
            while (stage1Result.next()) {
                user_favorite_id = stage1Result.getInt("id");
            }
            return user_favorite_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to create new user-beer favorite.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteUserPlaceFavoriteSQL_stage1 =
            "DELETE FROM " +
                    "   user_beer_favorites " +
                    "WHERE " +
                    "   account_id = ? " +
                    "AND " +
                    "   beer_id = ?";

    public boolean deleteUserBeerFavorite (
            String cookie,
            int beer_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        try {
            // Just check the session, no permissions here becaues all users can
            // have favorites (it doesn't affect any other accounts) and all breweries
            // can be favorited.
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.deleteUserPlaceFavoriteSQL_stage1);
            stage1.setInt(1, this.userCookie.userID);
            stage1.setInt(2, beer_id);
            stage1.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete user-beer favorite.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public HashMap<Integer, Beer> readUserBeerFavorites(
            String cookie,
            int limit,
            int offset
    ) throws Exception {
        return new HashMap<Integer, Beer>();
    }
}
