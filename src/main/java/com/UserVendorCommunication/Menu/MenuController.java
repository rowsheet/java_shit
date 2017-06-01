package com.UserVendorCommunication.Menu;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MenuController extends AbstractController {

    public int createBeerReview(
            String cookie,
            int beer_id,
            int stars,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(beer_id, "beer_id");
        this.validateStars(stars);
        this.validateText(content, "content");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.createBeerReview(
            cookie,
            beer_id,
            stars,
            content
        );
    }

    public int createFoodReview(
            String cookie,
            int vendor_food_id,
            int stars,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(vendor_food_id, "food_id");
        this.validateStars(stars);
        this.validateText(content, "content");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.createFoodReview(
                cookie,
                vendor_food_id,
                stars,
                content
        );
    }
}

