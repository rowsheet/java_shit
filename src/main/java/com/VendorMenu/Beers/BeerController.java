package com.VendorMenu.Beers;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerController extends AbstractController {

    public int createBeer(
            String cookie,
            String name,
            int color,
            int bitterness,
            int abv,
            String beer_style,
            String[] beer_tastes,
            String description,
            float price,
            String[] beer_sizes
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateBeerColor(color);
        this.validateBitterness(bitterness);
        this.validateAbv(abv);
        this.validateBeerStyle(beer_style);
        for (String beer_taste: beer_tastes) {
            this.validateBeerTaste(beer_taste);
        }
        this.validateText(description, "description");
        for (String beer_size: beer_sizes) {
            this.validateBeerSize(beer_size);
        }
        // Initialize model and create the data.
        BeerModel beerModel = new BeerModel();
        return beerModel.createBeer(
                cookie,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes
        );
    }
}
