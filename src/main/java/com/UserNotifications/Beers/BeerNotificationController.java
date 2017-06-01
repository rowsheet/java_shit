package com.UserNotifications.Beers;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BeerNotificationController extends AbstractController {
    public int createBeerNotification(
            String cookie,
            String name,
            int min_color,
            int max_color,
            int min_bitterness,
            int max_bitterness,
            int min_abv,
            int max_abv,
            String[] beer_styles,
            String[] beer_tastes
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateBeerColor(min_color);
        this.validateBeerColor(max_color);
        this.validateBitterness(min_bitterness);
        this.validateBitterness(max_bitterness);
        this.validateAbv(min_abv);
        this.validateAbv(max_abv);
        for (String beer_style: beer_styles) {
            this.validateBeerStyle(beer_style);
        }
        for (String beer_taste: beer_tastes) {
            this.validateBeerTaste(beer_taste);
        }
        // Initialize model and create the data.
        BeerNotificationModel beerNotificationModel = new BeerNotificationModel();
        return beerNotificationModel.createBeerNotification(
            cookie,
            name,
            min_color,
            max_color,
            min_bitterness,
            max_bitterness,
            min_abv,
            max_abv,
            beer_styles,
            beer_tastes
        );
    }
}

