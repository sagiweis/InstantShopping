package com.dys.instantshopping.Comparators;

import com.dys.instantshopping.objects.Market;

import java.util.Comparator;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class MarketNameComparator implements Comparator<Market>{
    public int compare (Market left, Market right) {
        return left.getName().compareTo(right.getName());
    }
}
