package com.VendorMemberships.Promotions;

import com.Common.AbstractController;
import com.VendorMemberships.Promotions.PromotionModel;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class PromotionController extends AbstractController {

    public int createPromotion(
            String cookie,
            int membership_id,
            String title,
            String description,
            String start_date,
            String end_date,
            int max_accounts,
            String[] promotion_categories
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(title, "title");
        this.validateText(description, "description");
        this.validateTimestamp(start_date, "start_date");
        this.validateTimestamp(end_date, "end_date");
        for (String promtion_category: promotion_categories) {
            this.validatePromotionCategory(promtion_category);
        }
        // Initialize model and create the data.
        PromotionModel promotionModel = new PromotionModel();
        return promotionModel.createPromotion(
            cookie,
            membership_id,
            title,
            description,
            start_date,
            end_date,
            max_accounts,
            promotion_categories
        );
    }
}

