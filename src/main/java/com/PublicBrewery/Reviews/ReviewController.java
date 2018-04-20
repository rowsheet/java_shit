package com.PublicBrewery.Reviews;

import com.Common.AbstractController;
import com.Common.VendorDrinkReview;
import com.Common.VendorFoodReview;
import com.Common.BeerReview;
import com.Common.VendorReview;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/18/17.
 */
public class ReviewController extends AbstractController {
    public String loadBreweryReviews (
            int brewery_id,
            int limit,
            int offset
    ) throws Exception {
        this.validateID(brewery_id, "brewery_id");
        ReviewModel reviewModel = new ReviewModel();
        HashMap<Integer, VendorReview> vendorReviewHashMap = new HashMap<Integer, VendorReview>();
        vendorReviewHashMap = reviewModel.loadBreweryReviews(
                brewery_id,
                limit,
                offset
        );
        return this.returnJSON(vendorReviewHashMap);
    }

    public String loadFoodReviews(
            int food_id
    ) throws Exception {
        // Validate shit.
        this.validateID(food_id, "food_id");
        // Model shit.
        ReviewModel reviewModel = new ReviewModel();
        HashMap<Integer, VendorFoodReview> foodReviewHashMap = new HashMap<Integer, VendorFoodReview>();
        foodReviewHashMap = reviewModel.loadVendorFoodReviews(
                food_id
        );
        return this.returnJSON(foodReviewHashMap);
    }

    public String loadDrinkReviews(
            int drink_id
    ) throws Exception {
        // Validate shit.
        this.validateID(drink_id, "drink_id");
        // Model shit.
        ReviewModel reviewModel = new ReviewModel();
        HashMap<Integer, VendorDrinkReview> drinkReviewHashMap = new HashMap<Integer, VendorDrinkReview>();
        drinkReviewHashMap = reviewModel.loadVendorDrinkReviews(
                drink_id
        );
        return this.returnJSON(drinkReviewHashMap);
    }

    public String loadBeerReviews(
            int beer_id
    ) throws Exception {
        // Validate shit.
        this.validateID(beer_id, "beer_id");
        // Model shit.
        ReviewModel reviewModel = new ReviewModel();
        HashMap<Integer, BeerReview> beerReviewHashMap = new HashMap<Integer, BeerReview>();
        beerReviewHashMap = reviewModel.loadBeerReviews(
                beer_id
        );
        return this.returnJSON(beerReviewHashMap);
    }

}
