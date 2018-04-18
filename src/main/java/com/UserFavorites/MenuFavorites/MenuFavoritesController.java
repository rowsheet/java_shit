package com.UserFavorites.MenuFavorites;

import com.Common.AbstractController;
import com.Common.UserFavorites.BeerMenu;
import com.Common.UserFavorites.DrinkMenu;
import com.Common.UserFavorites.FoodMenu;

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
    public String loadFoodFavorites(
            String cookie
    ) throws Exception {
        this.validateString(cookie, "cookie");
        MenuFavoritesModel menuFavoritesModel = new MenuFavoritesModel();
        FoodMenu foodMenu = menuFavoritesModel.loadFoodFavorites(
                cookie
        );
        return this.returnJSON(foodMenu);
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
    public String loadDrinkFavorites(
            String cookie
    ) throws Exception {
        this.validateString(cookie, "cookie");
        MenuFavoritesModel menuFavoritesModel = new MenuFavoritesModel();
        DrinkMenu drinkMenu = menuFavoritesModel.loadDrinkFavorites(
                cookie
        );
        return this.returnJSON(drinkMenu);
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
    public String loadBeerFavorites(
            String cookie
    ) throws Exception {
        this.validateString(cookie, "cookie");
        MenuFavoritesModel menuFavoritesModel = new MenuFavoritesModel();
        BeerMenu beerMenu = menuFavoritesModel.loadBeerFavorites(
                cookie
        );
        return this.returnJSON(beerMenu);
    }
}
