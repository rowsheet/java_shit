package com.PublicSearch.BasicSearch;

import com.Common.AbstractController;
import com.Common.UserFavorites.*;

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
            int offset
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        // Return items in "favorite" format, like they appear in "my_favorites".
        HashMap<Integer, FavoritePlace> placeHashMap = basicSearchModel.basicSearchVendors(
                keywords,
                limit,
                offset
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
        BeerMenu beerMenu = basicSearchModel.basicSearchBeers(
                keywords,
                limit,
                offset
        );
        return this.returnJSON(beerMenu);
    }

    public String basicSearchFoods(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        FoodMenu foodMenu = basicSearchModel.basicSearchFoods(
                keywords,
                limit,
                offset
        );
        return this.returnJSON(foodMenu);
    }

    public String basicSearchDrinks(
            String[] keywords,
            int limit,
            int offset
    ) throws Exception {
        this.validateBasicSearch(keywords, limit, offset);
        BasicSearchModel basicSearchModel = new BasicSearchModel();
        DrinkMenu drinkMenu = basicSearchModel.basicSearchDrinks(
                keywords,
                limit,
                offset
        );
        return this.returnJSON(drinkMenu);
    }
}
