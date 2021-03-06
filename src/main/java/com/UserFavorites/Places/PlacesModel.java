package com.UserFavorites.Places;

import com.Common.AbstractModel;
import com.Common.PublicVendor.Brewery;
import com.Common.UserFavorites.FavoritePlace;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    public boolean createUserPlaceFavorite(
            String cookie,
            int vendor_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        try {
            // Just check the session, no permissions here becaues all users can
            // have favorites (it doesn't affect any other accounts) and all breweries
            // can be favorited.
            int account_id = this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.createUserPlaceFavoriteSQL_stage1);
            stage1.setInt(1, this.userCookie.userID);
            stage1.setInt(2, vendor_id);
            stage1.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // If it's already been added, they're probably un-favoriting it.
            if (ex.getMessage().contains("user_vendor_favorites_account_id_vendor_id_idx")) {
                this.deleteUserPlaceFavorite(
                        cookie,
                        vendor_id
                );
                return false;
            }
            throw new Exception("Unable to create new user-place favorite.");
        } finally {
            if (stage1 != null) {
                stage1.close();
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
            int vendor_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        try {
            // Just check the session, no permissions here becaues all users can
            // have favorites (it doesn't affect any other accounts) and all breweries
            // can be favorited.
            int account_id = this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.deleteUserPlaceFavoriteSQL_stage1);
            stage1.setInt(1, this.userCookie.userID);
            stage1.setInt(2, vendor_id);
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

    private String loadUserPlaceFavoritesSQL =
            "SELECT " +
                    "   v.id as vendor_id, " +
                    "   COALESCE(vi.display_name, 'NA') AS display_name, " +
                    "   COALESCE(vi.about_text, 'NA') AS about_text, " +
                    "   COALESCE(TO_CHAR(vi.mon_open, 'HH:MI AM'), '00:00:00') AS mon_open," +
                    "   COALESCE(TO_CHAR(vi.mon_close, 'HH:MI AM'), '00:00:00') AS mon_close," +
                    "   COALESCE(TO_CHAR(vi.tue_open, 'HH:MI AM'), '00:00:00') AS tue_open," +
                    "   COALESCE(TO_CHAR(vi.tue_close, 'HH:MI AM'), '00:00:00') AS tue_close," +
                    "   COALESCE(TO_CHAR(vi.wed_open, 'HH:MI AM'), '00:00:00') AS wed_open," +
                    "   COALESCE(TO_CHAR(vi.wed_close, 'HH:MI AM'), '00:00:00') AS wed_close," +
                    "   COALESCE(TO_CHAR(vi.thu_open, 'HH:MI AM'), '00:00:00') AS thu_open," +
                    "   COALESCE(TO_CHAR(vi.thu_close, 'HH:MI AM'), '00:00:00') AS thu_close," +
                    "   COALESCE(TO_CHAR(vi.fri_open, 'HH:MI AM'), '00:00:00') AS fri_open," +
                    "   COALESCE(TO_CHAR(vi.fri_close, 'HH:MI AM'), '00:00:00') AS fri_close," +
                    "   COALESCE(TO_CHAR(vi.sat_open, 'HH:MI AM'), '00:00:00') AS sat_open," +
                    "   COALESCE(TO_CHAR(vi.sat_close, 'HH:MI AM'), '00:00:00') AS sat_close," +
                    "   COALESCE(TO_CHAR(vi.sun_open, 'HH:MI AM'), '00:00:00') AS sun_open," +
                    "   COALESCE(TO_CHAR(vi.sun_close, 'HH:MI AM'), '00:00:00') AS sun_close," +
                    "   v.street_address, " +
                    "   v.city, " +
                    "   v.state, " +
                    "   v.zip, " +
                    "   COALESCE(vi.public_phone, 'NA') AS public_phone, " +
                    "   COALESCE(vi.public_email, 'NA') AS public_email, " +
                    "   COALESCE(v.google_maps_address, 'NA') AS google_maps_address, " +
                    "   COALESCE(v.latitude, 0.0) AS latitude, " +
                    "   COALESCE(v.longitude, 0.0) AS longitude, " +
                    "   COALESCE(v.google_maps_zoom, 0) AS google_maps_zoom," +
                    "   vi.short_type_description, " +
                    "   vi.short_text_description, " +
                    "   COALESCE(v.short_code, 'NA') AS short_code, " +
                    "   vi.main_image_id, " +
                    "   vi.main_gallery_id," +
                    "   COALESCE(vpi.filename, 'NA') AS main_image_filename " +
                    "FROM " +
                    "   vendors v " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   v.id = vi.vendor_id " +
                    "LEFT JOIN " +
                    "   vendor_page_images vpi " +
                    "ON " +
                    "   vi.main_image_id = vpi.id " +
                    "LEFT JOIN " +
                    "   user_vendor_favorites uvf " +
                    "ON " +
                    "   v.id = uvf.vendor_id " +
                    "WHERE " +
                    "   uvf.account_id = ?";

    public HashMap<Integer, FavoritePlace> loadUserPlaceFavorites(
            String cookie
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            this.validateUserCookie(cookie);
            HashMap<Integer, FavoritePlace> favoritePlaceHashMap = new HashMap<Integer, FavoritePlace>();
            preparedStatement = this.DAO.prepareStatement(loadUserPlaceFavoritesSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FavoritePlace favoritePlace = new FavoritePlace();
                favoritePlace.vendor_id = resultSet.getInt("vendor_id");
                favoritePlace.mon_open = resultSet.getString("mon_open");
                favoritePlace.mon_close = resultSet.getString("mon_close");
                favoritePlace.tue_open = resultSet.getString("tue_open");
                favoritePlace.tue_close = resultSet.getString("tue_close");
                favoritePlace.wed_open = resultSet.getString("wed_open");
                favoritePlace.wed_close = resultSet.getString("wed_close");
                favoritePlace.thu_open = resultSet.getString("thu_open");
                favoritePlace.thu_close = resultSet.getString("thu_close");
                favoritePlace.fri_open = resultSet.getString("fri_open");
                favoritePlace.fri_close = resultSet.getString("fri_close");
                favoritePlace.sat_open = resultSet.getString("sat_open");
                favoritePlace.sat_close = resultSet.getString("sat_close");
                favoritePlace.sun_open = resultSet.getString("sun_open");
                favoritePlace.sun_close = resultSet.getString("sun_close");
                favoritePlace.short_type_description = resultSet.getString("short_type_description");
                favoritePlace.short_text_description = resultSet.getString("short_text_description");
                favoritePlace.display_name = resultSet.getString("display_name");
                favoritePlace.google_maps_address = resultSet.getString("google_maps_address");
                favoritePlace.latitude = resultSet.getFloat("latitude");
                favoritePlace.longitude = resultSet.getFloat("longitude");
                favoritePlace.google_maps_zoom = resultSet.getInt("google_maps_zoom");
                favoritePlace.street_address = resultSet.getString("street_address");
                favoritePlace.city = resultSet.getString("city");
                favoritePlace.state = resultSet.getString("state");
                favoritePlace.zip = resultSet.getString("zip");
                favoritePlace.main_gallery_id = resultSet.getInt("main_gallery_id");
                favoritePlace.main_image_filename = resultSet.getString("main_image_filename");
                favoritePlace.about_text = resultSet.getString("about_text");
                favoritePlace.public_phone = resultSet.getString("public_phone");
                favoritePlace.public_email = resultSet.getString("public_email");
                favoritePlace.short_code = resultSet.getString("short_code");
                favoritePlaceHashMap.put(favoritePlace.vendor_id, favoritePlace);
            }
            return favoritePlaceHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to load favorite places.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
