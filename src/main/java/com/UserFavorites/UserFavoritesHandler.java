package com.UserFavorites;

import com.UserFavorites.Places.PlacesController;
import com.UserFavorites.MenuFavorites.MenuFavoritesController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
@WebService
public class UserFavoritesHandler {

    @WebMethod
    public boolean createFoodFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_food_id") int vendor_food_id
    ) throws Exception {
        MenuFavoritesController reviewController = new MenuFavoritesController();
        return reviewController.createFoodFavorite(
                cookie,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean deleteFoodFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_food_id") int vendor_food_id
    ) throws Exception {
        MenuFavoritesController reviewController = new MenuFavoritesController();
        return reviewController.deleteFoodFavorite(
                cookie,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean createDrinkFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        MenuFavoritesController reviewController = new MenuFavoritesController();
        return reviewController.createDrinkFavorite(
                cookie,
                vendor_drink_id
        );
    }

    @WebMethod
    public boolean deleteDrinkFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        MenuFavoritesController reviewController = new MenuFavoritesController();
        return reviewController.deleteDrinkFavorite(
                cookie,
                vendor_drink_id
        );
    }

    @WebMethod
    public boolean createBeerFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        MenuFavoritesController reviewController = new MenuFavoritesController();
        return reviewController.createBeerFavorite(
                cookie,
                beer_id
        );
    }

    @WebMethod
    public boolean deleteBeerFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        MenuFavoritesController reviewController = new MenuFavoritesController();
        return reviewController.deleteBeerFavorite(
                cookie,
                beer_id
        );
    }

    /*-----------------------------------------
     *  PLACES
     -----------------------------------------*/

    @WebMethod
    public int createUserPlaceFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        PlacesController placesController = new PlacesController();
        return placesController.createUserPlaceFavorite(
                cookie,
                brewery_id
        );
    }

    @WebMethod
    public boolean deleteUserPlaceFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        PlacesController placesController = new PlacesController();
        return placesController.deleteUserPlaceFavorite(
                cookie,
                brewery_id
        );
    }

    @WebMethod
    public String readUserPlaceFavorites(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        PlacesController placesController = new PlacesController();
        return placesController.readUserPlaceFavorites(
                cookie,
                limit,
                offset
        );
    }
}
