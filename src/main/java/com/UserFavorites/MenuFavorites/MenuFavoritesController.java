package com.UserFavorites.MenuFavorites;

import com.Common.AbstractController;

public class MenuFavoritesController extends AbstractController {
    public boolean createFoodFavorite(
            String cookie,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(vendor_food_id, "vendor_food_id");
        MenuFavoritesModel reviewModel = new MenuFavoritesModel();
        return reviewModel.createFoodFavorite(
                cookie,
                vendor_food_id
        );
    }
    public boolean deleteFoodFavorite(
            String cookie,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(vendor_food_id, "vendor_food_id");
        MenuFavoritesModel reviewModel = new MenuFavoritesModel();
        return reviewModel.deleteFoodFavorite(
                cookie,
                vendor_food_id
        );
    }
    public boolean createDrinkFavorite(
            String cookie,
            int vendor_drink_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(vendor_drink_id, "vendor_drink_id");
        MenuFavoritesModel reviewModel = new MenuFavoritesModel();
        return reviewModel.createDrinkFavorite(
                cookie,
                vendor_drink_id
        );
    }
    public boolean deleteDrinkFavorite(
            String cookie,
            int vendor_drink_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(vendor_drink_id, "vendor_drink_id");
        MenuFavoritesModel reviewModel = new MenuFavoritesModel();
        return reviewModel.deleteDrinkFavorite(
                cookie,
                vendor_drink_id
        );
    }
    public boolean createBeerFavorite(
            String cookie,
            int beer_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(beer_id, "beer_id");
        MenuFavoritesModel reviewModel = new MenuFavoritesModel();
        return reviewModel.createBeerFavorite(
                cookie,
                beer_id
        );
    }
    public boolean deleteBeerFavorite(
            String cookie,
            int beer_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(beer_id, "beer_id");
        MenuFavoritesModel reviewModel = new MenuFavoritesModel();
        return reviewModel.deleteBeerFavorite(
                cookie,
                beer_id
        );
    }
}
