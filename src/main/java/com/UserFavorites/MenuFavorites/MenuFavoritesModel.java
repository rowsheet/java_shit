package com.UserFavorites.MenuFavorites;

import com.Common.AbstractModel;
import com.Common.UserFavorites.*;
import com.Common.VendorDrinkTag;
import com.Common.BeerTag;
import com.Common.VendorFoodTag;
import com.Common.VendorNutritionalFact;
import com.Common.Color;

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

    private String loadFoodFavorites_sql_images =
            "SELECT DISTINCT ON (vfi.vendor_food_id) " +
                    "   vfi.vendor_food_id, " +
                    "   vfi.display_order, " +
                    "   vfi.filename " +
                    "FROM " +
                    "   vendor_food_favorites vff " +
                    "LEFT JOIN " +
                    "   vendor_food_images vfi " +
                    "ON " +
                    "   vff.vendor_food_id = vfi.vendor_food_id " +
                    "WHERE " +
                    "   vff.account_id = ? " +
                    "AND " +
                    "   vfi.id IS NOT NULL " +
                    "ORDER BY " +
                    "   vfi.vendor_food_id, " +
                    "   vfi.display_order ASC";

    private String loadFoodFavorites_sql =
        "SELECT" +
                "   vf.id AS vendor_food_id," +
                "   vf.vendor_id AS vendor_food_vendor_id," +
                "   vf.name AS vendor_food_name," +
                "   vf.description AS vendor_food_description," +
                "   vf.price AS vendor_food_price," +
                "   vf.food_sizes AS vendor_food_food_sizes," +
                "   vf.id AS id," +
                "   vf.creation_timestamp AS creation_timestamp," +
                "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vf.creation_timestamp::date)) AS creation_days_ago, " +
                "   vfc.name AS vendor_food_category_name," +
                "   vfc.hex_color AS vendor_food_category_hex_color, " +
                "   vfc.vendor_id AS vendor_food_category_vendor_id, " +
                "   vfc.id AS vendor_food_category_id, " +
                "   vfc.description AS vendor_food_category_description, " +
                "   vft1.id AS tag_one_id, " +
                "   vft1.name AS tag_one_name, " +
                "   vft1.hex_color AS tag_one_hex_color, " +
                "   vft1.tag_type AS tag_one_tag_type, " +
                "   vft2.id AS tag_two_id, " +
                "   vft2.name AS tag_two_name, " +
                "   vft2.hex_color AS tag_two_hex_color, " +
                "   vft2.tag_type AS tag_two_tag_type, " +
                "   vft3.id AS tag_three_id, " +
                "   vft3.name AS tag_three_name, " +
                "   vft3.hex_color AS tag_three_hex_color, " +
                "   vft3.tag_type AS tag_three_tag_type, " +
                "   vft4.id AS tag_four_id, " +
                "   vft4.name AS tag_four_name, " +
                "   vft4.hex_color AS tag_four_hex_color, " +
                "   vft4.tag_type AS tag_four_tag_type, " +
                "   vft5.id AS tag_five_id, " +
                "   vft5.name AS tag_five_name, " +
                "   vft5.hex_color AS tag_five_hex_color, " +
                "   vft5.tag_type AS tag_five_tag_type, " +
                "   vnf.id AS nutritional_fact_id, " +
                "   vnf.serving_size AS nutritional_fact_serving_size, " +
                "   vnf.calories AS nutritional_fact_calories, " +
                "   vnf.calories_from_fat  AS nutritional_fact_calories_from_fat, " +
                "   vnf.total_fat  AS nutritional_fact_total_fat, " +
                "   vnf.saturated_fat  AS nutritional_fact_saturated_fat, " +
                "   vnf.trans_fat  AS nutritional_fact_trans_fat, " +
                "   vnf.cholesterol  AS nutritional_fact_cholesterol, " +
                "   vnf.sodium AS nutritional_fact_sodium, " +
                "   vnf.total_carbs  AS nutritional_fact_total_carbs, " +
                "   vnf.dietary_fiber  AS nutritional_fact_dietary_fiber, " +
                "   vnf.sugar  AS nutritional_fact_sugar, " +
                "   vnf.vitamin_a  AS nutritional_fact_vitamin_a, " +
                "   vnf.vitamin_b  AS nutritional_fact_vitamin_b, " +
                "   vnf.vitamin_c  AS nutritional_fact_vitamin_c, " +
                "   vnf.vitamin_d  AS nutritional_fact_vitamin_d, " +
                "   vnf.calcium  AS nutritional_fact_calcium, " +
                "   vnf.iron AS nutritional_fact_iron, " +
                "   vnf.protein  AS nutritional_fact_protein, " +
                "   vnf.profile_name AS nutritional_fact_profile_name, " +
                "   vi.display_name AS vendor_name " +
                "FROM " +
                "   vendor_food_favorites vff " +
                "LEFT JOIN " +
                "   vendor_foods AS vf " +
                "ON " +
                "   vff.vendor_food_id = vf.id " +
                "LEFT JOIN " +
                "   vendors v " +
                "ON " +
                "   vf.vendor_id = v.id " +
                "LEFT JOIN " +
                "   vendor_info vi " +
                "ON " +
                "   v.id = vi.vendor_id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft1 " +
                "ON " +
                "   vf.tag_one = vft1.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft2 " +
                "ON " +
                "   vf.tag_two = vft2.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft3 " +
                "ON " +
                "   vf.tag_three = vft3.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft4 " +
                "ON " +
                "   vf.tag_four = vft4.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft5 " +
                "ON " +
                "   vf.tag_five = vft5.id " +
                "LEFT JOIN" +
                "   vendor_food_categories AS vfc " +
                "ON " +
                "   vf.vendor_food_category_id = vfc.id " +
                "LEFT JOIN " +
                "   vendor_nutritional_facts vnf " +
                "ON " +
                "   vf.nutritional_facts_id = vnf.id " +
                "WHERE" +
                "   vff.account_id = ?";

    public FoodMenu loadFoodFavorites(
            String cookie
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementImages = null;
        ResultSet resultSetImages = null;
        try {
            this.validateUserCookie(cookie);
            FoodMenu foodMenu = new FoodMenu();
            preparedStatement = this.DAO.prepareStatement(this.loadFoodFavorites_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
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
                foodMenu.menuItems.put(vendorFood.vendor_food_id, vendorFood);
            }
            preparedStatementImages = this.DAO.prepareStatement(this.loadFoodFavorites_sql_images);
            preparedStatementImages.setInt(1, this.userCookie.userID);
            resultSetImages = preparedStatementImages.executeQuery();
            while (resultSetImages.next()) {
                int vendor_food_id = resultSetImages.getInt("vendor_food_id");
                foodMenu.menuItems.get(vendor_food_id).thumbnail = resultSetImages.getString("filename");
            }
            return foodMenu;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to load favorite foods.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatementImages != null) {
                preparedStatementImages.close();
            }
            if (resultSetImages != null) {
                resultSetImages.close();
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

    private String loadDrinkFavorites_sql =
            "SELECT" +
                    "   vd.id AS vendor_drink_id," +
                    "   vd.vendor_id AS vendor_id," +
                    "   vd.name AS vendor_drink_name," +
                    "   vd.description AS vendor_drink_description," +
                    "   vd.price AS vendor_drink_price," +
                    "   vd.hex_one AS vendor_drink_hex_one, " +
                    "   vd.hex_two AS vendor_drink_hex_two, " +
                    "   vd.hex_three AS vendor_drink_hex_three, " +
                    "   vd.hex_background AS vendor_drink_hex_background, " +
                    "   vd.drink_serve_temp AS vendor_drink_serve_temp, " +
                    "   vd.servings AS vendor_drink_servings," +
                    "   vd.icon_enum AS vendor_drink_icon_enum, " +
                    "   vd.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vd.creation_timestamp::date)) AS creation_days_ago, " +
                    "   vdc.id AS vendor_drink_category_id, " +
                    "   vdc.name AS vendor_drink_category_name, " +
                    "   vdc.hex_color AS vendor_drink_category_hex_color, " +
                    "   vdc.icon_enum AS vendor_drink_category_icon_enum, " +
                    "   vdc.description AS vendor_drink_category_description, " +
                    "   vdt1.id AS tag_one_id, " +
                    "   vdt1.name AS tag_one_name, " +
                    "   vdt1.hex_color AS tag_one_hex_color, " +
                    "   vdt1.tag_type AS tag_one_tag_type, " +
                    "   vdt2.id AS tag_two_id, " +
                    "   vdt2.name AS tag_two_name, " +
                    "   vdt2.hex_color AS tag_two_hex_color, " +
                    "   vdt2.tag_type AS tag_two_tag_type, " +
                    "   vdt3.id AS tag_three_id, " +
                    "   vdt3.name AS tag_three_name, " +
                    "   vdt3.hex_color AS tag_three_hex_color, " +
                    "   vdt3.tag_type AS tag_three_tag_type, " +
                    "   vdt4.id AS tag_four_id, " +
                    "   vdt4.name AS tag_four_name, " +
                    "   vdt4.hex_color AS tag_four_hex_color, " +
                    "   vdt4.tag_type AS tag_four_tag_type, " +
                    "   vdt5.id AS tag_five_id, " +
                    "   vdt5.name AS tag_five_name, " +
                    "   vdt5.hex_color AS tag_five_hex_color, " +
                    "   vdt5.tag_type AS tag_five_tag_type," +
                    "   vnf.id AS nutritional_fact_id, " +
                    "   vnf.serving_size AS nutritional_fact_serving_size, " +
                    "   vnf.calories AS nutritional_fact_calories, " +
                    "   vnf.calories_from_fat  AS nutritional_fact_calories_from_fat, " +
                    "   vnf.total_fat  AS nutritional_fact_total_fat, " +
                    "   vnf.saturated_fat  AS nutritional_fact_saturated_fat, " +
                    "   vnf.trans_fat  AS nutritional_fact_trans_fat, " +
                    "   vnf.cholesterol  AS nutritional_fact_cholesterol, " +
                    "   vnf.sodium AS nutritional_fact_sodium, " +
                    "   vnf.total_carbs  AS nutritional_fact_total_carbs, " +
                    "   vnf.dietary_fiber  AS nutritional_fact_dietary_fiber, " +
                    "   vnf.sugar  AS nutritional_fact_sugar, " +
                    "   vnf.vitamin_a  AS nutritional_fact_vitamin_a, " +
                    "   vnf.vitamin_b  AS nutritional_fact_vitamin_b, " +
                    "   vnf.vitamin_c  AS nutritional_fact_vitamin_c, " +
                    "   vnf.vitamin_d  AS nutritional_fact_vitamin_d, " +
                    "   vnf.calcium  AS nutritional_fact_calcium, " +
                    "   vnf.iron AS nutritional_fact_iron, " +
                    "   vnf.protein  AS nutritional_fact_protein, " +
                    "   vnf.profile_name AS nutritional_fact_profile_name," +
                    "   vi.display_name AS vendor_name " +
                    "FROM " +
                    "   vendor_drink_favorites vdf " +
                    "LEFT JOIN " +
                    "   vendor_drinks vd " +
                    "ON " +
                    "   vdf.vendor_drink_id = vd.id " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   vd.vendor_id = v.id " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   v.id = vi.vendor_id " +
                    "LEFT JOIN " +
                    "   vendor_drink_categories vdc " +
                    "ON " +
                    "   vd.vendor_drink_category_id = vdc.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt1 " +
                    "ON " +
                    "   vd.tag_one = vdt1.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt2 " +
                    "ON " +
                    "   vd.tag_two = vdt2.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt3 " +
                    "ON " +
                    "   vd.tag_three = vdt3.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt4 " +
                    "ON " +
                    "   vd.tag_four = vdt4.id " +
                    "LEFT JOIN " +
                    "   vendor_drink_tags vdt5 " +
                    "ON " +
                    "   vd.tag_five = vdt5.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   vd.nutritional_facts_id = vnf.id " +
                    "WHERE" +
                    "   vdf.account_id = ?";

    public DrinkMenu loadDrinkFavorites(
            String cookie
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            DrinkMenu drinkMenu = new DrinkMenu();
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(loadDrinkFavorites_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
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
                drinkMenu.menuItems.put(vendorDrink.vendor_drink_id, vendorDrink);
            }
            return drinkMenu;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to load favorite drinks");
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

    private String loadBeerFavorites_sql =
            "SELECT" +
                    "   b.id AS beer_id," +
                    "   b.vendor_id AS beer_vendor_id," +
                    "   b.name AS beer_name," +
                    "   b.color AS beer_color," +
                    "   b.bitterness AS beer_bitterness," +
                    "   b.abv AS beer_abv," +
                    "   b.beer_style AS beer_style," +
                    "   b.beer_tastes AS beer_tastes," +
                    "   b.description AS beer_description," +
                    "   b.price AS beer_price," +
                    "   b.beer_sizes AS beer_sizes, " +
                    "   b.hop_score AS beer_hop_score, " +
                    "   b.creation_timestamp AS creation_timestamp, " +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', b.creation_timestamp::date)) AS creation_days_ago, " +
                    "   bc.name AS beer_category_name, " +
                    "   bc.hex_color AS beer_category_hex_color, " +
                    "   bc.id AS beer_category_id, " +
                    "   bc.description AS beer_category_description, " +
                    "   vbt1.id AS tag_one_id, " +
                    "   vbt1.name AS tag_one_name, " +
                    "   vbt1.hex_color AS tag_one_hex_color, " +
                    "   vbt1.tag_type AS tag_one_tag_type, " +
                    "   vbt2.id AS tag_two_id, " +
                    "   vbt2.name AS tag_two_name, " +
                    "   vbt2.hex_color AS tag_two_hex_color, " +
                    "   vbt2.tag_type AS tag_two_tag_type, " +
                    "   vbt3.id AS tag_three_id, " +
                    "   vbt3.name AS tag_three_name, " +
                    "   vbt3.hex_color AS tag_three_hex_color, " +
                    "   vbt3.tag_type AS tag_three_tag_type, " +
                    "   vbt4.id AS tag_four_id, " +
                    "   vbt4.name AS tag_four_name, " +
                    "   vbt4.hex_color AS tag_four_hex_color, " +
                    "   vbt4.tag_type AS tag_four_tag_type, " +
                    "   vbt5.id AS tag_five_id, " +
                    "   vbt5.name AS tag_five_name, " +
                    "   vbt5.hex_color AS tag_five_hex_color, " +
                    "   vbt5.tag_type AS tag_five_tag_type, " +
                    "   vnf.id AS nutritional_fact_id, " +
                    "   vnf.serving_size AS nutritional_fact_serving_size, " +
                    "   vnf.calories AS nutritional_fact_calories, " +
                    "   vnf.calories_from_fat  AS nutritional_fact_calories_from_fat, " +
                    "   vnf.total_fat  AS nutritional_fact_total_fat, " +
                    "   vnf.saturated_fat  AS nutritional_fact_saturated_fat, " +
                    "   vnf.trans_fat  AS nutritional_fact_trans_fat, " +
                    "   vnf.cholesterol  AS nutritional_fact_cholesterol, " +
                    "   vnf.sodium AS nutritional_fact_sodium, " +
                    "   vnf.total_carbs  AS nutritional_fact_total_carbs, " +
                    "   vnf.dietary_fiber  AS nutritional_fact_dietary_fiber, " +
                    "   vnf.sugar  AS nutritional_fact_sugar, " +
                    "   vnf.vitamin_a  AS nutritional_fact_vitamin_a, " +
                    "   vnf.vitamin_b  AS nutritional_fact_vitamin_b, " +
                    "   vnf.vitamin_c  AS nutritional_fact_vitamin_c, " +
                    "   vnf.vitamin_d  AS nutritional_fact_vitamin_d, " +
                    "   vnf.calcium  AS nutritional_fact_calcium, " +
                    "   vnf.iron AS nutritional_fact_iron, " +
                    "   vnf.protein  AS nutritional_fact_protein, " +
                    "   vnf.profile_name AS nutritional_fact_profile_name, " +
                    "   vi.display_name AS vendor_name " +
                    "FROM " +
                    "   beer_favorites AS bf " +
                    "LEFT JOIN " +
                    "   beers AS b " +
                    "ON " +
                    "   bf.beer_id = b.id " +
                    "LEFT JOIN " +
                    "   vendor_info vi " +
                    "ON " +
                    "   b.vendor_id = vi.vendor_id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt1 " +
                    "ON " +
                    "   b.tag_one = vbt1.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt2 " +
                    "ON " +
                    "   b.tag_two = vbt2.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt3 " +
                    "ON " +
                    "   b.tag_three = vbt3.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt4 " +
                    "ON " +
                    "   b.tag_four = vbt4.id " +
                    "LEFT JOIN " +
                    "   beer_tags vbt5 " +
                    "ON " +
                    "   b.tag_five = vbt5.id " +
                    "LEFT JOIN" +
                    "   beer_categories bc " +
                    "ON " +
                    "   b.beer_category_id = bc.id " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   b.nutritional_facts_id = vnf.id " +
                    "WHERE" +
                    "   bf.account_id = ?";

    public BeerMenu loadBeerFavorites(
            String cookie
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            this.validateUserCookie(cookie);
            BeerMenu beerMenu = new BeerMenu();
            preparedStatement = this.DAO.prepareStatement(this.loadBeerFavorites_sql);
            preparedStatement.setInt(1, this.userCookie.userID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
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
                beerMenu.menuItems.put(beer.beer_id, beer);
            }
            return beerMenu;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to load beer favorites.");
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
