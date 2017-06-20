package com.PublicBrewery.Reviews;

import com.Common.AbstractController;
import com.Common.VendorReview;
import com.sun.org.apache.regexp.internal.RE;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/18/17.
 */
public class ReviewController extends AbstractController {
    public String loadBreweryReviews (
            int brewery_id,
            int limit,
            int offset
    ) throws Exception {
        this.validateID(brewery_id, "brewery_id");
        ReviewModel reviewModel = new ReviewModel();
        HashMap<Integer, VendorReview> vendorReviewHashMap = new HashMap<Integer, VendorReview>();
        vendorReviewHashMap = reviewModel.loadBreweryReviews(
                brewery_id,
                limit,
                offset
        );
        return this.returnJSON(vendorReviewHashMap);
    }
}
