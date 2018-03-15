package com.VendorMenu.Foods;

import com.Common.AbstractController;
import jnr.ffi.annotations.In;

import java.util.ArrayList;

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
            int food_category_id,
            int nutritional_fact_id,
            int food_tag_id_one,
            int food_tag_id_two,
            int food_tag_id_three,
            int food_tag_id_four,
            int food_tag_id_five
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateText(description, "description");
        this.validatePrice(price);
        // Validate for null (empty) ids for tags. JAVA SOAP will make this zero.
        this.validateNullID(nutritional_fact_id, "id");
        this.validateNullID(food_tag_id_one, "food_tag_id_one");
        this.validateNullID(food_tag_id_two, "food_tag_id_two");
        this.validateNullID(food_tag_id_three, "food_tag_id_three");
        this.validateNullID(food_tag_id_four, "food_tag_id_four");
        this.validateNullID(food_tag_id_five, "food_tag_id_five");
        if (food_sizes.length == 0) {
            throw new Exception("Must have at least one \"food size\".");
        }
        for (String food_size: food_sizes) {
            this.validateFoodSize(food_size);
        }
        this.validateID(id, "food ID");
        this.validateID(food_category_id, "food category ID");
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                food_tag_id_one,
                food_tag_id_two,
                food_tag_id_three,
                food_tag_id_four,
                food_tag_id_five
        );
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes,
                food_category_id,
                nutritional_fact_id,
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
        );
    }

    /*
    Note:
        May contain cover image. If so, insert into images. Do not validate as it may
        be missing.
     */
    public int createFood(
            String cookie,
            String name,
            String description,
            float price,
            String[] food_sizes,
            int food_category_id,
            int nutritional_fact_id,
            int food_tag_id_one,
            int food_tag_id_two,
            int food_tag_id_three,
            int food_tag_id_four,
            int food_tag_id_five,
            String cover_image
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateText(description, "description");
        this.validatePrice(price);
        // Validate for null (empty) ids for tags. JAVA SOAP will make this zero.
        this.validateNullID(nutritional_fact_id, "id");
        this.validateNullID(food_tag_id_one, "food_tag_id_one");
        this.validateNullID(food_tag_id_two, "food_tag_id_two");
        this.validateNullID(food_tag_id_three, "food_tag_id_three");
        this.validateNullID(food_tag_id_four, "food_tag_id_four");
        this.validateNullID(food_tag_id_five, "food_tag_id_five");
        if (food_sizes.length == 0) {
            throw new Exception("Must have at least one \"food size\".");
        }
        for (String food_size: food_sizes) {
            this.validateFoodSize(food_size);
        }
        this.validateID(food_category_id, "food category ID");
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                food_tag_id_one,
                food_tag_id_two,
                food_tag_id_three,
                food_tag_id_four,
                food_tag_id_five
        );
        // Initialize model and create the data.
        FoodModel foodModel = new FoodModel();
        return foodModel.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes,
                food_category_id,
                nutritional_fact_id,
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4),
                cover_image
        );
    }

    public int createFoodCategory(
            String cookie,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        this.validateString(name, "name");
        // Initialize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.createFoodCategory(
                cookie,
                name,
                hex_color,
                description
        );
    }

    public boolean updateFoodCategory(
            String cookie,
            int id,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name.");
        this.validateHexColor(hex_color);
        // Initialize model and create data.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateFoodCategory(
                cookie,
                id,
                name,
                hex_color,
                description
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

    public int createVendorFoodTag (
            String cookie,
            String name,
            String hex_color,
            String tag_type
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateHexColor(hex_color);
        this.validateMenuItemTagType(tag_type);
        // Initialize model and create food tag.
        FoodModel foodModel = new FoodModel();
        return foodModel.createVendorFoodTag(
                cookie,
                name,
                hex_color,
                tag_type
        );
    }

    public Boolean updateVendorFoodTag (
            String cookie,
            int id,
            String name,
            String hex_color,
            String tag_type
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "id");
        this.validateString(name, "name");
        this.validateHexColor(hex_color);
        this.validateMenuItemTagType(tag_type);
        // Initialize model and return model response.
        FoodModel foodModel = new FoodModel();
        return foodModel.updateVendorFoodTag(
                cookie,
                id,
                name,
                hex_color,
                tag_type
        );
    }

    public Boolean deleteVendorFoodTag (
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "id");
        // Initialize model and return model response.
        FoodModel foodModel = new FoodModel();
        return foodModel.deleteVendorFoodTag(
                cookie,
                id
        );
    }

}
