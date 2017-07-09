package com.Common;

import jnr.ffi.annotations.In;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/9/17.
 */
public class TrendingOverview {
    public HashMap<Integer, BeerSearchResult> trending_beers;
    public HashMap<Integer, BeerSearchResult> newest_beers;
    public HashMap<Integer, BeerSearchResult> top_beers;

    public TrendingOverview() {
        this.trending_beers = new HashMap<Integer, BeerSearchResult>();
        this.newest_beers = new HashMap<Integer, BeerSearchResult>();
        this.top_beers = new HashMap<Integer, BeerSearchResult>();
    }
}
