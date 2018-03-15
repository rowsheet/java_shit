package com.Common.PublicVendor;

import com.Common.VendorDrink;
import com.Common.VendorDropdownContainer;

import java.util.HashMap;

public class DrinkMenu {
    public HashMap<Integer, VendorDrink> menuItems;
    public VendorDropdownContainer dropDowns;
    public DrinkMenu() {
        this.menuItems = new HashMap<Integer, VendorDrink>();
        this.dropDowns = new VendorDropdownContainer();
    }
}
