package com.PublicBrewery.Reviews;

import com.Common.AbstractModel;
import com.Common.VendorReview;

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
}
