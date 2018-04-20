package com.UserVendorCommunication;

import com.UserVendorCommunication.Blogs.BlogController;
import com.UserVendorCommunication.Menu.MenuController;
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
}
