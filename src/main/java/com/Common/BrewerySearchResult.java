package com.Common;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 7/2/17.
 */
public class BrewerySearchResult {
    public HashMap<Integer, Brewery> breweries;
    public HashMap<Integer, Geolocation> geolocations;
    public BrewerySearchResult() {
        this.breweries = new HashMap<Integer, Brewery>();
        this.geolocations = new HashMap<Integer, Geolocation>();
    }
}
