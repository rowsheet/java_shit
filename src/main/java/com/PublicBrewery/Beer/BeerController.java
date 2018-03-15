package com.PublicBrewery.Beer;

import com.Common.AbstractController;
import com.Common.Beer;
import com.Common.PublicVendor.BeerMenu;

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
        BeerMenu beerMenu = new BeerMenu();
        // Load the data using the model.
        beerMenu = beerModel.loadBeerMenu(
                brewery_id
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(beerMenu);
    }

    // @TODO STOP USING THIS!
    public String loadBeerMenuPaginated(
        int brewery_id,
        int limit,
        int offset,
        String order_by,
        boolean descending // will put newest first by default.
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id"); // Yes, I know it's called brewery_id and its the same thing.
        this.validateBeerMenuOrderBy(order_by);
        // Initialize model and create the data.
        BeerModel beerModel = new BeerModel();
        // Load the data structure we're loading.
        HashMap<Integer, Beer> beerHashMap = new HashMap<Integer, Beer>();
        // Load the data using the model.
        beerHashMap = beerModel.loadBeerMenuPaginated(
                brewery_id,
                limit,
                offset,
                order_by,
                descending
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(beerHashMap);
    }

}
