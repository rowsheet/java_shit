package com.PublicBrewery;

import com.PublicBrewery.Brewery.BreweryController;
import com.PublicBrewery.Beer.BeerController;
import com.PublicBrewery.Food.FoodController;
import com.PublicBrewery.Events.EventController;

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
    // @WebMethod
    // public String loadBeerDetails(
    //         @WebParam(name="beer_id") int beer_id
    // ) throws Exception {
    //     return "something";
    // }

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
    // @WebMethod
    // public String loadFoodDetail(
    //         @WebParam(name="food_id") int food_id
    // ) throws Exception {
    //     return "something";
    // }

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
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        EventController eventController = new EventController();
        return eventController.loadEvents(
                brewery_id,
                limit,
                offset
        );
    }

    /**
     * Loads event details with all other info.
     *
     * @param event_id
     * @return JSON
     */
    // @WebMethod
    // public String loadEventDetail(
    //         @WebParam(name="event_id") int event_id
    // ) throws Exception {
    //     return "something";
    // }

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
}
