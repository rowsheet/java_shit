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

    /**
     * Takes a vendor cookie (associated to a session) and data for a new beer
     * and creates it, returning the ID of the new beer.
     *
     * @param cookie
     * @param name
     * @param color
     * @param bitterness
     * @param abv
     * @param beer_style
     * @param beer_tastes
     * @param description
     * @param price
     * @param beer_sizes
     * @param hop_score
     * @return
     * @throws Exception
     */
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
            @WebParam(name="beer_sizes") String[] beer_sizes,
            @WebParam(name="hop_score") String hop_score
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
                beer_sizes,
                hop_score
        );
    }

    /**
     * Takes a cookie of a vendor (associated with a session), along with information
     * about a particular beer.
     *
     * Ensures session is valid, permissions exists, and ensures ownership of beer_id
     * to vendor_id.
     *
     * Finally updates data returning true.
     *
     * Throws exception, otherwise.
     *
     * @param cookie
     * @param id
     * @param name
     * @param color
     * @param bitterness
     * @param abv
     * @param beer_style
     * @param beer_tastes
     * @param description
     * @param price
     * @param beer_sizes
     * @param hop_score
     * @return true on success, else exception.
     * @throws Exception
     */
    @WebMethod
    public boolean updateBeer(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="name") String name,
            @WebParam(name="color") int color,
            @WebParam(name="bitterness") int bitterness,
            @WebParam(name="abv") int abv,
            @WebParam(name="beer_style") String beer_style,
            @WebParam(name="beer_tastes") String[] beer_tastes,
            @WebParam(name="description") String description,
            @WebParam(name="price") float price,
            @WebParam(name="beer_sizes") String[] beer_sizes,
            @WebParam(name="hop_score") String hop_score
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeer(
                cookie,
                id,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes,
                hop_score
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
        return foodController.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes
        );
    }

    @WebMethod
    public boolean updateFood(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="name") String name,
            @WebParam(name="description") String description,
            @WebParam(name="price") float price,
            @WebParam(name="food_sizes") String[] food_sizes
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes
        );
    }
}
