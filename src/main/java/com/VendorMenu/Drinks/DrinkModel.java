package com.VendorMenu.Drinks;

import com.Common.AbstractModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Types;

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
                    "   hex_background = ?," +
                    "   drink_serve_temp = ?::drink_serve_temp," +
                    "   servings = ?::drink_servings," +
                    "   icon_enum = ?::vendor_drink_icon," +
                    "   nutritional_facts_id = ?," +
                    "   tag_one = ?, " +
                    "   tag_two = ?, " +
                    "   tag_three = ?, " +
                    "   tag_four = ?, " +
                    "   tag_five = ? " +
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
                    "   hex_background," +
                    "   drink_serve_temp, " +
                    "   servings," +
                    "   icon_enum," +
                    "   nutritional_facts_id, " +
                    "   tag_one, " +
                    "   tag_two, " +
                    "   tag_three, " +
                    "   tag_four, " +
                    "   tag_five " +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?,?,?,?,?::drink_serve_temp,?::drink_servings,?::vendor_drink_icon,?,?,?,?,?,?)" +
                    "RETURNING id";

    private String deleteSpiritDrinkAssociationSQL =
            "DELETE FROM " +
                    "   spirit_drink_associations " +
                    "WHERE " +
                    "   vendor_drink_id = ?";

    private String insertSpiritDrinkAssociationSQL =
            "INSERT INTO " +
                    "   spirit_drink_associations" +
                    "(" +
                    "   spirit_id, " +
                    "   vendor_drink_id" +
                    ") VALUES (?,?)";

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
     * NOTE: Updating drinks is as much of a bitch as creating them. Again (go to notes for "create" function),
     * there will have to be an array of prepared statements for all spirit-drink associations.
     *
     * Updates drink item per request as long as sesion and user valid.
     *
     *      1) Validates session and permission.
     *      2) Ensures resource ownership.
     *      3) Updates data in drink table.
     *      4) Delete all drink-spirit associations.
     *      5) Re-insert all drink-spirit associations.
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
            String hex_background,
            int[] spirit_ids,
            String drink_serve_temp,
            String servings,
            String icon_enum,
            int nutritional_fact_id,
            int drink_tag_id_one,
            int drink_tag_id_two,
            int drink_tag_id_three,
            int drink_tag_id_four,
            int drink_tag_id_five
    ) throws Exception {
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        PreparedStatement stage4 = null;
        // Create an array of prepared statements for each spirit_id association insert.
        ArrayList<PreparedStatement> spirit_id_statements = new ArrayList<PreparedStatement>();
        for (int i = 0; i < spirit_ids.length; i++) {
            PreparedStatement preparedStatement = null;
            spirit_id_statements.add(preparedStatement);
        }
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
            stage3.setString(9, drink_serve_temp);
            stage3.setString(10, servings);
            stage3.setString(11, icon_enum);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (nutritional_fact_id == 0) {
                stage3.setNull(12, Types.INTEGER);
            } else {
                stage3.setInt(12, nutritional_fact_id);
            }
            if (drink_tag_id_one == 0) {
                stage3.setNull(13, Types.INTEGER);
            } else {
                stage3.setInt(13, drink_tag_id_one);
            }
            if (drink_tag_id_two == 0) {
                stage3.setNull(14, Types.INTEGER);
            } else {
                stage3.setInt(14, drink_tag_id_two);
            }
            if (drink_tag_id_three == 0) {
                stage3.setNull(15, Types.INTEGER);
            } else {
                stage3.setInt(15, drink_tag_id_three);
            }
            if (drink_tag_id_four == 0) {
                stage3.setNull(16, Types.INTEGER);
            } else {
                stage3.setInt(16, drink_tag_id_four);
            }
            if (drink_tag_id_five == 0) {
                stage3.setNull(17, Types.INTEGER);
            } else {
                stage3.setInt(17, drink_tag_id_five);
            }
            stage3.setInt(18, id);
            stage3.execute();
            /*
            Stage 4
            Remove old spirit-drink associations.
             */
            stage4 = this.DAO.prepareStatement(this.deleteSpiritDrinkAssociationSQL);
            stage4.setInt(1, drink_id);
            stage4.execute();
            /*
            Stage 5
            Update table spirit associations for each spirit-id specified.
            */
            for (int i = 0; i < spirit_ids.length; i++) {
                PreparedStatement preparedStatement = spirit_id_statements.get(i);
                preparedStatement = this.DAO.prepareStatement(this.insertSpiritDrinkAssociationSQL);
                preparedStatement.setInt(1, spirit_ids[i]);
                preparedStatement.setInt(2, drink_id);
                preparedStatement.execute();
            }
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
            if (stage4 != null) {
                stage4.close();
            }
            for (int i = 0; i < spirit_id_statements.size(); i++) {
                if (spirit_id_statements.get(i) != null) {
                    spirit_id_statements.get(i).close();
                }
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /**
     * Creating and updating drinks are a bitch because alcoholic drinks have
     * a one-to-many relationship with spirits which are all stored on the
     * spirits-drinks association table.
     *
     * For every sprit_id that exists, a seperate query has to be made, so there
     * will also be an ARRAY of prepared statments made before the try-catch block
     * depending on this for that reason. Buckle up.
     *
     * @param cookie
     * @param name
     * @param description
     * @param price
     * @param drink_category_id
     * @param hex_one
     * @param hex_two
     * @param hex_three
     * @param hex_background
     * @param spirit_ids
     * @return
     * @throws Exception
     */

    public int createDrink(
            String cookie,
            String name,
            String description,
            float price,
            int drink_category_id,
            String hex_one,
            String hex_two,
            String hex_three,
            String hex_background,
            int[] spirit_ids,
            String drink_serve_temp,
            String servings,
            String icon_enum,
            int nutritional_fact_id,
            int drink_tag_id_one,
            int drink_tag_id_two,
            int drink_tag_id_three,
            int drink_tag_id_four,
            int drink_tag_id_five
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        // Create an array of prepared statements for each spirit_id association insert.
        ArrayList<PreparedStatement> spirit_id_statements = new ArrayList<PreparedStatement>();
        for (int i = 0; i < spirit_ids.length; i++) {
            PreparedStatement preparedStatement = null;
            spirit_id_statements.add(preparedStatement);
        }
        try {
            /*
            Disable auto-commit.
             */
            this.DAO.setAutoCommit(false);
            // We need to validate the vendor request and make sure "drink_menu" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookieVendorFeature(cookie, "drink_menu");
            // After we insert the record, we need to get the ID of the record back.
            /*
            Stage 1
            Update drink table.
             */
            stage1 = this.DAO.prepareStatement(this.createDrinkSQL);
            stage1.setInt(1, this.vendorCookie.vendorID);
            stage1.setInt(2, this.vendorCookie.requestFeatureID);
            stage1.setString(3, name);
            stage1.setString(4, description);
            stage1.setFloat(5, price);
            stage1.setInt(6, drink_category_id);
            stage1.setString(7, hex_one);
            stage1.setString(8, hex_two);
            stage1.setString(9, hex_three);
            stage1.setString(10, hex_background);
            stage1.setString(11, drink_serve_temp);
            stage1.setString(12, servings);
            stage1.setString(13, icon_enum);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (nutritional_fact_id == 0) {
                stage1.setNull(14, Types.INTEGER);
            } else {
                stage1.setInt(14, nutritional_fact_id);
            }
            if (drink_tag_id_one == 0) {
                stage1.setNull(15, Types.INTEGER);
            } else {
                stage1.setInt(15, drink_tag_id_one);
            }
            if (drink_tag_id_two == 0) {
                stage1.setNull(16, Types.INTEGER);
            } else {
                stage1.setInt(16, drink_tag_id_two);
            }
            if (drink_tag_id_three == 0) {
                stage1.setNull(17, Types.INTEGER);
            } else {
                stage1.setInt(17, drink_tag_id_three);
            }
            if (drink_tag_id_four == 0) {
                stage1.setNull(18, Types.INTEGER);
            } else {
                stage1.setInt(18, drink_tag_id_four);
            }
            if (drink_tag_id_five == 0) {
                stage1.setNull(19, Types.INTEGER);
            } else {
                stage1.setInt(19, drink_tag_id_five);
            }
            stage1Result = stage1.executeQuery();
            // Get the id of the new entry.
            int drink_id = 0;
            while (stage1Result.next()) {
                drink_id = stage1Result.getInt("id");
            }
            if (drink_id == 0) {
                throw new DrinkException("Unable to create drink."); // Unknown reason.
            }
            /*
            Stage 2
            Update table spirit associations for each spirit-id specified.
             */
            for (int i = 0; i < spirit_ids.length; i++) {
                PreparedStatement preparedStatement = spirit_id_statements.get(i);
                preparedStatement = this.DAO.prepareStatement(this.insertSpiritDrinkAssociationSQL);
                preparedStatement.setInt(1, spirit_ids[i]);
                preparedStatement.setInt(2, drink_id);
                preparedStatement.execute();
            }
            /*
            Done. Commit.
             */
            this.DAO.commit();
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
            if (ex.getMessage().contains("spirit_drink_associations_spirit_id_fkey")) {
                throw new Exception("Invalid alcohol type id.");
            }
            // Unknown reason.
            throw new Exception("Unable to create drink.");
        } finally {
            // Clean up and return.
            if (stage1 != null) {
                stage1 = null;
            }
            if (stage1Result != null) {
                stage1Result = null;
            }
            // Close all the prepared statements made from spirit_ids.
            for (int i = 0; i < spirit_id_statements.size(); i++) {
                if (spirit_id_statements.get(i) != null) {
                    spirit_id_statements.get(i).close();
                }
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
                    "   hex_color, " +
                    "   description " +
                    ") VALUES (?,?,?,?) " +
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
                    "   hex_color = ?, " +
                    "   description = ? " +
                    "WHERE " +
                    "   id = ?";

    private String deleteDrinkCategorySQL =
            "DELETE FROM vendor_drink_categories " +
                    "WHERE " +
                    "   id = ?";

    public int createDrinkCategory(
            String cookie,
            String category_name,
            String hex_color,
            String description
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
            preparedStatement.setString(4, description);
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
                throw new Exception("This account already has an drink category with that name!");
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
            String name,
            String hex_color,
            String description
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
            stage3.setString(1, name);
            stage3.setString(2, hex_color);
            stage3.setString(3, description);
            stage3.setInt(4, id);
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

    private String validateVendorDrinkTagOwnershipSQL =
            "SELECT " +
                    "   id " +
                    "FROM " +
                    "   vendor_drink_tags " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String createVendorDrinkTagSQL =
            "INSERT INTO " +
                    "vendor_drink_tags ( " +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type " +
                    ") VALUES (?,?,?,?::menu_item_tag_type) " +
                    "RETURNING id";

    private String updateVendorDrinkTagSQL =
            "UPDATE " +
                    "   vendor_drink_tags " +
                    "SET " +
                    "   name = ?, " +
                    "   hex_color = ?, " +
                    "   tag_type = ?::menu_item_tag_type " +
                    "WHERE " +
                    "   id = ? ";

    private String deleteVendorDrinkTagSQL =
            "DELETE FROM " +
                    "   vendor_drink_tags " +
                    "WHERE " +
                    "   id = ?";


    /**
     * 1) Validate cookie (use "drink_menu" permission).
     * 2) Create vendor_drink_tag.
     *
     * @param cookie
     * @param name
     * @param hex_color
     * @return
     * @throws Exception
     */
    public int createVendorDrinkTag (
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
            this.validateCookieVendorFeature(cookie, "drink_menu");
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.createVendorDrinkTagSQL);
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
                throw new Exception("Unable to create new vendor_drink_tag.");
            }
            return id;
        } catch (Exception ex) {
            // Try to parse exception.
            if (ex.getMessage().contains("vendor_drink_tags_vendor_id_name_idx")) {
                throw new Exception("A drink item tag with that name already exists!");
            }
            // Don't know why.
            throw new Exception("Unable to create new vendor_drink_tag.");
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
     * 1) Validate cookie (use "drink_menu" permission).
     * 2) Update vendor_drink_tag
     *
     * @param cookie
     * @param id
     * @param new_name
     * @param hex_color
     * @param new_tag_type
     * @return
     * @throws Exception
     */
    public Boolean updateVendorDrinkTag(
            String cookie,
            int id,
            String new_name,
            String hex_color,
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
            this.validateCookieVendorFeature(cookie, "drink_menu");
            validationPreparedStatement = this.DAO.prepareStatement(this.validateVendorDrinkTagOwnershipSQL);
            validationPreparedStatement.setInt(1, id);
            validationPreparedStatement.setInt(2, this.vendorCookie.vendorID);
            validationResultSet = validationPreparedStatement.executeQuery();
            int validation_id = 0;
            while (validationResultSet.next()) {
                validation_id = validationResultSet.getInt("id");
            }
            if (validation_id == 0) {
                throw new DrinkException("This account does not have permission to update this vendor_drink_tag");
            }
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.updateVendorDrinkTagSQL);
            preparedStatement.setString(1, new_name);
            preparedStatement.setString(2, hex_color);
            preparedStatement.setString(3, new_tag_type);
            preparedStatement.setInt(4, id);
            preparedStatement.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update vendor_drink_tag.");
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
     * 1) Validate cookie (using "drink_menu" permission).
     * 2) Delete vendor_drink_tag.
     *
     * @param cookie
     * @param id
     * @return
     * @throws Exception
     */

    public Boolean deleteVendorDrinkTag(
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
            this.validateCookieVendorFeature(cookie, "drink_menu");
            validationPreparedStatement = this.DAO.prepareStatement(this.validateVendorDrinkTagOwnershipSQL);
            validationPreparedStatement.setInt(1, id);
            validationPreparedStatement.setInt(2, this.vendorCookie.vendorID);
            validationResultSet = validationPreparedStatement.executeQuery();
            int validation_id = 0;
            while (validationResultSet.next()) {
                validation_id = validationResultSet.getInt("id");
            }
            if (validation_id == 0) {
                throw new DrinkException("This account does not have permission to delete this vendor_drink_tag");
            }
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.deleteVendorDrinkTagSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (DrinkException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete vendor_drink_tag.");
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
