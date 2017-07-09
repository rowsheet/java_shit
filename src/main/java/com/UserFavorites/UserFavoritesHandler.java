package com.UserFavorites;

import com.Common.Beer;
import com.UserFavorites.Beers.BeerController;
import com.UserFavorites.Places.PlacesController;
import com.UserFavorites.Places.PlacesModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
@WebService
public class UserFavoritesHandler {

    @WebMethod
    public int createUserBeerFavorite (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.createUserBeerFavorite(
                cookie,
                beer_id
        );
    }

    @WebMethod
    public boolean deleteUserBeerFavorite (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteUserBeerFavorite(
                cookie,
                beer_id
        );
    }

    @WebMethod
    public String readUserBeerFavorites(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.readUserBeerFavorites(
                cookie,
                limit,
                offset
        );
    }

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
