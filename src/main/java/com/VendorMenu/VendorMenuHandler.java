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

    /*
    BEER
     */

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
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "color") int color,
            @WebParam(name = "bitterness") int bitterness,
            @WebParam(name = "abv") int abv,
            @WebParam(name = "beer_style") String beer_style,
            @WebParam(name = "beer_tastes") String[] beer_tastes,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "beer_sizes") String[] beer_sizes,
            @WebParam(name = "hop_score") String hop_score
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
     * <p>
     * Ensures session is valid, permissions exists, and ensures ownership of beer_id
     * to vendor_id.
     * <p>
     * Finally updates data returning true.
     * <p>
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
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "color") int color,
            @WebParam(name = "bitterness") int bitterness,
            @WebParam(name = "abv") int abv,
            @WebParam(name = "beer_style") String beer_style,
            @WebParam(name = "beer_tastes") String[] beer_tastes,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "beer_sizes") String[] beer_sizes,
            @WebParam(name = "hop_score") String hop_score
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
    public boolean deleteBeer(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeer(
                cookie,
                id
        );
    }

    /*
    FOODS
     */

    @WebMethod
    public int createFood(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "food_sizes") String[] food_sizes,
            @WebParam(name = "food_category_id") int food_category_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes,
                food_category_id
        );
    }

    @WebMethod
    public boolean updateFood(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "food_sizes") String[] food_sizes,
            @WebParam(name = "food_category_id") int food_category_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes,
                food_category_id
        );
    }

    @WebMethod
    public boolean deleteFood(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteFood(
                cookie,
                id
        );
    }

    /*
    FOOD CATEGORIES
     */

    @WebMethod
    public int createFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="category_name") String category_name,
            @WebParam(name="hex_color") String hex_color
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createFoodCategory(
                cookie,
                category_name,
                hex_color
        );
    }

    @WebMethod
    public boolean updateFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="new_category_name") String new_category_name,
            @WebParam(name="new_hex_color") String new_hex_color
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFoodCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color
        );
    }

    @WebMethod
    public boolean deleteFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteFoodCategory(
                cookie,
                id
        );
    }

    /*
    FOOD IMAGES
     */

    /**
     * Inserts record into vendor_food_images with default
     * display order and a null vendor_food_id.
     *
     * Record must belong to a vendor with the feature "vendor_food_images".
     *
     * Although food_id can be null, it has a comopound foreign key
     * to vendor_foods alongside the vendor_id, so the image must be owned
     * by a food item owned by the same vendor_id.
     *
     * Returns filepath of new image.
     *
     * @param cookie
     * @param filename
     * @return filepath
     * @throws Exception
     */
    @WebMethod
    public String uploadVendorFoodImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "filename") String filename,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.uploadVendorFoodImage(
                cookie,
                filename,
                vendor_food_id
        );
    }

    /**
     * Updates image. Returns true or throws exception message.
     *
     * @param cookie
     * @param food_image_id
     * @param display_order
     * @return success
     * @throws Exception
     */
    @WebMethod
    public boolean updateVendorFoodImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_image_id") int food_image_id,
            @WebParam(name = "display_order") int display_order
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateVendorFoodImage(
                cookie,
                food_image_id,
                display_order
        );
    }

    /**
     * Deletes image. Returns filename or throws exception.
     *
     * @param cookie
     * @param food_image_id
     * @return
     * @throws Exception
     */
    @WebMethod
    public String deleteVendorFoodImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_image_id") int food_image_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteVendorFoodImage(
                cookie,
                food_image_id
        );
    }

    /**
     * Inserts record into beer_images with default
     * display order and a null beer_id.
     *
     * Record must belong to a vendor with the feature "beer_images".
     *
     * Although beer_id can be null, it has a comopound foreign key
     * to beers alongside the vendor_id, so the image must be owned
     * by a beer item owned by the same vendor_id.
     *
     * Returns filepath of new image.
     *
     * @param cookie
     * @param filename
     * @return filepath
     * @throws Exception
     */
    @WebMethod
    public String uploadBeerImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "filename") String filename,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.uploadBeerImage(
                cookie,
                filename,
                beer_id
        );
    }


    /**
     * Updates image. Returns true or throws exception message.
     *
     * @param cookie
     * @param beer_image_id
     * @param display_order
     * @return success
     * @throws Exception
     */
    @WebMethod
    public boolean updateBeerImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_image_id") int beer_image_id,
            @WebParam(name = "display_order") int display_order
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeerImage(
                cookie,
                beer_image_id,
                display_order
        );
    }

    /**
     * Deletes image. Returns filename or throws exception.
     *
     * @param cookie
     * @param beer_image_id
     * @return
     * @throws Exception
     */
    @WebMethod
    public String deleteBeerImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_image_id") int beer_image_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeerImage(
                cookie,
                beer_image_id
        );
    }
}
