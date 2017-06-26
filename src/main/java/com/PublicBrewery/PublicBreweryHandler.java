package com.PublicBrewery;

import com.Common.Beer;
import com.PublicBrewery.Brewery.BreweryController;
import com.PublicBrewery.Beer.BeerController;
import com.PublicBrewery.Food.FoodController;
import com.PublicBrewery.Events.EventController;
import com.PublicBrewery.Food.FoodModel;
import com.PublicBrewery.Reviews.ReviewController;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

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
     *
     *      1) About
     *      2) Hours
     *      3) Location
     *      4) Food Menu
     *      5) Beer Menu
     *      6) Events
     *      7) Reviews
     *
     * @param brewery_id
     * @return JSON
     */
    @WebMethod
    public String loadBreweryInfo (
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="beer_limit") int beer_limit,
            @WebParam(name="food_limit") int food_limit,
            @WebParam(name="image_limit") int image_limit,
            @WebParam(name="event_limit") int event_limit,
            @WebParam(name="review_limit") int review_limit
    ) throws Exception {
        /*
        It just so happens that client-side, if int parameters are not set, they
        will default to zero. We take advantage of this by giving the client the
        option to set limits. If the limits are zero, they will be some default
        value.

        Java doesn't support default values and overloading all these is just
        too much.

        @TODO Set these in some app config, possibly in env vars or calibrated per
        request pattern if it gets to that point.
         */
        if (beer_limit == 0) {
            beer_limit = 9;
        }
        if (food_limit == 0) {
            food_limit = 9;
        }
        if (image_limit == 0) {
            image_limit = 8; // 4x2 thing on the thing.
        }
        if (event_limit == 0) {
            event_limit = 5;
        }
        if (review_limit == 0) {
            review_limit = 7; // because
        }
        BreweryController breweryController = new BreweryController();
        return breweryController.loadBreweryInfo(
                brewery_id,
                beer_limit,
                food_limit,
                image_limit,
                event_limit,
                review_limit
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

    @WebMethod
    public String loadBeerMenuPaginated(
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset,
            @WebParam(name="order_by") String order_by,
            @WebParam(name="descending") boolean descending
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.loadBeerMenuPaginated(
                brewery_id,
                limit,
                offset,
                order_by,
                descending
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

    @WebMethod
    public String loadFoodMenuPaginated(
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset,
            @WebParam(name="order_by") String order_by,
            @WebParam(name="descending") boolean descending
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.loadFoodMenuPaginated(
                brewery_id,
                limit,
                offset,
                order_by,
                descending
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
     * @param limit
     * @param offset
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
     * @param limit
     * @param offset
     * @return JSON
     * @throws Exception
     */
    @WebMethod
    public String loadBreweryReviews(
            @WebParam(name="brewery_id") int brewery_id,
            @WebParam(name="limit") int limit,
            @WebParam(name="offset") int offset
    ) throws Exception {
        ReviewController reviewController = new ReviewController();
        return reviewController.loadBreweryReviews(
                brewery_id,
                limit,
                offset
        );
    }
}
