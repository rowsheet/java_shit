package com.VendorMenu.Extras;

import com.Common.AbstractModel;
import com.Common.VendorDropdownContainer;
import com.Common.VendorNutritionalFact;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class ExtrasModel extends AbstractModel {

    public ExtrasModel() throws Exception {}

    private String createNutritionalFactSQL =
        "INSERT INTO " +
                "   vendor_nutritional_facts (" +
                "   vendor_id, " +
                "   serving_size, " +
                "   calories, " +
                "   calories_from_fat, " +
                "   total_fat, " +
                "   saturated_fat, " +
                "   trans_fat, " +
                "   cholesterol, " +
                "   sodium, " +
                "   total_carbs, " +
                "   dietary_fiber, " +
                "   sugar, " +
                "   protein, " +
                "   vitamin_a, " +
                "   vitamin_b, " +
                "   vitamin_c, " +
                "   vitamin_d, " +
                "   calcium, " +
                "   iron," +
                "   profile_name " +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "RETURNING id";

    private String updateNutritionalFactsSQL =
        "UPDATE " +
                "   vendor_nutritional_facts " +
                "SET " +
                "   vendor_id = ?, " +
                "   serving_size = ?, " +
                "   calories = ?, " +
                "   calories_from_fat = ?, " +
                "   total_fat = ?, " +
                "   saturated_fat = ?, " +
                "   trans_fat = ?, " +
                "   cholesterol = ?, " +
                "   sodium = ?, " +
                "   total_carbs = ?, " +
                "   dietary_fiber = ?, " +
                "   sugar = ?, " +
                "   protein = ?, " +
                "   vitamin_a = ?, " +
                "   vitamin_b = ?, " +
                "   vitamin_c = ?, " +
                "   vitamin_d = ?, " +
                "   calcium = ?, " +
                "   iron = ?," +
                "   profile_name = ? " +
                "WHERE " +
                "   id = ?";

    private String verifyNutritionalFactOwnershipSQL =
        "SELECT " +
                "   vendor_id " +
                "FROM " +
                "   vendor_nutritional_facts " +
                "WHERE " +
                "   id = ?";

    private String deleteNutritionalFactsSQL =
        "DELETE FROM " +
                "   vendor_nutritional_facts " +
                "WHERE " +
                "   id = ?";

    public int createNutritionalFact(
            String cookie,
            String profile_name,
            int serving_size,
            int calories,
            int calories_from_fat,
            int total_fat,
            int saturated_fat,
            int trans_fat,
            int cholesterol,
            int sodium,
            int total_carbs,
            int dietary_fiber,
            int sugar,
            int protein,
            int vitamin_a,
            int vitamin_b,
            int vitamin_c,
            int vitamin_d,
            int calcium,
            int iron
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate permission
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Insert data.
            preparedStatement = this.DAO.prepareStatement(this.createNutritionalFactSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, serving_size);
            preparedStatement.setInt(3, calories);
            preparedStatement.setInt(4, calories_from_fat);
            preparedStatement.setInt(5, total_fat);
            preparedStatement.setInt(6, saturated_fat);
            preparedStatement.setInt(7, trans_fat);
            preparedStatement.setInt(8, cholesterol);
            preparedStatement.setInt(9, sodium);
            preparedStatement.setInt(10, total_carbs);
            preparedStatement.setInt(11, dietary_fiber);
            preparedStatement.setInt(12, sugar);
            preparedStatement.setInt(13, protein);
            preparedStatement.setInt(14, vitamin_a);
            preparedStatement.setInt(15, vitamin_b);
            preparedStatement.setInt(16, vitamin_c);
            preparedStatement.setInt(17, vitamin_d);
            preparedStatement.setInt(18, calcium);
            preparedStatement.setInt(19, iron);
            preparedStatement.setString(20, profile_name);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new Exception("Unable to create nutritional facts entry");
            }
            return id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why
            if (ex.getMessage().contains("vendor_nutritional_facts_profile_name_vendor_id_idx")) {
                throw new Exception("A Nutritional Fact profile with that name already exists!");
            }
            throw new Exception("Unable to create nutritional facts entry");
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

    public boolean updateNutritionalFact(
            String cookie,
            int id,
            String profile_name,
            int serving_size,
            int calories,
            int calories_from_fat,
            int total_fat,
            int saturated_fat,
            int trans_fat,
            int cholesterol,
            int sodium,
            int total_carbs,
            int dietary_fiber,
            int sugar,
            int protein,
            int vitamin_a,
            int vitamin_b,
            int vitamin_c,
            int vitamin_d,
            int calcium,
            int iron
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Set auto-commit to false.
            this.DAO.setAutoCommit(false);
            // Validate cookie.
            this.validateCookieVendorFeature(cookie,"nutritional_facts");
            // Verify resource ownership.
            stage1 = this.DAO.prepareStatement(this.verifyNutritionalFactOwnershipSQL);
            stage1.setInt(1, id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to update this nutritional fact profile.");
            }
            // Update data.
            stage2 = this.DAO.prepareStatement(this.updateNutritionalFactsSQL);
            stage2.setInt(1, this.vendorCookie.vendorID);
            stage2.setInt(2, serving_size);
            stage2.setInt(3, calories);
            stage2.setInt(4, calories_from_fat);
            stage2.setInt(5, total_fat);
            stage2.setInt(6, saturated_fat);
            stage2.setInt(7, trans_fat);
            stage2.setInt(8, cholesterol);
            stage2.setInt(9, sodium);
            stage2.setInt(10, total_carbs);
            stage2.setInt(11, dietary_fiber);
            stage2.setInt(12, sugar);
            stage2.setInt(13, protein);
            stage2.setInt(14, vitamin_a);
            stage2.setInt(15, vitamin_b);
            stage2.setInt(16, vitamin_c);
            stage2.setInt(17, vitamin_d);
            stage2.setInt(18, calcium);
            stage2.setInt(19, iron);
            stage2.setString(20, profile_name);
            stage2.setInt(21, id);
            stage2.execute();
            // Done. Commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update nutritional facts profile.");
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean deleteNutritionalFact(
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            // Set auto-commit to false.
            this.DAO.setAutoCommit(false);
            // Verify resource ownership.
            stage1 = this.DAO.prepareStatement(this.verifyNutritionalFactOwnershipSQL);
            stage1.setInt(1, id);
            stage1Result = stage1.executeQuery();
            int vendor_id = 0;
            while (stage1Result.next()) {
                vendor_id = stage1Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to delete this nutritional fact profile.");
            }
            // Delete the data.
            stage2 = this.DAO.prepareStatement(this.deleteNutritionalFactsSQL);
            stage2.setInt(1, id);
            stage2.execute();
            // Done. Commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete nutritional facts profile.");
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String verifyFoodOwnershipSQL =
        "SELECT " +
                "   vendor_id " +
                "FROM " +
                "   vendor_foods " +
                "WHERE " +
                "   id = ?";

    private String associateFoodNutritionalFactSQL =
        "UPDATE " +
                "   vendor_foods " +
                "SET " +
                "   nutritional_facts_id = ? " +
                "WHERE " +
                "   id = ?";

    private String disassociateFoodNutritionalFactSQL =
        "UPDATE " +
                "   vendor_foods " +
                "SET " +
                "   nutritional_facts_id = NULL " +
                "WHERE " +
                "   id = ?";

    public boolean associateFoodNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement verify_food = null;
        ResultSet verify_food_result = null;
        PreparedStatement verify_nutritional_facts = null;
        ResultSet verify_nutritional_facts_result = null;
        PreparedStatement associate = null;
        try {
            // Disable auto commit.
            this.DAO.setAutoCommit(false);
            // Verify session.
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Verify food ownership.
            verify_food = this.DAO.prepareStatement(this.verifyFoodOwnershipSQL);
            verify_food.setInt(1, vendor_food_id);
            verify_food_result = verify_food.executeQuery();
            int vendor_id = 0;
            while (verify_food_result.next()) {
                vendor_id = verify_food_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to associate this nutritional fact profile to this food.");
            }
            // Verify nutritional fact ownership.
            verify_nutritional_facts = this.DAO.prepareStatement(this.verifyNutritionalFactOwnershipSQL);
            verify_nutritional_facts.setInt(1, nutritional_fact_id);
            verify_nutritional_facts_result = verify_nutritional_facts.executeQuery();
            vendor_id = 0;
            while (verify_nutritional_facts_result.next()) {
                vendor_id = verify_nutritional_facts_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to associate this nutritional fact profile to this food.");
            }
            // Make association.
            associate = this.DAO.prepareStatement(this.associateFoodNutritionalFactSQL);
            associate.setInt(1, nutritional_fact_id);
            associate.setInt(2, vendor_food_id);
            associate.execute();
            // Done commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to associate food to nutritional fact profile.");
        } finally {
            if (verify_food != null) {
                verify_food.close();
            }
            if (verify_food_result != null) {
                verify_food_result.close();
            }
            if (verify_nutritional_facts != null) {
                verify_nutritional_facts.close();
            }
            if (verify_nutritional_facts_result != null) {
                verify_nutritional_facts_result.close();
            }
            if (associate != null) {
                associate.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean disassociateFoodNutritionalFact(
            String cookie,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement verify_food = null;
        ResultSet verify_food_result = null;
        PreparedStatement disassociate = null;
        try {
            // Disable auto commit.
            this.DAO.setAutoCommit(false);
            // Verify session.
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Verify food ownership.
            verify_food = this.DAO.prepareStatement(this.verifyFoodOwnershipSQL);
            verify_food.setInt(1, vendor_food_id);
            verify_food_result = verify_food.executeQuery();
            int vendor_id = 0;
            while (verify_food_result.next()) {
                vendor_id = verify_food_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to disassociate this nutritional fact profile to this food.");
            }
            // Remove association.
            disassociate = this.DAO.prepareStatement(this.disassociateFoodNutritionalFactSQL);
            disassociate.setInt(1, vendor_food_id);
            disassociate.execute();
            // Done commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to disassociate food to nutritional fact profile.");
        } finally {
            if (verify_food != null) {
                verify_food.close();
            }
            if (verify_food_result != null) {
                verify_food_result.close();
            }
            if (disassociate != null) {
                disassociate.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String verifyVendorDrinkOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_drinks " +
                    "WHERE " +
                    "   id = ?";

    private String associateVendorDrinksNutritionalFactSQL =
            "UPDATE " +
                    "   vendor_drinks " +
                    "SET " +
                    "   nutritional_facts_id = ? " +
                    "WHERE " +
                    "   id = ?";

    private String disassociateVendorDrinksNutritionalFactSQL =
            "UPDATE " +
                    "   vendor_drinks " +
                    "SET " +
                    "   nutritional_facts_id = NULL " +
                    "WHERE " +
                    "   id = ?";

    public boolean associateDrinkNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement verify_drink = null;
        ResultSet verify_drink_result = null;
        PreparedStatement verify_nutritional_facts = null;
        ResultSet verify_nutritional_facts_result = null;
        PreparedStatement associate = null;
        try {
            // Disable auto commit.
            this.DAO.setAutoCommit(false);
            // Verify session.
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Verify food ownership.
            verify_drink = this.DAO.prepareStatement(this.verifyVendorDrinkOwnershipSQL);
            verify_drink.setInt(1, vendor_drink_id);
            verify_drink_result = verify_drink.executeQuery();
            int vendor_id = 0;
            while (verify_drink_result.next()) {
                vendor_id = verify_drink_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to associate this nutritional fact profile to this drink.");
            }
            // Verify nutritional fact ownership.
            verify_nutritional_facts = this.DAO.prepareStatement(this.verifyNutritionalFactOwnershipSQL);
            verify_nutritional_facts.setInt(1, nutritional_fact_id);
            verify_nutritional_facts_result = verify_nutritional_facts.executeQuery();
            vendor_id = 0;
            while (verify_nutritional_facts_result.next()) {
                vendor_id = verify_nutritional_facts_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to associate this nutritional fact profile to this drink.");
            }
            // Make association.
            associate = this.DAO.prepareStatement(this.associateVendorDrinksNutritionalFactSQL);
            associate.setInt(1, nutritional_fact_id);
            associate.setInt(2, vendor_drink_id);
            associate.execute();
            // Done commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to associate drink to nutritional fact profile.");
        } finally {
            if (verify_drink != null) {
                verify_drink.close();
            }
            if (verify_drink_result != null) {
                verify_drink_result.close();
            }
            if (verify_nutritional_facts != null) {
                verify_nutritional_facts.close();
            }
            if (verify_nutritional_facts_result != null) {
                verify_nutritional_facts_result.close();
            }
            if (associate != null) {
                associate.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean disassociateDrinkNutritionalFact(
            String cookie,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement verify_drink = null;
        ResultSet verify_drink_result = null;
        PreparedStatement disassociate = null;
        try {
            // Disable auto commit.
            this.DAO.setAutoCommit(false);
            // Verify session.
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Verify food ownership.
            verify_drink = this.DAO.prepareStatement(this.verifyVendorDrinkOwnershipSQL);
            verify_drink.setInt(1, vendor_drink_id);
            verify_drink_result = verify_drink.executeQuery();
            int vendor_id = 0;
            while (verify_drink_result.next()) {
                vendor_id = verify_drink_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to disassociate this nutritional fact profile to this drink.");
            }
            // Remove association.
            disassociate = this.DAO.prepareStatement(this.disassociateVendorDrinksNutritionalFactSQL);
            disassociate.setInt(1, vendor_drink_id);
            disassociate.execute();
            // Done commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to disassociate drink to nutritional fact profile.");
        } finally {
            if (verify_drink != null) {
                verify_drink.close();
            }
            if (verify_drink_result != null) {
                verify_drink_result.close();
            }
            if (disassociate != null) {
                disassociate.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String verifyBeerOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   beers " +
                    "WHERE " +
                    "   id = ?";

    private String associateBeerNutritionalFactSQL =
            "UPDATE " +
                    "   beers " +
                    "SET " +
                    "   nutritional_facts_id = ? " +
                    "WHERE " +
                    "   id = ?";

    private String disassociateBeerNutritionalFactSQL =
            "UPDATE " +
                    "   beers " +
                    "SET " +
                    "   nutritional_facts_id = NULL " +
                    "WHERE " +
                    "   id = ?";

    public boolean associateBeerNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            int beer_id
    ) throws Exception {
        PreparedStatement verify_beer = null;
        ResultSet verify_beer_result = null;
        PreparedStatement verify_nutritional_facts = null;
        ResultSet verify_nutritional_facts_result = null;
        PreparedStatement associate = null;
        try {
            // Disable auto commit.
            this.DAO.setAutoCommit(false);
            // Verify session.
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Verify beer ownership.
            verify_beer = this.DAO.prepareStatement(this.verifyBeerOwnershipSQL);
            verify_beer.setInt(1, beer_id);
            verify_beer_result = verify_beer.executeQuery();
            int vendor_id = 0;
            while (verify_beer_result.next()) {
                vendor_id = verify_beer_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to associate this nutritional fact profile to this beer.");
            }
            // Verify nutritional fact ownership.
            verify_nutritional_facts = this.DAO.prepareStatement(this.verifyNutritionalFactOwnershipSQL);
            verify_nutritional_facts.setInt(1, nutritional_fact_id);
            verify_nutritional_facts_result = verify_nutritional_facts.executeQuery();
            vendor_id = 0;
            while (verify_nutritional_facts_result.next()) {
                vendor_id = verify_nutritional_facts_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to associate this nutritional fact profile to this beer.");
            }
            // Make association.
            associate = this.DAO.prepareStatement(this.associateBeerNutritionalFactSQL);
            associate.setInt(1, nutritional_fact_id);
            associate.setInt(2, beer_id);
            associate.execute();
            // Done commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to associate beer to nutritional fact profile.");
        } finally {
            if (verify_beer != null) {
                verify_beer.close();
            }
            if (verify_beer_result != null) {
                verify_beer_result.close();
            }
            if (verify_nutritional_facts != null) {
                verify_nutritional_facts.close();
            }
            if (verify_nutritional_facts_result != null) {
                verify_nutritional_facts_result.close();
            }
            if (associate != null) {
                associate.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public boolean disassociateBeerNutritionalFact(
            String cookie,
            int beer_id
    ) throws Exception {
        PreparedStatement verify_beer = null;
        ResultSet verify_beer_result = null;
        PreparedStatement disassociate = null;
        try {
            // Disable auto commit.
            this.DAO.setAutoCommit(false);
            // Verify session.
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            // Verify food ownership.
            verify_beer = this.DAO.prepareStatement(this.verifyBeerOwnershipSQL);
            verify_beer.setInt(1, beer_id);
            verify_beer_result = verify_beer.executeQuery();
            int vendor_id = 0;
            while (verify_beer_result.next()) {
                vendor_id = verify_beer_result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new ExtrasException("This account does not have permission to disassociate this nutritional fact profile to this beer.");
            }
            // Make association.
            disassociate = this.DAO.prepareStatement(this.disassociateBeerNutritionalFactSQL);
            disassociate.setInt(1, beer_id);
            disassociate.execute();
            // Done commit.
            this.DAO.commit();
            return true;
        } catch (ExtrasException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to disassociate beer to nutritional fact profile.");
        } finally {
            if (verify_beer != null) {
                verify_beer.close();
            }
            if (verify_beer_result != null) {
                verify_beer_result.close();
            }
            if (disassociate != null) {
                disassociate.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadNutritionalFactsSQL =
        "SELECT " +
                "   id, " +
                "   vendor_id, " +
                "   serving_size, " +
                "   calories, " +
                "   calories_from_fat, " +
                "   total_fat, " +
                "   saturated_fat, " +
                "   trans_fat, " +
                "   cholesterol, " +
                "   sodium, " +
                "   total_carbs, " +
                "   dietary_fiber, " +
                "   sugar, " +
                "   vitamin_a, " +
                "   vitamin_b, " +
                "   vitamin_c, " +
                "   vitamin_d, " +
                "   calcium, " +
                "   iron, " +
                "   protein, " +
                "   profile_name, " +
                "   creation_timestamp, " +
                "   ABS(DATE_PART('day', now()::date) - DATE_PART('day', creation_timestamp::date)) AS creation_days_ago " +
                "FROM " +
                "   vendor_nutritional_facts " +
                "WHERE " +
                "   vendor_id = ?";

    public HashMap<Integer, VendorNutritionalFact> loadNutritionalFacts(
            String cookie
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            this.validateCookieVendorFeature(cookie, "nutritional_facts");
            preparedStatement = this.DAO.prepareStatement(this.loadNutritionalFactsSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            resultSet = preparedStatement.executeQuery();
            HashMap<Integer, VendorNutritionalFact> vendorNutritionalFactHashMap = new HashMap<Integer, VendorNutritionalFact>();
            while (resultSet.next()) {
                VendorNutritionalFact vendorNutritionalFact = new VendorNutritionalFact();
                vendorNutritionalFact.id = resultSet.getInt("id");
                vendorNutritionalFact.vendor_id = this.vendorCookie.vendorID;
                vendorNutritionalFact.serving_size = resultSet.getInt("serving_size");
                vendorNutritionalFact.calories = resultSet.getInt("calories");
                vendorNutritionalFact.calories_from_fat = resultSet.getInt("calories_from_fat");
                vendorNutritionalFact.total_fat = resultSet.getInt("total_fat");
                vendorNutritionalFact.saturated_fat = resultSet.getInt("saturated_fat");
                vendorNutritionalFact.trans_fat = resultSet.getInt("trans_fat");
                vendorNutritionalFact.cholesterol = resultSet.getInt("cholesterol");
                vendorNutritionalFact.sodium = resultSet.getInt("sodium");
                vendorNutritionalFact.total_carbs = resultSet.getInt("total_carbs");
                vendorNutritionalFact.dietary_fiber = resultSet.getInt("dietary_fiber");
                vendorNutritionalFact.sugar = resultSet.getInt("sugar");
                vendorNutritionalFact.vitamin_a = resultSet.getInt("vitamin_a");
                vendorNutritionalFact.vitamin_b = resultSet.getInt("vitamin_b");
                vendorNutritionalFact.vitamin_c = resultSet.getInt("vitamin_c");
                vendorNutritionalFact.vitamin_d = resultSet.getInt("vitamin_d");
                vendorNutritionalFact.calcium = resultSet.getInt("calcium");
                vendorNutritionalFact.iron = resultSet.getInt("iron");
                vendorNutritionalFact.protein = resultSet.getInt("protein");
                vendorNutritionalFact.profile_name = resultSet.getString("profile_name");
                vendorNutritionalFact.creation_timestamp = resultSet.getString("creation_timestamp");
                vendorNutritionalFact.creation_days_ago = resultSet.getString("creation_days_ago");
                vendorNutritionalFactHashMap.put(vendorNutritionalFact.id, vendorNutritionalFact);
            }
            return vendorNutritionalFactHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load nutritional facts");
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

    /**
    Things in python were calling entire menus when they just needed dropdowns.
    This will save bandwidth.
     */
    public VendorDropdownContainer loadVendorDropdowns(
            String cookie
    ) throws Exception {
        try {
            // Validate vendor cookie.
            this.validateVendorCookie(cookie);
            // Return dropdowns from abstract model.
            return this.getVendorDropdowns(this.vendorCookie.vendorID, this.DAO);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // I don't know.
            throw new Exception("Unable to get vendor dropdowns.");
        } finally {
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

}
