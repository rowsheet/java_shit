package com.VendorMenu.Ingredients;

import com.Common.*;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

public class IngredientsModel extends AbstractModel {

    public IngredientsModel() throws Exception {}
    /*------------------------------------------------------------
    ███████╗ ██████╗  ██████╗ ██████╗ ███████╗
    ██╔════╝██╔═══██╗██╔═══██╗██╔══██╗██╔════╝
    █████╗  ██║   ██║██║   ██║██║  ██║███████╗
    ██╔══╝  ██║   ██║██║   ██║██║  ██║╚════██║
    ██║     ╚██████╔╝╚██████╔╝██████╔╝███████║
    ╚═╝      ╚═════╝  ╚═════╝ ╚═════╝ ╚══════╝
    FOOD INGREDIENTS
    ------------------------------------------------------------*/

    private String confirmFoodIngredientSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_food_ingredients " +
                    "WHERE " +
                    "   id = ?";

    private String confirmFoodOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_foods " +
                    "WHERE " +
                    "   id = ?";

    private String deleteFoodIngredientSQL =
            "DELETE FROM " +
                    "   vendor_food_ingredients " +
                    "WHERE " +
                    "   id = ?";

    private String createFoodIngredientSQL =
            "INSERT INTO " +
                    "vendor_food_ingredients( " +
                    "   name, " +
                    "   description, " +
                    "   source, " +
                    "   keywords, " +
                    "   vendor_id," +
                    "   feature_id," +
                    "   tag_one, " +
                    "   tag_two, " +
                    "   tag_three, " +
                    "   tag_four, " +
                    "   tag_five, " +
                    "   nutritional_facts_id " +
                    ") VALUES (?,?,?,?::varchar[],?,?,?,?,?,?,?,?) " +
                    "RETURNING id";

    private String updateFoodIngredientSQL =
            "UPDATE vendor_food_ingredients " +
                    "SET " +
                    "   name = ?, " +
                    "   description = ?, " +
                    "   source = ?, " +
                    "   keywords = ?::varchar[]," +
                    "   vendor_id = ?, " +
                    "   feature_id = ?, " +
                    "   tag_one = ?, " +
                    "   tag_two = ?, " +
                    "   tag_three = ?, " +
                    "   tag_four = ?, " +
                    "   tag_five = ?, " +
                    "   nutritional_facts_id = ? " +
                    "WHERE " +
                    "   id = ?";

    private String createFoodIngredientAssociationSQL =
            "INSERT INTO " +
                    "vendor_food_ingredient_associations( " +
                    "   vendor_food_id, " +
                    "   vendor_food_ingredient_id, " +
                    "   vendor_id " +
                    ") VALUES (?,?,?) " +
                    "RETURNING id";


    private String deleteFoodIngredientAssociationSQL =
            "DELETE FROM " +
                    "   vendor_food_ingredient_associations " +
                    "WHERE " +
                    "   vendor_food_id = ? " +
                    "AND " +
                    "   vendor_food_ingredient_id = ?";

