package com.dys.instantshopping.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sagi on 26/04/2016.
 */
public class ShoppingList {
    private List<Product> ProductsList;
    private Date ShopDate;

    public ShoppingList() {
        this.ProductsList = new ArrayList<Product>();
        this.ShopDate = new Date();
    }

}
