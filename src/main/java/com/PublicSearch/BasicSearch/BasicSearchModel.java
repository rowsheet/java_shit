package com.PublicSearch.BasicSearch;

import com.Common.AbstractModel;
import com.Common.UserFavorites.FavoriteBeer;
import com.Common.UserFavorites.FavoritePlace;
import com.Common.UserFavorites.FavoriteVendorDrink;
import com.Common.UserFavorites.FavoriteVendorFood;
import com.Common.BeerTag;
import com.Common.VendorFoodTag;
import com.Common.VendorDrinkTag;
import com.Common.VendorNutritionalFact;
import com.Common.Color;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Basic search will return "favorite" data-structres, not full ones (because they're simple).
 * In the future, something better will take this place. For now (while we're in beta), this
 * will just use keywords.
 */
public class BasicSearchModel extends AbstractModel  {
    public BasicSearchModel() throws Exception { super(); }

    private String basicSearchVendorsSQL_getIDs =
            "SELECT vendor_id " +
                    "FROM " +
                    "   english_vendor_search_ids evs " +
                    "WHERE " +
                    "<%to_tsquery%> " +
                    "ORDER BY ( " +
                    "<%ts_rank%> " +
                    ") DESC " +
                    "LIMIT ? OFFSET ?;";

    private String basicSearchVendorsSQL_fromIDS =
            "SELECT " +
                    "   vendor_id, " +
                    "   display_name, " +
                    "   about_text, " +
                    "   mon_open," +
                    "   mon_close," +
                    "   tue_open," +
                    "   tue_close," +
                    "   wed_open," +
                    "   wed_close," +
                    "   thu_open," +
                    "   thu_close," +
                    "   fri_open," +
                    "   fri_close," +
                    "   sat_open," +
                    "   sat_close," +
                    "   sun_open," +
                    "   sun_close," +
                    "   street_address, " +
                    "   city, " +
                    "   state, " +
                    "   zip, " +
                    "   public_phone, " +
                    "   public_email, " +
                    "   google_maps_address, " +
                    "   latitude, " +
                    "   longitude, " +
                    "   google_maps_zoom," +
                    "   short_type_description, " +
                    "   short_text_description, " +
                    "   short_code, " +
                    "   main_image_id, " +
                    "   main_gallery_id," +
                    "   main_image_filename " +
                    "FROM " +
                    "   english_vendor_search " +
                    "WHERE " +
                    "   vendor_id = ANY(?)";

    /**
     * It's all in the views.
     * @param keywords
     * @return
     * @throws Exception
     */
    public HashMap<Integer, FavoritePlace> basicSearchVendors(
            String[] keywords,
            int limit,
            int offset,
            int reid // depreciated.
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, FavoritePlace> placeHashMap = new HashMap<Integer, FavoritePlace>();
            ArrayList<Integer> vendor_ids = new ArrayList<Integer>();
            // Build to_tsquery.
            String to_tsquery = "";
            String to_tsquery_ps=  "evs.D @@ TO_TSQUERY('english', ?) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(to_tsquery_ps);
                preparedStatement.setString(1, keywords[i]);
                to_tsquery += preparedStatement.toString();
                if (i < (keywords.length -1)) {
                    to_tsquery += " OR ";
                }
            }
            // Build ts_rank.
            String ts_rank = "";
            int denominator = 1;
            String ts_rank_ps = "(TS_RANK(evs.D, TO_TSQUERY('english', ?))) / " + Float.toString(denominator);
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(ts_rank_ps);
                preparedStatement.setString(1, keywords[i]);
                ts_rank += preparedStatement.toString();
                if (i < (keywords.length -1)) {
                    ts_rank += " + ";
                }
                denominator += 0.379;
            }
            this.basicSearchVendorsSQL_getIDs = this.basicSearchVendorsSQL_getIDs.replace("<%to_tsquery%>", to_tsquery);
            this.basicSearchVendorsSQL_getIDs = this.basicSearchVendorsSQL_getIDs.replace("<%ts_rank%>", ts_rank);
            preparedStatement = this.DAO.prepareStatement(this.basicSearchVendorsSQL_getIDs);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                vendor_ids.add(resultSet.getInt("vendor_id"));
            }
            if (vendor_ids.size() == 0) {
                return placeHashMap;
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

    private String basicSearchBeersSQL_getIDs =
        "SELECT " +
                "   beer_id " +
                "FROM " +
                "   english_beer_search_ids s " +
                "WHERE " +
                "<%to_tsquery%> " +
                "ORDER BY ( " +
                "<%ts_rank%> " +
                ") DESC " +
                "LIMIT ? OFFSET ?;";

    private String basicSearchBeersSQL_fromIDs =
        "SELECT " +
                "    beer_id, " +
                "    beer_vendor_id, " +
                "    beer_name, " +
                "    beer_color, " +
                "    beer_bitterness, " +
                "    beer_abv, " +
                "    beer_style, " +
                "    beer_tastes, " +
                "    beer_description, " +
                "    beer_price, " +
                "    beer_sizes,  " +
                "    beer_hop_score,  " +
                "    creation_timestamp,  " +
                "    creation_days_ago,  " +
                "    beer_category_name,  " +
                "    beer_category_hex_color,  " +
                "    beer_category_id,  " +
                "    beer_category_description,  " +
                "    tag_one_id,  " +
                "    tag_one_name,  " +
                "    tag_one_hex_color,  " +
                "    tag_one_tag_type,  " +
                "    tag_two_id,  " +
                "    tag_two_name,  " +
                "    tag_two_hex_color,  " +
                "    tag_two_tag_type,  " +
                "    tag_three_id,  " +
                "    tag_three_name,  " +
                "    tag_three_hex_color,  " +
                "    tag_three_tag_type,  " +
                "    tag_four_id,  " +
                "    tag_four_name,  " +
                "    tag_four_hex_color,  " +
                "    tag_four_tag_type,  " +
                "    tag_five_id,  " +
                "    tag_five_name,  " +
                "    tag_five_hex_color,  " +
                "    tag_five_tag_type,  " +
                "    nutritional_fact_id,  " +
                "    nutritional_fact_serving_size,  " +
                "    nutritional_fact_calories,  " +
                "    nutritional_fact_calories_from_fat,  " +
                "    nutritional_fact_total_fat,  " +
                "    nutritional_fact_saturated_fat,  " +
                "    nutritional_fact_trans_fat,  " +
                "    nutritional_fact_cholesterol,  " +
                "    nutritional_fact_sodium,  " +
                "    nutritional_fact_total_carbs,  " +
                "    nutritional_fact_dietary_fiber,  " +
                "    nutritional_fact_sugar,  " +
                "    nutritional_fact_vitamin_a,  " +
                "    nutritional_fact_vitamin_b,  " +
                "    nutritional_fact_vitamin_c,  " +
                "    nutritional_fact_vitamin_d,  " +
                "    nutritional_fact_calcium,  " +
                "    nutritional_fact_iron,  " +
                "    nutritional_fact_protein,  " +
                "    nutritional_fact_profile_name,  " +
                "    vendor_name  " +
                "FROM " +
                "    english_beer_search " +
                "WHERE " +
                "   beer_id = ANY(?)";

    public HashMap<Integer, FavoriteBeer> basicSearchBeers(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, FavoriteBeer> beerHashMap = new HashMap<Integer, FavoriteBeer>();
            ArrayList<Integer> beer_ids = new ArrayList<Integer>();
            // Build to_tsquery.
            String to_tsquery = "";
            String to_tsquery_ps = "s.D @@ TO_TSQUERY('english', ?) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(to_tsquery_ps);
                preparedStatement.setString(1, keywords[i]);
                to_tsquery += preparedStatement.toString();
                if (i < (keywords.length - 1)) {
                    to_tsquery += " OR ";
                }
            }
            // Build ts_rank.
            String ts_rank = "";
            String ts_rank_ps = "TS_RANK(s.D, TO_TSQUERY('english', ?)) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(ts_rank_ps);
                preparedStatement.setString(1, keywords[i]);
                ts_rank += preparedStatement.toString();
                if (i < (keywords.length - 1)) {
                    ts_rank += " + ";
                }
            }
            // Swap terms in query.
            this.basicSearchBeersSQL_getIDs = this.basicSearchBeersSQL_getIDs.replace("<%to_tsquery%>", to_tsquery);
            this.basicSearchBeersSQL_getIDs = this.basicSearchBeersSQL_getIDs.replace("<%ts_rank%>", ts_rank);
            preparedStatement = this.DAO.prepareStatement(this.basicSearchBeersSQL_getIDs);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            // Fetch ids.
            while (resultSet.next()) {
                beer_ids.add(resultSet.getInt("beer_id"));
            }
            preparedStatement = this.DAO.prepareStatement(basicSearchBeersSQL_fromIDs);
            preparedStatement.setArray(1, this.DAO.createArrayOf("INTEGER", beer_ids.toArray()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = new Color();
                FavoriteBeer beer = new FavoriteBeer();
                beer.vendor_name = resultSet.getString("vendor_name");
                beer.beer_id = resultSet.getInt("beer_id");
                beer.vendor_id = resultSet.getInt("beer_vendor_id");
                beer.name = resultSet.getString("beer_name");
                beer.color = resultSet.getInt("beer_color");
                beer.bitterness = resultSet.getInt("beer_bitterness");
                beer.abv = resultSet.getInt("beer_abv");
                beer.beer_style = resultSet.getString("beer_style");
                // Beer tastes are an array of enums in postgres.
                Array beer_tastes = resultSet.getArray("beer_tastes");
                String[] str_beer_tastes = (String[]) beer_tastes.getArray();
                beer.beer_tastes = str_beer_tastes;
                beer.description = resultSet.getString("beer_description");
                beer.price = resultSet.getFloat("beer_price");
                // Beer sizes are an array of enums in postgres.
                Array beer_sizes = resultSet.getArray("beer_sizes");
                String[] str_beer_sizes = (String[]) beer_sizes.getArray();
                beer.beer_sizes = str_beer_sizes;
                beer.hop_score = resultSet.getString("beer_hop_score");
                beer.beer_category.name = resultSet.getString("beer_category_name");
                beer.beer_category.hex_color = resultSet.getString("beer_category_hex_color");
                beer.beer_category.vendor_id = beer.vendor_id;
                beer.beer_category.id = resultSet.getInt("beer_category_id");
                beer.beer_category.description = resultSet.getString("beer_category_description");
                beer.beer_category.text_color = color.getInverseBW(beer.beer_category.hex_color);
                beer.creation_timestamp = resultSet.getString("creation_timestamp");
                beer.creation_days_ago = resultSet.getString("creation_days_ago");
                BeerTag tag_one = new BeerTag();
                BeerTag tag_two = new BeerTag();
                BeerTag tag_three = new BeerTag();
                BeerTag tag_four = new BeerTag();
                BeerTag tag_five = new BeerTag();
                tag_one.id = resultSet.getInt("tag_one_id");
                tag_one.name = resultSet.getString("tag_one_name");
                tag_one.hex_color = resultSet.getString("tag_one_hex_color");
                tag_one.tag_type = resultSet.getString("tag_one_tag_type");
                tag_one.vendor_id = beer.vendor_id;
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                beer.tag_one = tag_one;
                tag_two.id = resultSet.getInt("tag_two_id");
                tag_two.name = resultSet.getString("tag_two_name");
                tag_two.hex_color = resultSet.getString("tag_two_hex_color");
                tag_two.tag_type = resultSet.getString("tag_two_tag_type");
                tag_two.vendor_id = beer.vendor_id;
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                beer.tag_two = tag_two;
                tag_three.id = resultSet.getInt("tag_three_id");
                tag_three.name = resultSet.getString("tag_three_name");
                tag_three.hex_color = resultSet.getString("tag_three_hex_color");
                tag_three.tag_type = resultSet.getString("tag_three_tag_type");
                tag_three.vendor_id = beer.vendor_id;
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                beer.tag_three = tag_three;
                tag_four.id = resultSet.getInt("tag_four_id");
                tag_four.name = resultSet.getString("tag_four_name");
                tag_four.hex_color = resultSet.getString("tag_four_hex_color");
                tag_four.tag_type = resultSet.getString("tag_four_tag_type");
                tag_four.vendor_id = beer.vendor_id;
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                beer.tag_four = tag_four;
                tag_five.id = resultSet.getInt("tag_five_id");
                tag_five.name = resultSet.getString("tag_five_name");
                tag_five.hex_color = resultSet.getString("tag_five_hex_color");
                tag_five.tag_type = resultSet.getString("tag_five_tag_type");
                tag_five.vendor_id = beer.vendor_id;
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                beer.tag_five = tag_five;
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = resultSet.getInt("nutritional_fact_id");
                vendorNutritionalFact.vendor_id = beer.vendor_id;
                vendorNutritionalFact.serving_size = resultSet.getInt("nutritional_fact_serving_size");
                vendorNutritionalFact.calories = resultSet.getInt("nutritional_fact_calories");
                vendorNutritionalFact.calories_from_fat = resultSet.getInt("nutritional_fact_calories_from_fat");
                vendorNutritionalFact.total_fat = resultSet.getInt("nutritional_fact_total_fat");
                vendorNutritionalFact.saturated_fat = resultSet.getInt("nutritional_fact_saturated_fat");
                vendorNutritionalFact.trans_fat = resultSet.getInt("nutritional_fact_trans_fat");
                vendorNutritionalFact.cholesterol = resultSet.getInt("nutritional_fact_cholesterol");
                vendorNutritionalFact.sodium = resultSet.getInt("nutritional_fact_sodium");
                vendorNutritionalFact.total_carbs = resultSet.getInt("nutritional_fact_total_carbs");
                vendorNutritionalFact.dietary_fiber = resultSet.getInt("nutritional_fact_dietary_fiber");
                vendorNutritionalFact.sugar = resultSet.getInt("nutritional_fact_sugar");
                vendorNutritionalFact.vitamin_a = resultSet.getInt("nutritional_fact_vitamin_a");
                vendorNutritionalFact.vitamin_b = resultSet.getInt("nutritional_fact_vitamin_b");
                vendorNutritionalFact.vitamin_c = resultSet.getInt("nutritional_fact_vitamin_c");
                vendorNutritionalFact.vitamin_d = resultSet.getInt("nutritional_fact_vitamin_d");
                vendorNutritionalFact.calcium = resultSet.getInt("nutritional_fact_calcium");
                vendorNutritionalFact.iron = resultSet.getInt("nutritional_fact_iron");
                vendorNutritionalFact.protein = resultSet.getInt("nutritional_fact_protein");
                vendorNutritionalFact.profile_name = resultSet.getString("nutritional_fact_profile_name");
                beer.nutritional_facts = vendorNutritionalFact;
                beerHashMap.put(beer.beer_id, beer);
            }
            return beerHashMap;
        } catch (Exception ex)  {
            System.out.println(ex.getMessage());
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

    private String basicSearchFoodsSQL_getIDs =
        "SELECT " +
                "   vendor_food_id " +
                "FROM " +
                "   english_vendor_food_search_ids s " +
                "WHERE " +
                "<%to_tsquery%> " +
                "ORDER BY ( " +
                "<%ts_rank%> " +
                ") DESC " +
                "LIMIT ? OFFSET ?;";

    private String basicSearchFoodsSQL_FromIDS =
        "SELECT " +
                "   vendor_food_id," +
                "   vendor_food_vendor_id," +
                "   vendor_food_name," +
                "   vendor_food_description," +
                "   vendor_food_price," +
                "   vendor_food_food_sizes," +
                "   creation_timestamp," +
                "   creation_days_ago, " +
                "   vendor_food_category_name," +
                "   vendor_food_category_hex_color, " +
                "   vendor_food_category_vendor_id, " +
                "   vendor_food_category_id, " +
                "   vendor_food_category_description, " +
                "   tag_one_id, " +
                "   tag_one_name, " +
                "   tag_one_hex_color, " +
                "   tag_one_tag_type, " +
                "   tag_two_id, " +
                "   tag_two_name, " +
                "   tag_two_hex_color, " +
                "   tag_two_tag_type, " +
                "   tag_three_id, " +
                "   tag_three_name, " +
                "   tag_three_hex_color, " +
                "   tag_three_tag_type, " +
                "   tag_four_id, " +
                "   tag_four_name, " +
                "   tag_four_hex_color, " +
                "   tag_four_tag_type, " +
                "   tag_five_id, " +
                "   tag_five_name, " +
                "   tag_five_hex_color, " +
                "   tag_five_tag_type, " +
                "   nutritional_fact_id, " +
                "   nutritional_fact_serving_size, " +
                "   nutritional_fact_calories, " +
                "   nutritional_fact_calories_from_fat, " +
                "   nutritional_fact_total_fat, " +
                "   nutritional_fact_saturated_fat, " +
                "   nutritional_fact_trans_fat, " +
                "   nutritional_fact_cholesterol, " +
                "   nutritional_fact_sodium, " +
                "   nutritional_fact_total_carbs, " +
                "   nutritional_fact_dietary_fiber, " +
                "   nutritional_fact_sugar, " +
                "   nutritional_fact_vitamin_a, " +
                "   nutritional_fact_vitamin_b, " +
                "   nutritional_fact_vitamin_c, " +
                "   nutritional_fact_vitamin_d, " +
                "   nutritional_fact_calcium, " +
                "   nutritional_fact_iron, " +
                "   nutritional_fact_protein, " +
                "   nutritional_fact_profile_name, " +
                "   vendor_name " +
                "FROM " +
                "   english_vendor_food_search " +
                "WHERE " +
                "   vendor_food_id = ANY(?)";

    private String basicSearchFoodSQL_getImageThumbnails =
        "SELECT DISTINCT ON(vendor_food_id) " +
                "   vendor_food_id, " +
                "   filename " +
                "FROM " +
                "   vendor_food_images " +
                "WHERE " +
                "   vendor_food_id = ANY(?) " +
                "ORDER BY " +
                "   vendor_food_id, " +
                "   display_order ASC";

    public HashMap<Integer, FavoriteVendorFood> basicSearchFoods(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, FavoriteVendorFood> foodHashMap = new HashMap<Integer, FavoriteVendorFood>();
            ArrayList<Integer> vendor_food_ids = new ArrayList<Integer>();
            // Build to_tsquery.
            String to_tsquery = "";
            String to_tsquery_ps = "s.D @@ TO_TSQUERY('english', ?) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(to_tsquery_ps);
                preparedStatement.setString(1, keywords[i]);
                to_tsquery += preparedStatement.toString();
                if (i < (keywords.length - 1)) {
                    to_tsquery += " OR ";
                }
            }
            // Build ts_rank.
            String ts_rank = "";
            String ts_rank_ps = "TS_RANK(s.D, TO_TSQUERY('english', ?)) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(ts_rank_ps);
                preparedStatement.setString(1, keywords[i]);
                ts_rank += preparedStatement.toString();
                if (i < (keywords.length - 1)) {
                    ts_rank += " + ";
                }
            }
            // Swap terms in query.
            this.basicSearchFoodsSQL_getIDs = this.basicSearchFoodsSQL_getIDs.replace("<%to_tsquery%>", to_tsquery);
            this.basicSearchFoodsSQL_getIDs = this.basicSearchFoodsSQL_getIDs.replace("<%ts_rank%>", ts_rank);
            preparedStatement = this.DAO.prepareStatement(this.basicSearchFoodsSQL_getIDs);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            // Fetch ids.
            while (resultSet.next()) {
                vendor_food_ids.add(resultSet.getInt("vendor_food_id"));
            }
            preparedStatement = this.DAO.prepareStatement(basicSearchFoodsSQL_FromIDS);
            preparedStatement.setArray(1, this.DAO.createArrayOf("INTEGER", vendor_food_ids.toArray()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = new Color();
                FavoriteVendorFood vendorFood = new FavoriteVendorFood();
                vendorFood.vendor_name = resultSet.getString("vendor_name");
                vendorFood.vendor_id = resultSet.getInt("vendor_food_vendor_id");
                vendorFood.vendor_food_id = resultSet.getInt("vendor_food_id");
                vendorFood.name = resultSet.getString("vendor_food_name");
                vendorFood.description = resultSet.getString("vendor_food_description");
                vendorFood.price = resultSet.getFloat("vendor_food_price");
                vendorFood.creation_timestamp = resultSet.getString("creation_timestamp");
                vendorFood.creation_days_ago = resultSet.getString("creation_days_ago");
                Array food_sizes_array = resultSet.getArray("vendor_food_food_sizes");
                String[] str_food_sizes = (String[]) food_sizes_array.getArray();
                vendorFood.food_sizes = str_food_sizes;
                vendorFood.vendor_food_category.name = resultSet.getString("vendor_food_category_name");
                vendorFood.vendor_food_category.vendor_id = resultSet.getInt("vendor_food_category_vendor_id");
                vendorFood.vendor_food_category.id = resultSet.getInt("vendor_food_category_id");
                vendorFood.vendor_food_category.description = resultSet.getString("vendor_food_category_description");
                vendorFood.vendor_food_category.hex_color = resultSet.getString("vendor_food_category_hex_color");
                vendorFood.vendor_food_category.text_color = color.getInverseBW(vendorFood.vendor_food_category.hex_color);
                VendorFoodTag tag_one = new VendorFoodTag();
                VendorFoodTag tag_two = new VendorFoodTag();
                VendorFoodTag tag_three = new VendorFoodTag();
                VendorFoodTag tag_four = new VendorFoodTag();
                VendorFoodTag tag_five = new VendorFoodTag();
                tag_one.id = resultSet.getInt("tag_one_id");
                tag_one.name = resultSet.getString("tag_one_name");
                tag_one.hex_color = resultSet.getString("tag_one_hex_color");
                tag_one.tag_type = resultSet.getString("tag_one_tag_type");
                tag_one.vendor_id = vendorFood.vendor_id;
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                vendorFood.tag_one = tag_one;
                tag_two.id = resultSet.getInt("tag_two_id");
                tag_two.name = resultSet.getString("tag_two_name");
                tag_two.hex_color = resultSet.getString("tag_two_hex_color");
                tag_two.tag_type = resultSet.getString("tag_two_tag_type");
                tag_two.vendor_id = vendorFood.vendor_id;
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                vendorFood.tag_two = tag_two;
                tag_three.id = resultSet.getInt("tag_three_id");
                tag_three.name = resultSet.getString("tag_three_name");
                tag_three.hex_color = resultSet.getString("tag_three_hex_color");
                tag_three.tag_type = resultSet.getString("tag_three_tag_type");
                tag_three.vendor_id = vendorFood.vendor_id;
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                vendorFood.tag_three = tag_three;
                tag_four.id = resultSet.getInt("tag_four_id");
                tag_four.name = resultSet.getString("tag_four_name");
                tag_four.hex_color = resultSet.getString("tag_four_hex_color");
                tag_four.tag_type = resultSet.getString("tag_four_tag_type");
                tag_four.vendor_id = vendorFood.vendor_id;
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                vendorFood.tag_four = tag_four;
                tag_five.id = resultSet.getInt("tag_five_id");
                tag_five.name = resultSet.getString("tag_five_name");
                tag_five.hex_color = resultSet.getString("tag_five_hex_color");
                tag_five.tag_type = resultSet.getString("tag_five_tag_type");
                tag_five.vendor_id = vendorFood.vendor_id;
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                vendorFood.tag_five = tag_five;
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = resultSet.getInt("nutritional_fact_id");
                vendorNutritionalFact.vendor_id = vendorFood.vendor_id;
                vendorNutritionalFact.serving_size = resultSet.getInt("nutritional_fact_serving_size");
                vendorNutritionalFact.calories = resultSet.getInt("nutritional_fact_calories");
                vendorNutritionalFact.calories_from_fat = resultSet.getInt("nutritional_fact_calories_from_fat");
                vendorNutritionalFact.total_fat = resultSet.getInt("nutritional_fact_total_fat");
                vendorNutritionalFact.saturated_fat = resultSet.getInt("nutritional_fact_saturated_fat");
                vendorNutritionalFact.trans_fat = resultSet.getInt("nutritional_fact_trans_fat");
                vendorNutritionalFact.cholesterol = resultSet.getInt("nutritional_fact_cholesterol");
                vendorNutritionalFact.sodium = resultSet.getInt("nutritional_fact_sodium");
                vendorNutritionalFact.total_carbs = resultSet.getInt("nutritional_fact_total_carbs");
                vendorNutritionalFact.dietary_fiber = resultSet.getInt("nutritional_fact_dietary_fiber");
                vendorNutritionalFact.sugar = resultSet.getInt("nutritional_fact_sugar");
                vendorNutritionalFact.vitamin_a = resultSet.getInt("nutritional_fact_vitamin_a");
                vendorNutritionalFact.vitamin_b = resultSet.getInt("nutritional_fact_vitamin_b");
                vendorNutritionalFact.vitamin_c = resultSet.getInt("nutritional_fact_vitamin_c");
                vendorNutritionalFact.vitamin_d = resultSet.getInt("nutritional_fact_vitamin_d");
                vendorNutritionalFact.calcium = resultSet.getInt("nutritional_fact_calcium");
                vendorNutritionalFact.iron = resultSet.getInt("nutritional_fact_iron");
                vendorNutritionalFact.protein = resultSet.getInt("nutritional_fact_protein");
                vendorNutritionalFact.profile_name = resultSet.getString("nutritional_fact_profile_name");
                vendorFood.nutritional_facts = vendorNutritionalFact;
                foodHashMap.put(vendorFood.vendor_food_id, vendorFood);
            }
            preparedStatement = this.DAO.prepareStatement(this.basicSearchFoodSQL_getImageThumbnails);
            preparedStatement.setArray(1, this.DAO.createArrayOf("INTEGER", vendor_food_ids.toArray()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                foodHashMap.get(resultSet.getInt("vendor_food_id")).thumbnail = resultSet.getString("filename");
            }
            return foodHashMap;
        } catch (Exception ex)  {
            System.out.println(ex.getMessage());
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

    private String basicSearchDrinksSQL_getIDs =
            "SELECT " +
                    "   vendor_drink_id " +
                    "FROM " +
                    "   english_vendor_drink_search_ids s " +
                    "WHERE " +
                    "<%to_tsquery%> " +
                    "ORDER BY ( " +
                    "<%ts_rank%> " +
                    ") DESC " +
                    "LIMIT ? OFFSET ?;";

    private String basicSearchDrinkSQL_fromIDs =
        "SELECT " +
                "    vendor_drink_id, " +
                "    vendor_id, " +
                "    vendor_drink_name, " +
                "    vendor_drink_description, " +
                "    vendor_drink_price, " +
                "    vendor_drink_hex_one,  " +
                "    vendor_drink_hex_two,  " +
                "    vendor_drink_hex_three,  " +
                "    vendor_drink_hex_background,  " +
                "    vendor_drink_serve_temp,  " +
                "    vendor_drink_servings, " +
                "    vendor_drink_icon_enum,  " +
                "    creation_timestamp,  " +
                "    creation_days_ago,  " +
                "    vendor_drink_category_id,  " +
                "    vendor_drink_category_name,  " +
                "    vendor_drink_category_hex_color,  " +
                "    vendor_drink_category_icon_enum,  " +
                "    vendor_drink_category_description,  " +
                "    tag_one_id,  " +
                "    tag_one_name,  " +
                "    tag_one_hex_color,  " +
                "    tag_one_tag_type,  " +
                "    tag_two_id,  " +
                "    tag_two_name,  " +
                "    tag_two_hex_color,  " +
                "    tag_two_tag_type,  " +
                "    tag_three_id,  " +
                "    tag_three_name,  " +
                "    tag_three_hex_color,  " +
                "    tag_three_tag_type,  " +
                "    tag_four_id,  " +
                "    tag_four_name,  " +
                "    tag_four_hex_color,  " +
                "    tag_four_tag_type,  " +
                "    tag_five_id,  " +
                "    tag_five_name,  " +
                "    tag_five_hex_color,  " +
                "    tag_five_tag_type, " +
                "    nutritional_fact_id,  " +
                "    nutritional_fact_serving_size,  " +
                "    nutritional_fact_calories,  " +
                "    nutritional_fact_calories_from_fat,  " +
                "    nutritional_fact_total_fat,  " +
                "    nutritional_fact_saturated_fat,  " +
                "    nutritional_fact_trans_fat,  " +
                "    nutritional_fact_cholesterol,  " +
                "    nutritional_fact_sodium,  " +
                "    nutritional_fact_total_carbs,  " +
                "    nutritional_fact_dietary_fiber,  " +
                "    nutritional_fact_sugar,  " +
                "    nutritional_fact_vitamin_a,  " +
                "    nutritional_fact_vitamin_b,  " +
                "    nutritional_fact_vitamin_c,  " +
                "    nutritional_fact_vitamin_d,  " +
                "    nutritional_fact_calcium,  " +
                "    nutritional_fact_iron,  " +
                "    nutritional_fact_protein,  " +
                "    nutritional_fact_profile_name, " +
                "    vendor_name  " +
                "FROM " +
                "   english_vendor_drink_search " +
                "WHERE " +
                "   vendor_drink_id = ANY(?)";

    public HashMap<Integer, FavoriteVendorDrink> basicSearchDrinks(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            HashMap<Integer, FavoriteVendorDrink> drinkHashMap = new HashMap<Integer, FavoriteVendorDrink>();
            ArrayList<Integer> vendor_drink_ids = new ArrayList<Integer>();
            // Build to_tsquery.
            String to_tsquery = "";
            String to_tsquery_ps = "s.D @@ TO_TSQUERY('english', ?) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(to_tsquery_ps);
                preparedStatement.setString(1, keywords[i]);
                to_tsquery += preparedStatement.toString();
                if (i < (keywords.length - 1)) {
                    to_tsquery += " OR ";
                }
            }
            // Build ts_rank.
            String ts_rank = "";
            String ts_rank_ps = "TS_RANK(s.D, TO_TSQUERY('english', ?)) ";
            for (int i = 0; i < keywords.length; i++) {
                preparedStatement = this.DAO.prepareStatement(ts_rank_ps);
                preparedStatement.setString(1, keywords[i]);
                ts_rank += preparedStatement.toString();
                if (i < (keywords.length - 1)) {
                    ts_rank += " + ";
                }
            }
            // Swap terms in query.
            this.basicSearchDrinksSQL_getIDs = this.basicSearchDrinksSQL_getIDs.replace("<%to_tsquery%>", to_tsquery);
            this.basicSearchDrinksSQL_getIDs = this.basicSearchDrinksSQL_getIDs.replace("<%ts_rank%>", ts_rank);
            preparedStatement = this.DAO.prepareStatement(this.basicSearchDrinksSQL_getIDs);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            resultSet = preparedStatement.executeQuery();
            // Fetch ids.
            while (resultSet.next()) {
                vendor_drink_ids.add(resultSet.getInt("vendor_drink_id"));
            }
            preparedStatement = this.DAO.prepareStatement(basicSearchDrinkSQL_fromIDs);
            preparedStatement.setArray(1, this.DAO.createArrayOf("INTEGER", vendor_drink_ids.toArray()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = new Color();
                FavoriteVendorDrink vendorDrink = new FavoriteVendorDrink();
                vendorDrink.vendor_name = resultSet.getString("vendor_name");
                vendorDrink.vendor_drink_id = resultSet.getInt("vendor_drink_id");
                vendorDrink.name = resultSet.getString("vendor_drink_name");
                vendorDrink.vendor_id = resultSet.getInt("vendor_id");
                vendorDrink.description = resultSet.getString("vendor_drink_description");
                vendorDrink.price = resultSet.getFloat("vendor_drink_price");
                vendorDrink.hex_one = resultSet.getString("vendor_drink_hex_one");
                vendorDrink.hex_two = resultSet.getString("vendor_drink_hex_two");
                vendorDrink.hex_three = resultSet.getString("vendor_drink_hex_three");
                vendorDrink.hex_background = resultSet.getString("vendor_drink_hex_background");
                vendorDrink.hex_icon_text = color.getInverseBW(vendorDrink.hex_background);
                vendorDrink.drink_serve_temp = resultSet.getString("vendor_drink_serve_temp");
                vendorDrink.servings = resultSet.getString("vendor_drink_servings");
                vendorDrink.icon_enum = resultSet.getString("vendor_drink_icon_enum");
                vendorDrink.creation_timestamp = resultSet.getString("creation_timestamp");
                vendorDrink.creation_days_ago = resultSet.getString("creation_days_ago");
                vendorDrink.vendor_drink_category.vendor_id = vendorDrink.vendor_id;
                vendorDrink.vendor_drink_category.name = resultSet.getString("vendor_drink_category_name");
                vendorDrink.vendor_drink_category.hex_color = resultSet.getString("vendor_drink_category_hex_color");
                vendorDrink.vendor_drink_category.id = resultSet.getInt("vendor_drink_category_id");
                vendorDrink.vendor_drink_category.description = resultSet.getString("vendor_drink_category_description");
                vendorDrink.vendor_drink_category.text_color = color.getInverseBW(vendorDrink.vendor_drink_category.hex_color);
                VendorDrinkTag tag_one = new VendorDrinkTag();
                VendorDrinkTag tag_two = new VendorDrinkTag();
                VendorDrinkTag tag_three = new VendorDrinkTag();
                VendorDrinkTag tag_four = new VendorDrinkTag();
                VendorDrinkTag tag_five = new VendorDrinkTag();
                tag_one.id = resultSet.getInt("tag_one_id");
                tag_one.name = resultSet.getString("tag_one_name");
                tag_one.hex_color = resultSet.getString("tag_one_hex_color");
                tag_one.tag_type = resultSet.getString("tag_one_tag_type");
                tag_one.vendor_id = vendorDrink.vendor_id;
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                vendorDrink.tag_one = tag_one;
                tag_two.id = resultSet.getInt("tag_two_id");
                tag_two.name = resultSet.getString("tag_two_name");
                tag_two.hex_color = resultSet.getString("tag_two_hex_color");
                tag_two.tag_type = resultSet.getString("tag_two_tag_type");
                tag_two.vendor_id = vendorDrink.vendor_id;
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                vendorDrink.tag_two = tag_two;
                tag_three.id = resultSet.getInt("tag_three_id");
                tag_three.name = resultSet.getString("tag_three_name");
                tag_three.hex_color = resultSet.getString("tag_three_hex_color");
                tag_three.tag_type = resultSet.getString("tag_three_tag_type");
                tag_three.vendor_id = vendorDrink.vendor_id;
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                vendorDrink.tag_three = tag_three;
                tag_four.id = resultSet.getInt("tag_four_id");
                tag_four.name = resultSet.getString("tag_four_name");
                tag_four.hex_color = resultSet.getString("tag_four_hex_color");
                tag_four.tag_type = resultSet.getString("tag_four_tag_type");
                tag_four.vendor_id = vendorDrink.vendor_id;
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                vendorDrink.tag_four = tag_four;
                tag_five.id = resultSet.getInt("tag_five_id");
                tag_five.name = resultSet.getString("tag_five_name");
                tag_five.hex_color = resultSet.getString("tag_five_hex_color");
                tag_five.tag_type = resultSet.getString("tag_five_tag_type");
                tag_five.vendor_id = vendorDrink.vendor_id;
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                vendorDrink.tag_five = tag_five;
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = resultSet.getInt("nutritional_fact_id");
                vendorNutritionalFact.vendor_id = vendorDrink.vendor_id;
                vendorNutritionalFact.serving_size = resultSet.getInt("nutritional_fact_serving_size");
                vendorNutritionalFact.calories = resultSet.getInt("nutritional_fact_calories");
                vendorNutritionalFact.calories_from_fat = resultSet.getInt("nutritional_fact_calories_from_fat");
                vendorNutritionalFact.total_fat = resultSet.getInt("nutritional_fact_total_fat");
                vendorNutritionalFact.saturated_fat = resultSet.getInt("nutritional_fact_saturated_fat");
                vendorNutritionalFact.trans_fat = resultSet.getInt("nutritional_fact_trans_fat");
                vendorNutritionalFact.cholesterol = resultSet.getInt("nutritional_fact_cholesterol");
                vendorNutritionalFact.sodium = resultSet.getInt("nutritional_fact_sodium");
                vendorNutritionalFact.total_carbs = resultSet.getInt("nutritional_fact_total_carbs");
                vendorNutritionalFact.dietary_fiber = resultSet.getInt("nutritional_fact_dietary_fiber");
                vendorNutritionalFact.sugar = resultSet.getInt("nutritional_fact_sugar");
                vendorNutritionalFact.vitamin_a = resultSet.getInt("nutritional_fact_vitamin_a");
                vendorNutritionalFact.vitamin_b = resultSet.getInt("nutritional_fact_vitamin_b");
                vendorNutritionalFact.vitamin_c = resultSet.getInt("nutritional_fact_vitamin_c");
                vendorNutritionalFact.vitamin_d = resultSet.getInt("nutritional_fact_vitamin_d");
                vendorNutritionalFact.calcium = resultSet.getInt("nutritional_fact_calcium");
                vendorNutritionalFact.iron = resultSet.getInt("nutritional_fact_iron");
                vendorNutritionalFact.protein = resultSet.getInt("nutritional_fact_protein");
                vendorNutritionalFact.profile_name = resultSet.getString("nutritional_fact_profile_name");
                vendorDrink.nutritional_facts = vendorNutritionalFact;
                drinkHashMap.put(vendorDrink.vendor_drink_id, vendorDrink);
            }
            return drinkHashMap;
        } catch (Exception ex)  {
            System.out.println(ex.getMessage());
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
}
