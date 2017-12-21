package com.VendorMenu.Drinks;

import com.Common.AbstractController;

public class DrinkController extends AbstractController {

    public boolean deleteDrink(
            String cookie,
            int id
    ) throws Exception {
        // Validate input perameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "drink ID");
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.deleteDrink(
                cookie,
                id);
    }

    public boolean updateDrink(
            String cookie,
            int id,
            String name,
            String description,
            float price,
            int drink_category_id,
            String hex_one,
            String hex_two,
            String hex_three,
            String hex_background,
            int[] spirit_ids
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "drink ID");
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateText(description, "description");
        this.validatePrice(price);
        this.validateID(drink_category_id, "drink category ID");
        this.validateHexColor(hex_one);
        this.validateHexColor(hex_two);
        this.validateHexColor(hex_three);
        this.validateHexColor(hex_background);
        this.validateSpiritIDs(spirit_ids);
        // Initialize model and create the data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.updateDrink(
                cookie,
                id,
                name,
                description,
                price,
                drink_category_id,
                hex_one,
                hex_two,
                hex_three,
                hex_background,
                spirit_ids);
    }

    public int createDrink(
            String cookie,
            String name,
            String description,
            float price,
            int drink_category_id,
            String hex_one,
            String hex_two,
            String hex_three,
            String hex_background,
            int[] spirit_ids
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateText(description, "description");
        this.validatePrice(price);
        this.validateID(drink_category_id, "drink category ID");
        this.validateHexColor(hex_one);
        this.validateHexColor(hex_two);
        this.validateHexColor(hex_three);
        this.validateHexColor(hex_background);
        this.validateSpiritIDs(spirit_ids);
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.createDrink(
                cookie,
                name,
                description,
                price,
                drink_category_id,
                hex_one,
                hex_two,
                hex_three,
                hex_background,
                spirit_ids);
    }

    public int createDrinkCategory(
            String cookie,
            String category_name,
            String hex_color,
            String icon_enum
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        this.validateDrinkIconEnum(icon_enum);
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.createDrinkCategory(
                cookie,
                category_name,
                hex_color,
                icon_enum);
    }

    public boolean updateDrinkCategory(
            String cookie,
            int id,
            String new_category_name,
            String new_hex_color,
            String new_icon_enum
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(new_category_name, "new_category_name.");
        this.validateHexColor(new_hex_color);
        this.validateDrinkIconEnum(new_icon_enum);
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.updateDrinkCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color,
                new_icon_enum);
    }

    public boolean deleteDrinkCategory(
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "drink_category_id");
        // Initialize mode and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.deleteDrinkCategory(
                cookie,
                id);
    }

    public String uploadVendorDrinkImage (
            String cookie,
            String filename,
            int vendor_drink_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.uploadVendorDrinkImage(
               cookie,
                filename,
                vendor_drink_id);
    }

    public boolean updateVendorDrinkImage (
            String cookie,
            int drink_image_id,
            int display_order
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(drink_image_id, "drink_image_id");
        // Initilize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.updateVendorDrinkImage(
                cookie,
                drink_image_id,
                display_order);
    }

    public String deleteVendorDrinkImage (
            String cookie,
            int drink_image_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(drink_image_id, "drink_image_id");
        // Initilize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.deleteVendorDrinkImage(
                cookie,
                drink_image_id);
    }

}

