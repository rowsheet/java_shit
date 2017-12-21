package com.PublicBrewery.Drink;

import com.Common.AbstractController;
import com.Common.VendorDrink;
import java.util.HashMap;

public class DrinkController extends AbstractController {
    public String loadDrinkMenu(
            int brewery_id
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id"); // Yes, I know it's called brewery_id and its the same thing.
        // Initialize model and create the data.
        DrinkModel drinkModel = new DrinkModel();
        // Load the data structure we're loading.
        HashMap<Integer, VendorDrink> drinkHashMap = new HashMap<Integer, VendorDrink>();
        // Load the data using the model.
        drinkHashMap = drinkModel.loadDrinkMenu(
                brewery_id
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(drinkHashMap);
    }

    public String loadDrinkMenuPaginated(
            int brewery_id,
            int limit,
            int offset,
            boolean descending // will put newest first by default.
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id"); // Yes, I know it's called brewery_id and its the same thing.
        // Initialize model and create the data.
        DrinkModel drinkModel = new DrinkModel();
        // Load the data structure we're loading.
        HashMap<Integer, VendorDrink> drinkHashMap = new HashMap<Integer, VendorDrink>();
        // Load the data using the model.
        drinkHashMap = drinkModel.loadDrinkMenuPaginated(
                brewery_id,
                limit,
                offset,
                descending
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(drinkHashMap);
    }
}
