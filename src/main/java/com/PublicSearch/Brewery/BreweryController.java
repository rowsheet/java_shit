package com.PublicSearch.Brewery;

import com.Common.AbstractController;
import com.Common.BrewerySearchResult;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
public class BreweryController extends AbstractController {
    public String searchBreweries(
        int min_occupancy,
        int max_occupancy,
        String[] brewery_has,
        String[] brewery_friendly,
        boolean open_now,
        float latitude,
        float longitude,
        float radius,
        int limit,
        int offset
    ) throws Exception {
        // Validate input parameters.
        if (min_occupancy > max_occupancy) {
            throw new Exception("Max occupancy must be larger than min occupancy");
        }
        // Validate all brewery "has".
        for (int i = 0; i < brewery_has.length; i++) {
            this.validateBreweryHas(brewery_has[i]);
        }
        // For brewery "has", if they are all selected, just pass an array with
        // the string "Any" and the model will not add the where clause in
        // the query.
        if (brewery_has.length == 0) {
            brewery_has = new String[]{"Any"};
        }
        // Validate all brewery "friendly".
        for (int i = 0; i < brewery_friendly.length; i++) {
            this.validateBreweryFriendly(brewery_friendly[i]);
        }
        // For brewery "has", if they are all selected, just pass an array with
        // the string "Any" and the model will not add the where clause in
        // the query.
        // Model.
        if (brewery_friendly.length == 0) {
            brewery_friendly = new String[]{"Any"};
        }
        // Validate lat long
        this.validateLatLong(latitude, longitude);
        if (radius < 0 || radius > 10) {
            throw new Exception("Invalid brewery search radius.");
        }
        // Initialize model and get data.
        BreweryModel breweryModel = new BreweryModel();
        BrewerySearchResult brewerySearchResult = new BrewerySearchResult();
        brewerySearchResult = breweryModel.searchBreweries(
                min_occupancy,
                max_occupancy,
                brewery_has,
                brewery_friendly,
                open_now,
                latitude,
                longitude,
                radius,
                limit,
                offset
        );
        return this.returnJSON(brewerySearchResult);
    }
}
