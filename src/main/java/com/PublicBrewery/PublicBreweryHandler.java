package com.PublicBrewery;

import com.PublicBrewery.Brewery.BreweryController;
import com.PublicBrewery.Beer.BeerController;
import com.PublicBrewery.Food.FoodController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
@WebService
public class PublicBreweryHandler {

    /**
     * Load the basic brewery page info:
     *      1) About
     *      2) Hours
     *      3) Location
     *
     * @param brewery_id
     * @return JSON
     */
    @WebMethod
    public String loadBreweryInfo (
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        BreweryController breweryController = new BreweryController();
        return breweryController.loadBreweryInfo(
                brewery_id
        );
    }

    /**
     * Loads all the beers and beer categories for a particular brewery.
     *
     * @param brewery_id
     * @return JSON
     */
    @WebMethod
    public String loadBeerMenu(
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.loadBeerMenu(
                brewery_id
        );
    }

    /**
     * Loads beer details, including reviews and full images (to be put in a modal).
     *
     * @param beer_id
     * @return JSON
     */
    @WebMethod
    public String loadBeerDetails(
            @WebParam(name="beer_id") int beer_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads all the foods and food categoreis for a particular brewery.
     *
     * @param brewery_id
     * @return JSON
     */
    @WebMethod
    public String loadFoodMenu(
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.loadFoodMenu(
                brewery_id
        );
    }

    /**
     * Loads food details, including reviews and full images (to be put in a modal).
     *
     * @param food_id
     * @return JSON
     */
    @WebMethod
    public String loadFoodDetail(
            @WebParam(name="food_id") int food_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads events for a brewery in chronological order.
     *
     * @param brewery_id
     * @param pagination
     * @param count
     * @return JSON
     */

    @WebMethod
    public String loadEvents(
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="pagination") int pagination,
            @WebParam(name="count") int count
    ) throws Exception {
        return "something";
    }

    /**
     * Loads event details with all other info.
     *
     * @param event_id
     * @return JSON
     */
    @WebMethod
    public String loadEventDetail(
            @WebParam(name="event_id") int event_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads all promotions for a brewery.
     *
     * @param brewery_id
     * @return JSON
     */
    @WebMethod
    public String loadPromotions(
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads all information for a promotion based on the ID.
     *
     * @param promotion_id
     * @return JSON
     */
    @WebMethod
    public String loadPromotionDetails(
            @WebParam(name="promotions_id") int promotion_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads top three posts and blog categories for brewery in chronological order.
     *
     * @param brewery_id
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadBlogPage (
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads category specific paginated posts for brewery
     *
     * @param brewery_id
     * @param category
     * @param pagination
     * @param count
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadPosts (
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="category") int category,
            @WebParam(name="pagination") int pagination,
            @WebParam(name="count") int count
    ) throws Exception {
        return "something";
    }

    /**
     * Loads bio page for brewery (all people and their basic info)
     * @param brewery_id
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadBios(
            @WebParam(name="brewery_id") int brewery_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads bio account details for account_id assuming it's a vendor type.
     *
     * @param account_id
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadBioDetail(
            @WebParam(name="account_id") int account_id
    ) throws Exception {
        return "something";
    }

    /**
     * Loads chronological brewery reviews for a brewery based on a pagination and count.
     * @param brewery_id
     * @param pagination
     * @param count
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadBreweryReviews(
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="pagination") int pagination,
            @WebParam(name="count") int count
    ) throws Exception {
        return "something";
    }

    /**
     * Loads chronological food reviews for a brewery based on a pagination and count.
     * @param brewery_id
     * @param pagination
     * @param count
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadFoodReviews(
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="pagination") int pagination,
            @WebParam(name="count") int count
    ) throws Exception {
        return "something";
    }
}

