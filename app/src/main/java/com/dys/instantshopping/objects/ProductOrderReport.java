package com.dys.instantshopping.objects;

import java.util.List;

/**
 * Created by Sagi on 08/06/2016.
 */
public class ProductOrderReport {
    public String MarketId;
    public List<String> CategoryBefore;
    public String CategoryAfter;

    public ProductOrderReport(String marketId, List<String> before, String after)
    {
        this.MarketId = marketId;
        this.CategoryBefore = before;
        this.CategoryAfter = after;
    }
}
