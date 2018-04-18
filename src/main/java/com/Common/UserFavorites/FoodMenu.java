package com.Common.UserFavorites;

import java.util.HashMap;

public class FoodMenu {
    public HashMap<Integer, FavoriteVendorFood> menuItems;
    public FoodMenu() {
        this.menuItems = new HashMap<Integer, FavoriteVendorFood>();
    }
}
