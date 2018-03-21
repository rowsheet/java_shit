package com.VendorMenu.Ingredients;

import com.Common.AbstractController;
import com.Common.AbstractModel;
import com.Common.BeerIngredient;
import com.Common.VendorFoodIngredient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class IngredientsController extends AbstractController {
    /*------------------------------------------------------------
    FOOD INGREDIENTS
    ------------------------------------------------------------*/
    public int createFoodIngredient(
            String cookie,
            String name,
            String description,
            String source,
            String[] keywords,
            int nutritional_fact_id,
            int tag_one,
            int tag_two,
            int tag_three,
            int tag_four,
            int tag_five
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        this.validateNullID(tag_one, "tag_one");
        this.validateNullID(tag_two, "tag_two");
        this.validateNullID(tag_three, "tag_three");
        this.validateNullID(tag_four, "tag_four");
        this.validateNullID(tag_five, "tag_five");
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createFoodIngredient(
                cookie,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
        );
    }

    public boolean updateFoodIngredient(
            String cookie,
            int id,
            String name,
            String description,
            String source,
            String[] keywords,
            int nutritional_fact_id,
            int tag_one,
            int tag_two,
            int tag_three,
            int tag_four,
            int tag_five
    ) throws Exception {
        this.validateID(id, "id");
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        this.validateNullID(tag_one, "tag_one");
        this.validateNullID(tag_two, "tag_two");
        this.validateNullID(tag_three, "tag_three");
        this.validateNullID(tag_four, "tag_four");
        this.validateNullID(tag_five, "tag_five");
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.updateFoodIngredient(
                cookie,
                id,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
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

    public String loadFoodIngredients(
            String cookie
    ) throws Exception {
        this.validateString(cookie, "cookie");
        IngredientsModel ingredientsModel = new IngredientsModel();
        ArrayList<VendorFoodIngredient> foodIngredients = new ArrayList<VendorFoodIngredient>();
        foodIngredients = ingredientsModel.loadFoodIngredients(
                cookie
        );
        return this.returnJSON(foodIngredients);
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
            int vendor_drink_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(drink_ingredient_id, "drink_ingredient_id");
        this.validateID(vendor_drink_id, "vendor_drink_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return  ingredientsModel.createDrinkIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_drink_id
        );
    }

    public boolean deleteDrinkIngredientAssociation(
            String cookie,
            int drink_ingredient_id,
            int vendor_drink_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(drink_ingredient_id, "drink_ingredient_id");
        this.validateID(vendor_drink_id, "vendor_drink_id");
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.deleteDrinkIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_drink_id
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
            String[] keywords,
            int nutritional_fact_id,
            int tag_one,
            int tag_two,
            int tag_three,
            int tag_four,
            int tag_five
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        this.validateNullID(tag_one, "tag_one");
        this.validateNullID(tag_two, "tag_two");
        this.validateNullID(tag_three, "tag_three");
        this.validateNullID(tag_four, "tag_four");
        this.validateNullID(tag_five, "tag_five");
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.createBeerIngredient(
                cookie,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
        );
    }

    public boolean updateBeerIngredient(
            String cookie,
            int id,
            String name,
            String description,
            String source,
            String[] keywords,
            int nutritional_fact_id,
            int tag_one,
            int tag_two,
            int tag_three,
            int tag_four,
            int tag_five
    ) throws Exception {
        this.validateID(id, "id");
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateString(description, "description");
        this.validateString(source, "source");
        this.validateKeywordString(keywords);
        this.validateNullID(tag_one, "tag_one");
        this.validateNullID(tag_two, "tag_two");
        this.validateNullID(tag_three, "tag_three");
        this.validateNullID(tag_four, "tag_four");
        this.validateNullID(tag_five, "tag_five");
        // Remove possible duplicate tags.
        ArrayList<Integer> tag_array = this.filterTagArray(
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
        IngredientsModel ingredientsModel = new IngredientsModel();
        return ingredientsModel.updateBeerIngredient(
                cookie,
                id,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_array.get(0),
                tag_array.get(1),
                tag_array.get(2),
                tag_array.get(3),
                tag_array.get(4)
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

    public String loadBeerIngredients(
            String cookie
    ) throws Exception {
        this.validateString(cookie, "cookie");
        IngredientsModel ingredientsModel = new IngredientsModel();
        ArrayList<BeerIngredient> beerIngredients = new ArrayList<BeerIngredient>();
        beerIngredients = ingredientsModel.loadBeerIngredients(
                cookie
        );
        return this.returnJSON(beerIngredients);
    }
}
