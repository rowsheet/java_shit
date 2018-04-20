package com.UserVendorCommunication.Reviews;

import com.Common.AbstractController;
import com.sun.org.apache.regexp.internal.RE;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class ReviewController extends AbstractController {

    public int createVendorReview(
            String cookie,
            int vendor_id,
            int stars,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(vendor_id, "vendor_id");
        this.validateStars(stars);
        this.validateText(content, "content");
        // Initialize model and create the data.
        ReviewModel reviewModel = new ReviewModel();
        return reviewModel.createVendorReview(
            cookie,
            vendor_id,
            stars,
            content
        );
    }
}

