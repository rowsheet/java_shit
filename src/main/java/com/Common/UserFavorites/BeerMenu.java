package com.Common.UserFavorites;

import com.Common.Beer;

import java.util.HashMap;

public class BeerMenu {
    public HashMap<Integer, FavoriteBeer> menuItems;
    // Might add some other things like stats later.
    public BeerMenu() {
        this.menuItems = new HashMap<Integer, FavoriteBeer>();
    }
}
