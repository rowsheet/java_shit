package com.PublicBrewery.Brewery;

import com.Common.AbstractController;
import com.Common.Brewery;

import com.google.gson.*;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
public class BreweryController extends AbstractController {
    public String loadBreweryInfo(
            int brewery_id,
            int beer_limit,
            int food_limit,
            int image_limit,
            int event_limit,
            int review_limit,
            int drink_limit
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id");
        // Initialize model and create the data.
        BreweryModel breweryModel = new BreweryModel();
        // Load the data structure we're loading.
        Brewery brewery = null;
        // Load the data using the model.
        brewery = breweryModel.loadBreweryInfo(
                brewery_id,
                beer_limit,
                food_limit,
                image_limit,
                event_limit,
                review_limit,
                drink_limit
        );
        // Return JSON or data structure returned by model.
        // Don't
        return this.returnJSON(brewery);
    }
}
