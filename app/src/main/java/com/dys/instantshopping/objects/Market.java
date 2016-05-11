package com.dys.instantshopping.objects;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class Market {
    private Location location;
    private String name;
    //TODO: adds order list or parameter or something


    public Market(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public Float getDistance(Location CurrentLocation) {
        return location.distanceTo(CurrentLocation);
    }

    public String getName() {
        return name;
    }
}
