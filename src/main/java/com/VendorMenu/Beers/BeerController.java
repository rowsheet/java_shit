package com.VendorMenu.Beers;

import com.Common.AbstractController;
import com.Common.Beer;

import java.awt.peer.SystemTrayPeer;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerController extends AbstractController {

    public boolean deleteBeer(
            String cookie,
            int id
    ) throws Exception {
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeer(
                cookie,
                id
        );
    }

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

    public String uploadBeerImage (
        String cookie,
        String filename,
        int beer_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(filename, "filename");
        // Initialze model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.uploadBeerImage(
                cookie,
                filename,
                beer_id
        );
    }

    public boolean updateBeerImage (
            String cookie,
            int beer_image_id,
            int display_order
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(beer_image_id, "beer_image_id");
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.updateBeerImage(
                cookie,
                beer_image_id,
                display_order
        );
    }

    public String deleteBeerImage (
            String cookie,
            int beer_image_id
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(beer_image_id, "beer_image_id");
        // Initialize model and create data.
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteBeerImage(
                cookie,
                beer_image_id
        );
    }

}
