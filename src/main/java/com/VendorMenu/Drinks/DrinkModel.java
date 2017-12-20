package com.VendorMenu.Drinks;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DrinkModel extends AbstractModel {

    private String deleteDrinkSQL_stage3 =
            "DELETE FROM " +
                    "   vendor_drinks " +
                    "WHERE " +
                    "   id = ?";

    private String confirmDrinkOwnershipSQL =
            "SELECT " +
                    "   vd.id " +
                    "FROM " +
                    "   vendor_drinks vd " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   vd.vendor_id = v.id " +
                    "WHERE " +
                    "   vd.id = ? " +
                    "AND " +
                    "   v.id = ? " +
                    "LIMIT 1";

    private String updateDrinkSQL_stage3 =
            "UPDATE " +
                    "   vendor_drinks " +
                    "SET " +
                    "   name = ?, " +
                    "   price = ?, " +
                    "   description = ?, " +
                    "   vendor_drink_category_id = ?," +
                    "   hex_one = ?," +
                    "   hex_two = ?," +
                    "   hex_three = ?," +
                    "   hex_background = ? " +
                    "WHERE " +
                    "   id = ?";

    private String createDrinkSQL =
            "INSERT INTO" +
                    "   vendor_drinks" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name, " +
                    "   description," +
                    "   price," +
                    "   vendor_drink_category_id, " +
                    "   hex_one, " +
                    "   hex_two, " +
                    "   hex_three, " +
                    "   hex_background" +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?,?,?)" +
                    "RETURNING id";

    public DrinkModel() throws Exception {}

    /**
     * Deletes drink item per request as long as session and user valid.
     *
     *      1) Validates session and permission.
     *      2) Ensures resource ownership.
     *      3) Deletes drink.
     *
     * Do all in a transaction or roll back.
     *
     * @param cookie
     * @param id
     * @return true on success else exception.
     * @throws Exception
     */
    public boolean deleteDrink (
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
            this.validateCookieVendorFeature(cookie, "drink_menu");
            /*
            Stage 2
            Ensure resource owndership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmDrinkOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int drink_id = 0;
            while (stage2Result.next()) {
                drink_id = stage2Result.getInt("id");
            }
            if (drink_id == 0) {
                // Drink is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new DrinkException("This account does not have have permission to delete this drink item."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteDrinkSQL_stage3);
            stage3.setInt(1, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Unknown reason.
            throw new Exception("Unable to delete drink.");
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
     * Updates drink item per request as long as sesion and user valid.
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
     * @return
     * @throws Exception
     */
    public boolean updateDrink (
            String cookie,
            int id,
            String name,
            String description,
            float price,
            int drink_category_id,
            String hex_one,
            String hex_two,
            String hex_three,
            String hex_background
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
            this.validateCookieVendorFeature(cookie, "drink_menu");
            /*
            Stage 2
            Ensure resource owndership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmDrinkOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int drink_id = 0;
            while (stage2Result.next()) {
                drink_id = stage2Result.getInt("id");
            }
            if (drink_id == 0) {
                // Drink is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new DrinkException("This account does not have have permission to change this drink item."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateDrinkSQL_stage3);
            stage3.setString(1, name);
            stage3.setFloat(2, price);
            stage3.setString(3, description);
            stage3.setInt(4, drink_category_id);
            stage3.setString(5, hex_one);
            stage3.setString(6, hex_two);
            stage3.setString(7, hex_three);
            stage3.setString(8, hex_background);
            stage3.setInt(9, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Unknown reason.
            throw new Exception("Unable to update drink.");
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

    public int createDrink(
            String cookie,
            String name,
            String description,
            float price,
            int drink_category_id,
            String hex_one,
            String hex_two,
            String hex_three,
            String hex_background
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "drink_menu" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookieVendorFeature(cookie, "drink_menu");
            // After we insert the record, we need to get the ID of the record back.
            preparedStatement = this.DAO.prepareStatement(this.createDrinkSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, this.vendorCookie.requestFeatureID);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, description);
            preparedStatement.setFloat(5, price);
            preparedStatement.setInt(6, drink_category_id);
            preparedStatement.setString(7, hex_one);
            preparedStatement.setString(8, hex_two);
            preparedStatement.setString(9, hex_three);
            preparedStatement.setString(10, hex_background);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int drink_id = 0;
            while (resultSet.next()) {
                drink_id = resultSet.getInt("id");
            }
            if (drink_id == 0) {
                throw new DrinkException("Unable to create drink."); // Unknown reason.
            }
            return drink_id;
        } catch (DrinkException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Try to parse the error message.
            /*
            if (ex.getMessage().contains("vendor_drinks_vendor_id_name_idx") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This brewery already has a drink item with that name!");
            }
            */
            // Unknown reason.
            throw new Exception("Unable to create drink.");
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

    private String createDrinkCategorySQL =
            "INSERT INTO " +
                    "vendor_drink_categories (" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color" +
                    ") VALUES (?,?,?) " +
                    "RETURNING id";

    private String confirmDrinkCategoryOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   vendor_drink_categories " +
                    "WHERE " +
                    "   id = ?";

    private String updateDrinkCategorySQL =
            "UPDATE vendor_drink_categories " +
                    "SET " +
                    "   name = ?, " +
                    "   hex_color = ? " +
                    "WHERE " +
                    "   id = ?";

    private String deleteDrinkCategorySQL =
            "DELETE FROM vendor_drink_categories " +
                    "WHERE " +
                    "   id = ?";

    public int createDrinkCategory(
            String cookie,
            String category_name,
            String hex_color
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie. Just check for the "drink_menu" permission. Drink categories
            // doesn't need to be it's own permission.
            this.validateCookieVendorFeature(cookie, "drink_menu");
            // Just insert the category returning the ID.
            preparedStatement = this.DAO.prepareStatement(this.createDrinkCategorySQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setString(2, category_name);
            preparedStatement.setString(3, hex_color);
            resultSet = preparedStatement.executeQuery();
            // Get the new id of the new category.
            int drink_category_id = 0;
            while (resultSet.next()) {
                drink_category_id = resultSet.getInt("id");
            }
            if (drink_category_id == 0) {
                throw new DrinkException("Unable to create drink.");
            }
            return drink_category_id;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // Try to parse exception message.
            if (ex.getMessage().contains("drink_categories_vendor_id_name_idx")) {
                throw new Exception("This account already has an drink with that name!");
            }
            throw new Exception("Unable to create drink category.");
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

    public boolean updateDrinkCategory(
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
            Just validate off of the "drink_menu" permission.
             */
            this.validateCookieVendorFeature(cookie, "drink_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmDrinkCategoryOwnershipSQL);
            stage2.setInt(1, id);
            stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new DrinkException("This account does not have permission to update this drink category.");
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateDrinkCategorySQL);
            stage3.setString(1, new_category_name);
            stage3.setString(2, new_hex_color);
            stage3.setInt(3, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            throw new Exception("Unable to update drink category.");
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

    public boolean deleteDrinkCategory(
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
            this.validateCookieVendorFeature(cookie, "drink_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmDrinkCategoryOwnershipSQL);
            stage2.setInt(1, id);
            stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new DrinkException("This account does not have permission to delete this drink category.");
            }
            /*
            Stage 3
            Delete data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteDrinkCategorySQL);
            stage3.setInt(1, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (DrinkException ex) {
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
            throw new Exception("Unable to delete drink category.");
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

    private String verifyDrinkOwnershipSQL =
            "SELECT " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   vendor_drinks " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String uploadVendorDrinkImageSQL_stage2 =
            "INSERT INTO " +
                    "   vendor_drink_images" +
                    "(" +
                    "   vendor_drink_id, " +
                    "   filename, " +
                    "   feature_id, " +
                    "   vendor_id" +
                    ") VALUES (" +
                    "?,?,?,?)";

    public String uploadVendorDrinkImage(
            String cookie,
            String filename,
            int vendor_drink_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Validate feature permissions.
             */
            this.validateCookieVendorFeature(cookie, "vendor_drink_images");
            /*
            Stage 1
            Validate Resource ownership.
             */
            stage1 = this.DAO.prepareStatement(this.verifyDrinkOwnershipSQL);
            stage1.setInt(1, vendor_drink_id);
            stage1.setInt(2, this.vendorCookie.vendorID);
            stage1Result = stage1.executeQuery();
            int count_star = 0;
            while (stage1Result.next()) {
                count_star = stage1Result.getInt("count_star");
            }
            if (count_star == 0) {
                throw new DrinkException("You do not have permission to upload photos for this drink.");
            }
            /*
            Create filepath.
            right now, it's just going to be:
                vendor_id/vendor_drink_id
             */
            String file_path = Integer.toString(vendorCookie.vendorID) + "/" +
                    Integer.toString(vendor_drink_id) + "/" + filename;
            /*
            Stage 2
            Insert new record.
             */
            stage2 = this.DAO.prepareStatement(this.uploadVendorDrinkImageSQL_stage2);
            stage2.setInt(1, vendor_drink_id);
            stage2.setString(2, file_path);
            stage2.setInt(3, this.vendorCookie.requestFeatureID);
            stage2.setInt(4, this.vendorCookie.vendorID);
            stage2.execute();
            /*
            Done.
             */
            return file_path;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Try to parse the exception.
            if (ex.getMessage().contains("vendor_drink_id") &&
                    ex.getMessage().contains("filename") &&
                    ex.getMessage().contains("already exists")) {
                throw new Exception("This drink already has an image named: " + filename  + ".");
            }
            // Unknown reason.
            throw new Exception("Unable to upload drink image.");
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

    public boolean updateVendorDrinkImage (
            String cookie,
            int drink_image_id,
            int display_order
    ) throws Exception {
        return true;
    }

    public String deleteVendorDrinkImage (
            String cookie,
            int drink_image_id
    ) throws Exception {
        return "something";
    }
}
