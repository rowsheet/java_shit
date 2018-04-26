package com.UserVendorCommunication;

import com.UserVendorCommunication.Blogs.BlogController;
import com.UserVendorCommunication.Menu.MenuController;
import com.UserVendorCommunication.Menu.MenuModel;
import com.UserVendorCommunication.Reviews.ReviewController;
import com.UserVendorCommunication.Reviews.ReviewModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class UserVendorCommunicationHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    @WebMethod
    public int createDrinkReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_drink_id") int vendor_drink_id,
            @WebParam(name="content") String content
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createDrinkReview(
                cookie,
                vendor_drink_id,
                content
        );
    }

    @WebMethod
    public boolean deleteDrinkReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="review_id") int review_id
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteDrinkReview(
                cookie,
                review_id
        );
    }

    @WebMethod
    public int createBeerReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id,
            @WebParam(name="content") String content
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createBeerReview(
                cookie,
                beer_id,
                content
        );
    }

    @WebMethod
    public boolean deleteBeerReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="review_id") int review_id
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteBeerReview(
                cookie,
                review_id
        );
    }

    @WebMethod
    public int createFoodReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_food_id") int vendor_food_id,
            @WebParam(name="content") String content
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createFoodReview(
                cookie,
                vendor_food_id,
                content
        );
    }

    @WebMethod
    public boolean deleteFoodReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="review_id") int review_id
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteFoodReview(
                cookie,
                review_id
        );
    }

    @WebMethod
    public int createVendorReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_id") int vendor_id,
            @WebParam(name="stars") int stars,
            @WebParam(name="content") String content
    ) throws Exception {
        ReviewController reviewController = new ReviewController();
        return reviewController.createVendorReview(
                cookie,
                vendor_id,
                stars,
                content
        );
    }

    /*
    REVIEW IMAGES
     */
    // On review image upload:
    // Return vacant image number so it's easy to
    // delete on the failed upload step of the
    // web server.

    @WebMethod
    public int uploadFoodReviewImage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="food_review_id") int food_review_id,
            @WebParam(name="file_path") String file_path
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.uploadFoodReviewImage(
                cookie,
                food_review_id,
                file_path
        );
    }

    @WebMethod
    public boolean deleteFoodReviewImage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="food_review_id") int food_review_id,
            @WebParam(name="image_number") int image_number
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteFoodReviewImage(
                cookie,
                food_review_id,
                image_number
        );
    }

    @WebMethod
    public int uploadDrinkReviewImage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="drink_review_id") int drink_review_id,
            @WebParam(name="file_path") String file_path
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.uploadDrinkReviewImage(
                cookie,
                drink_review_id,
                file_path
        );
    }

    @WebMethod
    public boolean deleteDrinkReviewImage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="drink_review_id") int drink_review_id,
            @WebParam(name="image_number") int image_number
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteDrinkReviewImage(
                cookie,
                drink_review_id,
                image_number
        );
    }

    @WebMethod
    public int uploadBeerReviewImage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_review_id") int beer_review_id,
            @WebParam(name="file_path") String file_path
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.uploadBeerReviewImage(
                cookie,
                beer_review_id,
                file_path
        );
    }

    @WebMethod
    public boolean deleteBeerReviewImage(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_review_id") int beer_review_id,
            @WebParam(name="image_number") int image_number
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteBeerReviewImage(
                cookie,
                beer_review_id,
                image_number
        );
    }

    @WebMethod
    public boolean createReviewFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="review_id") int review_id,
            @WebParam(name="resource") String resource
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createReviewFavorite(
                cookie,
                review_id,
                resource
        );
    }

    @WebMethod
    public boolean deleteReviewFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="review_id") int review_id,
            @WebParam(name="resource") String resource
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteReviewFavorite(
                cookie,
                review_id,
                resource
        );
    }

    @WebMethod
    public int createReviewReply(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="review_id") int review_id,
            @WebParam(name="content") String content,
            @WebParam(name="resource") String resource
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createReviewReply(
                cookie,
                review_id,
                content,
                resource
        );
    }

    @WebMethod
    public boolean deleteReviewReply(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="reply_id") int reply_id,
            @WebParam(name="resource") String resource
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.deleteReviewReply(
                cookie,
                reply_id,
                resource
        );
    }

    @WebMethod
    public boolean flagVendorReview(
            @WebParam(name="review_id") int review_id,
            @WebParam(name="resource") String resource,
            @WebParam(name="why") String why,
            @WebParam(name="ip_address") String ip_address
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.flagVendorReview(
                review_id,
                resource,
                why,
                ip_address
        );
    }
}
