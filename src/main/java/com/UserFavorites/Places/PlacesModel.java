package com.UserFavorites.Places;

import com.Common.AbstractModel;
import com.Common.Brewery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
public class PlacesModel extends AbstractModel {

    public PlacesModel() throws Exception {}

    private String createUserPlaceFavoriteSQL_stage1 =
        "INSERT INTO " +
                "   user_vendor_favorites (" +
                "   account_id, " +
                "   vendor_id" +
                ") VALUES (?,?) " +
                "RETURNING id";

    public int createUserPlaceFavorite(
            String cookie,
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        try {
            // Just check the session, no permissions here becaues all users can
            // have favorites (it doesn't affect any other accounts) and all breweries
            // can be favorited.
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.createUserPlaceFavoriteSQL_stage1);
            stage1.setInt(1, this.userCookie.userID);
            stage1.setInt(2, brewery_id);
            stage1Result = stage1.executeQuery();
            int user_favorite_id = 0;
            while (stage1Result.next()) {
                user_favorite_id = stage1Result.getInt("id");
            }
            return user_favorite_id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to create new user-place favorite.");
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
                    "   user_vendor_favorites " +
                    "WHERE " +
                    "   account_id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    public boolean deleteUserPlaceFavorite(
            String cookie,
            int brewery_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        try {
            // Just check the session, no permissions here becaues all users can
            // have favorites (it doesn't affect any other accounts) and all breweries
            // can be favorited.
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.deleteUserPlaceFavoriteSQL_stage1);
            stage1.setInt(1, this.userCookie.userID);
            stage1.setInt(2, brewery_id);
            stage1.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete user-place favorite.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public HashMap<Integer, Brewery> readUserPlaceFavorites(
            String cookie,
            int limit,
            int offset
    ) throws Exception {
        return new HashMap<Integer, Brewery>();
    }
}
