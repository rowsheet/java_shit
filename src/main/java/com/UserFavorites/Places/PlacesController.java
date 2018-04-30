package com.UserFavorites.Places;

import com.Common.AbstractController;
import com.Common.UserFavorites.FavoritePlace;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/5/17.
 */
public class PlacesController extends AbstractController {

    public boolean createUserPlaceFavorite(
            String cookie,
            int vendor_id
    ) throws Exception {
        PlacesModel placesModel = new PlacesModel();
        return placesModel.createUserPlaceFavorite(
                cookie,
                vendor_id
        );
    }

    public boolean deleteUserPlaceFavorite(
            String cookie,
            int vendor_id
    ) throws Exception {
        PlacesModel placesModel = new PlacesModel();
        return placesModel.deleteUserPlaceFavorite(
                cookie,
                vendor_id
        );
    }

    public String loadUserPlaceFavorites(
            String cookie
    ) throws Exception {
        PlacesModel placesModel = new PlacesModel();
        HashMap<Integer, FavoritePlace> favoritePlaceHashMap = new HashMap<Integer, FavoritePlace>();
        favoritePlaceHashMap = placesModel.loadUserPlaceFavorites(
                cookie
        );
        return this.returnJSON(favoritePlaceHashMap);
    }
}
