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
            String[] food_sizes,
            int food_category_id
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
        this.validateID(food_category_id, "food category ID");
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes,
                food_category_id
        );
    }

    public int createFood(
            String cookie,
            String name,
            String description,
            float price,
            String[] food_sizes,
            int food_category_id
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
        this.validateID(food_category_id, "food category ID");
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        return foodModel.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes,
                food_category_id
        );
    }

    public int createFoodCategory(
            String cookie,
            String category_name,
            String hex_color
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        // Initialize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.createFoodCategory(
                cookie,
                category_name,
                hex_color
        );
    }

    public boolean updateFoodCategory(
            String cookie,
            int id,
            String new_category_name,
            String new_hex_color
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(new_category_name, "new_category_name.");
        this.validateHexColor(new_hex_color);
        // Initialize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateFoodCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color
        );
    }

    public boolean deleteFoodCategory(
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "food_category_id");
        // Initialize mode and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.deleteFoodCategory(
                cookie,
                id
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
