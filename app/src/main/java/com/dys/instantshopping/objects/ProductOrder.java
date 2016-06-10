package com.dys.instantshopping.objects;

/**
 * Created by Sagi on 08/06/2016.
 */
public class ProductOrder {
    public String _id;
    public String MarketId;
    public String CategoryBefore;
    public String CategoryAfter;
    public int Count;

    public ProductOrder(String marketId, String before, String after)
    {
        this.CategoryBefore = before;
        this.CategoryAfter = after;
        this.Count = 0;
    }
}
