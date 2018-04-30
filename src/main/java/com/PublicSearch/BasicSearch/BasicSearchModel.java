package com.PublicSearch.BasicSearch;

import com.Common.AbstractModel;
import com.Common.UserFavorites.FavoriteBeer;
import com.Common.UserFavorites.FavoritePlace;
import com.Common.UserFavorites.FavoriteVendorDrink;
import com.Common.UserFavorites.FavoriteVendorFood;
import jnr.ffi.annotations.In;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Basic search will return "favorite" data-structres, not full ones (because they're simple).
 * In the future, something better will take this place. For now (while we're in beta), this
 * will just use keywords.
 */
public class BasicSearchModel extends AbstractModel  {
    public BasicSearchModel() throws Exception { super(); }

    private String basicSearchVendorsSQL_shortcode =
            "SELECT id " +
                    "FROM " +
                    "   vendors " +
                    "WHERE " +
                    "   short_code = UPPER(?)"; // No need to limit, short_code is unique and this is an exact match.

    private String basicSearchVendorsSQL_reid1 =
            "SELECT vendor_id " +
                    "FROM " +
                    "   vendor_info " +
                    "WHERE " +
                    "   display_name ILIKE ANY (?) " +
                    "LIMIT ? OFFSET ?";

    private String basicSearchVendorsSQL_reid2 =
            "SELECT vendor_id " +
                    "FROM " +
                    "   vendor_info " +
                    "WHERE " +
                    "   short_text_description ILIKE ANY (?) " +
                    "LIMIT ? OFFSET ?";

    private String basicSearchVendorsSQL_reid3 =
            "SELECT vendor_id " +
                    "FROM " +
                    "   vendor_info " +
                    "WHERE " +
                    "   about_text ILIKE ANY (?) " +
                    "LIMIT ? OFFSET ?";

    private String basicSearchVendorsSQL_fromIDS =
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
                    "   v.id = ANY(?)";

    /**
     * Search with ILIKE ANY in the following order:
     *      1) Search for anything with short-code.
     *      note: only do this if offset = 0 and keyword is the only argument;
     * Next prioritize in the following order.
     *      2) vendor_info.display_name.
     *      3) vendor_info.short_type_description.
     *      4) vendor_info.short_text_description.
     * If there are no results given the limit and offset, search for more in about_text.
     *      5) vendor_info.about_text.
     * Return a hash-map so duplicates are removed (length should be good if offset was incremented properly).
     * @param keywords
     * @return
     * @throws Exception
     */
    public HashMap<Integer, FavoritePlace> basicSearchVendors(
            String[] keywords,
            int limit,
            int offset,
            int reid
    ) throws Exception {
        System.out.println(keywords[0]);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, FavoritePlace> placeHashMap = new HashMap<Integer, FavoritePlace>();
            ArrayList<Integer> vendor_ids = new ArrayList<Integer>();
            if (reid == 1) {
                preparedStatement = this.DAO.prepareStatement(basicSearchVendorsSQL_shortcode);
                preparedStatement.setString(1, keywords[0]);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    vendor_ids.add(resultSet.getInt("id"));
                }
            }
            for (int i = 0; i < keywords.length; i++) {
                keywords[i] = "%" + keywords[i] + "%";
            }
            if (reid == 1) {
                preparedStatement = this.DAO.prepareStatement(this.basicSearchVendorsSQL_reid1);
                preparedStatement.setArray(1, this.DAO.createArrayOf("VARCHAR", keywords));
                preparedStatement.setInt(2, limit - vendor_ids.size());
                preparedStatement.setInt(3, offset);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    vendor_ids.add(resultSet.getInt("vendor_id"));
                }
            }
            if (reid == 2) {
                preparedStatement = this.DAO.prepareStatement(this.basicSearchVendorsSQL_reid2);
                preparedStatement.setArray(1, this.DAO.createArrayOf("VARCHAR", keywords));
                preparedStatement.setInt(2, limit);
                preparedStatement.setInt(3, offset);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    vendor_ids.add(resultSet.getInt("vendor_id"));
                }
            }
            if (reid == 3) {
                preparedStatement = this.DAO.prepareStatement(this.basicSearchVendorsSQL_reid3);
                preparedStatement.setArray(1, this.DAO.createArrayOf("VARCHAR", keywords));
                preparedStatement.setInt(2, limit);
                preparedStatement.setInt(3, offset);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    vendor_ids.add(resultSet.getInt("vendor_id"));
                }
            }
            preparedStatement = this.DAO.prepareStatement(this.basicSearchVendorsSQL_fromIDS);
            preparedStatement.setArray(1, this.DAO.createArrayOf("INTEGER", vendor_ids.toArray()));
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
                placeHashMap.put(favoritePlace.vendor_id, favoritePlace);
            }
            return placeHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Fuck.
            throw new Exception("Ooops... Something went wrong with search! We're fixing it as fast as we can!");
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

    public HashMap<Integer, FavoriteBeer> basicSearchBeers(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, FavoriteBeer> favoriteBeerHashMap = new HashMap<Integer, FavoriteBeer>();
        return favoriteBeerHashMap;
    }

    public HashMap<Integer, FavoriteVendorFood> basicSearchFoods(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, FavoriteVendorFood> favoriteVendorFoodHashMap = new HashMap<Integer, FavoriteVendorFood>();
        return favoriteVendorFoodHashMap;
    }

    public HashMap<Integer, FavoriteVendorDrink> basicSearchDrinks(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, FavoriteVendorDrink> favoriteVendorDrinkHashMap = new HashMap<Integer, FavoriteVendorDrink>();
        return favoriteVendorDrinkHashMap;
    }
}
