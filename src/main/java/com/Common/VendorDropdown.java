package com.Common;

import java.util.HashMap;

public class VendorDropdown {
    public String type;
    // Although we will usually use "selectpicker" in the Bootstrap, this option is here
    // for traditional selects.
    public HashMap<String, String> options;
    public VendorDropdown() {
        this.type = "";
        this.options = new HashMap<String, String>();
    }
}
