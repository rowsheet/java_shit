package com.PublicBrewery.Reviews;

import com.Common.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/18/17.
 */
public class ReviewModel extends AbstractModel {

    private String loadBreweryReviewsSQL_stage1 =
            "SELECT " +
                    "   vr.id AS review_id, " +
                    "   vr.account_id, " +
                    "   vr.vendor_id, " +
                    "   vr.stars, " +
                    "   vr.content, " +
                    // Create the username where it's null.
                    "   COALESCE(a.username, concat('user'::text, TRIM(LEADING FROM to_char(a.id, '999999999'))::text)) as username," +
                    // Calculate how many days ago the post was posted.
                    "   DATE_PART('day', now()::date) - DATE_PART('day', vr.creation_date::date) as days_ago " +
                    "FROM " +
                    "   vendor_reviews vr " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   vr.account_id = a.id " +
                    "WHERE " +
                    "   vr.vendor_id = ? " +
                    "ORDER BY " +
                    "   vr.creation_date DESC " +
                    "LIMIT ? OFFSET ?";

    public ReviewModel() throws Exception {}

    /**
     * Load the reviews for this vendor with an offset and limit as
     * a hash_map of vendor reviews keyed by their review_id.
     * @param brewery_id
     * @param offset
     * @return
     * @throws Exception
     */
    public HashMap<Integer, VendorReview> loadBreweryReviews(
            int brewery_id,
            int limit,
            int offset
    ) throws Exception {
        HashMap<Integer, VendorReview> vendorReviewHashMap = new HashMap<Integer, VendorReview>();
        PreparedStatement stage1 = null;
        ResultSet stage1Result = null;
        try {
            /*
            Stage 1
             */
            stage1 = this.DAO.prepareStatement(this.loadBreweryReviewsSQL_stage1);
            stage1.setInt(1, brewery_id);
            stage1.setInt(2, limit);
            stage1.setInt(3, offset);
            stage1Result = stage1.executeQuery();
            while (stage1Result.next()) {
                VendorReview vendorReview = new VendorReview();
                vendorReview.review_id = stage1Result.getInt("review_id");
                vendorReview.account_id = stage1Result.getInt("account_id");
                vendorReview.vendor_id = stage1Result.getInt("vendor_id");
                vendorReview.stars = stage1Result.getInt("stars");
                vendorReview.content = stage1Result.getString("content");
                vendorReview.username = stage1Result.getString("username");
                vendorReview.days_ago = stage1Result.getInt("days_ago");
                vendorReviewHashMap.put(vendorReview.review_id, vendorReview);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception("Unable to fetch vendor reviews.");
        } finally {
            if (stage1 != null) {
                stage1.close();
            }
            if (stage1Result != null) {
                stage1Result.close();
            }
        }
        return vendorReviewHashMap;
    }

    private String loadVendorFoodReviews_sql =
            "SELECT " +
                    "   vfr.id AS review_id, " +
                    "   vfr.account_id, " +
                    "   vfr.vendor_food_id, " +
                    "   vfr.content, " +
                    "   DATE_PART('day', now()::date) - DATE_PART('day', vfr.creation_timestamp::date) as days_ago, " +
                    "   a.username, " +
                    "   ai.first_name, " +
                    "   ai.last_name, " +
                    "   a.id AS account_id, " +
                    "   vfr.review_image_one, " +
                    "   vfr.review_image_two, " +
                    "   vfr.review_image_three, " +
                    "   vfr.review_image_four, " +
                    "   vfr.review_image_five " +
                    "FROM " +
                    "   vendor_food_reviews vfr " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   vfr.account_id = a.id " +
                    "LEFT JOIN " +
                    "   user_account_info ai " +
                    "ON " +
                    "   a.id = ai.account_id " +
                    "WHERE " +
                    "   vfr.vendor_food_id = ?" +
                    "ORDER BY " +
                    "   vfr.id " +
                    "ASC";

    private String loadVendorFoodReviewsSQL_stage2 =
            "SELECT DISTINCT " +
                    "   vfr.id, " +
                    "   COUNT(vfrf.*) AS count_star " +
                    "FROM " +
                    "   vendor_food_reviews vfr " +
                    "LEFT JOIN " +
                    "   vendor_food_review_favorites vfrf " +
                    "ON " +
                    "   vfr.id = vfrf.review_id " +
                    "WHERE " +
                    "   vfr.vendor_food_id = ? " +
                    "GROUP BY 1";

    public HashMap<Integer, VendorFoodReview> loadVendorFoodReviews(
            int food_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        try {
            preparedStatement = this.DAO.prepareStatement(this.loadVendorFoodReviews_sql);
            preparedStatement.setInt(1, food_id);
            resultSet = preparedStatement.executeQuery();
            HashMap<Integer, VendorFoodReview> vendorFoodReviewHashMap = new HashMap<Integer, VendorFoodReview>();
            while (resultSet.next()) {
                VendorFoodReview vendorFoodReview = new VendorFoodReview();
                vendorFoodReview.review_id =  resultSet.getInt("review_id");
                vendorFoodReview.account_id = resultSet.getInt("account_id");
                vendorFoodReview.first_name = resultSet.getString("first_name");
                vendorFoodReview.last_name = resultSet.getString("last_name");
                if (vendorFoodReview.first_name.equals("Enter First Name")) {
                    vendorFoodReview.first_name = "Anonymous";
                }
                if (vendorFoodReview.last_name.equals("Enter Last Name")) {
                    vendorFoodReview.last_name = "";
                }
                vendorFoodReview.content = resultSet.getString("content");
                vendorFoodReview.vendor_food_id = resultSet.getShort("vendor_food_id");
                vendorFoodReview.days_ago = resultSet.getShort("days_ago");
                vendorFoodReview.review_image_one = resultSet.getString("review_image_one");
                vendorFoodReview.review_image_two = resultSet.getString("review_image_two");
                vendorFoodReview.review_image_three = resultSet.getString("review_image_three");
                vendorFoodReview.review_image_four = resultSet.getString("review_image_four");
                vendorFoodReview.review_image_five = resultSet.getString("review_image_five");
                // Key by hash map.
                vendorFoodReviewHashMap.put(vendorFoodReview.review_id, vendorFoodReview);
            }
            stage2 = this.DAO.prepareStatement(this.loadVendorFoodReviewsSQL_stage2);
            stage2.setInt(1, food_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                int review_id = stage2Result.getInt("id");
                int count_star = stage2Result.getInt("count_star");
                vendorFoodReviewHashMap.get(review_id).total_favorites = count_star;
            }
            return vendorFoodReviewHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load vendor food reviews.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadVendorDrinkReviews_sql =
            "SELECT " +
                    "   vdr.id AS review_id, " +
                    "   vdr.account_id, " +
                    "   vdr.vendor_drink_id, " +
                    "   vdr.content, " +
                    "   DATE_PART('day', now()::date) - DATE_PART('day', vdr.creation_timestamp::date) as days_ago, " +
                    "   a.username, " +
                    "   ai.first_name, " +
                    "   ai.last_name, " +
                    "   a.id AS account_id, " +
                    "   vdr.review_image_one, " +
                    "   vdr.review_image_two, " +
                    "   vdr.review_image_three, " +
                    "   vdr.review_image_four, " +
                    "   vdr.review_image_five " +
                    "FROM " +
                    "   vendor_drink_reviews vdr " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   vdr.account_id = a.id " +
                    "LEFT JOIN " +
                    "   user_account_info ai " +
                    "ON " +
                    "   a.id = ai.account_id " +
                    "WHERE " +
                    "   vdr.vendor_drink_id = ?" +
                    "ORDER BY " +
                    "   vdr.id " +
                    "ASC";

    private String loadVendorDrinkReviewsSQL_stage2 =
            "SELECT DISTINCT " +
                    "   vdr.id, " +
                    "   COUNT(vdrf.*) AS count_star " +
                    "FROM " +
                    "   vendor_drink_reviews vdr " +
                    "LEFT JOIN " +
                    "   vendor_drink_review_favorites vdrf " +
                    "ON " +
                    "   vdr.id = vdrf.review_id " +
                    "WHERE " +
                    "   vdr.vendor_drink_id = ? " +
                    "GROUP BY 1";

    public HashMap<Integer, VendorDrinkReview> loadVendorDrinkReviews(
            int drink_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        try {
            preparedStatement = this.DAO.prepareStatement(this.loadVendorDrinkReviews_sql);
            preparedStatement.setInt(1, drink_id);
            resultSet = preparedStatement.executeQuery();
            HashMap<Integer, VendorDrinkReview> vendorDrinkReviewHashMap = new HashMap<Integer, VendorDrinkReview>();
            while (resultSet.next()) {
                VendorDrinkReview vendorDrinkReview = new VendorDrinkReview();
                vendorDrinkReview.review_id =  resultSet.getInt("review_id");
                vendorDrinkReview.account_id = resultSet.getInt("account_id");
                vendorDrinkReview.first_name = resultSet.getString("first_name");
                vendorDrinkReview.last_name = resultSet.getString("last_name");
                if (vendorDrinkReview.first_name.equals("Enter First Name")) {
                    vendorDrinkReview.first_name = "Anonymous";
                }
                if (vendorDrinkReview.last_name.equals("Enter Last Name")) {
                    vendorDrinkReview.last_name = "";
                }
                vendorDrinkReview.content = resultSet.getString("content");
                vendorDrinkReview.vendor_drink_id = resultSet.getShort("vendor_drink_id");
                vendorDrinkReview.days_ago = resultSet.getShort("days_ago");
                vendorDrinkReview.review_image_one = resultSet.getString("review_image_one");
                vendorDrinkReview.review_image_two = resultSet.getString("review_image_two");
                vendorDrinkReview.review_image_three = resultSet.getString("review_image_three");
                vendorDrinkReview.review_image_four = resultSet.getString("review_image_four");
                vendorDrinkReview.review_image_five = resultSet.getString("review_image_five");
                // Key by hash map.
                vendorDrinkReviewHashMap.put(vendorDrinkReview.review_id, vendorDrinkReview);
            }
            stage2 = this.DAO.prepareStatement(this.loadVendorDrinkReviewsSQL_stage2);
            stage2.setInt(1, drink_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                int review_id = stage2Result.getInt("id");
                int count_star = stage2Result.getInt("count_star");
                vendorDrinkReviewHashMap.get(review_id).total_favorites = count_star;
            }
            return vendorDrinkReviewHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load vendor drink reviews.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadBeerReviews_sql =
            "SELECT " +
                    "   br.id AS review_id, " +
                    "   br.account_id, " +
                    "   br.beer_id, " +
                    "   br.content, " +
                    "   DATE_PART('day', now()::date) - DATE_PART('day', br.creation_timestamp::date) as days_ago, " +
                    "   a.username, " +
                    "   ai.first_name, " +
                    "   ai.last_name, " +
                    "   a.id AS account_id, " +
                    "   br.review_image_one, " +
                    "   br.review_image_two, " +
                    "   br.review_image_three, " +
                    "   br.review_image_four, " +
                    "   br.review_image_five " +
                    "FROM " +
                    "   beer_reviews br " +
                    "LEFT JOIN " +
                    "   accounts a " +
                    "ON " +
                    "   br.account_id = a.id " +
                    "LEFT JOIN " +
                    "   user_account_info ai " +
                    "ON " +
                    "   a.id = ai.account_id " +
                    "WHERE " +
                    "   br.beer_id = ?" +
                    "ORDER BY " +
                    "   br.id " +
                    "ASC";

    private String loadVendorBeerReviewsSQL_stage2 =
            "SELECT DISTINCT " +
                    "   br.id, " +
                    "   COUNT(brf.*) AS count_star " +
                    "FROM " +
                    "   beer_reviews br " +
                    "LEFT JOIN " +
                    "   beer_review_favorites brf " +
                    "ON " +
                    "   br.id = brf.review_id " +
                    "WHERE " +
                    "   br.beer_id = ? " +
                    "GROUP BY 1";

    public HashMap<Integer, BeerReview> loadBeerReviews(
            int beer_id
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement stage2 = null;
        ResultSet stage2Result = null;
        try {
            preparedStatement = this.DAO.prepareStatement(this.loadBeerReviews_sql);
            preparedStatement.setInt(1, beer_id);
            resultSet = preparedStatement.executeQuery();
            HashMap<Integer, BeerReview> beerReviewHashMap = new HashMap<Integer, BeerReview>();
            while (resultSet.next()) {
                BeerReview beerReview = new BeerReview();
                beerReview.review_id =  resultSet.getInt("review_id");
                beerReview.account_id = resultSet.getInt("account_id");
                beerReview.first_name = resultSet.getString("first_name");
                beerReview.last_name = resultSet.getString("last_name");
                if (beerReview.first_name.equals("Enter First Name")) {
                    beerReview.first_name = "Anonymous";
                }
                if (beerReview.last_name.equals("Enter Last Name")) {
                    beerReview.last_name = "";
                }
                beerReview.content = resultSet.getString("content");
                beerReview.beer_id = resultSet.getShort("beer_id");
                beerReview.days_ago = resultSet.getShort("days_ago");
                beerReview.review_image_one = resultSet.getString("review_image_one");
                beerReview.review_image_two = resultSet.getString("review_image_two");
                beerReview.review_image_three = resultSet.getString("review_image_three");
                beerReview.review_image_four = resultSet.getString("review_image_four");
                beerReview.review_image_five = resultSet.getString("review_image_five");
                // Key by hash map.
                beerReviewHashMap.put(beerReview.review_id, beerReview);
            }
            stage2 = this.DAO.prepareStatement(this.loadVendorBeerReviewsSQL_stage2);
            stage2.setInt(1, beer_id);
            stage2Result = stage2.executeQuery();
            while (stage2Result.next()) {
                int review_id = stage2Result.getInt("id");
                int count_star = stage2Result.getInt("count_star");
                beerReviewHashMap.get(review_id).total_favorites = count_star;
            }
            return beerReviewHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // Don't know why.
            throw new Exception("Unable to load vendor beer reviews.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (stage2 != null) {
                stage2.close();
            }
            if (stage2Result != null) {
                stage2Result.close();
            }
            if (this.DAO != null) {
                this.DAO.close();
            }
        }
    }

    private String loadReviewRepliesSQL =
        "SELECT " +
                "   rt.id, " +
                "   rt.account_id, " +
                "   rt.content, " +
                "   DATE_PART('day', now()::date) - DATE_PART('day', rt.creation_timestamp::date) as days_ago, " +
                "   ai.first_name, " +
                "   ai.last_name " +
                "FROM " +
                "   <%target_table%> rt " +
                "LEFT JOIN " +
                "   user_account_info ai " +
                "ON " +
                "   rt.account_id = ai.account_id " +
                "WHERE " +
                "   rt.review_id = ?";

    HashMap<Integer, VendorReviewReply> loadReviewReplies(
            int review_id,
            String resource
    ) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String target_table = "";
            if (resource.equals("food")) {
                target_table = "vendor_food_review_replies";
            } else if (resource.equals("drink")) {
                target_table = "vendor_drink_review_replies";
            } else if (resource.equals("beer")) {
                target_table = "beer_review_replies";
            } else {
                throw new Exception("Unrecognized review reply resource.");
            }
            this.loadReviewRepliesSQL = this.loadReviewRepliesSQL.replace("<%target_table%>", target_table);
            preparedStatement = this.DAO.prepareStatement(this.loadReviewRepliesSQL);
            preparedStatement.setInt(1, review_id);
            resultSet = preparedStatement.executeQuery();
            HashMap<Integer, VendorReviewReply> reviewReplyHashMap = new HashMap<Integer, VendorReviewReply>();
            while (resultSet.next()) {
                VendorReviewReply vendorReviewReply = new VendorReviewReply();
                vendorReviewReply.id = resultSet.getInt("id");
                vendorReviewReply.root_review_id = review_id;
                vendorReviewReply.account_id = resultSet.getInt("account_id");
                vendorReviewReply.content = resultSet.getString("content");
                vendorReviewReply.days_ago = resultSet.getInt("days_ago");
                vendorReviewReply.first_name = resultSet.getString("first_name");
                vendorReviewReply.last_name = resultSet.getString("last_name");
                if (vendorReviewReply.first_name.equals("Enter First Name")) {
                    vendorReviewReply.first_name = "Anonymous";
                }
                if (vendorReviewReply.last_name.equals("Enter Last Name")) {
                    vendorReviewReply.last_name = "";
                }
                reviewReplyHashMap.put(vendorReviewReply.id, vendorReviewReply);
            }
            return reviewReplyHashMap;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Unable to load review replies.");
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
