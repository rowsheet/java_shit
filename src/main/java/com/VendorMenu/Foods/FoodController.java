package com.VendorMenu.Foods;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class FoodController extends AbstractController {
    public int createFood(
            String cookie,
            String name,
            String description,
            float price,
            String[] food_sizes
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateText(description, "description");
        this.validatePrice(price);
        if (food_sizes.length == 0) {
            throw new Exception("Must have at least one \"food size\".");
        }
        for (String food_size: food_sizes) {
            this.validateFoodSize(food_size);
        }
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        return foodModel.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes
        );
    }
}
