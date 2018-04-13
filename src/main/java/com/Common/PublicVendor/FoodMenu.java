package com.Common.PublicVendor;

import com.Common.VendorDropdownContainer;
import com.Common.VendorFood;
import com.Common.VendorMedia.VendorPageImageGallery;

import java.util.HashMap;

public class FoodMenu {
    public HashMap<Integer, VendorFood> menuItems;
    public VendorDropdownContainer dropDowns;
    public VendorPageImageGallery mainGallery;
    public FoodMenu() {
        this.menuItems = new HashMap<Integer, VendorFood>();
        this.dropDowns = new VendorDropdownContainer();
        this.mainGallery = new VendorPageImageGallery();
    }
}
