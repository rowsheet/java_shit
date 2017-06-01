package com.VendorMenu;

import com.VendorMenu.Beers.BeerController;
import com.VendorMenu.Foods.FoodController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class VendorMenuHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    @WebMethod
    public int createBeer(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="color") int color,
            @WebParam(name="bitterness") int bitterness,
            @WebParam(name="abv") int abv,
            @WebParam(name="beer_style") String beer_style,
            @WebParam(name="beer_tastes") String[] beer_tastes,
            @WebParam(name="description") String description,
            @WebParam(name="price") float price,
            @WebParam(name="beer_sizes") String[] beer_sizes
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.createBeer(
                cookie,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes
        );
    }

    @WebMethod
    public int createFood(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="name") String name,
            @WebParam(name="description") String description,
            @WebParam(name="price") float price,
            @WebParam(name="food_sizes") String[] food_sizes
    ) throws Exception {
        FoodController foodController = new FoodController();
        return 1;
    }
}
