package com.VendorMenu;
import com.Common.AbstractModel;
import com.Common.VendorDropdownContainer;
import com.VendorMenu.Beers.BeerController;
import com.VendorMenu.Drinks.DrinkController;
import com.VendorMenu.Extras.ExtrasController;
import com.VendorMenu.Extras.ExtrasException;
import com.VendorMenu.Extras.ExtrasModel;
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
            @WebParam(name = "beer_category_id") int beer_category_id,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "beer_tag_id_one") int beer_tag_id_one,
            @WebParam(name = "beer_tag_id_two") int beer_tag_id_two,
            @WebParam(name = "beer_tag_id_three") int beer_tag_id_three,
            @WebParam(name = "beer_tag_id_four") int beer_tag_id_four,
            @WebParam(name = "beer_tag_id_five") int beer_tag_id_five
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
                beer_category_id,
                nutritional_fact_id,
                beer_tag_id_one,
                beer_tag_id_two,
                beer_tag_id_three,
                beer_tag_id_four,
                beer_tag_id_five
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
            @WebParam(name = "beer_category_id") int beer_category_id,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "beer_tag_id_one") int beer_tag_id_one,
            @WebParam(name = "beer_tag_id_two") int beer_tag_id_two,
            @WebParam(name = "beer_tag_id_three") int beer_tag_id_three,
            @WebParam(name = "beer_tag_id_four") int beer_tag_id_four,
            @WebParam(name = "beer_tag_id_five") int beer_tag_id_five
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
                beer_category_id,
                nutritional_fact_id,
                beer_tag_id_one,
                beer_tag_id_two,
                beer_tag_id_three,
                beer_tag_id_four,
                beer_tag_id_five
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
            @WebParam(name="name") String name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="description") String description
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.createBeerCategory(
                cookie,
                name,
                hex_color,
                description
        );
    }

    @WebMethod
    public boolean updateBeerCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="name") String name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="description") String description
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeerCategory(
                cookie,
                id,
                name,
                hex_color,
                description
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
    public int uploadBeerImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "file_path") String file_path,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.uploadBeerImage(
                cookie,
                file_path,
                beer_id
        );
    }

    @WebMethod
    public boolean updateBeerImages (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_ids") int[] image_ids
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeerImages(
                cookie,
                image_ids
        );
    }

    // @TODO Implement in model.
    @WebMethod
    public boolean deleteBeerImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_id") int image_id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeerImage(
                cookie,
                image_id
        );
    }

    /*------------------------------------------------------------
    BEER TAGS
    ------------------------------------------------------------*/

    @WebMethod
    public int createBeerTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color,
            @WebParam(name = "tag_type") String tag_type
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.createBeerTag(
                cookie,
                name,
                hex_color,
                tag_type
        );
    }

    @WebMethod
    public boolean updateBeerTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color,
            @WebParam(name = "tag_type") String tag_type
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.updateBeerTag(
                cookie,
                id,
                name,
                hex_color,
                tag_type
        );
    }

    @WebMethod
    public boolean deleteBeerTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        BeerController beerController = new BeerController();
        return beerController.deleteBeerTag(
                cookie,
                id
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
            @WebParam(name = "food_category_id") int food_category_id,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "food_tag_id_one") int food_tag_id_one,
            @WebParam(name = "food_tag_id_two") int food_tag_id_two,
            @WebParam(name = "food_tag_id_three") int food_tag_id_three,
            @WebParam(name = "food_tag_id_four") int food_tag_id_four,
            @WebParam(name = "food_tag_id_five") int food_tag_id_five,
            @WebParam(name = "cover_image") String cover_image
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createFood(
                cookie,
                name,
                description,
                price,
                food_sizes,
                food_category_id,
                nutritional_fact_id,
                food_tag_id_one,
                food_tag_id_two,
                food_tag_id_three,
                food_tag_id_four,
                food_tag_id_five,
                cover_image
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
            @WebParam(name = "food_category_id") int food_category_id,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "food_tag_id_one") int food_tag_id_one,
            @WebParam(name = "food_tag_id_two") int food_tag_id_two,
            @WebParam(name = "food_tag_id_three") int food_tag_id_three,
            @WebParam(name = "food_tag_id_four") int food_tag_id_four,
            @WebParam(name = "food_tag_id_five") int food_tag_id_five
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFood(
                cookie,
                id,
                name,
                description,
                price,
                food_sizes,
                food_category_id,
                nutritional_fact_id,
                food_tag_id_one,
                food_tag_id_two,
                food_tag_id_three,
                food_tag_id_four,
                food_tag_id_five
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
            @WebParam(name="name") String name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="description") String description
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createFoodCategory(
                cookie,
                name,
                hex_color,
                description
        );
    }

    @WebMethod
    public boolean updateFoodCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="name") String name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="description") String description
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateFoodCategory(
                cookie,
                id,
                name,
                hex_color,
                description
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
    public int uploadVendorFoodImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "file_path") String file_path,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.uploadVendorFoodImage(
                cookie,
                file_path,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean updateVendorFoodImages(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_ids") int[] image_ids
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateVendorFoodImages(
                cookie,
                image_ids
        );
    }

    @WebMethod
    public boolean deleteVendorFoodImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_id") int image_id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteVendorFoodImage(
                cookie,
                image_id
        );
    }

    /*------------------------------------------------------------
    FOOD TAGS
    ------------------------------------------------------------*/

    @WebMethod
    public int createFoodTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color,
            @WebParam(name = "tag_type") String tag_type
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.createVendorFoodTag(
                cookie,
                name,
                hex_color,
                tag_type
        );
    }

    @WebMethod
    public boolean updateFoodTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color,
            @WebParam(name = "tag_type") String tag_type
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.updateVendorFoodTag(
                cookie,
                id,
                name,
                hex_color,
                tag_type
        );
    }

    @WebMethod
    public boolean deleteFoodTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        FoodController foodController = new FoodController();
        return foodController.deleteVendorFoodTag(
                cookie,
                id
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
            @WebParam(name = "drink_serve_temp") String drink_serve_temp,
            @WebParam(name = "servings") String servings,
            @WebParam(name = "icon_enum") String icon_enum,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "drink_tag_id_one") int drink_tag_id_one,
            @WebParam(name = "drink_tag_id_two") int drink_tag_id_two,
            @WebParam(name = "drink_tag_id_three") int drink_tag_id_three,
            @WebParam(name = "drink_tag_id_four") int drink_tag_id_four,
            @WebParam(name = "drink_tag_id_five") int drink_tag_id_five
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
                icon_enum,
                nutritional_fact_id,
                drink_tag_id_one,
                drink_tag_id_two,
                drink_tag_id_three,
                drink_tag_id_four,
                drink_tag_id_five
        );
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
            @WebParam(name = "drink_serve_temp") String drink_serve_temp,
            @WebParam(name = "servings") String servings,
            @WebParam(name = "icon_enum") String icon_enum,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "drink_tag_id_one") int drink_tag_id_one,
            @WebParam(name = "drink_tag_id_two") int drink_tag_id_two,
            @WebParam(name = "drink_tag_id_three") int drink_tag_id_three,
            @WebParam(name = "drink_tag_id_four") int drink_tag_id_four,
            @WebParam(name = "drink_tag_id_five") int drink_tag_id_five
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
                icon_enum,
                nutritional_fact_id,
                drink_tag_id_one,
                drink_tag_id_two,
                drink_tag_id_three,
                drink_tag_id_four,
                drink_tag_id_five
        );
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
            @WebParam(name="name") String name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="description") String description
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.createDrinkCategory(
                cookie,
                name,
                hex_color,
                description);
    }

    @WebMethod
    public boolean updateDrinkCategory (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="name") String name,
            @WebParam(name="hex_color") String hex_color,
            @WebParam(name="description") String description
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.updateDrinkCategory(
                cookie,
                id,
                name,
                hex_color,
                description);
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
    public int uploadVendorDrinkImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "file_path") String file_path,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.uploadVendorDrinkImage(
                cookie,
                file_path,
                vendor_drink_id);
    }

    @WebMethod
    public boolean updateVendorDrinkImages (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_ids") int[] image_ids
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.updateVendorDrinkImages(
                cookie,
                image_ids);
    }

    @WebMethod
    public boolean deleteVendorDrinkImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_id") int image_id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.deleteVendorDrinkImage(
            cookie,
            image_id);
    }

    /*------------------------------------------------------------
    DRINK TAGS
    ------------------------------------------------------------*/

    @WebMethod
    public int createDrinkTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color,
            @WebParam(name = "tag_type") String tag_type
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.createVendorDrinkTag(
                cookie,
                name,
                hex_color,
                tag_type
        );
    }

    @WebMethod
    public boolean updateDrinkTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color,
            @WebParam(name = "tag_type") String tag_type
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.updateVendorDrinkTag(
                cookie,
                id,
                name,
                hex_color,
                tag_type
        );
    }

    @WebMethod
    public boolean deleteDrinkTag(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        DrinkController drinkController = new DrinkController();
        return drinkController.deleteVendorDrinkTag(
                cookie,
                id
        );
    }

    /* INGREDIENTS SECTION ------------------------------------------------------------
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
            @WebParam(name = "key_words") String[] keywords,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "tag_one") int tag_one,
            @WebParam(name = "tag_two") int tag_two,
            @WebParam(name = "tag_three") int tag_three,
            @WebParam(name = "tag_four") int tag_four,
            @WebParam(name = "tag_five") int tag_five
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createFoodIngredient(
                cookie,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
    }

    @WebMethod
    public boolean updateFoodIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "tag_one") int tag_one,
            @WebParam(name = "tag_two") int tag_two,
            @WebParam(name = "tag_three") int tag_three,
            @WebParam(name = "tag_four") int tag_four,
            @WebParam(name = "tag_five") int tag_five
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.updateFoodIngredient(
                cookie,
                id,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
    }

    @WebMethod
    public boolean deleteFoodIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteFoodIngredient(
                cookie,
                id
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
    FOOD INGREDIENTS (READ)
    Note: Ingredients reads are vendor-privileged requests.
    ------------------------------------------------------------*/

    @WebMethod
    public String loadFoodIngredients(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.loadFoodIngredients(
                cookie
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
            @WebParam(name = "key_words") String[] keywords,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "tag_one") int tag_one,
            @WebParam(name = "tag_two") int tag_two,
            @WebParam(name = "tag_three") int tag_three,
            @WebParam(name = "tag_four") int tag_four,
            @WebParam(name = "tag_five") int tag_five
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createDrinkIngredient(
                cookie,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
    }

    @WebMethod
    public boolean updateDrinkIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "tag_one") int tag_one,
            @WebParam(name = "tag_two") int tag_two,
            @WebParam(name = "tag_three") int tag_three,
            @WebParam(name = "tag_four") int tag_four,
            @WebParam(name = "tag_five") int tag_five
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.updateDrinkIngredient(
                cookie,
                id,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
    }

    @WebMethod
    public boolean deleteDrinkIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteDrinkIngredient(
                cookie,
                id
        );
    }

    @WebMethod
    public boolean createDrinkIngredientAssociation(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "drink_ingredient_id") int drink_ingredient_id,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createDrinkIngredientAssociation(
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
    DRINK INGREDIENTS (READ)
    Note: Ingredients reads are vendor-privileged requests.
    ------------------------------------------------------------*/

    @WebMethod
    public String loadDrinkIngredients(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.loadDrinkIngredients(
                cookie
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
            @WebParam(name = "key_words") String[] keywords,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "tag_one") int tag_one,
            @WebParam(name = "tag_two") int tag_two,
            @WebParam(name = "tag_three") int tag_three,
            @WebParam(name = "tag_four") int tag_four,
            @WebParam(name = "tag_five") int tag_five
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.createBeerIngredient(
                cookie,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
    }

    @WebMethod
    public boolean updateBeerIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id,
            @WebParam(name = "name") String name,
            @WebParam(name = "description") String description,
            @WebParam(name = "source") String source,
            @WebParam(name = "key_words") String[] keywords,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "tag_one") int tag_one,
            @WebParam(name = "tag_two") int tag_two,
            @WebParam(name = "tag_three") int tag_three,
            @WebParam(name = "tag_four") int tag_four,
            @WebParam(name = "tag_five") int tag_five
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.updateBeerIngredient(
                cookie,
                id,
                name,
                description,
                source,
                keywords,
                nutritional_fact_id,
                tag_one,
                tag_two,
                tag_three,
                tag_four,
                tag_five
        );
    }

    @WebMethod
    public boolean deleteBeerIngredient(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.deleteBeerIngredient(
                cookie,
                id
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

    /*------------------------------------------------------------
    BEER INGREDIENTS (READ)
    Note: Ingredients reads are vendor-privileged requests.
    ------------------------------------------------------------*/

    @WebMethod
    public String loadBeerIngredients(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        IngredientsController ingredientsController = new IngredientsController();
        return ingredientsController.loadBeerIngredients(
                cookie
        );
    }

    /* EXTRAS SECTION ------------------------------------------------------------
    ███████╗██╗  ██╗████████╗██████╗  █████╗ ███████╗
    ██╔════╝╚██╗██╔╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝
    █████╗   ╚███╔╝    ██║   ██████╔╝███████║███████╗
    ██╔══╝   ██╔██╗    ██║   ██╔══██╗██╔══██║╚════██║
    ███████╗██╔╝ ██╗   ██║   ██║  ██║██║  ██║███████║
    ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
    ------------------------------------------------------------*/

    @WebMethod
    public String loadVendorDropdowns(
            @WebParam(name="cookie") String cookie
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.loadVendorDropdowns(
                cookie
        );
    }

    /*------------------------------------------------------------
    NUTRITIONAL FACTS
    ------------------------------------------------------------*/

    @WebMethod
    public int createNutritionalFact(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="profile_name") String profile_name,
            @WebParam(name="serving_size") int serving_size,
            @WebParam(name="calories") int calories,
            @WebParam(name="calories_from_fat") int calories_from_fat,
            @WebParam(name="total_fat") int total_fat,
            @WebParam(name="saturated_fat") int saturated_fat,
            @WebParam(name="trans_fat") int trans_fat,
            @WebParam(name="cholesterol") int cholesterol,
            @WebParam(name="sodium") int sodium,
            @WebParam(name="total_carbs") int total_carbs,
            @WebParam(name="dietary_fiber") int dietary_fiber,
            @WebParam(name="sugar") int sugar,
            @WebParam(name="protein") int protein,
            @WebParam(name="vitamin_a") int vitamin_a,
            @WebParam(name="vitamin_b") int vitamin_b,
            @WebParam(name="vitamin_c") int vitamin_c,
            @WebParam(name="vitamin_d") int vitamin_d,
            @WebParam(name="calcium") int calcium,
            @WebParam(name="iron") int iron
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.createNutritionalFact(
                cookie,
                profile_name,
                serving_size,
                calories,
                calories_from_fat,
                total_fat,
                saturated_fat,
                trans_fat,
                cholesterol,
                sodium,
                total_carbs,
                dietary_fiber,
                sugar,
                protein,
                vitamin_a,
                vitamin_b,
                vitamin_c,
                vitamin_d,
                calcium,
                iron
        );
    }

    @WebMethod
    public boolean updateNutritionalFact(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="id") int id,
            @WebParam(name="profile_name") String profile_name,
            @WebParam(name="serving_size") int serving_size,
            @WebParam(name="calories") int calories,
            @WebParam(name="calories_from_fat") int calories_from_fat,
            @WebParam(name="total_fat") int total_fat,
            @WebParam(name="saturated_fat") int saturated_fat,
            @WebParam(name="trans_fat") int trans_fat,
            @WebParam(name="cholesterol") int cholesterol,
            @WebParam(name="sodium") int sodium,
            @WebParam(name="total_carbs") int total_carbs,
            @WebParam(name="dietary_fiber") int dietary_fiber,
            @WebParam(name="sugar") int sugar,
            @WebParam(name="protein") int protein,
            @WebParam(name="vitamin_a") int vitamin_a,
            @WebParam(name="vitamin_b") int vitamin_b,
            @WebParam(name="vitamin_c") int vitamin_c,
            @WebParam(name="vitamin_d") int vitamin_d,
            @WebParam(name="calcium") int calcium,
            @WebParam(name="iron") int iron
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.updateNutritionalFact(
                cookie,
                id,
                profile_name,
                serving_size,
                calories,
                calories_from_fat,
                total_fat,
                saturated_fat,
                trans_fat,
                cholesterol,
                sodium,
                total_carbs,
                dietary_fiber,
                sugar,
                protein,
                vitamin_a,
                vitamin_b,
                vitamin_c,
                vitamin_d,
                calcium,
                iron
        );
    }

    @WebMethod
    public boolean deleteNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "id") int id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.deleteNutritionalFact(
                cookie,
                id
        );
    }

    @WebMethod
    public boolean associateFoodNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.associateFoodNutritionalFact(
                cookie,
                nutritional_fact_id,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean disassociateFoodNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "vendor_food_id") int vendor_food_id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return  extrasController.disassociateFoodNutritionalFact(
                cookie,
                vendor_food_id
        );
    }

    @WebMethod
    public boolean associateDrinkNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.associateDrinkNutritionalFact(
                cookie,
                nutritional_fact_id,
                vendor_drink_id
        );
    }

    @WebMethod
    public boolean disassociateDrinkNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "vendor_drink_id") int vendor_drink_id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.disassociateDrinkNutritionalFact(
                cookie,
                vendor_drink_id
        );
    }

    @WebMethod
    public boolean associateBeerNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "nutritional_fact_id") int nutritional_fact_id,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return  extrasController.associateBeerNutritionalFact(
                cookie,
                nutritional_fact_id,
                beer_id
        );
    }

    @WebMethod
    public boolean disassociateBeerNutritionalFact(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "beer_id") int beer_id
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.disassociateBeerNutritionalFact(
                cookie,
                beer_id
        );
    }

    /*------------------------------------------------------------
    NUTRITIONAL FACTS (READ)
    Note: Nutritional facts reads are vendor-privileged requests.
    ------------------------------------------------------------*/

    @WebMethod
    public String loadNutritionalFacts(
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        ExtrasController extrasController = new ExtrasController();
        return extrasController.loadNutritionalFacts(
                cookie
        );
    }

}
