package com.PublicBrewery.Food;

import com.Common.AbstractController;
import com.Common.PublicVendor.FoodMenu;
import com.Common.VendorFood;
import jnr.ffi.annotations.In;

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
        FoodMenu foodMenu = new FoodMenu();
        // Load the data using the model.
        foodMenu = foodModel.loadFoodMenu(
                brewery_id
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(foodMenu);
    }

    /*
    DEPRECIATED
    public String loadFoodMenuPaginated(
            int brewery_id,
            int limit,
            int offset,
            String order_by,
            boolean descending // will put newest first by default.
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "vendor_id"); // Yes, I know it's called brewery_id and its the same thing.
        this.validateFoodMenuOrderBy(order_by);
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        // Load the data structure we're loading.
        HashMap<Integer, VendorFood> vendorFoodHashMap = new HashMap<Integer, VendorFood>();
        // Load the data using the model.
        vendorFoodHashMap = foodModel.loadFoodMenuPaginated(
                brewery_id,
                limit,
                offset,
                order_by,
                descending
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(vendorFoodHashMap);
    }
    */
}
