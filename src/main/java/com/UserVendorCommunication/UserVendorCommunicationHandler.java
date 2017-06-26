package com.UserVendorCommunication;

import com.UserVendorCommunication.Blogs.BlogController;
import com.UserVendorCommunication.Menu.MenuController;
import com.UserVendorCommunication.Reviews.ReviewController;

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
    public int createBeerReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id,
            @WebParam(name="stars") int stars,
            @WebParam(name="content") String content
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createBeerReview(
                cookie,
                beer_id,
                stars,
                content
        );
    }

    @WebMethod
    public int createFoodReview(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_food_id") int vendor_food_id,
            @WebParam(name="stars") int stars,
            @WebParam(name="content") String content
    ) throws Exception {
        MenuController menuController = new MenuController();
        return menuController.createFoodReview(
                cookie,
                vendor_food_id,
                stars,
                content
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

    @WebMethod
    public int createBlogPostComment(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_post_id") int vendor_post_id,
            @WebParam(name="content") String content
    ) throws Exception {
        BlogController blogController = new BlogController();
        return blogController.createBlogPostComment(
                cookie,
                vendor_post_id,
                content
        );
    }

    @WebMethod
    public int createBlogPostCommentReply(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_post_comment_id") int vendor_post_comment_id,
            @WebParam(name="content") String content
    ) throws Exception {
        BlogController blogController = new BlogController();
        return blogController.createBlogPostCommentReply(
                cookie,
                vendor_post_comment_id,
                content
        );
    }

}
