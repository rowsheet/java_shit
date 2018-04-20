package com.UserVendorCommunication.Menu;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class MenuController extends AbstractController {

    public int createBeerReview(
            String cookie,
            int beer_id,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(beer_id, "Beer ID");
        this.validateText(content, "Content");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.createBeerReview(
            cookie,
            beer_id,
            content
        );
    }

    public boolean deleteBeerReview(
            String cookie,
            int review_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteBeerReview(
                cookie,
                review_id
        );
    }

    public int createFoodReview(
            String cookie,
            int vendor_food_id,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(vendor_food_id, "Food ID");
        this.validateText(content, "Content");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.createFoodReview(
                cookie,
                vendor_food_id,
                content
        );
    }

    public boolean deleteFoodReview(
            String cookie,
            int review_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteFoodReview(
                cookie,
                review_id
        );
    }

    public int createDrinkReview(
            String cookie,
            int vendor_food_id,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(vendor_food_id, "drink_id");
        this.validateText(content, "content");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.createDrinkReview(
                cookie,
                vendor_food_id,
                content
        );
    }

    public boolean deleteDrinkReview(
            String cookie,
            int review_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        // Initialize model and create the data.
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteDrinkReview(
                cookie,
                review_id
        );
    }
}
