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
            int[] spirit_ids,
            String drink_serve_temp,
            String servings,
            String icon_enum,
            int nutritional_fact_id,
            int drink_tag_id_one,
            int drink_tag_id_two,
            int drink_tag_id_three,
            int drink_tag_id_four,
            int drink_tag_id_five
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
        this.validateDrinkServeTemp(drink_serve_temp);
        this.validateDrinkServingSize(servings);
        this.validateDrinkIconEnum(icon_enum);
        // Validate for null (empty) ids for tags. JAVA SOAP will make this zero.
        this.validateNullID(nutritional_fact_id, "id");
        this.validateNullID(drink_tag_id_one, "drink_tag_id_one");
        this.validateNullID(drink_tag_id_two, "drink_tag_id_two");
        this.validateNullID(drink_tag_id_three, "drink_tag_id_three");
        this.validateNullID(drink_tag_id_four, "drink_tag_id_four");
        this.validateNullID(drink_tag_id_five, "drink_tag_id_five");
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
                spirit_ids,
                drink_serve_temp,
                servings,
                icon_enum,
                nutritional_fact_id,
                drink_tag_id_one,
                drink_tag_id_two,
                drink_tag_id_three,
                drink_tag_id_four,
                drink_tag_id_five);
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
            int[] spirit_ids,
            String drink_serve_temp,
            String servings,
            String icon_enum,
            int nutritional_fact_id,
            int drink_tag_id_one,
            int drink_tag_id_two,
            int drink_tag_id_three,
            int drink_tag_id_four,
            int drink_tag_id_five
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
        this.validateDrinkServeTemp(drink_serve_temp);
        this.validateDrinkServingSize(servings);
        this.validateDrinkIconEnum(icon_enum);
        // Validate for null (empty) ids for tags. JAVA SOAP will make this zero.
        this.validateNullID(nutritional_fact_id, "id");
        this.validateNullID(drink_tag_id_one, "drink_tag_id_one");
        this.validateNullID(drink_tag_id_two, "drink_tag_id_two");
        this.validateNullID(drink_tag_id_three, "drink_tag_id_three");
        this.validateNullID(drink_tag_id_four, "drink_tag_id_four");
        this.validateNullID(drink_tag_id_five, "drink_tag_id_five");
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
                spirit_ids,
                drink_serve_temp,
                servings,
                icon_enum,
                nutritional_fact_id,
                drink_tag_id_one,
                drink_tag_id_two,
                drink_tag_id_three,
                drink_tag_id_four,
                drink_tag_id_five);
    }

    public int createDrinkCategory(
            String cookie,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        // Validate input parameters.
        // Don't validate description for null. Null is ok.
        this.validateString(cookie, "cookie");
        this.validateHexColor(hex_color);
        this.validateString(name, "name");
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.createDrinkCategory(
                cookie,
                name,
                hex_color,
                description);
    }

    public boolean updateDrinkCategory(
            String cookie,
            int id,
            String name,
            String hex_color,
            String description
    ) throws Exception {
        // Validate input parameters.
        // Don't validate description for null. Null is ok.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name.");
        this.validateHexColor(hex_color);
        // Initialize model and create data.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.updateDrinkCategory(
                cookie,
                id,
                name,
                hex_color,
                description);
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

    public int createVendorDrinkTag (
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
        // Initialize model and create beer tag.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.createVendorDrinkTag(
                cookie,
                name,
                hex_color,
                tag_type
        );
    }

    public Boolean updateVendorDrinkTag (
            String cookie,
            int id,
            String new_name,
            String hex_color,
            String tag_type
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "id");
        this.validateString(new_name, "new_name");
        this.validateHexColor(hex_color);
        this.validateMenuItemTagType(tag_type);
        // Initialize model and return model response.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.updateVendorDrinkTag(
                cookie,
                id,
                new_name,
                hex_color,
                tag_type
        );
    }

    public Boolean deleteVendorDrinkTag (
            String cookie,
            int id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "id");
        // Initialize model and return model response.
        DrinkModel drinkModel = new DrinkModel();
        return drinkModel.deleteVendorDrinkTag(
                cookie,
                id
        );
    }

}

