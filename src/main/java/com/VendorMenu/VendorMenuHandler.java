package com.VendorMenu;

import com.VendorMenu.Beers.BeerController;
import com.VendorMenu.Drinks.DrinkController;
import com.VendorMenu.Foods.FoodController;
import com.VendorMenu.Ingredients.IngredientsController;
import jnr.ffi.annotations.In;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class VendorMenuHandler {

    /* BEER SECTION ------------------------------------------------------------
    ██████╗ ███████╗███████╗██████╗
    ██╔══██╗██╔════╝██╔════╝██╔══██╗
    ██████╔╝█████╗  █████╗  ██████╔╝
    ██╔══██╗██╔══╝  ██╔══╝  ██╔══██╗
    ██████╔╝███████╗███████╗██║  ██║
    ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
    ------------------------------------------------------------*/

    /*------------------------------------------------------------
    BEER MENU
    ------------------------------------------------------------*/

    @WebMethod
    public int createBeer(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "color") int color,
            @WebParam(name = "bitterness") int bitterness,
            @WebParam(name = "abv") int abv,
            @WebParam(name = "beer_style") String beer_style,
            @WebParam(name = "beer_tastes") String[] beer_tastes,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "beer_sizes") String[] beer_sizes,
            @WebParam(name = "hop_score") String hop_score,
            @WebParam(name = "beer_category_id") int beer_category_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.createBeer(
                cookie,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes,
                hop_score,
                beer_category_id
        );
    }

    @WebMethod
    public boolean updateBeer(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "color") int color,
            @WebParam(name = "bitterness") int bitterness,
            @WebParam(name = "abv") int abv,
            @WebParam(name = "beer_style") String beer_style,
            @WebParam(name = "beer_tastes") String[] beer_tastes,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "beer_sizes") String[] beer_sizes,
            @WebParam(name = "hop_score") String hop_score,
            @WebParam(name = "beer_category_id") int beer_category_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeer(
                cookie,
                id,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes,
                hop_score,
                beer_category_id
        );
    }

    @WebMethod
    public boolean deleteBeer(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeer(
                cookie,
                id
        );
    }

    /*------------------------------------------------------------
    BEER CATEGORIES
    ------------------------------------------------------------*/

    @WebMethod
    public int createBeerCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="category_name") String category_name,
            @WebParam(name="hex_color") String hex_color
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.createBeerCategory(
                cookie,
                category_name,
                hex_color
        );
    }

    @WebMethod
    public boolean updateBeerCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="new_category_name") String new_category_name,
            @WebParam(name="new_hex_color") String new_hex_color
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeerCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color
        );
    }

    @WebMethod
    public boolean deleteBeerCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeerCategory(
                cookie,
                id
        );
    }

    /*------------------------------------------------------------
    BEER IMAGES
    ------------------------------------------------------------*/

    @WebMethod
    public String uploadBeerImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "filename") String filename,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.uploadBeerImage(
                cookie,
                filename,
                beer_id
        );
    }

    @WebMethod
    public boolean updateBeerImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_image_id") int beer_image_id,
            @WebParam(name = "display_order") int display_order
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeerImage(
                cookie,
                beer_image_id,
                display_order
        );
    }

    @WebMethod
    public String deleteBeerImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_image_id") int beer_image_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeerImage(
                cookie,
                beer_image_id
        );
    }

    /* FOOD SECTION ------------------------------------------------------------
    ███████╗ ██████╗  ██████╗ ██████╗
    ██╔════╝██╔═══██╗██╔═══██╗██╔══██╗
    █████╗  ██║   ██║██║   ██║██║  ██║
    ██╔══╝  ██║   ██║██║   ██║██║  ██║
    ██║     ╚██████╔╝╚██████╔╝██████╔╝
    ╚═╝      ╚═════╝  ╚═════╝ ╚═════╝
     ------------------------------------------------------------*/

    /*------------------------------------------------------------
    FOOD MENU
    ------------------------------------------------------------*/

    @WebMethod
    public int createFood(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "food_sizes") String[] food_sizes,
            @WebParam(name = "food_category_id") int food_category_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes,
                food_category_id
        );
    }

    @WebMethod
    public boolean updateFood(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "food_sizes") String[] food_sizes,
            @WebParam(name = "food_category_id") int food_category_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes,
                food_category_id
        );
    }

    @WebMethod
    public boolean deleteFood(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteFood(
                cookie,
                id
        );
    }

    /*------------------------------------------------------------
    FOOD CATEGORIES
    ------------------------------------------------------------*/

    @WebMethod
    public int createFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="category_name") String category_name,
            @WebParam(name="hex_color") String hex_color
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createFoodCategory(
                cookie,
                category_name,
                hex_color
        );
    }

    @WebMethod
    public boolean updateFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="new_category_name") String new_category_name,
            @WebParam(name="new_hex_color") String new_hex_color
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFoodCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color
        );
    }

    @WebMethod
    public boolean deleteFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteFoodCategory(
                cookie,
                id
        );
    }

    /*------------------------------------------------------------
    FOOD IMAGES
    ------------------------------------------------------------*/

    @WebMethod
    public String uploadVendorFoodImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "filename") String filename,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.uploadVendorFoodImage(
                cookie,
                filename,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean updateVendorFoodImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_image_id") int food_image_id,
            @WebParam(name = "display_order") int display_order
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateVendorFoodImage(
                cookie,
                food_image_id,
                display_order
        );
    }

    @WebMethod
    public String deleteVendorFoodImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_image_id") int food_image_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteVendorFoodImage(
                cookie,
                food_image_id
        );
    }


    /* DRINK SECTION ------------------------------------------------------------
    ██████╗ ██████╗ ██╗███╗   ██╗██╗  ██╗███████╗
    ██╔══██╗██╔══██╗██║████╗  ██║██║ ██╔╝██╔════╝
    ██║  ██║██████╔╝██║██╔██╗ ██║█████╔╝ ███████╗
    ██║  ██║██╔══██╗██║██║╚██╗██║██╔═██╗ ╚════██║
    ██████╔╝██║  ██║██║██║ ╚████║██║  ██╗███████║
    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝
     ------------------------------------------------------------*/

    /*------------------------------------------------------------
    DRINK MENU
    ------------------------------------------------------------*/

    @WebMethod
    public int createDrink(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "drink_category_id") int drink_category_id,
            @WebParam(name = "hex_one") String hex_one,
            @WebParam(name = "hex_two") String hex_two,
            @WebParam(name = "hex_three") String hex_three,
            @WebParam(name = "hex_background") String hex_background,
            @WebParam(name = "spirit_ids") int[] spirit_ids,
            @WebParam(name="drink_serve_temp") String drink_serve_temp,
            @WebParam(name="servings") String servings,
            @WebParam(name="icon_enum") String icon_enum
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.createDrink(
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
                icon_enum);
    }

    @WebMethod
    public boolean updateDrink(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "price") float price,
            @WebParam(name = "drink_category_id") int drink_category_id,
            @WebParam(name = "hex_one") String hex_one,
            @WebParam(name = "hex_two") String hex_two,
            @WebParam(name = "hex_three") String hex_three,
            @WebParam(name = "hex_background") String hex_background,
            @WebParam(name = "spirit_ids") int[] spirit_ids,
            @WebParam(name="drink_serve_temp") String drink_serve_temp,
            @WebParam(name="servings") String servings,
            @WebParam(name="icon_enum") String icon_enum
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.updateDrink(
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
                icon_enum);
    }

    @WebMethod
    public boolean deleteDrink(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.deleteDrink(
                cookie,
                id);
    }

    /*------------------------------------------------------------
    DRINK CATEGORIES
    ------------------------------------------------------------*/

    @WebMethod
    public int createDrinkCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="category_name") String category_name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="icon_enum") String icon_enum
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.createDrinkCategory(
                cookie,
                category_name,
                hex_color,
                icon_enum);
    }

    @WebMethod
    public boolean updateDrinkCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="new_category_name") String new_category_name,
            @WebParam(name="new_hex_color") String new_hex_color,
            @WebParam(name="new_icon_enum") String new_icon_enum
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.updateDrinkCategory(
                cookie,
                id,
                new_category_name,
                new_hex_color,
                new_icon_enum);
    }

    @WebMethod
    public boolean deleteDrinkCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.deleteDrinkCategory(
                cookie,
                id);
    }

    /*------------------------------------------------------------
    DRINK IMAGES
    ------------------------------------------------------------*/

    @WebMethod
    public String uploadVendorDrinkImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "filename") String filename,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.uploadVendorDrinkImage(
                cookie,
                filename,
                vendor_drink_id);
    }

    @WebMethod
    public boolean updateVendorDrinkImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_image_id") int drink_image_id,
            @WebParam(name = "display_order") int display_order
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.updateVendorDrinkImage(
                cookie,
                drink_image_id,
                display_order);
    }

    @WebMethod
    public String deleteVendorDrinkImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_image_id") int drink_image_id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.deleteVendorDrinkImage(
            cookie,
            drink_image_id);
    }

    /* BEER SECTION ------------------------------------------------------------
    ██╗███╗   ██╗ ██████╗ ██████╗ ███████╗██████╗ ██╗███████╗███╗   ██╗████████╗███████╗
    ██║████╗  ██║██╔════╝ ██╔══██╗██╔════╝██╔══██╗██║██╔════╝████╗  ██║╚══██╔══╝██╔════╝
    ██║██╔██╗ ██║██║  ███╗██████╔╝█████╗  ██║  ██║██║█████╗  ██╔██╗ ██║   ██║   ███████╗
    ██║██║╚██╗██║██║   ██║██╔══██╗██╔══╝  ██║  ██║██║██╔══╝  ██║╚██╗██║   ██║   ╚════██║
    ██║██║ ╚████║╚██████╔╝██║  ██║███████╗██████╔╝██║███████╗██║ ╚████║   ██║   ███████║
    ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═════╝ ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚══════╝
    ------------------------------------------------------------*/

    /*------------------------------------------------------------
    FOOD INGREDIENTS
    ------------------------------------------------------------*/
    @WebMethod
    public int createFoodIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createFoodIngredient(
                cookie,
                name,
                description,
                source,
                keywords
        );
    }

    @WebMethod
    public boolean updateFoodIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_ingredient_id") int food_ingredient_id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.updateFoodIngredient(
                cookie,
                food_ingredient_id,
                name,
                description,
                source,
                keywords
        );
    }

    @WebMethod
    public boolean deleteFoodIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_ingredient_id") int food_ingredient_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteFoodIngredient(
                cookie,
                food_ingredient_id
        );
    }

    @WebMethod
    public boolean createFoodIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_ingredient_id") int food_ingredient_id,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createFoodIngredientAssociation(
                cookie,
                food_ingredient_id,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean deleteFoodIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "food_ingredient_id") int food_ingredient_id,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteFoodIngredientAssociation(
                cookie,
                food_ingredient_id,
                vendor_food_id
        );
    }
    /*------------------------------------------------------------
    DRINK INGREDIENTS
    ------------------------------------------------------------*/
    @WebMethod
    public int createDrinkIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createDrinkIngredient(
                cookie,
                name,
                description,
                source,
                keywords
        );
    }

    @WebMethod
    public boolean updateDrinkIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_ingredient_id") int drink_ingredient_id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.updateDrinkIngredient(
                cookie,
                drink_ingredient_id,
                name,
                description,
                source,
                keywords
        );
    }

    @WebMethod
    public boolean deleteDrinkIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_ingredient_id") int drink_ingredient_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteDrinkIngredient(
                cookie,
                drink_ingredient_id
        );
    }

    @WebMethod
    public boolean createDrinkIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_ingredient_id") int drink_ingredient_id,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return  ingredientsController.createDrinkIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_drink_id
        );
    }

    @WebMethod
    public boolean deleteDrinkIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_ingredient_id") int drink_ingredient_id,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteDrinkIngredientAssociation(
                cookie,
                drink_ingredient_id,
                vendor_drink_id
        );
    }
    /*------------------------------------------------------------
    BEER INGREDIENTS
    ------------------------------------------------------------*/
    @WebMethod
    public int createBeerIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createBeerIngredient(
                cookie,
                name,
                description,
                source,
                keywords
        );
    }

    @WebMethod
    public boolean updateBeerIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_ingredient_id") int beer_ingredient_id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.updateBeerIngredient(
                cookie,
                beer_ingredient_id,
                name,
                description,
                source,
                keywords
        );
    }

    @WebMethod
    public boolean deleteBeerIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_ingredient_id") int beer_ingredient_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteBeerIngredient(
                cookie,
                beer_ingredient_id
        );
    }

    @WebMethod
    public boolean createBeerIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_ingredient_id") int beer_ingredient_id,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createBeerIngredientAssociation(
                cookie,
                beer_ingredient_id,
                beer_id
        );
    }

    @WebMethod
    public boolean deleteBeerIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_ingredient_id") int beer_ingredient_id,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteBeerIngredientAssociation(
                cookie,
                beer_ingredient_id,
                beer_id
        );
    }
}
