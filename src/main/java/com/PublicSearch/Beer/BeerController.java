package com.PublicSearch.Beer;

import com.Common.AbstractController;
import com.Common.Beer;
import com.Common.BeerSearchResult;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/30/17.
 */
public class BeerController extends AbstractController {

    public String searchBeers(
            int min_color,
            int max_color,
            int min_bitterness,
            int max_bitterness,
            int min_abv,
            int max_abv,
            String[] styles,
            String[] tastes,
            int limit,
            int offset
    ) throws Exception {
        // Validate input parameters.
        this.validateBeerColor(min_color);
        this.validateBeerColor(max_color);
        if (min_color > max_color) {
            throw new Exception("Min color cannot be greater than max color.");
        }
        this.validateBitterness(min_bitterness);
        this.validateBitterness(max_bitterness);
        if (min_bitterness > max_bitterness) {
            throw new Exception("Min bitterness cannot be greater than max bitterness.");
        }
        this.validateAbv(min_abv);
        this.validateAbv(max_abv);
        if (min_abv > max_abv) {
            throw new Exception("Min abv cannot be greater than max abv.");
        }
        // For beer styles, if they are all selected, just pass an array with
        // the string "Any" and the model will not add the where clause in
        // the query.
        for (int i = 0; i < styles.length; i++) {
            this.validateBeerStyle(styles[i]);
        }
        // Assuming all passed validation and are unique (unique is the assumption), the
        // length should be 14 (this means new beer styles need to increment here for search).
        if (styles.length == 14) {
            styles = new String[]{"Any"};
        }
        // For beer tastes, if they are all selected, just pass an array with
        // the string "Any" and the model will not add the where clause in
        // the query.
        for (int i = 0; i < tastes.length; i++) {
            this.validateBeerTaste(tastes[i]);
        }
        // Assuming all passed validation and are unique (unique is the assumption), the
        // length should be 14 (this means new beer styles need to increment here for search).
        if (tastes.length == 6) {
            tastes = new String[]{"Any"};
        }
        // Create model and get data.
        BeerModel beerModel = new BeerModel();
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        beerSearchResultHashMap = beerModel.searchBeers(
                min_color,
                max_color,
                min_bitterness,
                max_bitterness,
                min_abv,
                max_abv,
                styles,
                tastes,
                limit,
                offset
        );
        return this.returnJSON(beerSearchResultHashMap);
    }
}
