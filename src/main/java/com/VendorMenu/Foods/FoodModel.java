package com.VendorMenu.Foods;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class FoodModel extends AbstractModel {

    private String deleteFoodSQL_stage3 =
            "DELETE FROM " +
                    "   vendor_foods " +
                    "WHERE " +
                    "   id = ?";

    private String confirmFoodOwnershipSQL =
            "SELECT " +
                    "   vf.id " +
                    "FROM " +
                    "   vendor_foods vf " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   vf.vendor_id = v.id " +
                    "WHERE " +
                    "   vf.id = ? " +
                    "AND " +
                    "   v.id = ? " +
                    "LIMIT 1";

    private String updateFoodSQL_stage3 =
            "UPDATE " +
                    "   vendor_foods " +
                    "SET " +
                    "   name = ?, " +
                    "   price = ?, " +
                    "   description = ?, " +
                    "   food_sizes = ?::food_size[] " +
                    "WHERE " +
                    "   id = ?";

    private String createFoodSQL =
            "INSERT INTO" +
                    "   vendor_foods" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name, " +
                    "   description," +
                    "   price," +
                    "   food_sizes" +
                    ") VALUES (" +
                    "?,?,?,?,?,?::food_size[])" +
                    "RETURNING id";

    public FoodModel() throws Exception {}

    /**
     * Deletes food item per request as long as session and user valid.
     *
     *      1) Validates session and permission.
     *      2) Ensures resource ownership.
     *      3) Deletes food.
     *
     * Do all in a transaction or roll back.
     *
     * @param cookie
     * @param id
     * @return true on success else exception.
     * @throws Exception
     */
    public boolean deleteFood (
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            /*
            Stage 2
            Ensure resource owndership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmFoodOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int food_id = 0;
            while (stage2Result.next()) {
                food_id = stage2Result.getInt("id");
            }
            if (food_id == 0) {
                // Food is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new FoodException("This account does not have have permission to delete this food item."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteFoodSQL_stage3);
            stage3.setInt(1, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Unknown reason.
            throw new Exception("Unable to delete food.");
        } finally {
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /**
     * Updates food item per request as long as sesion and user valid.
     *
     *      1) Validates session and permission.
     *      2) Ensures resource ownership.
     *      3) Updates data.
     *
     * Do all in a transaction or roll back.
     *
     * @param cookie
     * @param id
     * @param name
     * @param description
     * @param price
     * @param food_sizes
     * @return
     * @throws Exception
     */
    public boolean updateFood (
            String cookie,
            int id,
            String name,
            String description,
            float price,
            String[] food_sizes
    ) throws Exception {
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            /*
            Stage 2
            Ensure resource owndership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmFoodOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int food_id = 0;
            while (stage2Result.next()) {
                food_id = stage2Result.getInt("id");
            }
            if (food_id == 0) {
                // Food is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new FoodException("This account does not have have permission to change this food item."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateFoodSQL_stage3);
            stage3.setString(1, name);
            stage3.setFloat(2, price);
            stage3.setString(3, description);
            stage3.setArray(4, this.DAO.createArrayOf("food_size", food_sizes));
            stage3.setInt(5, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Unknown reason.
            throw new Exception("Unable to update food.");
        } finally {
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public int createFood(
            String cookie,
            String name,
            String description,
            float price,
            String[] food_sizes
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "food_menu" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookieVendorFeature(cookie, "food_menu");
            // After we insert the record, we need to get the ID of the record back.
            preparedStatement = this.DAO.prepareStatement(this.createFoodSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, this.vendorCookie.requestFeatureID);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, description);
            preparedStatement.setFloat(5, price);
            preparedStatement.setArray(6, this.DAO.createArrayOf("food_size", food_sizes));
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int food_id = 0;
            while (resultSet.next()) {
                food_id = resultSet.getInt("id");
            }
            if (food_id == 0) {
                throw new FoodException("Unable to create food."); // Unknown reason.
            }
            return food_id;
        } catch (FoodException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse the error message.
            if (ex.getMessage().contains("vendor_foods_vendor_id_name_idx") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This brewery already has a food item with that name!");
            }
            // Unknown reason.
            throw new Exception("Unable to create food.");
        } finally {
            // Clean up and return.
            if (preparedStatement != null) {
                preparedStatement = null;
            }
            if (resultSet != null) {
                resultSet = null;
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /*
    public String uploadVendorFoodImage (
            String cookie,
            String filename,
            int vendor_food_id
    ) throws Exception {
        return "something";
    }
    */

    private String verifyFoodOwnershipSQL =
            "SELECT " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   vendor_foods " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String uploadVendorFoodImageSQL_stage2 =
            "INSERT INTO " +
                    "   vendor_food_images" +
                    "(" +
                    "   vendor_food_id, " +
                    "   filename, " +
                    "   feature_id, " +
                    "   vendor_id" +
                    ") VALUES (" +
                    "?,?,?,?)";

    public String uploadVendorFoodImage(
            String cookie,
            String filename,
            int vendor_food_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Validate feature permissions.
             */
            this.validateCookieVendorFeature(cookie, "vendor_food_images");
            /*
            Stage 1
            Validate Resource ownership.
             */
            stage1 = this.DAO.prepareStatement(this.verifyFoodOwnershipSQL);
            stage1.setInt(1, vendor_food_id);
            stage1.setInt(2, this.vendorCookie.vendorID);
            stage1Result = stage1.executeQuery();
            int count_star = 0;
            while (stage1Result.next()) {
                count_star = stage1Result.getInt("count_star");
            }
            if (count_star == 0) {
                throw new FoodException("You do not have permission to upload photos for this food.");
            }
            /*
            Create filepath.
            right now, it's just going to be:
                vendor_id/vendor_food_id
             */
            String file_path = Integer.toString(vendorCookie.vendorID) + "/" +
                    Integer.toString(vendor_food_id) + "/" + filename;
            /*
            Stage 2
            Insert new record.
             */
            stage2 = this.DAO.prepareStatement(this.uploadVendorFoodImageSQL_stage2);
            stage2.setInt(1, vendor_food_id);
            stage2.setString(2, file_path);
            stage2.setInt(3, this.vendorCookie.requestFeatureID);
            stage2.setInt(4, this.vendorCookie.vendorID);
            stage2.execute();
            /*
            Done.
             */
            return file_path;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Try to parse the exception.
            if (ex.getMessage().contains("vendor_food_id") &&
                    ex.getMessage().contains("filename") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This food already has an image named: " + filename  + ".");
            }
            // Unknown reason.
            throw new Exception("Unable to upload food image.");
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

    public boolean updateVendorFoodImage (
            String cookie,
            int food_image_id,
            int display_order
    ) throws Exception {
        return true;
    }

    public String deleteVendorFoodImage (
            String cookie,
            int food_image_id
    ) throws Exception {
        return "something";
    }

}
