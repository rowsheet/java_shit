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
            String food_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
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
            String food_ingredient_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteFoodIngredient(
                cookie,
                food_ingredient_id
        );
    }

    public boolean createFoodIngredientAssociation(
            String cookie,
            String food_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createFoodIngredientAssociation(
                cookie,
                food_ingredient_id,
                vendor_food_id
        );
    }

    public boolean deleteFoodIngredientAssociation(
            String cookie,
            String food_ingredient_id,
            String vendor_food_id
    ) throws Exception {
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
            String drink_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
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
            String drink_ingredient_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteDrinkIngredient(
                cookie,
                drink_ingredient_id
        );
    }

    public boolean createDrinkIngredientAssociation(
            String cookie,
            String drink_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return  ingredientsModel.createFoodIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_food_id
        );
    }

    public boolean deleteDrinkIngredientAssociation(
            String cookie,
            String drink_ingredient_id,
            String vendor_food_id
    ) throws Exception {
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
            String beer_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
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
            String beer_ingredient_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteBeerIngredient(
                cookie,
                beer_ingredient_id
        );
    }

    public boolean createBeerIngredientAssociation(
            String cookie,
            String beer_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createBeerIngredientAssociation(
                cookie,
                beer_ingredient_id,
                vendor_food_id
        );
    }

    public boolean deleteBeerIngredientAssociation(
            String cookie,
            String beer_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteBeerIngredientAssociation(
                cookie,
                beer_ingredient_id,
                vendor_food_id
        );
    }
}
