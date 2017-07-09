package com.Trending.Beers;

import com.Common.AbstractController;
import com.Common.BeerSearchResult;
import com.Common.TrendingOverview;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/8/17.
 */
public class BeersController extends AbstractController {

    public String fetchTrendingOverview() throws Exception {
        //Nothing needs to be validated.
        TrendingOverview trendingOverview = new TrendingOverview();
        BeersModel beersModel = new BeersModel();
        // Since this loads on the main page, I cannot have this throw a soap fault or else
        // the main page will be broken.
        try {
            trendingOverview.trending_beers = beersModel.fetchTrendingBeers(20, 0);
            trendingOverview.newest_beers = beersModel.fetchNewetBeers(20, 0);
            trendingOverview.top_beers = beersModel.fetchTopBeers(20, 0);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // The original empty trendingOverview will get returned. Front-end will check for that.
        }
        return this.returnJSON(trendingOverview);
    }

    /**
     * Fetches most newly reviewed beers.
     *
     * @param limit
     * @return
     */
    public String fetchTrendingBeers (
        int limit,
        int offset
    ) throws Exception {
        // Nothing needs to be validated.
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        BeersModel beersModel = new BeersModel();
        beerSearchResultHashMap = beersModel.fetchTrendingBeers(
                limit,
                offset
        );
        return this.returnJSON(beerSearchResultHashMap);
    }

    /**
     * Fetchest beers with the highest comments.
     *
     * @param limit
     * @return
     */
    public String fetchTopBeers (
        int limit,
        int offset
    ) throws Exception {
        // Nothing needs to be validated.
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        BeersModel beersModel = new BeersModel();
        beerSearchResultHashMap = beersModel.fetchTopBeers(
                limit,
                offset
        );
        return this.returnJSON(beerSearchResultHashMap);
    }

    /**
     * Fetchest beers with the newst creation_timestamps.
     *
     * @param limit
     * @return
     */
    public String fetchNewetBeers (
        int limit,
        int offset
    ) throws Exception {
        // Nothing needs to be validated.
        HashMap<Integer, BeerSearchResult> beerSearchResultHashMap = new HashMap<Integer, BeerSearchResult>();
        BeersModel beersModel = new BeersModel();
        beerSearchResultHashMap = beersModel.fetchNewetBeers(
                limit,
                offset
        );
        return this.returnJSON(beerSearchResultHashMap);
    }
}
