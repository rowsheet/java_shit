package com.VendorMenu.Beers;

import com.Common.AbstractModel;
import com.VendorAccounts.Limit.LimitException;
import com.VendorAccounts.Limit.LimitModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerModel extends AbstractModel {

    /*
    Confirm's beer owndership per vendor_id (From session table)
    and beer_id in request.
     */
    private String confirmBeerOwnershipSQL =
            "SELECT " +
                    "   b.id " +
                    "FROM" +
                    "   beers b " +
                    "LEFT JOIN " +
                    "   vendors v " +
                    "ON " +
                    "   b.vendor_id = v.id " +
                    "WHERE " +
                    "   b.id = ? " +
                    "AND " +
                    "   v.id = ? " +
                    "LIMIT 1";

    private String deleteBeerSQL_stage3 =
            "DELETE FROM " +
                    "   beers " +
                    "WHERE " +
                    "   id = ?";

    private String updateBeerSQL_stage3 =
            "UPDATE " +
                    "   beers " +
                    "SET" +
                    "   name = ?, " +
                    "   color = ?, " +
                    "   bitterness = ?, " +
                    "   abv = ?, " +
                    "   beer_style = ?::beer_style, " +
                    "   beer_tastes = ?::beer_taste[], " +
                    "   description = ?, " +
                    "   price = ?, " +
                    "   beer_sizes = ?::beer_size[], " +
                    "   hop_score = ?::hop_score," +
                    "   beer_category_id = ?," +
                    "   nutritional_facts_id = ?," +
                    "   tag_one = ?, " +
                    "   tag_two = ?, " +
                    "   tag_three = ?, " +
                    "   tag_four = ?, " +
                    "   tag_five = ? " +
                    "WHERE " +
                    "   id = ?";

    private String createBeerSQL =
            "INSERT INTO" +
                    "   beers" +
                    "(" +
                    "   vendor_id," +
                    "   feature_id," +
                    "   name," +
                    "   color," +
                    "   bitterness," +
                    "   abv," +
                    "   beer_style," +
                    "   beer_tastes," +
                    "   description," +
                    "   price," +
                    "   beer_sizes," +
                    "   hop_score, " +
                    "   beer_category_id," +
                    "   nutritional_facts_id, " +
                    "   tag_one, " +
                    "   tag_two, " +
                    "   tag_three, " +
                    "   tag_four, " +
                    "   tag_five " +
                    ") VALUES (" +
                    "?,?,?,?,?,?,?::beer_style,?::beer_taste[],?,?,?::beer_size[],?::hop_score,?,?,?,?,?,?,?)" +
                    "RETURNING id";

    public BeerModel() throws Exception {}

    /**
     * Deletes a beer with matching ID assuming beer is owned by user.
     *
     *      1) Validates session and permissions.
     *      2) Ensures resource ownership.
     *      3) Updates data.
     *
     * Do all three steps in a transaction and roll back on exception;
     *
     * @param cookie
     * @param id
     * @return boolean true on success or error.
     * @throws Exception
     */
    public boolean deleteBeer(
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
            this.validateCookieVendorFeature(cookie, "beer_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmBeerOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int beer_id = 0;
            while (stage2Result.next()) {
                beer_id = stage2Result.getInt("id");
            }
            if (beer_id == 0) {
                // Beer is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new BeerException("This account does not have have permission to delete this beer."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteBeerSQL_stage3);
            stage3.setInt(1, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // Unknown error.
            throw new Exception("Unable to delete beer.");
        } finally {
            /*
            Reset auto-commit to true.
             */
            this.DAO.setAutoCommit(true);
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

    /**
     * Updates beer as per request as long as session and user valid and beer is
     * ownded by user. Does three things:
     *
     *      1) Validates session and permissions.
     *      2) Ensures resource ownership.
     *      3) Updates data.
     *
     * Do all three steps in a transaction and roll back on exception.
     *
     * @param cookie
     * @param id
     * @param name
     * @param color
     * @param bitterness
     * @param abv
     * @param beer_style
     * @param beer_tastes
     * @param description
     * @param price
     * @param beer_sizes
     * @param hop_score
     * @return
     * @throws Exception
     */
    public boolean updateBeer(
        String cookie,
        int id,
        String name,
        int color,
        int bitterness,
        int abv,
        String beer_style,
        String[] beer_tastes,
        String description,
        float price,
        String[] beer_sizes,
        String hop_score,
        int beer_category_id,
        int nutritional_fact_id,
        int beer_tag_id_one,
        int beer_tag_id_two,
        int beer_tag_id_three,
        int beer_tag_id_four,
        int beer_tag_id_five
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
            this.validateCookieVendorFeature(cookie, "beer_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmBeerOwnershipSQL);
            stage2.setInt(1, id);
            stage2.setInt(2, this.vendorCookie.vendorID);
            stage2Result = stage2.executeQuery();
            int beer_id = 0;
            while (stage2Result.next()) {
                beer_id = stage2Result.getInt("id");
            }
            if (beer_id == 0) {
                // Beer is not owned by sender of this request. This might
                // be some malicious shit. Record it and blacklist.
                // @TODO blacklist this request.
                throw new BeerException("This account does not have have permission to change this beer."); // Fuck off
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateBeerSQL_stage3);
            stage3.setString(1, name);
            stage3.setInt(2, color);
            stage3.setInt(3, bitterness);
            stage3.setInt(4, abv);
            stage3.setString(5, beer_style);
            stage3.setArray(6, this.DAO.createArrayOf("beer_taste", beer_tastes));
            stage3.setString(7, description);
            stage3.setFloat(8, price);
            stage3.setArray(9, this.DAO.createArrayOf("beer_size", beer_sizes));
            stage3.setString(10, hop_score);
            stage3.setInt(11, beer_category_id);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (nutritional_fact_id == 0) {
                stage3.setNull(12, Types.INTEGER);
            } else {
                stage3.setInt(12, nutritional_fact_id);
            }
            if (beer_tag_id_one == 0) {
                stage3.setNull(13, Types.INTEGER);
            } else {
                stage3.setInt(13, beer_tag_id_one);
            }
            if (beer_tag_id_two == 0) {
                stage3.setNull(14, Types.INTEGER);
            } else {
                stage3.setInt(14, beer_tag_id_two);
            }
            if (beer_tag_id_three == 0) {
                stage3.setNull(15, Types.INTEGER);
            } else {
                stage3.setInt(15, beer_tag_id_three);
            }
            if (beer_tag_id_four == 0) {
                stage3.setNull(16, Types.INTEGER);
            } else {
                stage3.setInt(16, beer_tag_id_four);
            }
            if (beer_tag_id_five == 0) {
                stage3.setNull(17, Types.INTEGER);
            } else {
                stage3.setInt(17, beer_tag_id_five);
            }
            stage3.setInt(18, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            // Try to parse the exception message.
            if (ex.getMessage().contains("beer_beer_categories_fk")) {
                throw new Exception("Invalid beer category. Make sure you are the owner of that beer category_id.");
            }
            // Unknown error.
            throw new Exception("Unable to update beer.");
        } finally {
            /*
            Reset auto-commit to true.
             */
            this.DAO.setAutoCommit(true);
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

    public int createBeer(
            String cookie,
            String name,
            int color,
            int bitterness,
            int abv,
            String beer_style,
            String[] beer_tastes,
            String description,
            float price,
            String[] beer_sizes,
            String hop_score,
            int beer_category_id,
            int nutritional_fact_id,
            int beer_tag_id_one,
            int beer_tag_id_two,
            int beer_tag_id_three,
            int beer_tag_id_four,
            int beer_tag_id_five
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "beer_menu" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookieVendorFeature(cookie, "beer_menu");
            // Check limits.
            LimitModel limitModel = new LimitModel();
            limitModel.checkLimit(
                    this.vendorCookie.vendorID,
                    "beers",
                    "beer_limit"
            );
            // After we insert the record, we need to get the ID of the record back.
            preparedStatement = this.DAO.prepareStatement(this.createBeerSQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setInt(2, this.vendorCookie.requestFeatureID);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, color);
            preparedStatement.setInt(5, bitterness);
            preparedStatement.setInt(6, abv);
            preparedStatement.setString(7, beer_style);
            preparedStatement.setArray(8, this.DAO.createArrayOf("beer_taste", beer_tastes));
            preparedStatement.setString(9, description);
            preparedStatement.setFloat(10, price);
            preparedStatement.setArray(11, this.DAO.createArrayOf("beer_size", beer_sizes));
            preparedStatement.setString(12, hop_score);
            preparedStatement.setInt(13, beer_category_id);
            // Since the IDs are possible null, we will have to deal with zeros given for empty
            // entries by JAVA SOAP. I know this is ugly.
            if (nutritional_fact_id == 0) {
                preparedStatement.setNull(14, Types.INTEGER);
            } else {
                preparedStatement.setInt(14, nutritional_fact_id);
            }
            if (beer_tag_id_one == 0) {
                preparedStatement.setNull(15, Types.INTEGER);
            } else {
                preparedStatement.setInt(15, beer_tag_id_one);
            }
            if (beer_tag_id_two == 0) {
                preparedStatement.setNull(16, Types.INTEGER);
            } else {
                preparedStatement.setInt(16, beer_tag_id_two);
            }
            if (beer_tag_id_three == 0) {
                preparedStatement.setNull(17, Types.INTEGER);
            } else {
                preparedStatement.setInt(17, beer_tag_id_three);
            }
            if (beer_tag_id_four == 0) {
                preparedStatement.setNull(18, Types.INTEGER);
            } else {
                preparedStatement.setInt(18, beer_tag_id_four);
            }
            if (beer_tag_id_five == 0) {
                preparedStatement.setNull(19, Types.INTEGER);
            } else {
                preparedStatement.setInt(19, beer_tag_id_five);
            }
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_id = 0;
            while (resultSet.next()) {
                beer_id = resultSet.getInt("id");
            }
            if (beer_id == 0) {
                throw new BeerException("Unable to create beer.");
            }
            return beer_id;
            // Return unable exception if no id found.
        } catch (LimitException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Try to parse exception.
            if (ex.getMessage().contains("beers_vendor_id_name_idx") &&
                    ex.getMessage().contains("already exists")) {
                // Name for beer taken with this vendor_id (unique constraint).
                throw new Exception("This brewery already as a beer with that name!");
            }
            if (ex.getMessage().contains("beer_beer_categories_fk")) {
                throw new Exception("Invalid beer category. Make sure you are the owner of that beer category_id.");
            }
            throw new Exception("Unable to create beer."); // Unknown reason.
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

    private String createBeerCategorySQL =
            "INSERT INTO " +
                    "beer_categories (" +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color, " +
                    "   description" +
                    ") VALUES (?,?,?,?) " +
                    "RETURNING id";

    private String confirmBeerCategoryOwnershipSQL =
            "SELECT " +
                    "   vendor_id " +
                    "FROM " +
                    "   beer_categories " +
                    "WHERE " +
                    "   id = ?";

    private String updateBeerCategorySQL =
            "UPDATE beer_categories " +
                    "SET " +
                    "   name = ?, " +
                    "   hex_color = ?, " +
                    "   description = ? " +
                    "WHERE " +
                    "   id = ?";

    private String deleteBeerCategorySQL =
            "DELETE FROM beer_categories " +
                    "WHERE " +
                    "   id = ?";

    public int createBeerCategory(
            String cookie,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Validate cookie. Just check for the "beer_menu" permission. Beer categories
            // doesn't need to be it's own permission.
            this.validateCookieVendorFeature(cookie, "beer_menu");
            // Check limits.
            LimitModel limitModel = new LimitModel();
            limitModel.checkLimit(
                    this.vendorCookie.vendorID,
                    "beer_categories",
                    "beer_category_limit"
            );
            // Just insert the category returning the ID.
            preparedStatement = this.DAO.prepareStatement(this.createBeerCategorySQL);
            preparedStatement.setInt(1, this.vendorCookie.vendorID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, hex_color);
            if (description == null) {
                preparedStatement.setNull(4, Types.VARCHAR);
            } else {
                preparedStatement.setString(4, description);
            }
            resultSet = preparedStatement.executeQuery();
            // Get the new id of the new category.
            int beer_category_id = 0;
            while (resultSet.next()) {
                beer_category_id = resultSet.getInt("id");
            }
            if (beer_category_id == 0) {
                throw new BeerException("Unable to create beer.");
            }
            return beer_category_id;
        } catch (LimitException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // Try to parse exception message.
            if (ex.getMessage().contains("beer_categories_vendor_id_name_idx")) {
                throw new Exception("This account already has an beer category with that name!");
            }
            throw new Exception("Unable to create beer category.");
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

    public boolean updateBeerCategory(
            String cookie,
            int id,
            String category_name,
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
            Just validate off of the "beer_menu" permission.
             */
            this.validateCookieVendorFeature(cookie, "beer_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmBeerCategoryOwnershipSQL);
            stage2.setInt(1, id);
            stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new BeerException("This account does not have permission to update this beer category.");
            }
            /*
            Stage 3
            Update data.
             */
            stage3 = this.DAO.prepareStatement(this.updateBeerCategorySQL);
            stage3.setString(1, category_name);
            stage3.setString(2, hex_color);
            if (description == null) {
                stage3.setNull(3, Types.VARCHAR);
            } else {
                stage3.setString(3, description);
            }
            stage3.setInt(4, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("ROLLING BACK");
            this.DAO.rollback();
            throw new Exception("Unable to update beer category.");
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

    public boolean deleteBeerCategory(
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
            this.validateCookieVendorFeature(cookie, "beer_menu");
            /*
            Stage 2
            Ensure resource ownership.
             */
            stage2 = this.DAO.prepareStatement(this.confirmBeerCategoryOwnershipSQL);
            stage2.setInt(1, id);
            stage2Result = stage2.executeQuery();
            int vendor_id = 0;
            while (stage2Result.next()) {
                vendor_id = stage2Result.getInt("vendor_id");
            }
            if (vendor_id != this.vendorCookie.vendorID) {
                throw new BeerException("This account does not have permission to delete this beer category.");
            }
            /*
            Stage 3
            Delete data.
             */
            stage3 = this.DAO.prepareStatement(this.deleteBeerCategorySQL);
            stage3.setInt(1, id);
            stage3.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (BeerException ex) {
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
            throw new Exception("Unable to delete beer category.");
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

    private String verifyBeerOwnershipSQL =
            "SELECT " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   beers " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String verifyBeerImageOwnershipSQL =
            "SELECT " +
                    "   COUNT(*) as count_star " +
                    "FROM " +
                    "   beer_images " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String uploadBeerImageSQL_stage2 =
            "INSERT INTO " +
                    "   beer_images " +
                    "(" +
                    "   beer_id, " +
                    "   filename, " + // file_path, filename, same thing...
                    "   feature_id, " +
                    "   vendor_id" +
                    ") VALUES (?,?,?,?) " +
                    "RETURNING id";

    private String getBeerImageCountSQL =
            "SELECT " +
                    "   COUNT(*) AS count_star " +
                    "FROM " +
                    "   beer_images " +
                    "WHERE " +
                    "   beer_id = ?";

    private String deleteBeerImageSQL_stage2 =
            "DELETE FROM " +
                    "   beer_images " +
                    "WHERE " +
                    "   id = ?";

    public int uploadBeerImage(
            String cookie,
            String file_path,
            int beer_id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        PreparedStatement stage3 = null;
        ResultSet stage3Result = null;
        try {
            /*
            Validate feature permissions.
             */
            this.validateCookieVendorFeature(cookie, "vendor_beer_images");
            // Check limits.
            LimitModel limitModel = new LimitModel();
            limitModel.checkImageLimit(
                    this.vendorCookie.vendorID,
                    "beer_images",
                    "beer_image_limit",
                    "beer",
                    beer_id
            );
            /*
            Stage 1
            Validate Resource ownership.
             */
            stage1 = this.DAO.prepareStatement(this.verifyBeerOwnershipSQL);
            stage1.setInt(1, beer_id);
            stage1.setInt(2, this.vendorCookie.vendorID);
            stage1Result = stage1.executeQuery();
            int count_star = 0;
            while (stage1Result.next()) {
                count_star = stage1Result.getInt("count_star");
            }
            if (count_star == 0) {
                throw new BeerException("You do not have permission to upload photos for this beer.");
            }
            /*
            Stage 2
            Verify count of images for that beer.
            */
            stage2 = this.DAO.prepareStatement(this.getBeerImageCountSQL);
            stage2.setInt(1, beer_id);
            stage2Result = stage2.executeQuery();
            int image_count = 0;
            while (stage2Result.next()) {
                image_count = stage2Result.getInt("count_star");
            }
            if (image_count > this.vendor_beer_image_limit) {
                throw new BeerException("Maximum " + Integer.toString(this.vendor_beer_image_limit) + " images per beer reached.");
            }
            /*
            Stage 3
            Insert new record.
             */
            stage3 = this.DAO.prepareStatement(this.uploadBeerImageSQL_stage2);
            stage3.setInt(1, beer_id);
            stage3.setString(2, file_path);
            stage3.setInt(3, this.vendorCookie.requestFeatureID);
            stage3.setInt(4, this.vendorCookie.vendorID);
            stage3Result = stage3.executeQuery();
            int image_id = 0;
            while (stage3Result.next()) {
                image_id = stage3Result.getInt("id");
            }
            if (image_id == 0) {
                throw new Exception("Unable to create beer image.");
            }
            /*
            Done.
             */
            return image_id;
        } catch (LimitException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Try to parse the exception.
            if (ex.getMessage().contains("beer_images_beer_id_filename_idx")) {
                throw new Exception("This beer already has an image with that name.");
            }
            // Unknown reason.
            throw new Exception("Unable to upload beer image.");
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
                stage2Result.close();
            }
            if (stage3 != null) {
                stage3.close();
            }
            if (stage3Result != null) {
                stage3Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String updateBeerImageSQL_stage1 =
            "UPDATE " +
                    "   beer_images " +
                    "SET " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ? " +
                    "RETURNING vendor_id";

    private String updateBeerImageSQL_stage2 =
            "UPDATE " +
                    "   beer_images " +
                    "SET " +
                    "   display_order = ? " +
                    "WHERE " +
                    "   id = ?";

    public boolean updateBeerImages(
            String cookie,
            int[] image_ids
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /*
            Validate cookie.
             */
            this.validateCookieVendorFeature(cookie, "vendor_beer_images");
            this.DAO.setAutoCommit(false);
            /*
            Ensure ownership of each resource while setting their values to negative
            to avoid collision on stage2.
             */
            int negative_display_order = -1;
            for (int i = 0; i < image_ids.length; i++) {
                preparedStatement = this.DAO.prepareStatement(this.updateBeerImageSQL_stage1);
                preparedStatement.setInt(1, negative_display_order);
                preparedStatement.setInt(2, image_ids[i]);
                resultSet = preparedStatement.executeQuery();
                int vendor_id = 0;
                while (resultSet.next()) {
                    vendor_id = resultSet.getInt("vendor_id");
                }
                if (vendor_id != this.vendorCookie.vendorID) {
                    throw new BeerException("You do not have permission to update this beer image.");
                }
                negative_display_order--;
            }
            /*
            Update the display order.
            This needs to be done in a transaction.
             */
            int display_order = 1;
            for (int i = 0; i < image_ids.length; i++) {
                preparedStatement = this.DAO.prepareStatement(this.updateBeerImageSQL_stage2);
                preparedStatement.setInt(1, display_order);
                preparedStatement.setInt(2, image_ids[i]);
                preparedStatement.execute();
                display_order++;
            }
            this.DAO.commit();
            /*
            Done.
             */
            return true;
        } catch (BeerException ex) {
            this.DAO.rollback();
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("ROLLING BACK");
            System.out.println(ex.getMessage());
            this.DAO.rollback();
            // Don't know why.
            throw new Exception("Unable to update beer images.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    public boolean deleteBeerImage (
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            /*
            Validate feature permissions.
             */
            this.validateCookieVendorFeature(cookie, "vendor_beer_images");
            /*
            Stage 1
            Validate Resource ownership.
             */
            stage1 = this.DAO.prepareStatement(this.verifyBeerImageOwnershipSQL);
            stage1.setInt(1, id);
            stage1.setInt(2, this.vendorCookie.vendorID);
            stage1Result = stage1.executeQuery();
            int count_star = 0;
            while (stage1Result.next()) {
                count_star = stage1Result.getInt("count_star");
            }
            if (count_star == 0) {
                throw new BeerException("You do not have permission to delete photos for this beer.");
            }
            /*
            Stage 2
            Delete record.
             */
            stage2 = this.DAO.prepareStatement(this.deleteBeerImageSQL_stage2);
            stage2.setInt(1, id);
            stage2.execute();
            /*
            Done.
             */
            return true;
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            // Unknown reason.
            throw new Exception("Unable to delete beer image.");
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

    private String validateBeerTagOwnershipSQL =
            "SELECT " +
                    "   id " +
                    "FROM " +
                    "   beer_tags " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   vendor_id = ?";

    private String createBeerTagSQL =
            "INSERT INTO " +
                    "beer_tags ( " +
                    "   vendor_id, " +
                    "   name, " +
                    "   hex_color," +
                    "   tag_type " +
                    ") VALUES (?,?,?,?::menu_item_tag_type) " +
                    "RETURNING id";

    private String updateBeerTagSQL =
            "UPDATE " +
                    "   beer_tags " +
                    "SET " +
                    "   name = ?, " +
                    "   hex_color = ?, " +
                    "   tag_type = ?::menu_item_tag_type " +
                    "WHERE " +
                    "   id = ? ";

    private String deleteBeerTagSQL =
            "DELETE FROM " +
                    "   beer_tags " +
                    "WHERE " +
                    "   id = ?";


    /**
     * 1) Validate cookie (use "beer_menu" permission).
     * 2) Create beer_tag.
     *
     * @param cookie
     * @param name
     * @param hex_color
     * @return
     * @throws Exception
     */
    public int createBeerTag (
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
            this.validateCookieVendorFeature(cookie, "beer_menu");
            // Check limits.
            LimitModel limitModel = new LimitModel();
            limitModel.checkLimit(
                    this.vendorCookie.vendorID,
                    "beer_tags",
                    "beer_tag_limit"
            );
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.createBeerTagSQL);
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
                throw new Exception("Unable to create new beer_tag.");
            }
            return id;
        } catch (LimitException ex) {
            System.out.print(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            // Try to parse exception.
            if (ex.getMessage().contains("beer_tags_vendor_id_name_idx")) {
                throw new Exception("A beer tag with that name already exists!");
            }
            // Don't know why.
            throw new Exception("Unable to create new beer_tag.");
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
     * 1) Validate cookie (use "beer_menu" permission).
     * 2) Update beer_tag
     *
     * @param cookie
     * @param id
     * @param new_name
     * @param new_hex_color
     * @param new_tag_type
     * @return
     * @throws Exception
     */
    public Boolean updateBeerTag(
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
            this.validateCookieVendorFeature(cookie, "beer_menu");
            validationPreparedStatement = this.DAO.prepareStatement(this.validateBeerTagOwnershipSQL);
            validationPreparedStatement.setInt(1, id);
            validationPreparedStatement.setInt(2, this.vendorCookie.vendorID);
            validationResultSet = validationPreparedStatement.executeQuery();
            int validation_id = 0;
            while (validationResultSet.next()) {
                validation_id = validationResultSet.getInt("id");
            }
            if (validation_id == 0) {
                throw new BeerException("This account does not have permission to update this beer_tag");
            }
            /*
            Stage 2
             */
            preparedStatement = this.DAO.prepareStatement(this.updateBeerTagSQL);
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
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to update beer_tag.");
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

    private String deleteBeerTagReferencesSQL =
            "UPDATE " +
                    "   beers " +
                    "SET " +
                    "   tag_one = (CASE WHEN tag_one = ? THEN NULL ELSE tag_one END)::INTEGER, " +
                    "   tag_two = (CASE WHEN tag_two = ? THEN NULL ELSE tag_two END)::INTEGER, " +
                    "   tag_three = (CASE WHEN tag_three = ? THEN NULL ELSE tag_three END)::INTEGER, " +
                    "   tag_four = (CASE WHEN tag_four = ? THEN NULL ELSE tag_four END)::INTEGER, " +
                    "   tag_five = (CASE WHEN tag_five = ? THEN NULL ELSE tag_five END)::INTEGER " +
                    "WHERE " +
                    "   tag_one = ? OR tag_two = ? OR tag_three = ? OR tag_four = ? OR tag_five = ?";

    private String deleteBeerIngredientsTagReferencesSQL =
            "UPDATE " +
                    "   beer_ingredients " +
                    "SET " +
                    "   tag_one = (CASE WHEN tag_one = ? THEN NULL ELSE tag_one END)::INTEGER, " +
                    "   tag_two = (CASE WHEN tag_two = ? THEN NULL ELSE tag_two END)::INTEGER, " +
                    "   tag_three = (CASE WHEN tag_three = ? THEN NULL ELSE tag_three END)::INTEGER, " +
                    "   tag_four = (CASE WHEN tag_four = ? THEN NULL ELSE tag_four END)::INTEGER, " +
                    "   tag_five = (CASE WHEN tag_five = ? THEN NULL ELSE tag_five END)::INTEGER " +
                    "WHERE " +
                    "   tag_one = ? OR tag_two = ? OR tag_three = ? OR tag_four = ? OR tag_five = ?";

    /**
     * Note: Tags are optional (default NULL) and vendor_id owned. There is a compound foreign
     * key constraint for vendor_id and tag_id with UPDATE CASCADE and DELETE NO ACTION.
     * If this tag exists anywhere it is referenced, the delete will fail, so we must first
     * delete all references of it (that are default NULL, five columns), then remove the tag,
     * all in a transaction.
     *
     * 1) Validate cookie (using "beer_menu" permission).
     * 2) Delete references.
     * 3) Delete beer_tag.
     *
     * @param cookie
     * @param id
     * @return
     * @throws Exception
     */

    public Boolean deleteBeerTag(
            String cookie,
            int id
    ) throws Exception {
        PreparedStatement validationPreparedStatement = null;
        ResultSet validationResultSet = null;
        PreparedStatement deleteBeerReferencesStatement = null;
        PreparedStatement deleteIngredientReferencesStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            this.DAO.setAutoCommit(false);
            /*
            Stage 1
             */
            this.validateCookieVendorFeature(cookie, "beer_menu");
            validationPreparedStatement = this.DAO.prepareStatement(this.validateBeerTagOwnershipSQL);
            validationPreparedStatement.setInt(1, id);
            validationPreparedStatement.setInt(2, this.vendorCookie.vendorID);
            validationResultSet = validationPreparedStatement.executeQuery();
            int validation_id = 0;
            while (validationResultSet.next()) {
                validation_id = validationResultSet.getInt("id");
            }
            if (validation_id == 0) {
                throw new BeerException("This account does not have permission to delete this beer_tag");
            }
            /*
            Stage 2
             */
            deleteBeerReferencesStatement = this.DAO.prepareStatement(this.deleteBeerTagReferencesSQL);
            deleteBeerReferencesStatement.setInt(1, id);
            deleteBeerReferencesStatement.setInt(2, id);
            deleteBeerReferencesStatement.setInt(3, id);
            deleteBeerReferencesStatement.setInt(4, id);
            deleteBeerReferencesStatement.setInt(5, id);
            deleteBeerReferencesStatement.setInt(6, id);
            deleteBeerReferencesStatement.setInt(7, id);
            deleteBeerReferencesStatement.setInt(8, id);
            deleteBeerReferencesStatement.setInt(9, id);
            deleteBeerReferencesStatement.setInt(10, id);
            deleteBeerReferencesStatement.execute();
            deleteIngredientReferencesStatement = this.DAO.prepareStatement(this.deleteBeerIngredientsTagReferencesSQL);
            deleteIngredientReferencesStatement.setInt(1, id);
            deleteIngredientReferencesStatement.setInt(2, id);
            deleteIngredientReferencesStatement.setInt(3, id);
            deleteIngredientReferencesStatement.setInt(4, id);
            deleteIngredientReferencesStatement.setInt(5, id);
            deleteIngredientReferencesStatement.setInt(6, id);
            deleteIngredientReferencesStatement.setInt(7, id);
            deleteIngredientReferencesStatement.setInt(8, id);
            deleteIngredientReferencesStatement.setInt(9, id);
            deleteIngredientReferencesStatement.setInt(10, id);
            deleteIngredientReferencesStatement.execute();
            /*
            Stage 3
             */
            preparedStatement = this.DAO.prepareStatement(this.deleteBeerTagSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            /*
            Done. Commit.
             */
            this.DAO.commit();
            return true;
        } catch (BeerException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete beer_tag.");
        } finally {
            if (validationPreparedStatement != null) {
                validationPreparedStatement.close();
            }
            if (validationResultSet != null) {
                validationResultSet.close();
            }
            if (deleteBeerReferencesStatement != null) {
                deleteBeerReferencesStatement.close();
            }
            if (deleteIngredientReferencesStatement != null) {
                deleteIngredientReferencesStatement.close();
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