    public int createFoodIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords,
            int nutritional_fact_id,
            int tag_one,
            int tag_two,
            int tag_three,
            int tag_four,
            int tag_five
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Insert data.
            preparedStatement = this.DAO.prepareStatement(this.createFoodIngredientSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, source);
            preparedStatement.setArray(4, this.DAO.createArrayOf("varchar", keywords));
            preparedStatement.setInt(5, this.vendorCookie.vendorID);
            preparedStatement.setInt(6, this.vendorCookie.requestFeatureID);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (tag_one == 0) {
                preparedStatement.setNull(7, Types.INTEGER);
            } else {
                preparedStatement.setInt(7, tag_one);
            }
            if (tag_two == 0) {
                preparedStatement.setNull(8, Types.INTEGER);
            } else {
                preparedStatement.setInt(8, tag_two);
            }
            if (tag_three == 0) {
                preparedStatement.setNull(9, Types.INTEGER);
            } else {
                preparedStatement.setInt(9, tag_three);
            }
            if (tag_four == 0) {
                preparedStatement.setNull(10, Types.INTEGER);
            } else {
                preparedStatement.setInt(10, tag_four);
            }
            if (tag_five == 0) {
                preparedStatement.setNull(11, Types.INTEGER);
            } else {
                preparedStatement.setInt(11, tag_five);
            }
            if (nutritional_fact_id == 0) {
                preparedStatement.setNull(12, Types.INTEGER);
            } else {
                preparedStatement.setInt(12, nutritional_fact_id);
            }
            System.out.println(nutritional_fact_id);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new IngredientsException("Unable to create food ingredient.");
            }
            return id;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse error.
            if (ex.getMessage().contains("vendor_food_ingredients_vendor_id_name_idx")) {
                throw new Exception("A food ingredient with that name already exists.");
            }
            // Don't know why.
            throw new Exception("Unable to create food ingredient.");
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

    public boolean updateFoodIngredient(
            String cookie,
            int id,
            String name,
            String description,
            String source,
            String[] keywords,
            int nutritional_fact_id,
            int tag_one,
            int tag_two,
            int tag_three,
            int tag_four,
            int tag_five
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(this.confirmFoodIngredientSQL);
            stage1.setInt(1, id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to update this food ingredient.");
            }
            // Update data.
            stage2 = this.DAO.prepareStatement(this.updateFoodIngredientSQL);
            stage2.setString(1, name);
            stage2.setString(2, description);
            stage2.setString(3, source);
            stage2.setArray(4, this.DAO.createArrayOf("varchar", keywords));
            stage2.setInt(5, this.vendorCookie.vendorID);
            stage2.setInt(6, this.vendorCookie.requestFeatureID);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (tag_one == 0) {
                stage2.setNull(7, Types.INTEGER);
            } else {
                stage2.setInt(7, tag_one);
            }
            if (tag_two == 0) {
                stage2.setNull(8, Types.INTEGER);
            } else {
                stage2.setInt(8, tag_two);
            }
            if (tag_three == 0) {
                stage2.setNull(9, Types.INTEGER);
            } else {
                stage2.setInt(9, tag_three);
            }
            if (tag_four == 0) {
                stage2.setNull(10, Types.INTEGER);
            } else {
                stage2.setInt(10, tag_four);
            }
            if (tag_five == 0) {
                stage2.setNull(11, Types.INTEGER);
            } else {
                stage2.setInt(11, tag_five);
            }
            if (nutritional_fact_id == 0) {
                stage2.setNull(12, Types.INTEGER);
            } else {
                stage2.setInt(12, nutritional_fact_id);
            }
            stage2.setInt(13, id);
            stage2.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update food ingredient.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteFoodIngredient(
            String cookie,
            int food_ingredient_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(confirmFoodIngredientSQL);
            stage1.setInt(1, food_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to delete this food ingredient.");
            }
            // Delete data.
            stage2 = this.DAO.prepareStatement(this.deleteFoodIngredientSQL);
            stage2.setInt(1, food_ingredient_id);
            stage2.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update food ingredient.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadFoodIngredientsSQL =
        "SELECT " +
                "   vfi.id AS vfi_id, " +
                "   vfi.vendor_id AS vfi_vendor_id, " +
                "   vfi.name AS vfi_name, " +
                "   vfi.description AS vfi_description, " +
                "   vfi.source AS vfi_source, " +
                "   vfi.keywords AS vfi_keywords, " +
                "   vfi.tag_one AS vfi_tag_one, " +
                "   vfi.tag_two AS vfi_tag_two, " +
                "   vfi.tag_three AS vfi_tag_three, " +
                "   vfi.tag_four AS vfi_tag_four, " +
                "   vfi.tag_five AS vfi_tag_five, " +
                "   vfi.nutritional_facts_id AS vfi_nutritional_facts_id," +
                "   vfi.verified AS vfi_verified, " +
                "   vfi.creation_timestamp vfi_creation_timestamp," +
                "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vfi.creation_timestamp::date)) AS vfi_creation_days_ago," +
                "   vnf.profile_name AS vnf_profile_name, " +
                "   vnf.id AS vnf_id, " +
                "   vnf.vendor_id AS vnf_vendor_id, " +
                "   vnf.serving_size AS vnf_serving_size, " +
                "   vnf.calories AS vnf_calories, " +
                "   vnf.calories_from_fat AS vnf_calories_from_fat, " +
                "   vnf.total_fat AS vnf_total_fat, " +
                "   vnf.saturated_fat AS vnf_saturated_fat, " +
                "   vnf.trans_fat AS vnf_trans_fat, " +
                "   vnf.cholesterol AS vnf_cholesterol, " +
                "   vnf.sodium AS vnf_sodium, " +
                "   vnf.total_carbs AS vnf_total_carbs, " +
                "   vnf.dietary_fiber AS vnf_dietary_fiber, " +
                "   vnf.sugar AS vnf_sugar, " +
                "   vnf.vitamin_a AS vnf_vitamin_a, " +
                "   vnf.vitamin_b AS vnf_vitamin_b, " +
                "   vnf.vitamin_c AS vnf_vitamin_c, " +
                "   vnf.vitamin_d AS vnf_vitamin_d, " +
                "   vnf.calcium AS vnf_calcium, " +
                "   vnf.iron AS vnf_iron, " +
                "   vnf.protein AS vnf_protein, " +
                "   vnf.creation_timestamp vnf_creation_timestamp," +
                "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vnf.creation_timestamp::date)) AS vnf_creation_days_ago," +
                "   vft1.id AS vft1_id, " +
                "   vft1.name AS vft1_name, " +
                "   vft1.hex_color AS vft1_hex_color, " +
                "   vft1.tag_type AS vft1_tag_type, " +
                "   vft2.id AS vft2_id, " +
                "   vft2.name AS vft2_name, " +
                "   vft2.hex_color AS vft2_hex_color, " +
                "   vft2.tag_type AS vft2_tag_type, " +
                "   vft3.id AS vft3_id, " +
                "   vft3.name AS vft3_name, " +
                "   vft3.hex_color AS vft3_hex_color, " +
                "   vft3.tag_type AS vft3_tag_type, " +
                "   vft4.id AS vft4_id, " +
                "   vft4.name AS vft4_name, " +
                "   vft4.hex_color AS vft4_hex_color, " +
                "   vft4.tag_type AS vft4_tag_type, " +
                "   vft5.id AS vft5_id, " +
                "   vft5.name AS vft5_name, " +
                "   vft5.hex_color AS vft5_hex_color, " +
                "   vft5.tag_type AS vft5_tag_type " +
                "FROM " +
                "   vendor_food_ingredients vfi " +
                "LEFT JOIN " +
                "   vendor_nutritional_facts vnf " +
                "ON " +
                "   vfi.nutritional_facts_id = vnf.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft1 " +
                "ON " +
                "   vfi.tag_one = vft1.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft2 " +
                "ON " +
                "   vfi.tag_two = vft2.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft3 " +
                "ON " +
                "   vfi.tag_three = vft3.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft4 " +
                "ON " +
                "   vfi.tag_four = vft4.id " +
                "LEFT JOIN " +
                "   vendor_food_tags vft5 " +
                "ON " +
                "   vfi.tag_five = vft5.id " +
                "WHERE " +
                "   vfi.vendor_id = ?";

    public ArrayList<VendorFoodIngredient> loadFoodIngredients(
        String cookie
    ) throws Exception {
        ArrayList<VendorFoodIngredient> foodIngredients = new ArrayList<VendorFoodIngredient>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate request.
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Get the data.
            preparedStatement = this.DAO.prepareStatement(this.loadFoodIngredientsSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = new Color();
                VendorFoodIngredient vendorFoodIngredient = new VendorFoodIngredient();
                vendorFoodIngredient.id = resultSet.getInt("vfi_id");
                vendorFoodIngredient.vendor_id = this.vendorCookie.vendorID;
                vendorFoodIngredient.name = resultSet.getString("vfi_name");
                vendorFoodIngredient.description = resultSet.getString("vfi_description");
                vendorFoodIngredient.source = resultSet.getString("vfi_source");
                Array keywordsArray = resultSet.getArray("vfi_keywords");
                String[] keywords = (String[]) keywordsArray.getArray();
                vendorFoodIngredient.keywords = keywords;
                VendorFoodTag tag_one = new VendorFoodTag();
                VendorFoodTag tag_two = new VendorFoodTag();
                VendorFoodTag tag_three = new VendorFoodTag();
                VendorFoodTag tag_four = new VendorFoodTag();
                VendorFoodTag tag_five = new VendorFoodTag();
                tag_one.vendor_id = this.vendorCookie.vendorID;
                tag_one.id = resultSet.getInt("vft1_id");
                tag_one.name = resultSet.getString("vft1_name");
                tag_one.hex_color = resultSet.getString("vft1_hex_color");
                tag_one.tag_type = resultSet.getString("vft1_tag_type");
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                vendorFoodIngredient.tag_one = tag_one;
                tag_two.vendor_id = this.vendorCookie.vendorID;
                tag_two.id = resultSet.getInt("vft2_id");
                tag_two.name = resultSet.getString("vft2_name");
                tag_two.hex_color = resultSet.getString("vft2_hex_color");
                tag_two.tag_type = resultSet.getString("vft2_tag_type");
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                vendorFoodIngredient.tag_two = tag_two;
                tag_three.vendor_id = this.vendorCookie.vendorID;
                tag_three.id = resultSet.getInt("vft3_id");
                tag_three.name = resultSet.getString("vft3_name");
                tag_three.hex_color = resultSet.getString("vft3_hex_color");
                tag_three.tag_type = resultSet.getString("vft3_tag_type");
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                vendorFoodIngredient.tag_three = tag_three;
                tag_four.vendor_id = this.vendorCookie.vendorID;
                tag_four.id = resultSet.getInt("vft4_id");
                tag_four.name = resultSet.getString("vft4_name");
                tag_four.hex_color = resultSet.getString("vft4_hex_color");
                tag_four.tag_type = resultSet.getString("vft4_tag_type");
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                vendorFoodIngredient.tag_four = tag_four;
                tag_five.vendor_id = this.vendorCookie.vendorID;
                tag_five.id = resultSet.getInt("vft5_id");
                tag_five.name = resultSet.getString("vft5_name");
                tag_five.hex_color = resultSet.getString("vft5_hex_color");
                tag_five.tag_type = resultSet.getString("vft5_tag_type");
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                vendorFoodIngredient.tag_five = tag_five;
                vendorFoodIngredient.verified = resultSet.getBoolean("vfi_verified");
                vendorFoodIngredient.creation_timestamp = resultSet.getString("vfi_creation_timestamp");
                vendorFoodIngredient.creation_days_ago = resultSet.getString("vfi_creation_days_ago");
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = resultSet.getInt("vnf_id");
                vendorNutritionalFact.profile_name = resultSet.getString("vnf_profile_name");
                vendorNutritionalFact.vendor_id = this.vendorCookie.vendorID;
                vendorNutritionalFact.serving_size = resultSet.getInt("vnf_serving_size");
                vendorNutritionalFact.calories = resultSet.getInt("vnf_calories");
                vendorNutritionalFact.calories_from_fat = resultSet.getInt("vnf_calories_from_fat");
                vendorNutritionalFact.total_fat = resultSet.getInt("vnf_total_fat");
                vendorNutritionalFact.saturated_fat = resultSet.getInt("vnf_saturated_fat");
                vendorNutritionalFact.trans_fat = resultSet.getInt("vnf_trans_fat");
                vendorNutritionalFact.cholesterol = resultSet.getInt("vnf_cholesterol");
                vendorNutritionalFact.sodium = resultSet.getInt("vnf_sodium");
                vendorNutritionalFact.total_carbs = resultSet.getInt("vnf_total_carbs");
                vendorNutritionalFact.dietary_fiber = resultSet.getInt("vnf_dietary_fiber");
                vendorNutritionalFact.sugar = resultSet.getInt("vnf_sugar");
                vendorNutritionalFact.vitamin_a = resultSet.getInt("vnf_vitamin_a");
                vendorNutritionalFact.vitamin_b = resultSet.getInt("vnf_vitamin_b");
                vendorNutritionalFact.vitamin_c = resultSet.getInt("vnf_vitamin_c");
                vendorNutritionalFact.vitamin_d = resultSet.getInt("vnf_vitamin_d");
                vendorNutritionalFact.calcium = resultSet.getInt("vnf_calcium");
                vendorNutritionalFact.iron = resultSet.getInt("vnf_iron");
                vendorNutritionalFact.protein = resultSet.getInt("vnf_protein");
                vendorNutritionalFact.creation_timestamp = resultSet.getString("vnf_creation_timestamp");
                vendorNutritionalFact.creation_days_ago = resultSet.getString("vnf_creation_days_ago");
                vendorFoodIngredient.nutritional_fact_profile = vendorNutritionalFact;
                foodIngredients.add(vendorFoodIngredient);
            }
            // Done.
            return foodIngredients;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load food ingredients.");
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

    public boolean createFoodIngredientAssociation(
            String cookie,
            int food_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Ensure resource ownership of ingredient.
            stage1 = this.DAO.prepareStatement(confirmFoodIngredientSQL);
            stage1.setInt(1, food_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to add this ingredient to this food.");
            }
            // Ensure resource ownership of food.
            stage2 = this.DAO.prepareStatement(this.confirmFoodOwnershipSQL);
            stage2.setInt(1, vendor_food_id);
            stage2Result = stage2.executeQuery();
            vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to add this ingredient to this food.");
            }
            // Create data.
            stage3 = this.DAO.prepareStatement(this.createFoodIngredientAssociationSQL);
            stage3.setInt(1, vendor_food_id);
            stage3.setInt(2, food_ingredient_id);
            stage3.setInt(3, this.vendorCookie.vendorID);
            stage3.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            if (ex.getMessage().contains("already exists.")) {
                // It already exists. Don't raise an exception.
                // Just return true.
                return true;
            }
            throw new Exception("Unable to add food ingredient to food.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage1Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteFoodIngredientAssociation(
            String cookie,
            int food_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Ensure resource ownership of ingredient.
            stage1 = this.DAO.prepareStatement(confirmFoodIngredientSQL);
            stage1.setInt(1, food_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to remove this ingredient from this food.");
            }
            // Ensure resource ownership of food.
            stage2 = this.DAO.prepareStatement(this.confirmFoodOwnershipSQL);
            stage2.setInt(1, vendor_food_id);
            stage2Result = stage2.executeQuery();
            vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to remove this ingredient from this food.");
            }
            // Delete data.
            stage3 = this.DAO.prepareStatement(this.deleteFoodIngredientAssociationSQL);
            stage3.setInt(1, vendor_food_id);
            stage3.setInt(2, food_ingredient_id);
            stage3.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to remove food ingredient to food.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage1Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
    /*------------------------------------------------------------
    ██████╗ ██████╗ ██╗███╗   ██╗██╗  ██╗███████╗
    ██╔══██╗██╔══██╗██║████╗  ██║██║ ██╔╝██╔════╝
    ██║  ██║██████╔╝██║██╔██╗ ██║█████╔╝ ███████╗
    ██║  ██║██╔══██╗██║██║╚██╗██║██╔═██╗ ╚════██║
    ██████╔╝██║  ██║██║██║ ╚████║██║  ██╗███████║
    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝
    DRINK INGREDIENTS
    ------------------------------------------------------------*/
    private String confirmDrinkIngredientSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_drink_ingredients " +
                    "WHERE " +
                    "   id = ?";

    private String confirmDrinkOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_drinks " +
                    "WHERE " +
                    "   id = ?";

    private String deleteDrinkIngredientSQL =
            "DELETE FROM " +
                    "   vendor_drink_ingredients " +
                    "WHERE " +
                    "   id = ?";

    private String createDrinkIngredientSQL =
            "INSERT INTO " +
                    "vendor_drink_ingredients( " +
                    "   name, " +
                    "   description, " +
                    "   source, " +
                    "   keywords, " +
                    "   vendor_id," +
                    "   feature_id" +
                    ") VALUES (?,?,?,?::varchar[],?,?) " +
                    "RETURNING id";

    private String updateDrinkIngredientSQL =
            "UPDATE vendor_drink_ingredients " +
                    "SET " +
                    "   name = ?, " +
                    "   description = ?, " +
                    "   source = ?, " +
                    "   keywords = ?::varchar[]," +
                    "   vendor_id = ?, " +
                    "   feature_id = ? " +
                    "WHERE " +
                    "   id = ?";

    private String createDrinkIngredientAssociationSQL =
            "INSERT INTO " +
                    "vendor_drink_ingredient_associations( " +
                    "   vendor_drink_id, " +
                    "   vendor_drink_ingredient_id" +
                    ") VALUES (?,?) " +
                    "RETURNING id";


    private String deleteDrinkIngredientAssociationSQL =
            "DELETE FROM " +
                    "   vendor_drink_ingredient_associations " +
                    "WHERE " +
                    "   vendor_drink_id = ? " +
                    "AND " +
                    "   vendor_drink_ingredient_id = ?";

    public int createDrinkIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "vendor_drink_ingredients");
            // Insert data.
            preparedStatement = this.DAO.prepareStatement(this.createDrinkIngredientSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, source);
            preparedStatement.setArray(4, this.DAO.createArrayOf("varchar", keywords));
            preparedStatement.setInt(5, this.vendorCookie.vendorID);
            preparedStatement.setInt(6, this.vendorCookie.requestFeatureID);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new IngredientsException("Unable to create drink ingredient.");
            }
            return id;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse error.
            if (ex.getMessage().contains("vendor_drink_ingredients_vendor_id_name_idx")) {
                throw new Exception("A drink ingredient with that name already exists.");
            }
            // Don't know why.
            throw new Exception("Unable to create drink ingredient.");
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

    public boolean updateDrinkIngredient(
            String cookie,
            int drink_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_drink_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(this.confirmDrinkIngredientSQL);
            stage1.setInt(1, drink_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to update this drink ingredient.");
            }
            // Update data.
            stage2 = this.DAO.prepareStatement(this.updateDrinkIngredientSQL);
            stage2.setString(1, name);
            stage2.setString(2, description);
            stage2.setString(3, source);
            stage2.setArray(4, this.DAO.createArrayOf("varchar", keywords));
            stage2.setInt(5, this.vendorCookie.vendorID);
            stage2.setInt(6, this.vendorCookie.requestFeatureID);
            stage2.setInt(7, drink_ingredient_id);
            stage2.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update drink ingredient.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteDrinkIngredient(
            String cookie,
            int drink_ingredient_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_drink_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(confirmDrinkIngredientSQL);
            stage1.setInt(1, drink_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to delete this drink ingredient.");
            }
            // Delete data.
            stage2 = this.DAO.prepareStatement(this.deleteDrinkIngredientSQL);
            stage2.setInt(1, drink_ingredient_id);
            stage2.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update drink ingredient.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadBeerIngredientsSQL =
            "SELECT " +
                    "   bi.id AS bi_id, " +
                    "   bi.vendor_id AS bi_vendor_id, " +
                    "   bi.name AS bi_name, " +
                    "   bi.description AS bi_description, " +
                    "   bi.source AS bi_source, " +
                    "   bi.keywords AS bi_keywords, " +
                    "   bi.tag_one AS bi_tag_one, " +
                    "   bi.tag_two AS bi_tag_two, " +
                    "   bi.tag_three AS bi_tag_three, " +
                    "   bi.tag_four AS bi_tag_four, " +
                    "   bi.tag_five AS bi_tag_five, " +
                    "   bi.nutritional_facts_id AS bi_nutritional_facts_id," +
                    "   bi.verified AS bi_verified, " +
                    "   bi.creation_timestamp bi_creation_timestamp," +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', bi.creation_timestamp::date)) AS bi_creation_days_ago," +
                    "   vnf.profile_name AS vnf_profile_name, " +
                    "   vnf.id AS vnf_id, " +
                    "   vnf.vendor_id AS vnf_vendor_id, " +
                    "   vnf.serving_size AS vnf_serving_size, " +
                    "   vnf.calories AS vnf_calories, " +
                    "   vnf.calories_from_fat AS vnf_calories_from_fat, " +
                    "   vnf.total_fat AS vnf_total_fat, " +
                    "   vnf.saturated_fat AS vnf_saturated_fat, " +
                    "   vnf.trans_fat AS vnf_trans_fat, " +
                    "   vnf.cholesterol AS vnf_cholesterol, " +
                    "   vnf.sodium AS vnf_sodium, " +
                    "   vnf.total_carbs AS vnf_total_carbs, " +
                    "   vnf.dietary_fiber AS vnf_dietary_fiber, " +
                    "   vnf.sugar AS vnf_sugar, " +
                    "   vnf.vitamin_a AS vnf_vitamin_a, " +
                    "   vnf.vitamin_b AS vnf_vitamin_b, " +
                    "   vnf.vitamin_c AS vnf_vitamin_c, " +
                    "   vnf.vitamin_d AS vnf_vitamin_d, " +
                    "   vnf.calcium AS vnf_calcium, " +
                    "   vnf.iron AS vnf_iron, " +
                    "   vnf.protein AS vnf_protein, " +
                    "   vnf.creation_timestamp vnf_creation_timestamp," +
                    "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', vnf.creation_timestamp::date)) AS vnf_creation_days_ago," +
                    "   bt1.id AS bt1_id, " +
                    "   bt1.name AS bt1_name, " +
                    "   bt1.hex_color AS bt1_hex_color, " +
                    "   bt1.tag_type AS bt1_tag_type, " +
                    "   bt2.id AS bt2_id, " +
                    "   bt2.name AS bt2_name, " +
                    "   bt2.hex_color AS bt2_hex_color, " +
                    "   bt2.tag_type AS bt2_tag_type, " +
                    "   bt3.id AS bt3_id, " +
                    "   bt3.name AS bt3_name, " +
                    "   bt3.hex_color AS bt3_hex_color, " +
                    "   bt3.tag_type AS bt3_tag_type, " +
                    "   bt4.id AS bt4_id, " +
                    "   bt4.name AS bt4_name, " +
                    "   bt4.hex_color AS bt4_hex_color, " +
                    "   bt4.tag_type AS bt4_tag_type, " +
                    "   bt5.id AS bt5_id, " +
                    "   bt5.name AS bt5_name, " +
                    "   bt5.hex_color AS bt5_hex_color, " +
                    "   bt5.tag_type AS bt5_tag_type " +
                    "FROM " +
                    "   beer_ingredients bi " +
                    "LEFT JOIN " +
                    "   vendor_nutritional_facts vnf " +
                    "ON " +
                    "   bi.nutritional_facts_id = vnf.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt1 " +
                    "ON " +
                    "   bi.tag_one = bt1.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt2 " +
                    "ON " +
                    "   bi.tag_two = bt2.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt3 " +
                    "ON " +
                    "   bi.tag_three = bt3.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt4 " +
                    "ON " +
                    "   bi.tag_four = bt4.id " +
                    "LEFT JOIN " +
                    "   beer_tags bt5 " +
                    "ON " +
                    "   bi.tag_five = bt5.id " +
                    "WHERE " +
                    "   bi.vendor_id = ?";

    public ArrayList<BeerIngredient> loadBeerIngredients(
            String cookie
    ) throws Exception {
        ArrayList<BeerIngredient> beerIngredients = new ArrayList<BeerIngredient>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate request.
            this.validateCookieVendorFeature(cookie, "beer_ingredients");
            // Get the data.
            preparedStatement = this.DAO.prepareStatement(this.loadBeerIngredientsSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Color color = new Color();
                BeerIngredient beerIngredient= new BeerIngredient();
                beerIngredient.id = resultSet.getInt("bi_id");
                beerIngredient.vendor_id = this.vendorCookie.vendorID;
                beerIngredient.name = resultSet.getString("bi_name");
                beerIngredient.description = resultSet.getString("bi_description");
                beerIngredient.source = resultSet.getString("bi_source");
                Array keywordsArray = resultSet.getArray("bi_keywords");
                String[] keywords = (String[]) keywordsArray.getArray();
                beerIngredient.keywords = keywords;
                BeerTag tag_one = new BeerTag();
                BeerTag tag_two = new BeerTag();
                BeerTag tag_three = new BeerTag();
                BeerTag tag_four = new BeerTag();
                BeerTag tag_five = new BeerTag();
                tag_one.vendor_id = this.vendorCookie.vendorID;
                tag_one.id = resultSet.getInt("bt1_id");
                tag_one.name = resultSet.getString("bt1_name");
                tag_one.hex_color = resultSet.getString("bt1_hex_color");
                tag_one.tag_type = resultSet.getString("bt1_tag_type");
                if (tag_one.id != 0) {
                    tag_one.text_color = color.getInverseBW(tag_one.hex_color);
                }
                beerIngredient.tag_one = tag_one;
                tag_two.vendor_id = this.vendorCookie.vendorID;
                tag_two.id = resultSet.getInt("bt2_id");
                tag_two.name = resultSet.getString("bt2_name");
                tag_two.hex_color = resultSet.getString("bt2_hex_color");
                tag_two.tag_type = resultSet.getString("bt2_tag_type");
                if (tag_two.id != 0) {
                    tag_two.text_color = color.getInverseBW(tag_two.hex_color);
                }
                beerIngredient.tag_two = tag_two;
                tag_three.vendor_id = this.vendorCookie.vendorID;
                tag_three.id = resultSet.getInt("bt3_id");
                tag_three.name = resultSet.getString("bt3_name");
                tag_three.hex_color = resultSet.getString("bt3_hex_color");
                tag_three.tag_type = resultSet.getString("bt3_tag_type");
                if (tag_three.id != 0) {
                    tag_three.text_color = color.getInverseBW(tag_three.hex_color);
                }
                beerIngredient.tag_three = tag_three;
                tag_four.vendor_id = this.vendorCookie.vendorID;
                tag_four.id = resultSet.getInt("bt4_id");
                tag_four.name = resultSet.getString("bt4_name");
                tag_four.hex_color = resultSet.getString("bt4_hex_color");
                tag_four.tag_type = resultSet.getString("bt4_tag_type");
                if (tag_four.id != 0) {
                    tag_four.text_color = color.getInverseBW(tag_four.hex_color);
                }
                beerIngredient.tag_four = tag_four;
                tag_five.vendor_id = this.vendorCookie.vendorID;
                tag_five.id = resultSet.getInt("bt5_id");
                tag_five.name = resultSet.getString("bt5_name");
                tag_five.hex_color = resultSet.getString("bt5_hex_color");
                tag_five.tag_type = resultSet.getString("bt5_tag_type");
                if (tag_five.id != 0) {
                    tag_five.text_color = color.getInverseBW(tag_five.hex_color);
                }
                beerIngredient.tag_five = tag_five;
                beerIngredient.verified = resultSet.getBoolean("bi_verified");
                beerIngredient.creation_timestamp = resultSet.getString("bi_creation_timestamp");
                beerIngredient.creation_days_ago = resultSet.getString("bi_creation_days_ago");
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = resultSet.getInt("vnf_id");
                vendorNutritionalFact.profile_name = resultSet.getString("vnf_profile_name");
                vendorNutritionalFact.vendor_id = this.vendorCookie.vendorID;
                vendorNutritionalFact.serving_size = resultSet.getInt("vnf_serving_size");
                vendorNutritionalFact.calories = resultSet.getInt("vnf_calories");
                vendorNutritionalFact.calories_from_fat = resultSet.getInt("vnf_calories_from_fat");
                vendorNutritionalFact.total_fat = resultSet.getInt("vnf_total_fat");
                vendorNutritionalFact.saturated_fat = resultSet.getInt("vnf_saturated_fat");
                vendorNutritionalFact.trans_fat = resultSet.getInt("vnf_trans_fat");
                vendorNutritionalFact.cholesterol = resultSet.getInt("vnf_cholesterol");
                vendorNutritionalFact.sodium = resultSet.getInt("vnf_sodium");
                vendorNutritionalFact.total_carbs = resultSet.getInt("vnf_total_carbs");
                vendorNutritionalFact.dietary_fiber = resultSet.getInt("vnf_dietary_fiber");
                vendorNutritionalFact.sugar = resultSet.getInt("vnf_sugar");
                vendorNutritionalFact.vitamin_a = resultSet.getInt("vnf_vitamin_a");
                vendorNutritionalFact.vitamin_b = resultSet.getInt("vnf_vitamin_b");
                vendorNutritionalFact.vitamin_c = resultSet.getInt("vnf_vitamin_c");
                vendorNutritionalFact.vitamin_d = resultSet.getInt("vnf_vitamin_d");
                vendorNutritionalFact.calcium = resultSet.getInt("vnf_calcium");
                vendorNutritionalFact.iron = resultSet.getInt("vnf_iron");
                vendorNutritionalFact.protein = resultSet.getInt("vnf_protein");
                vendorNutritionalFact.creation_timestamp = resultSet.getString("vnf_creation_timestamp");
                vendorNutritionalFact.creation_days_ago = resultSet.getString("vnf_creation_days_ago");
                beerIngredient.nutritional_fact_profile = vendorNutritionalFact;
                beerIngredients.add(beerIngredient);
            }
            // Done.
            return beerIngredients;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load beer ingredients.");
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

    public boolean createDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_drink_ingredients");
            // Ensure resource ownership of ingredient.
            stage1 = this.DAO.prepareStatement(confirmDrinkIngredientSQL);
            stage1.setInt(1, drink_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to add this ingredient to this drink.");
            }
            // Ensure resource ownership of drink.
            stage2 = this.DAO.prepareStatement(this.confirmDrinkOwnershipSQL);
            stage2.setInt(1, vendor_drink_id);
            stage2Result = stage2.executeQuery();
            vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to add this ingredient to this drink.");
            }
            // Create data.
            stage3 = this.DAO.prepareStatement(this.createDrinkIngredientAssociationSQL);
            stage3.setInt(1, vendor_drink_id);
            stage3.setInt(2, drink_ingredient_id);
            stage3.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to add drink ingredient to drink.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage1Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "vendor_drink_ingredients");
            // Ensure resource ownership of ingredient.
            stage1 = this.DAO.prepareStatement(confirmDrinkIngredientSQL);
            stage1.setInt(1, drink_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to remove this ingredient from this drink.");
            }
            // Ensure resource ownership of drink.
            stage2 = this.DAO.prepareStatement(this.confirmDrinkOwnershipSQL);
            stage2.setInt(1, vendor_drink_id);
            stage2Result = stage2.executeQuery();
            vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to remove this ingredient from this drink.");
            }
            // Delete data.
            stage3 = this.DAO.prepareStatement(this.deleteDrinkIngredientAssociationSQL);
            stage3.setInt(1, vendor_drink_id);
            stage3.setInt(2, drink_ingredient_id);
            stage3.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to remove drink ingredient to drink.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage1Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
    /*------------------------------------------------------------
    ██████╗ ███████╗███████╗██████╗ ███████╗
    ██╔══██╗██╔════╝██╔════╝██╔══██╗██╔════╝
    ██████╔╝█████╗  █████╗  ██████╔╝███████╗
    ██╔══██╗██╔══╝  ██╔══╝  ██╔══██╗╚════██║
    ██████╔╝███████╗███████╗██║  ██║███████║
    ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝╚══════╝
    BEER INGREDIENTS
    ------------------------------------------------------------*/
    private String confirmBeerIngredientSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   beer_ingredients " +
                    "WHERE " +
                    "   id = ?";

    private String confirmBeerOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   beers " +
                    "WHERE " +
                    "   id = ?";

    private String deleteBeerIngredientSQL =
            "DELETE FROM " +
                    "   beer_ingredients " +
                    "WHERE " +
                    "   id = ?";

    private String createBeerIngredientSQL =
            "INSERT INTO " +
                    "beer_ingredients( " +
                    "   name, " +
                    "   description, " +
                    "   source, " +
                    "   keywords, " +
                    "   vendor_id," +
                    "   feature_id" +
                    ") VALUES (?,?,?,?::varchar[],?,?) " +
                    "RETURNING id";

    private String updateBeerIngredientSQL =
            "UPDATE beer_ingredients " +
                    "SET " +
                    "   name = ?, " +
                    "   description = ?, " +
                    "   source = ?, " +
                    "   keywords = ?::varchar[]," +
                    "   vendor_id = ?, " +
                    "   feature_id = ? " +
                    "WHERE " +
                    "   id = ?";

    private String createBeerIngredientAssociationSQL =
            "INSERT INTO " +
                    "beer_ingredient_associations( " +
                    "   beer_id, " +
                    "   beer_ingredient_id" +
                    ") VALUES (?,?) " +
                    "RETURNING id";


    private String deleteBeerIngredientAssociationSQL =
            "DELETE FROM " +
                    "   beer_ingredient_associations " +
                    "WHERE " +
                    "   beer_id = ? " +
                    "AND " +
                    "   beer_ingredient_id = ?";

    public int createBeerIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie.
            this.validateCookieVendorFeature(cookie, "beer_ingredients");
            // Insert data.
            preparedStatement = this.DAO.prepareStatement(this.createBeerIngredientSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, source);
            preparedStatement.setArray(4, this.DAO.createArrayOf("varchar", keywords));
            preparedStatement.setInt(5, this.vendorCookie.vendorID);
            preparedStatement.setInt(6, this.vendorCookie.requestFeatureID);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new IngredientsException("Unable to create beer ingredient.");
            }
            return id;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse error.
            if (ex.getMessage().contains("beer_ingredients_vendor_id_name_idx")) {
                throw new Exception("A beer ingredient with that name already exists.");
            }
            // Don't know why.
            throw new Exception("Unable to create beer ingredient.");
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

    public boolean updateBeerIngredient(
            String cookie,
            int beer_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "beer_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(this.confirmBeerIngredientSQL);
            stage1.setInt(1, beer_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to update this beer ingredient.");
            }
            // Update data.
            stage2 = this.DAO.prepareStatement(this.updateBeerIngredientSQL);
            stage2.setString(1, name);
            stage2.setString(2, description);
            stage2.setString(3, source);
            stage2.setArray(4, this.DAO.createArrayOf("varchar", keywords));
            stage2.setInt(5, this.vendorCookie.vendorID);
            stage2.setInt(6, this.vendorCookie.requestFeatureID);
            stage2.setInt(7, beer_ingredient_id);
            stage2.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update beer ingredient.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteBeerIngredient(
            String cookie,
            int beer_ingredient_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "beer_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(confirmBeerIngredientSQL);
            stage1.setInt(1, beer_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to delete this beer ingredient.");
            }
            // Delete data.
            stage2 = this.DAO.prepareStatement(this.deleteBeerIngredientSQL);
            stage2.setInt(1, beer_ingredient_id);
            stage2.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update beer ingredient.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean createBeerIngredientAssociation(
            String cookie,
            int beer_ingredient_id,
            int beer_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "beer_ingredients");
            // Ensure resource ownership of ingredient.
            stage1 = this.DAO.prepareStatement(confirmBeerIngredientSQL);
            stage1.setInt(1, beer_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to add this ingredient to this beer.");
            }
            // Ensure resource ownership of beer.
            stage2 = this.DAO.prepareStatement(this.confirmBeerOwnershipSQL);
            stage2.setInt(1, beer_id);
            stage2Result = stage2.executeQuery();
            vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to add this ingredient to this beer.");
            }
            // Create data.
            stage3 = this.DAO.prepareStatement(this.createBeerIngredientAssociationSQL);
            stage3.setInt(1, beer_id);
            stage3.setInt(2, beer_ingredient_id);
            stage3.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to add beer ingredient to beer.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage1Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteBeerIngredientAssociation(
            String cookie,
            int beer_ingredient_id,
            int beer_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            // Disable auto-commit.
            this.DAO.setAutoCommit(false);
            // Validate request permission.
            this.validateCookieVendorFeature(cookie, "beer_ingredients");
            // Ensure resource ownership of ingredient.
            stage1 = this.DAO.prepareStatement(confirmBeerIngredientSQL);
            stage1.setInt(1, beer_ingredient_id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to remove this ingredient from this beer.");
            }
            // Ensure resource ownership of beer.
            stage2 = this.DAO.prepareStatement(this.confirmBeerOwnershipSQL);
            stage2.setInt(1, beer_id);
            stage2Result = stage2.executeQuery();
            vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new IngredientsException("This account does not have permission to remove this ingredient from this beer.");
            }
            // Delete data.
            stage3 = this.DAO.prepareStatement(this.deleteBeerIngredientAssociationSQL);
            stage3.setInt(1, beer_id);
            stage3.setInt(2, beer_ingredient_id);
            stage3.execute();
            // Commit and return.
            this.DAO.commit();
            return true;
        } catch (IngredientsException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to remove beer ingredient to beer.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage1Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}
