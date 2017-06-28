package com.VendorMenu.Foods;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class FoodController extends AbstractController {

    public boolean deleteFood(
            String cookie,
            int id
    ) throws Exception {
        // Validate input perameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "food ID");
        // Initialize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.deleteFood(
                cookie,
                id
        );
    }

    public boolean updateFood(
            String cookie,
            int id,
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
        this.validateID(id, "food ID");
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes
        );
    }

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

    public String uploadVendorFoodImage (
           String cookie,
           String filename,
           int vendor_food_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Initialize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.uploadVendorFoodImage(
                cookie,
                filename,
                vendor_food_id
        );
    }

    public boolean updateVendorFoodImage (
            String cookie,
            int food_image_id,
            int display_order
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(food_image_id, "food_image_id");
        // Initilize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateVendorFoodImage(
                cookie,
                food_image_id,
                display_order
        );
    }

    public String deleteVendorFoodImage (
            String cookie,
            int food_image_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(food_image_id, "food_image_id");
        // Initilize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.deleteVendorFoodImage(
                cookie,
                food_image_id
        );
    }

}
