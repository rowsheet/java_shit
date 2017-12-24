package com.VendorMenu.Ingredients;

import com.Common.AbstractController;
import com.Common.AbstractModel;

public class IngredientsController extends AbstractController {
    /*------------------------------------------------------------
    FOOD INGREDIENTS
    ------------------------------------------------------------*/
    public int createFoodIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createFoodIngredient(
                cookie,
                name,
                description,
                source,
                keywords
        );
    }

    public boolean updateFoodIngredient(
            String cookie,
            int food_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        this.validateID(food_ingredient_id, "food_ingredient_id");
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.updateFoodIngredient(
                cookie,
                food_ingredient_id,
                name,
                description,
                source,
                keywords
        );
    }

    public boolean deleteFoodIngredient(
            String cookie,
            int food_ingredient_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(food_ingredient_id, "food_ingredient_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteFoodIngredient(
                cookie,
                food_ingredient_id
        );
    }

    public boolean createFoodIngredientAssociation(
            String cookie,
            int food_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(food_ingredient_id, "food_ingredient_id");
        this.validateID(vendor_food_id, "vendor_food_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createFoodIngredientAssociation(
                cookie,
                food_ingredient_id,
                vendor_food_id
        );
    }

    public boolean deleteFoodIngredientAssociation(
            String cookie,
            int food_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(food_ingredient_id, "food_ingredient_id");
        this.validateID(vendor_food_id, "vendor_food_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteFoodIngredientAssociation(
                cookie,
                food_ingredient_id,
                vendor_food_id
        );
    }
    /*------------------------------------------------------------
    DRINK INGREDIENTS
    ------------------------------------------------------------*/
    public int createDrinkIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createDrinkIngredient(
                cookie,
                name,
                description,
                source,
                keywords
        );
    }

    public boolean updateDrinkIngredient(
            String cookie,
            int drink_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        this.validateID(drink_ingredient_id, "drink_ingredient_id");
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.updateDrinkIngredient(
                cookie,
                drink_ingredient_id,
                name,
                description,
                source,
                keywords
        );
    }

    public boolean deleteDrinkIngredient(
            String cookie,
            int drink_ingredient_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(drink_ingredient_id, "drink_ingredient_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteDrinkIngredient(
                cookie,
                drink_ingredient_id
        );
    }

    public boolean createDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(drink_ingredient_id, "drink_ingredient_id");
        this.validateID(vendor_food_id, "vendor_food_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return  ingredientsModel.createFoodIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_food_id
        );
    }

    public boolean deleteDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(drink_ingredient_id, "drink_ingredient_id");
        this.validateID(vendor_food_id, "vendor_food_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteDrinkIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_food_id
        );
    }
    /*------------------------------------------------------------
    BEER INGREDIENTS
    ------------------------------------------------------------*/
    public int createBeerIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createBeerIngredient(
                cookie,
                name,
                description,
                source,
                keywords
        );
    }

    public boolean updateBeerIngredient(
            String cookie,
            int beer_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        this.validateID(beer_ingredient_id, "beer_ingredient_id");
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.updateBeerIngredient(
                cookie,
                beer_ingredient_id,
                name,
                description,
                source,
                keywords
        );
    }

    public boolean deleteBeerIngredient(
            String cookie,
            int beer_ingredient_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(beer_ingredient_id, "beer_ingredient_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteBeerIngredient(
                cookie,
                beer_ingredient_id
        );
    }

    public boolean createBeerIngredientAssociation(
            String cookie,
            int beer_ingredient_id,
            int beer_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(beer_ingredient_id, "beer_ingredient_id");
        this.validateID(beer_id, "beer_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createBeerIngredientAssociation(
                cookie,
                beer_ingredient_id,
                beer_id
        );
    }

    public boolean deleteBeerIngredientAssociation(
            String cookie,
            int beer_ingredient_id,
            int beer_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(beer_ingredient_id, "beer_ingredient_id");
        this.validateID(beer_id, "beer_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteBeerIngredientAssociation(
                cookie,
                beer_ingredient_id,
                beer_id
        );
    }
}
