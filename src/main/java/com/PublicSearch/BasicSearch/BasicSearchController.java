package com.PublicSearch.BasicSearch;

import com.Common.AbstractController;
import com.Common.UserFavorites.FavoriteBeer;
import com.Common.UserFavorites.FavoritePlace;
import com.Common.UserFavorites.FavoriteVendorDrink;
import com.Common.UserFavorites.FavoriteVendorFood;

import java.util.HashMap;

public class BasicSearchController extends AbstractController  {
    public BasicSearchController() throws Exception {}

    private void validateBasicSearch(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        if (keywords.length > 20) {
            throw new Exception("Your search is ridiculously large! Please don't do that.");
        }
        int search_token_total = 0;
        for (int i = 0; i < keywords.length; i++) {
            this.validateString(keywords[i], "keyword: " + keywords[i]);
            if (keywords[i].length() > 50) {
                throw new Exception("Your search is ridiculously large! Please don't do that.");
            }
            search_token_total += keywords[i].length();
        }
        if (search_token_total > 250) {
            throw new Exception("Your search is ridiculously large! Please don't do that.");
        }
        if ((limit <= 0) || (offset < 0) || (limit > 50)) {
            throw new Exception("Invalid limit or offset.");
        }
    }

    public String basicSearchVendors(
            String[] keywords,
            int limit,
            int offset,
            int reid
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        if (reid != 1 && reid != 2 && reid != 3 && reid != 4) {
            throw new Exception("Invalid search.");
        }
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        // Return items in "favorite" format, like they appear in "my_favorites".
        HashMap<Integer, FavoritePlace> placeHashMap = basicSearchModel.basicSearchVendors(
                keywords,
                limit,
                offset,
                reid
        );
        return this.returnJSON(placeHashMap);
    }

    public String basicSearchBeers(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        HashMap<Integer, FavoriteBeer> beerHashMap = basicSearchModel.basicSearchBeers(
                keywords,
                limit,
                offset
        );
        return this.returnJSON(beerHashMap);
    }

    public String basicSearchFoods(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        HashMap<Integer, FavoriteVendorFood> foodHashMap = basicSearchModel.basicSearchFoods(
                keywords,
                limit,
                offset
        );
        return this.returnJSON(foodHashMap);
    }

    public String basicSearchDrinks(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        HashMap<Integer, FavoriteVendorDrink> drinkHashMap = basicSearchModel.basicSearchDrinks(
                keywords,
                limit,
                offset
        );
        return this.returnJSON(drinkHashMap);
    }
}
