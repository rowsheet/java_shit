package com.Common.PublicVendor;

import com.Common.Beer;
import com.Common.VendorDropdownContainer;
import com.Common.VendorMedia.VendorPageImageGallery;

import java.util.HashMap;

public class BeerMenu {
    public HashMap<Integer, Beer> menuItems;
    public VendorDropdownContainer dropDowns;
    public VendorPageImageGallery mainGallery;
    public BeerMenu() {
        this.menuItems = new HashMap<Integer, Beer>();
        this.dropDowns = new VendorDropdownContainer();
        this.mainGallery = new VendorPageImageGallery();
    }
}
