package com.Common.UserFavorites;

import com.Common.VendorFood;

public class FavoriteVendorFood extends VendorFood{
    public String vendor_name;
    public String thumbnail;
    public FavoriteVendorFood() {
        super();
        this.vendor_name = "";
        this.thumbnail = "";
    }
}
