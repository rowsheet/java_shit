package com.VendorMenu.Extras;

import com.Common.AbstractController;

public class ExtrasController extends AbstractController {
    public int createNutritionalFact(
            String cookie,
            String profile_name,
            int serving_size,
            int calories,
            int calories_from_fat,
            int total_fat,
            int saturated_fat,
            int trans_fat,
            int cholesterol,
            int sodium,
            int total_carbs,
            int dietary_fiber,
            int sugar,
            int protein,
            int vitamin_a,
            int vitamin_b,
            int vitamin_c,
            int vitamin_d,
            int calcium,
            int iron
    ) throws Exception {
        this.validateString(cookie, "cookie");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.createNutritionalFact(
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

    public boolean updateNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            String profile_name,
            int serving_size,
            int calories,
            int calories_from_fat,
            int total_fat,
            int saturated_fat,
            int trans_fat,
            int cholesterol,
            int sodium,
            int total_carbs,
            int dietary_fiber,
            int sugar,
            int protein,
            int vitamin_a,
            int vitamin_b,
            int vitamin_c,
            int vitamin_d,
            int calcium,
            int iron
    ) throws Exception {
        this.validateString(cookie,"cookie");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.updateNutritionalFact(
                cookie,
                nutritional_fact_id,
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

    public boolean deleteNutritionalFact(
            String cookie,
            int nutritional_fact_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.deleteNutritionalFact(
            cookie,
            nutritional_fact_id
        );
    }

    public boolean associateFoodNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(nutritional_fact_id, "nutritional_fact_id");
        System.out.println(vendor_food_id);
        this.validateID(vendor_food_id, "vendor_food_id");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.associateFoodNutritionalFact(
                cookie,
                nutritional_fact_id,
                vendor_food_id
        );
    }

    public boolean disassociateFoodNutritionalFact(
            String cookie,
            int vendor_food_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(vendor_food_id, "vendor_food_id");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.disassociateFoodNutritionalFact(
                cookie,
                vendor_food_id
        );
    }

    public boolean associateDrinkNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            int vendor_drink_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(nutritional_fact_id, "nutritional_fact_id");
        this.validateID(vendor_drink_id, "vendor_drink_id");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.associateDrinkNutritionalFact(
                cookie,
                nutritional_fact_id,
                vendor_drink_id
        );
    }

    public boolean disassociateDrinkNutritionalFact(
            String cookie,
            int vendor_drink_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(vendor_drink_id, "vendor_drink_id");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.disassociateDrinkNutritionalFact(
                cookie,
                vendor_drink_id
        );
    }

    public boolean associateBeerNutritionalFact(
            String cookie,
            int nutritional_fact_id,
            int beer_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(nutritional_fact_id, "nutritional_fact_id");
        this.validateID(beer_id, "beer_id");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.associateBeerNutritionalFact(
                cookie,
                nutritional_fact_id,
                beer_id
        );
    }

    public boolean disassociateBeerNutritionalFact(
            String cookie,
            int beer_id
    ) throws Exception {
        this.validateString(cookie, "cookie");
        this.validateID(beer_id, "beer_id");
        ExtrasModel extrasModel = new ExtrasModel();
        return extrasModel.disassociateBeerNutritionalFact(
                cookie,
                beer_id
        );
    }
}
