package com.Common;

/**
 * Created by alexanderkleinhans on 7/2/17.
 */
public class Geolocation {
    public float latitude;
    public float longitude;
    public float miles_away;
    public String name;
    public Geolocation() {
        this.latitude = (float)0.0;
        this.longitude = (float)0.0;
        this.miles_away = (float)0.0;
        this.name = null;
    }
}
