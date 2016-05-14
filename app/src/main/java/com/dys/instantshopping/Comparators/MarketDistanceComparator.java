package com.dys.instantshopping.Comparators;

import android.location.Location;

import com.dys.instantshopping.objects.Market;

import java.util.Comparator;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class MarketDistanceComparator implements Comparator<Market> {
    private Location curr;
    private Market chosenMarket;

    public MarketDistanceComparator(Location l) {
        this.curr = l;
    }

    @Override
    public int compare (Market left, Market right) {
        return ((Float)left.getDistance(this.curr)).compareTo(right.getDistance(this.curr));
    }
}
