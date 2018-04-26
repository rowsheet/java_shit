package com.UserVendorCommunication.Menu;

import com.Common.AbstractModel;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.*;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MenuModel extends AbstractModel {
    private String createBeerReviewSQL =
            "INSERT INTO" +
                    "   beer_reviews" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   beer_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?)" +
                    "RETURNING id";

    private String createFoodReviewSQL =
            "INSERT INTO" +
                    "   vendor_food_reviews" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   vendor_food_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?)" +
                    "RETURNING id";

    private String createDrinkReviewSQL =
            "INSERT INTO" +
                    "   vendor_drink_reviews" +
                    "(" +
                    "   account_id," +
                    "   user_permission_id," +
                    "   vendor_drink_id," +
                    "   content" +
                    ") VALUES (" +
                    "?,?,?,?)" +
                    "RETURNING id";

    public MenuModel() throws Exception {}

    public int createBeerReview(
            String cookie,
            int beer_id,
            String content
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the user request and make sure "beer_reviews" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookiePermission(cookie, "beer_reviews");
            preparedStatement = this.DAO.prepareStatement(this.createBeerReviewSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, this.userCookie.requestPermissionID);
            preparedStatement.setInt(3, beer_id);
            preparedStatement.setString(4, content);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_reivew_id = 0;
            while (resultSet.next()) {
                beer_reivew_id = resultSet.getInt("id");
            }
            if (beer_reivew_id == 0) {
                // Could create it. Don't know why...
                throw new MenuException("Unable to create new beer review.");
            }
            return beer_reivew_id;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("user_account_permissions")) {
                throw new Exception("You do not have permission to post beer reviews.");
            }
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("You've already created a review for this beer!");
            }
            // Unkonwn reason.
            throw new Exception("Unable to create beer review.");
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

    private String deleteBeerReview_sql =
            "DELETE FROM " +
                    "   beer_reviews " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    public boolean deleteBeerReview(
            String cookie,
            int review_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.deleteBeerReview_sql);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setInt(2, this.userCookie.userID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete beer review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public int createFoodReview(
            String cookie,
            int vendor_food_id,
            String content
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "food_reviews" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookiePermission(cookie, "vendor_food_review");
            preparedStatement = this.DAO.prepareStatement(this.createFoodReviewSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, this.userCookie.requestPermissionID);
            preparedStatement.setInt(3, vendor_food_id);
            preparedStatement.setString(4, content);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_reivew_id = 0;
            while (resultSet.next()) {
                beer_reivew_id = resultSet.getInt("id");
            }
            if (beer_reivew_id == 0) {
                // Could create it. Don't know why...
                throw new MenuException("Unable to create new food review.");
            }
            return beer_reivew_id;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("user_account_permissions")) {
                throw new Exception("You do not have permission to post food reviews.");
            }
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("You've already created a review for this food!");
            }
            throw new Exception("Unable to create food review.");
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

    private String deleteFoodReview_sql =
            "DELETE FROM " +
                    "   vendor_food_reviews " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    public boolean deleteFoodReview(
        String cookie,
        int review_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.deleteFoodReview_sql);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setInt(2, this.userCookie.userID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete food review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    public int createDrinkReview(
            String cookie,
            int vendor_drink_id,
            String content
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // We need to validate the vendor request and make sure "food_reviews" is one of the vendor features
            // and is in the cookie before we insert a new record.
            this.validateCookiePermission(cookie, "vendor_drink_review");
            preparedStatement = this.DAO.prepareStatement(this.createDrinkReviewSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, this.userCookie.requestPermissionID);
            preparedStatement.setInt(3, vendor_drink_id);
            preparedStatement.setString(4, content);
            resultSet = preparedStatement.executeQuery();
            // Get the id of the new entry.
            int beer_reivew_id = 0;
            while (resultSet.next()) {
                beer_reivew_id = resultSet.getInt("id");
            }
            if (beer_reivew_id == 0) {
                // Could create it. Don't know why...
                throw new MenuException("Unable to create new drink review.");
            }
            return beer_reivew_id;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("user_account_permissions")) {
                throw new Exception("You do not have permission to post food reviews.");
            }
            if (ex.getMessage().contains("already exists")) {
                throw new Exception("You've already created a review for this drink!");
            }
            throw new Exception("Unable to create drink review.");
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

    private String deleteDrinkReview_sql =
            "DELETE FROM " +
                    "   vendor_drink_reviews " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    public boolean deleteDrinkReview(
            String cookie,
            int review_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            preparedStatement = this.DAO.prepareStatement(this.deleteDrinkReview_sql);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setInt(2, this.userCookie.userID);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to delete drink review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    /*
    REVIEW IMAGES
     */

    private String uploadFoodReviewImageSQL_stage1 =
        "SELECT " +
                "   review_image_one, " +
                "   review_image_two, " +
                "   review_image_three, " +
                "   review_image_four, " +
                "   review_image_five " +
                "FROM " +
                "   vendor_food_reviews " +
                "WHERE " +
                "   id = ? " +
                "AND " +
                "   account_id = ?";

    private String uploadFoodReviewImageSQL_stage2 =
        "UPDATE " +
                "   vendor_food_reviews " +
                "SET " +
                "   review_image_<%vacant_image_number%> = ? " +
                "WHERE " +
                "   id = ? ";

    public int uploadFoodReviewImage(
            String cookie,
            int review_id,
            String filename
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.uploadFoodReviewImageSQL_stage1);
            stage1.setInt(1, review_id);
            stage1.setInt(2, this.userCookie.userID);
            stage1Result = stage1.executeQuery();
            // We want to insert into the first null value of one of five
            // possible values. If all five are taken, return exception
            // "already used five images".
            String vacant_image_number = "";
            int vacant_image_number_int = 0;
            boolean found = false;
            while (stage1Result.next()) {
                found = true;
                if (stage1Result.getString("review_image_one") == null) {
                    vacant_image_number = "one";
                    vacant_image_number_int = 1;
                } else if (stage1Result.getString("review_image_two") == null) {
                    vacant_image_number = "two";
                    vacant_image_number_int = 2;
                } else if (stage1Result.getString("review_image_three") == null) {
                    vacant_image_number = "three";
                    vacant_image_number_int = 3;
                } else if (stage1Result.getString("review_image_four") == null) {
                    vacant_image_number = "four";
                    vacant_image_number_int = 4;
                } else if (stage1Result.getString("review_image_five") == null) {
                    vacant_image_number = "five";
                    vacant_image_number_int = 5;
                } else {
                    throw new MenuException("You've already uploaded five images for this review.");
                }
            }
            if (!found) {
                throw new MenuException("Invalid review_id for image upload.");
            }
            // Modify the query to insert the correct vacant image number.
            this.uploadFoodReviewImageSQL_stage2 = this.uploadFoodReviewImageSQL_stage2.replace("<%vacant_image_number%>", vacant_image_number);
            stage2 = this.DAO.prepareStatement(this.uploadFoodReviewImageSQL_stage2);
            stage2.setString(1, filename);
            stage2.setInt(2, review_id);
            stage2.execute();
            return vacant_image_number_int; // Should not be zero, exception would be caught before then.
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteFoodReviewImage_sql =
        "UPDATE " +
                "   vendor_food_reviews " +
                "SET " +
                "   review_image_<%target_image_number%> = null " +
                "WHERE " +
                "   id = ? " +
                "AND " +
                "   account_id = ?";

    public boolean deleteFoodReviewImage(
            String cookie,
            int review_id,
            int image_number
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            String target_image_number = "";
            switch (image_number) {
                case 1:
                    target_image_number = "one";
                    break;
                case 2:
                    target_image_number = "two";
                    break;
                case 3:
                    target_image_number = "three";
                    break;
                case 4:
                    target_image_number = "four";
                    break;
                case 5:
                    target_image_number = "five";
                    break;
                default:
                    throw new MenuException("Invalid review image number.");
            }
            this.deleteFoodReviewImage_sql = this.deleteFoodReviewImage_sql.replace("<%target_image_number%>", target_image_number);
            preparedStatement = this.DAO.prepareStatement(this.deleteFoodReviewImage_sql);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setInt(2, this.userCookie.userID);
            preparedStatement.execute();
            return true;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete review image.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String uploadDrinkReviewImageSQL_stage1 =
            "SELECT " +
                    "   review_image_one, " +
                    "   review_image_two, " +
                    "   review_image_three, " +
                    "   review_image_four, " +
                    "   review_image_five " +
                    "FROM " +
                    "   vendor_drink_reviews " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    private String uploadDrinkReviewImageSQL_stage2 =
            "UPDATE " +
                    "   vendor_drink_reviews " +
                    "SET " +
                    "   review_image_<%vacant_image_number%> = ? " +
                    "WHERE " +
                    "   id = ? ";

    public int uploadDrinkReviewImage(
            String cookie,
            int review_id,
            String filename
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.uploadDrinkReviewImageSQL_stage1);
            stage1.setInt(1, review_id);
            stage1.setInt(2, this.userCookie.userID);
            stage1Result = stage1.executeQuery();
            // We want to insert into the first null value of one of five
            // possible values. If all five are taken, return exception
            // "already used five images".
            String vacant_image_number = "";
            int vacant_image_number_int = 0;
            boolean found = false;
            while (stage1Result.next()) {
                found = true;
                if (stage1Result.getString("review_image_one") == null) {
                    vacant_image_number = "one";
                    vacant_image_number_int = 1;
                } else if (stage1Result.getString("review_image_two") == null) {
                    vacant_image_number = "two";
                    vacant_image_number_int = 2;
                } else if (stage1Result.getString("review_image_three") == null) {
                    vacant_image_number = "three";
                    vacant_image_number_int = 3;
                } else if (stage1Result.getString("review_image_four") == null) {
                    vacant_image_number = "four";
                    vacant_image_number_int = 4;
                } else if (stage1Result.getString("review_image_five") == null) {
                    vacant_image_number = "five";
                    vacant_image_number_int = 5;
                } else {
                    throw new MenuException("You've already uploaded five images for this review.");
                }
            }
            if (!found) {
                throw new MenuException("Invalid review_id for image upload.");
            }
            // Modify the query to insert the correct vacant image number.
            this.uploadDrinkReviewImageSQL_stage2 = this.uploadDrinkReviewImageSQL_stage2.replace("<%vacant_image_number%>", vacant_image_number);
            stage2 = this.DAO.prepareStatement(this.uploadDrinkReviewImageSQL_stage2);
            stage2.setString(1, filename);
            stage2.setInt(2, review_id);
            stage2.execute();
            return vacant_image_number_int; // Should not be zero, exception would be caught before then.
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteDrinkReviewImage_sql =
            "UPDATE " +
                    "   vendor_drink_reviews " +
                    "SET " +
                    "   review_image_<%target_image_number%> = null " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    public boolean deleteDrinkReviewImage(
            String cookie,
            int review_id,
            int image_number
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            String target_image_number = "";
            switch (image_number) {
                case 1:
                    target_image_number = "one";
                    break;
                case 2:
                    target_image_number = "two";
                    break;
                case 3:
                    target_image_number = "three";
                    break;
                case 4:
                    target_image_number = "four";
                    break;
                case 5:
                    target_image_number = "five";
                    break;
                default:
                    throw new MenuException("Invalid review image number.");
            }
            this.deleteDrinkReviewImage_sql = this.deleteDrinkReviewImage_sql.replace("<%target_image_number%>", target_image_number);
            preparedStatement = this.DAO.prepareStatement(this.deleteDrinkReviewImage_sql);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setInt(2, this.userCookie.userID);
            preparedStatement.execute();
            return true;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete review image.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String uploadBeerReviewImageSQL_stage1 =
            "SELECT " +
                    "   review_image_one, " +
                    "   review_image_two, " +
                    "   review_image_three, " +
                    "   review_image_four, " +
                    "   review_image_five " +
                    "FROM " +
                    "   beer_reviews " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    private String uploadBeerReviewImageSQL_stage2 =
            "UPDATE " +
                    "   beer_reviews " +
                    "SET " +
                    "   review_image_<%vacant_image_number%> = ? " +
                    "WHERE " +
                    "   id = ? ";

    public int uploadBeerReviewImage(
            String cookie,
            int review_id,
            String filename
    ) throws Exception {
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        PreparedStatement stage2 = null;
        try {
            this.validateUserCookie(cookie);
            stage1 = this.DAO.prepareStatement(this.uploadBeerReviewImageSQL_stage1);
            stage1.setInt(1, review_id);
            stage1.setInt(2, this.userCookie.userID);
            stage1Result = stage1.executeQuery();
            // We want to insert into the first null value of one of five
            // possible values. If all five are taken, return exception
            // "already used five images".
            String vacant_image_number = "";
            int vacant_image_number_int = 0;
            boolean found = false;
            while (stage1Result.next()) {
                found = true;
                if (stage1Result.getString("review_image_one") == null) {
                    vacant_image_number = "one";
                    vacant_image_number_int = 1;
                } else if (stage1Result.getString("review_image_two") == null) {
                    vacant_image_number = "two";
                    vacant_image_number_int = 2;
                } else if (stage1Result.getString("review_image_three") == null) {
                    vacant_image_number = "three";
                    vacant_image_number_int = 3;
                } else if (stage1Result.getString("review_image_four") == null) {
                    vacant_image_number = "four";
                    vacant_image_number_int = 4;
                } else if (stage1Result.getString("review_image_five") == null) {
                    vacant_image_number = "five";
                    vacant_image_number_int = 5;
                } else {
                    throw new MenuException("You've already uploaded five images for this review.");
                }
            }
            if (!found) {
                throw new MenuException("Invalid review_id for image upload.");
            }
            // Modify the query to insert the correct vacant image number.
            this.uploadBeerReviewImageSQL_stage2 = this.uploadBeerReviewImageSQL_stage2.replace("<%vacant_image_number%>", vacant_image_number);
            stage2 = this.DAO.prepareStatement(this.uploadBeerReviewImageSQL_stage2);
            stage2.setString(1, filename);
            stage2.setInt(2, review_id);
            stage2.execute();
            return vacant_image_number_int; // Should not be zero, exception would be caught before then.
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
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
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteBeerReviewImage_sql =
            "UPDATE " +
                    "   beer_reviews " +
                    "SET " +
                    "   review_image_<%target_image_number%> = null " +
                    "WHERE " +
                    "   id = ? " +
                    "AND " +
                    "   account_id = ?";

    public boolean deleteBeerReviewImage(
            String cookie,
            int review_id,
            int image_number
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            this.validateUserCookie(cookie);
            String target_image_number = "";
            switch (image_number) {
                case 1:
                    target_image_number = "one";
                    break;
                case 2:
                    target_image_number = "two";
                    break;
                case 3:
                    target_image_number = "three";
                    break;
                case 4:
                    target_image_number = "four";
                    break;
                case 5:
                    target_image_number = "five";
                    break;
                default:
                    throw new MenuException("Invalid review image number.");
            }
            this.deleteBeerReviewImage_sql = this.deleteBeerReviewImage_sql.replace("<%target_image_number%>", target_image_number);
            preparedStatement = this.DAO.prepareStatement(this.deleteBeerReviewImage_sql);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setInt(2, this.userCookie.userID);
            preparedStatement.execute();
            return true;
        } catch (MenuException ex) {
            System.out.println(ex.getMessage());
            // This needs to bubble up.
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to delete review image.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String createReviewFavoriteSQL =
        "INSERT INTO " +
                "   <%target_table%> " +
                "(" +
                "   account_id, " +
                "   review_id " +
                ") VALUES (?,?)";

    public boolean createReviewFavorite (
            String cookie,
            int review_id,
            String resource
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            String table = "";
            if (resource.equals("food")) {
                table = "vendor_food_review_favorites";
            } else if (resource.equals("drink")) {
                table = "vendor_drink_review_favorites";
            } else if (resource.equals("beer")) {
                table = "beer_review_favorites";
            } else {
                throw new Exception("Unknown review resource.");
            }
            this.validateUserCookie(cookie);
            this.createReviewFavoriteSQL = this.createReviewFavoriteSQL.replace("<%target_table%>", table);
            preparedStatement = this.DAO.prepareStatement(this.createReviewFavoriteSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, review_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            if (
                    ex.getMessage().contains("beer_review_favorites_account_id_review_id_idx") ||
                    ex.getMessage().contains("vendor_food_review_favorites_account_id_review_id_idx") ||
                    ex.getMessage().contains("vendor_drink_review_favorites_account_id_review_id_idx")
                    ) {
                // Already added, just return true.
                return true;
            }
            System.out.println(ex.getMessage());
            throw new Exception("Unable to favorite review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String deleteReviewFavoriteSQL =
            "DELETE FROM " +
                    "   <%target_table%> " +
                    "WHERE " +
                    "   account_id = ? " +
                    "AND " +
                    "   review_id = ?";

    public boolean deleteReviewFavorite (
            String cookie,
            int review_id,
            String resource
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            String table = "";
            if (resource.equals("food")) {
                table = "vendor_food_review_favorites";
            } else if (resource.equals("drink")) {
                table = "vendor_drink_review_favorites";
            } else if (resource.equals("beer")) {
                table = "beer_review_favorites";
            } else {
                throw new Exception("Unknown review resource.");
            }
            this.validateUserCookie(cookie);
            this.deleteReviewFavoriteSQL = this.deleteReviewFavoriteSQL.replace("<%target_table%>", table);
            preparedStatement = this.DAO.prepareStatement(this.deleteReviewFavoriteSQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, review_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to un-favorite review.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String createReviewReplySQL =
            "INSERT INTO " +
                    "   <%target_table%> " +
                    "(" +
                    "   account_id, " +
                    "   review_id, " +
                    "   content" +
                    ") VALUES (?,?,?) " +
                    "RETURNING id";

    public int createReviewReply (
            String cookie,
            int review_id,
            String content,
            String resource
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String table = "";
            if (resource.equals("food")) {
                table = "vendor_food_review_replies";
            } else if (resource.equals("drink")) {
                table = "vendor_drink_review_replies";
            } else if (resource.equals("beer")) {
                table = "beer_review_replies";
            } else {
                throw new Exception("Unknown review resource.");
            }
            this.validateUserCookie(cookie);
            this.createReviewReplySQL = this.createReviewReplySQL.replace("<%target_table%>", table);
            preparedStatement = this.DAO.prepareStatement(this.createReviewReplySQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, review_id);
            preparedStatement.setString(3, content);
            resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            if (id == 0) {
                throw new Exception("Unable to add review reply");
            }
            return id;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (
                    ex.getMessage().contains("vendor_food_review_replies_account_id_review_id_idx") ||
                    ex.getMessage().contains("vendor_drink_review_replies_accont_id_review_id_idx") ||
                    ex.getMessage().contains("beer_review_replies_account_id_review_id_idx")
                    ) {
                throw new Exception("You already created a reply for this review.");
            }
            throw new Exception("Unable to add review reply.");
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

    private String deleteReviewReplySQL =
            "DELETE FROM " +
                    "   <%target_table%> " +
                    "WHERE " +
                    "   account_id = ? " +
                    "AND " +
                    "   id = ?";

    public boolean deleteReviewReply (
            String cookie,
            int reply_id,
            String resource
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            String table = "";
            if (resource.equals("food")) {
                table = "vendor_food_review_replies";
            } else if (resource.equals("drink")) {
                table = "vendor_drink_review_replies";
            } else if (resource.equals("beer")) {
                table = "beer_review_replies";
            } else {
                throw new Exception("Unknown review resource.");
            }
            this.validateUserCookie(cookie);
            this.deleteReviewReplySQL = this.deleteReviewReplySQL.replace("<%target_table%>", table);
            preparedStatement = this.DAO.prepareStatement(this.deleteReviewReplySQL);
            preparedStatement.setInt(1, this.userCookie.userID);
            preparedStatement.setInt(2, reply_id);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to remove review reply.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String flagVendorReviewSQL =
            "INSERT INTO " +
                    "   flagged_vendor_reviews " +
                    "(" +
                    "   review_id, " +
                    "   resource, " +
                    "   why," +
                    "   ip_address " +
                    ") VALUES (?,?,?,?)";

    public boolean flagVendorReview(
            int review_id,
            String resource,
            String why,
            String ip_address
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.DAO.prepareStatement(flagVendorReviewSQL);
            preparedStatement.setInt(1, review_id);
            preparedStatement.setString(2, resource);
            preparedStatement.setString(3, why);
            preparedStatement.setString(4, ip_address);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }
}

