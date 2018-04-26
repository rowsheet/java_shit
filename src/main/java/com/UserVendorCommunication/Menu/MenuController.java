package com.UserVendorCommunication.Menu;

import com.Common.AbstractController;

import java.awt.*;

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

    /*
    REVIEW IMAGES
     */

    public int uploadFoodReviewImage(
            String cookie,
            int review_id,
            String filename
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        this.validateString(filename, "filename");
        MenuModel menuModel = new MenuModel();
        return menuModel.uploadFoodReviewImage(
                cookie,
                review_id,
                filename
        );
    }

    public boolean deleteFoodReviewImage(
            String cookie,
            int reivew_id,
            int image_number
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(image_number, "image_number");
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteFoodReviewImage(
                cookie,
                reivew_id,
                image_number
        );
    }

    public int uploadDrinkReviewImage(
            String cookie,
            int review_id,
            String filename
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        this.validateString(filename, "filename");
        MenuModel menuModel = new MenuModel();
        return menuModel.uploadDrinkReviewImage(
                cookie,
                review_id,
                filename
        );
    }

    public boolean deleteDrinkReviewImage(
            String cookie,
            int reivew_id,
            int image_number
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(image_number, "image_number");
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteDrinkReviewImage(
                cookie,
                reivew_id,
                image_number
        );
    }

    public int uploadBeerReviewImage(
            String cookie,
            int review_id,
            String filename
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        this.validateString(filename, "filename");
        MenuModel menuModel = new MenuModel();
        return menuModel.uploadBeerReviewImage(
                cookie,
                review_id,
                filename
        );
    }

    public boolean deleteBeerReviewImage(
            String cookie,
            int reivew_id,
            int image_number
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(image_number, "image_number");
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteBeerReviewImage(
                cookie,
                reivew_id,
                image_number
        );
    }

    /*
    REVIEW FAVORITES
     */

    public boolean createReviewFavorite (
            String cookie,
            int review_id,
            String resource
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        this.validateString(resource, "resource");
        if (
                (resource.equals("food")) &&
                (resource.equals("drink")) &&
                (resource.equals("beer"))) {
            throw new Exception("Invalid menu resource.");
        }
        MenuModel menuModel = new MenuModel();
        return menuModel.createReviewFavorite(
                cookie,
                review_id,
                resource
        );
    }

    public boolean deleteReviewFavorite (
            String cookie,
            int review_id,
            String resource
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        this.validateString(resource, "resource");
        if (
                (resource.equals("food")) &&
                        (resource.equals("drink")) &&
                        (resource.equals("beer"))) {
            throw new Exception("Invalid menu resource.");
        }
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteReviewFavorite(
                cookie,
                review_id,
                resource
        );
    }

    /*
    REVIEW REPLIES
     */
    public int createReviewReply (
            String cookie,
            int review_id,
            String content,
            String resource
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(review_id, "review_id");
        this.validateString(content, "content");
        this.validateString(resource, "resource");
        if (
                (resource.equals("food")) &&
                        (resource.equals("drink")) &&
                        (resource.equals("beer"))) {
            throw new Exception("Invalid menu resource.");
        }
        MenuModel menuModel = new MenuModel();
        return menuModel.createReviewReply(
                cookie,
                review_id,
                content,
                resource
        );
    }

    public boolean deleteReviewReply (
            String cookie,
            int reply_id,
            String resource
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(reply_id, "reply_id");
        this.validateString(resource, "resource");
        if (
                (resource.equals("food")) &&
                        (resource.equals("drink")) &&
                        (resource.equals("beer"))) {
            throw new Exception("Invalid menu resource.");
        }
        MenuModel menuModel = new MenuModel();
        return menuModel.deleteReviewReply(
                cookie,
                reply_id,
                resource
        );
    }

    public boolean flagVendorReview(
            int review_id,
            String resource,
            String why,
            String ip_address
    ) throws Exception {
        this.validateID(review_id, "review_id");
        this.validateString(resource, "resource");
        this.validateString(why, "reason");
        this.validateString(ip_address, "ip_address");
        MenuModel menuModel = new MenuModel();
        return menuModel.flagVendorReview(
                review_id,
                resource,
                why,
                ip_address
        );
    }
}
