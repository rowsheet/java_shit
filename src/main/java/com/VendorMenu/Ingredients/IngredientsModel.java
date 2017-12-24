package com.VendorMenu.Ingredients;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IngredientsModel extends AbstractModel {

    public IngredientsModel() throws Exception {}
    /*------------------------------------------------------------
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
                    "   feature_id" +
                    ") VALUES (?,?,?,?::varchar[],?,?) " +
                    "RETURNING id";

    private String updateFoodIngredientSQL =
            "UPDATE vendor_food_ingredients " +
                    "SET " +
                    "   name = ?, " +
                    "   description = ?, " +
                    "   source = ?, " +
                    "   keywords = ?::varchar[]," +
                    "   vendor_id = ?, " +
                    "   feature_id = ? " +
                    "WHERE " +
                    "   id = ?";

    private String createFoodIngredientAssociationSQL =
            "INSERT INTO " +
                    "vendor_food_ingredient_associations( " +
                    "   vendor_food_id, " +
                    "   vendor_food_ingredient_id" +
                    ") VALUES (?,?) " +
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
            String[] keywords
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
            int food_ingredient_id,
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
            this.validateCookieVendorFeature(cookie, "vendor_food_ingredients");
            // Ensure resource ownership.
            stage1 = this.DAO.prepareStatement(this.confirmFoodIngredientSQL);
            stage1.setInt(1, food_ingredient_id);
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
            stage2.setInt(7, food_ingredient_id);
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
        }    }
    /*------------------------------------------------------------
    DRINK INGREDIENTS
    ------------------------------------------------------------*/
    public int createDrinkIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return 1;
    }

    public boolean updateDrinkIngredient(
            String cookie,
            int drink_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return true;
    }

    public boolean deleteDrinkIngredient(
            String cookie,
            int drink_ingredient_id
    ) throws Exception {
        return true;
    }

    public boolean createDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        return true;
    }

    public boolean deleteDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        return true;
    }
    /*------------------------------------------------------------
    BEER INGREDIENTS
    ------------------------------------------------------------*/
    public int createBeerIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return 1;
    }

    public boolean updateBeerIngredient(
            String cookie,
            int beer_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return true;
    }

    public boolean deleteBeerIngredient(
            String cookie,
            int beer_ingredient_id
    ) throws Exception {
        return true;
    }

    public boolean createBeerIngredientAssociation(
            String cookie,
            int beer_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        return true;
    }

    public boolean deleteBeerIngredientAssociation(
            String cookie,
            int beer_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        return true;
    }
}
