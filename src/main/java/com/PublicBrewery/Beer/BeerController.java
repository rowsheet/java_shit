package com.PublicBrewery.Beer;

import com.Common.AbstractController;
import com.Common.Beer;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/10/17.
 */
public class BeerController extends AbstractController {
    public String loadBeerMenu(
        int brewery_id
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id"); // Yes, I know it's called brewery_id and its the same thing.
        // Initialize model and create the data.
        BeerModel beerModel = new BeerModel();
        // Load the data structure we're loading.
        HashMap<Integer, Beer> beerHashMap = new HashMap<Integer, Beer>();
        // Load the data using the model.
        beerHashMap = beerModel.loadBeerMenu(
                brewery_id
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(beerHashMap);
    }
}