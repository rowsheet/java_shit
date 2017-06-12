package com.PublicBrewery.Food;

import com.Common.AbstractController;
import com.Common.VendorFood;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/11/17.
 */
public class FoodController extends AbstractController {
    public String loadFoodMenu(
            int brewery_id
    )
        throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id"); // vendor_id and brewery_id are the same thing.
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        // Load the data structure we're loading.
        HashMap<Integer, VendorFood> vendorFoodHashMap = new HashMap<Integer, VendorFood>();
        // Load the data using the model.
        vendorFoodHashMap = foodModel.loadFoodMenu(
                brewery_id
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(vendorFoodHashMap);
    }
}
