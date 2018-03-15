package com.UserFavorites.Places;

import com.Common.AbstractController;
import com.Common.PublicVendor.Brewery;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
public class PlacesController extends AbstractController {

    public int createUserPlaceFavorite(
            String cookie,
            int brewery_id
    ) throws Exception {
        PlacesModel placesModel = new PlacesModel();
        return placesModel.createUserPlaceFavorite(
                cookie,
                brewery_id
        );
    }

    public boolean deleteUserPlaceFavorite(
            String cookie,
            int brewery_id
    ) throws Exception {
        PlacesModel placesModel = new PlacesModel();
        return placesModel.deleteUserPlaceFavorite(
                cookie,
                brewery_id
        );
    }

    public String readUserPlaceFavorites(
            String cookie,
            int limit,
            int offset
    ) throws Exception {
        PlacesModel placesModel = new PlacesModel();
        HashMap<Integer, Brewery> breweryHashMap = new HashMap<Integer, Brewery>();
        breweryHashMap = placesModel.readUserPlaceFavorites(
                cookie,
                limit,
                offset
        );
        return this.returnJSON(breweryHashMap);
    }
}
