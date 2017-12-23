package com.VendorMenu.Ingredients;

import com.Common.AbstractModel;

public class IngredientsModel extends AbstractModel {

    public IngredientsModel() throws Exception {}
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
        return 1;
    }

    public boolean updateFoodIngredient(
            String cookie,
            String food_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return true;
    }

    public boolean deleteFoodIngredient(
            String cookie,
            String food_ingredient_id
    ) throws Exception {
        return true;
    }

    public boolean createFoodIngredientAssociation(
            String cookie,
            String food_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        return true;
    }

    public boolean deleteFoodIngredientAssociation(
            String cookie,
            String food_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        return true;
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
        return 1;
    }

    public boolean updateDrinkIngredient(
            String cookie,
            String drink_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return true;
    }

    public boolean deleteDrinkIngredient(
            String cookie,
            String drink_ingredient_id
    ) throws Exception {
        return true;
    }

    public boolean createDrinkIngredientAssociation(
            String cookie,
            String drink_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        return true;
    }

    public boolean deleteDrinkIngredientAssociation(
            String cookie,
            String drink_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        return true;
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
        return 1;
    }

    public boolean updateBeerIngredient(
            String cookie,
            String beer_ingredient_id,
            String name,
            String description,
            String source,
            String[] keywords
    ) throws Exception {
        return true;
    }

    public boolean deleteBeerIngredient(
            String cookie,
            String beer_ingredient_id
    ) throws Exception {
        return true;
    }

    public boolean createBeerIngredientAssociation(
            String cookie,
            String beer_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        return true;
    }

    public boolean deleteBeerIngredientAssociation(
            String cookie,
            String beer_ingredient_id,
            String vendor_food_id
    ) throws Exception {
        return true;
    }
}
