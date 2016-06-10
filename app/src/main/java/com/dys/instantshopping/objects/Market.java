package com.dys.instantshopping.objects;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class Market {

    private String _id;
    private String Name;
    private double Latitude;
    private double Longitude;

    public Market(String name, double latitude, double longitude)
    {
        this.Name = name;
        this.Latitude = latitude;
        this.Longitude = longitude;
    }

    public double getLatitude() {
        return this.Latitude;
    }

    public double getLongitude() {
        return this.Longitude;
    }

    public float getDistance(Location CurrentLocation) {
        Location location = new Location("MarketLocation");
        location.setLatitude(this.Latitude);
        location.setLongitude(this.Longitude);
        return ((int)location.distanceTo(CurrentLocation) / 1000);
    }

    public String getName() {
        return this.Name;
    }
}
