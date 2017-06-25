package com.VendorMenu.Beers;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerController extends AbstractController {

    public boolean updateBeer (
            String cookie,
            int id,
            String name,
            int color,
            int bitterness,
            int abv,
            String beer_style,
            String[] beer_tastes,
            String description,
            float price,
            String[] beer_sizes,
            String hop_score
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(id, "beer_id");
        this.validateString(name, "name");
        this.validateBeerColor(color);
        this.validateBitterness(bitterness);
        this.validateAbv(abv);
        this.validateBeerStyle(beer_style);
        if (beer_tastes.length == 0) {
            throw new Exception("Must have at least one \"beer taste\".");
        }
        for (String beer_taste: beer_tastes) {
            this.validateBeerTaste(beer_taste);
        }
        this.validateText(description, "description");
        if (beer_sizes.length == 0) {
            throw new Exception("Must have at least one \"beer size\".");
        }
        for (String beer_size: beer_sizes) {
            this.validateBeerSize(beer_size);
        }
        this.validateHopScore(hop_score);
        this.validatePrice(price);
        // Initialize model and create the data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeer(
                cookie,
                id,
                name,
                color,
                bitterness,
                abv,
                beer_style,
                beer_tastes,
                description,
                price,
                beer_sizes,
                hop_score
        );
    }

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
            String[] beer_sizes,
            String hop_score
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateBeerColor(color);
        this.validateBitterness(bitterness);
        this.validateAbv(abv);
        this.validateBeerStyle(beer_style);
        if (beer_tastes.length == 0) {
            throw new Exception("Must have at least one \"beer taste\".");
        }
        for (String beer_taste: beer_tastes) {
            this.validateBeerTaste(beer_taste);
        }
        this.validateText(description, "description");
        if (beer_sizes.length == 0) {
            throw new Exception("Must have at least one \"beer size\".");
        }
        for (String beer_size: beer_sizes) {
            this.validateBeerSize(beer_size);
        }
        this.validateHopScore(hop_score);
        this.validatePrice(price);
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
                beer_sizes,
                hop_score
        );
    }
}
