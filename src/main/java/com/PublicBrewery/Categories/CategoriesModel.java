// @TODO
package com.PublicBrewery.Categories;

import com.Common.AbstractModel;
import com.Common.VendorDrinkCategory;
import com.Common.VendorFoodCategory;

import java.util.HashMap;

public class CategoriesModel extends AbstractModel {

    public CategoriesModel() throws Exception {}

    public HashMap<Integer,VendorFoodCategory> loadFoodCategories() {
        return new HashMap<Integer, VendorFoodCategory>();
    }

    public HashMap<Integer,VendorDrinkCategory> loadDrinkCategories() {
        return new HashMap<Integer, VendorDrinkCategory>();
    }
}
