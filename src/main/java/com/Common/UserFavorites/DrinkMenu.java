package com.Common.UserFavorites;

import jnr.ffi.annotations.In;

import java.util.HashMap;

public class DrinkMenu {
    public HashMap<Integer, FavoriteVendorDrink> menuItems;
    public DrinkMenu() {
        this.menuItems = new HashMap<Integer, FavoriteVendorDrink>();
    }
}
