package com.UserFavorites.Beers;

import com.Common.AbstractController;
import com.Common.Beer;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
public class BeerController extends AbstractController {
    public int createUserBeerFavorite (
            String cookie,
            int beer_id
    ) throws Exception {
        BeerModel beerModel = new BeerModel();
        return beerModel.createUserBeerFavorite(
                cookie,
                beer_id
        );
    }

    public boolean deleteUserBeerFavorite (
            String cookie,
            int beer_id
    ) throws Exception {
        BeerModel beerModel = new BeerModel();
        return beerModel.deleteUserBeerFavorite(
                cookie,
                beer_id
        );
    }

    public String readUserBeerFavorites(
            String cookie,
            int limit,
            int offset
    ) throws Exception {
        BeerModel beerModel = new BeerModel();
        HashMap<Integer, Beer> beerHashMap = new HashMap<Integer, Beer>();
        beerHashMap = beerModel.readUserBeerFavorites(
                cookie,
                limit,
                offset
        );
        return this.returnJSON(beerHashMap);
    }
}
