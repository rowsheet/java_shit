package com.VendorMenu.Foods;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

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
                    "   food_sizes = ?::food_size[], " +
                    "   vendor_food_category_id = ?, " +
                    "   nutritional_facts_id = ?," +
                    "   tag_one = ?, " +
                    "   tag_two = ?, " +
                    "   tag_three = ?, " +
                    "   tag_four = ?, " +
                    "   tag_five = ? " +
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
                    "   food_sizes, " +
                    "   vendor_food_category_id, " +
                    "   nutritional_facts_id," +
                    "   tag_one, " +
                    "   tag_two, " +
                    "   tag_three, " +
                    "   tag_four, " +
                    "   tag_five " +
                    ") VALUES (" +
                    "?,?,?,?,?,?::food_size[],?,?,?,?,?,?,?)" +
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
            String[] food_sizes,
            int food_category_id,
            int nutritional_fact_id,
            int food_tag_id_one,
            int food_tag_id_two,
            int food_tag_id_three,
            int food_tag_id_four,
            int food_tag_id_five
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
            stage3.setInt(5, food_category_id);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (nutritional_fact_id == 0) {
                stage3.setNull(6, Types.INTEGER);
            } else {
                stage3.setInt(6, nutritional_fact_id);
            }
            if (food_tag_id_one == 0) {
                stage3.setNull( 7, Types.INTEGER);
            } else {
                stage3.setInt( 7, food_tag_id_one);
            }
            if (food_tag_id_two == 0) {
                stage3.setNull(8, Types.INTEGER);
            } else {
                stage3.setInt(8, food_tag_id_two);
            }
            if (food_tag_id_three == 0) {
                stage3.setNull(9, Types.INTEGER);
            } else {
                stage3.setInt(9, food_tag_id_three);
            }
            if (food_tag_id_four == 0) {
                stage3.setNull(10, Types.INTEGER);
            } else {
                stage3.setInt(10, food_tag_id_four);
            }
            if (food_tag_id_five == 0) {
                stage3.setNull(11, Types.INTEGER);
            } else {
                stage3.setInt(11, food_tag_id_five);
            }
            stage3.setInt(12, id);
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
            String[] food_sizes,
            int food_category_id,
            int nutritional_fact_id,
            int food_tag_id_one,
            int food_tag_id_two,
            int food_tag_id_three,
            int food_tag_id_four,
            int food_tag_id_five
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
            preparedStatement.setInt(7, food_category_id);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (nutritional_fact_id == 0) {
                preparedStatement.setNull(8, Types.INTEGER);
            } else {
                preparedStatement.setInt(8, nutritional_fact_id);
            }
            if (food_tag_id_one == 0) {
                preparedStatement.setNull(9, Types.INTEGER);
            } else {
                preparedStatement.setInt(9, food_tag_id_one);
            }
            if (food_tag_id_two == 0) {
                preparedStatement.setNull(10, Types.INTEGER);
            } else {
                preparedStatement.setInt(10, food_tag_id_two);
            }
            if (food_tag_id_three == 0) {
                preparedStatement.setNull(11, Types.INTEGER);
            } else {
                preparedStatement.setInt(11, food_tag_id_three);
            }
            if (food_tag_id_four == 0) {
                preparedStatement.setNull(12, Types.INTEGER);
            } else {
                preparedStatement.setInt(12, food_tag_id_four);
            }
            if (food_tag_id_five == 0) {
                preparedStatement.setNull(13, Types.INTEGER);
            } else {
                preparedStatement.setInt(13, food_tag_id_five);
            }
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
                throw new Exception("This account already has a food item with that name!");
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

    private String createFoodCategorySQL =
            "INSERT INTO " +
                    "vendor_food_categories (" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color" +
                    ") VALUES (?,?,?) " +
                    "RETURNING id";

    private String confirmFoodCategoryOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_food_categories " +
                    "WHERE " +
                    "   id = ?";

    private String updateFoodCategorySQL =
            "UPDATE vendor_food_categories " +
                    "SET " +
                    "   name = ?, " +
                    "   hex_color = ? " +
                    "WHERE " +
                    "   id = ?";

    private String deleteFoodCategorySQL =
            "DELETE FROM vendor_food_categories " +
                    "WHERE " +
                    "   id = ?";

    public int createFoodCategory(
            String cookie,
            String category_name,
            String hex_color
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie. Just check for the "food_menu" permission. Food categories
            // doesn't need to be it's own permission.
            this.validateCookieVendorFeature(cookie, "food_menu");
            // Just insert the category returning the ID.
            preparedStatement = this.DAO.prepareStatement(this.createFoodCategorySQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setString(2, category_name);
            preparedStatement.setString(3, hex_color);
            resultSet = preparedStatement.executeQuery();
            // Get the new id of the new category.
            int food_category_id = 0;
            while (resultSet.next()) {
                food_category_id = resultSet.getInt("id");
            }
            if (food_category_id == 0) {
                throw new FoodException("Unable to create food.");
            }
            return food_category_id;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // Try to parse exception message.
            if (ex.getMessage().contains("food_categories_vendor_id_name_idx")) {
                throw new Exception("This account already has an food with that name!");
            }
            throw new Exception("Unable to create food category.");
        } finally {
            // Clean up and return.
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

    public boolean updateFoodCategory(
            String cookie,
            int id,
            String new_category_name,
            String new_hex_color
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
            Just validate off of the "food_menu" permission.
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmFoodCategoryOwnershipSQL);
            stage2.setInt(1, id);
            stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new FoodException("This account does not have permission to update this food category.");
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateFoodCategorySQL);
            stage3.setString(1, new_category_name);
            stage3.setString(2, new_hex_color);
            stage3.setInt(3, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            throw new Exception("Unable to update food category.");
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

    public boolean deleteFoodCategory(
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        try {
            /*
            Disable auto commit.
             */
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
            Validate session.
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmFoodCategoryOwnershipSQL);
            stage2.setInt(1, id);
            stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new FoodException("This account does not have permission to delete this food category.");
            }
            /*
            Stage 3
            Delete data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteFoodCategorySQL);
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
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            /*
            Don't know what happened. Return unkonwn error.
             */
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            throw new Exception("Unable to delete food category.");
        } finally {
            /*
            Clean up.
             */
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

    private String validateVendorFoodTagOwnershipSQL =
            "SELECT " +
                    "   id " +
                    "FROM " +
                    "   vendor_food_tags " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String createVendorFoodTagSQL =
            "INSERT INTO " +
                    "vendor_food_tags ( " +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type " +
                    ") VALUES (?,?,?,?::menu_item_tag_type) " +
                    "RETURNING id";

    private String updateVendorFoodTagSQL =
            "UPDATE " +
                    "   vendor_food_tags " +
                    "SET " +
                    "   name = ?, " +
                    "   hex_color = ?, " +
                    "   tag_type = ?::menu_item_tag_type " +
                    "WHERE " +
                    "   id = ? ";

    private String deleteVendorFoodTagSQL =
            "DELETE FROM " +
                    "   vendor_food_tags " +
                    "WHERE " +
                    "   id = ?";


    /**
     * 1) Validate cookie (use "food_menu" permission).
     * 2) Create vendor_food_tag.
     *
     * @param cookie
     * @param name
     * @param hex_color
     * @return
     * @throws Exception
     */
    public int createVendorFoodTag (
            String cookie,
            String name,
            String hex_color,
            String tag_type
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /*
            Stage 1
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.createVendorFoodTagSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, hex_color);
            preparedStatement.setString(4, tag_type);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                // I don't really know why, but no non-zero ID returned, so...
                throw new Exception("Unable to create new vendor_food_tag.");
            }
            return id;
        } catch (Exception ex) {
            // Try to parse exception.
            if (ex.getMessage().contains("vendor_food_tags_vendor_id_name_idx")) {
                throw new Exception("A food item tag with that name already exists!");
            }
            // Don't know why.
            throw new Exception("Unable to create new vendor_food_tag.");
        } finally {
            if (preparedStatement !=  null) {
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
     * 1) Validate cookie (use "food_menu" permission).
     * 2) Update vendor_food_tag
     *
     * @param cookie
     * @param id
     * @param new_name
     * @param new_hex_color
     * @param new_tag_type
     * @return
     * @throws Exception
     */
    public Boolean updateVendorFoodTag(
            String cookie,
            int id,
            String new_name,
            String new_hex_color,
            String new_tag_type
    ) throws Exception {
        PreparedStatement validationPreparedStatement = null;
        ResultSet validationResultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            validationPreparedStatement = this.DAO.prepareStatement(this.validateVendorFoodTagOwnershipSQL);
            validationPreparedStatement.setInt(1, id);
            validationPreparedStatement.setInt(2, this.vendorCookie.vendorID);
            validationResultSet = validationPreparedStatement.executeQuery();
            int validation_id = 0;
            while (validationResultSet.next()) {
                validation_id = validationResultSet.getInt("id");
            }
            if (validation_id == 0) {
                throw new FoodException("This account does not have permission to update this vendor_food_tag");
            }
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.updateVendorFoodTagSQL);
            preparedStatement.setString(1, new_name);
            preparedStatement.setString(2, new_hex_color);
            preparedStatement.setString(3, new_tag_type);
            preparedStatement.setInt(4, id);
            preparedStatement.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update vendor_food_tag.");
        } finally {
            if (validationPreparedStatement != null) {
                validationPreparedStatement.close();
            }
            if (validationResultSet != null) {
                validationResultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /**
     * 1) Validate cookie (using "food_menu" permission).
     * 2) Delete vendor_food_tag.
     *
     * @param cookie
     * @param id
     * @return
     * @throws Exception
     */

    public Boolean deleteVendorFoodTag(
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement validationPreparedStatement = null;
        ResultSet validationResultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
             */
            this.validateCookieVendorFeature(cookie, "food_menu");
            validationPreparedStatement = this.DAO.prepareStatement(this.validateVendorFoodTagOwnershipSQL);
            validationPreparedStatement.setInt(1, id);
            validationPreparedStatement.setInt(2, this.vendorCookie.vendorID);
            validationResultSet = validationPreparedStatement.executeQuery();
            int validation_id = 0;
            while (validationResultSet.next()) {
                validation_id = validationResultSet.getInt("id");
            }
            if (validation_id == 0) {
                throw new FoodException("This account does not have permission to delete this vendor_food_tag");
            }
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.deleteVendorFoodTagSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (FoodException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete vendor_food_tag.");
        } finally {
            if (validationPreparedStatement != null) {
                validationPreparedStatement.close();
            }
            if (validationResultSet != null) {
                validationResultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

}
