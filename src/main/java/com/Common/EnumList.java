package com.Common;

import java.util.Map;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
public class EnumList {

    public final String[] beerStyles;
    public final String[] beerTastes;

    public EnumList() {
        this.beerStyles = new String[]{
            "Belgian Styles",
            "Bocks",
            "Brown Ales",
            "Dark Lagers",
            "Hybrid Beers",
            "India Pale Ales",
            "Pale Ales",
            "Pilseners and Pale Lagers",
            "Porters",
            "Scottish-Style Ales",
            "Specialty Beers",
            "Stouts",
            "Strong Ales",
            "Wheat Beers"
        };
        this.beerTastes = new String[]{
            "Sour",
            "Crisp",
            "Dark",
            "Malty",
            "Hoppy",
            "Fruity"
        };
    }
}
