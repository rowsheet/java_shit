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
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.createFoodFavorite(
                cookie,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean deleteFoodFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_food_id") int vendor_food_id
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.deleteFoodFavorite(
                cookie,
                vendor_food_id
        );
    }

    @WebMethod
    public String loadFoodFavorites(
            @WebParam(name="cookie") String cookie
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.loadFoodFavorites(
                cookie
        );
    }

    @WebMethod
    public boolean createDrinkFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.createDrinkFavorite(
                cookie,
                vendor_drink_id
        );
    }

    @WebMethod
    public boolean deleteDrinkFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.deleteDrinkFavorite(
                cookie,
                vendor_drink_id
        );
    }

    @WebMethod
    public String loadDrinkFavorites(
            @WebParam(name="cookie") String cookie
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.loadDrinkFavorites(
                cookie
        );
    }

    @WebMethod
    public boolean createBeerFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.createBeerFavorite(
                cookie,
                beer_id
        );
    }

    @WebMethod
    public boolean deleteBeerFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.deleteBeerFavorite(
                cookie,
                beer_id
        );
    }

    @WebMethod
    public String loadBeerFavorites(
            @WebParam(name="cookie") String cookie
    ) throws Exception {
        MenuFavoritesController menuFavoritesController = new MenuFavoritesController();
        return menuFavoritesController.loadBeerFavorites(
                cookie
        );
    }

    /*-----------------------------------------
     *  PLACES
     -----------------------------------------*/

    @WebMethod
    public boolean createUserPlaceFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_id") int vendor_id
    ) throws Exception {
        PlacesController placesController = new PlacesController();
        return placesController.createUserPlaceFavorite(
                cookie,
                vendor_id
        );
    }

    @WebMethod
    public boolean deleteUserPlaceFavorite(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="vendor_id") int vendor_id
    ) throws Exception {
        PlacesController placesController = new PlacesController();
        return placesController.deleteUserPlaceFavorite(
                cookie,
                vendor_id
        );
    }

    @WebMethod
    public String loadUserPlaceFavorites(
            @WebParam(name="cookie") String cookie
    ) throws Exception {
        PlacesController placesController = new PlacesController();
        return placesController.loadUserPlaceFavorites(
                cookie
        );
    }
}
