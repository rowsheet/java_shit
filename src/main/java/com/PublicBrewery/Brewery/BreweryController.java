package com.PublicBrewery.Brewery;

import com.Common.AbstractController;
import com.Common.Brewery;

import com.google.gson.*;

/**
 * Created by alexanderkleinhans on 6/9/17.
 */
public class BreweryController extends AbstractController {
    public String loadBreweryInfo(
            int brewery_id
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id");
        // Initialize model and create the data.
        BreweryModel breweryModel = new BreweryModel();
        // Load the data structure we're loading.
        Brewery brewery = null;
        // Load the data using the model.
        brewery = breweryModel.loadBreweryInfo(brewery_id);
        // Return JSON or data structure returned by model.
        return this.returnJSON(brewery);
    }
}
