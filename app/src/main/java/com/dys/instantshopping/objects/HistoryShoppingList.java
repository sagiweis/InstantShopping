package com.dys.instantshopping.objects;

import java.util.Date;

/**
 * Created by Sagi on 10/06/2016.
 */
public class HistoryShoppingList extends ShoppingList{
    public Date ShopDate;
    public Date getShopDate(){
        return this.ShopDate;
    }

    public HistoryShoppingList(ShoppingList list){
        super(list.getProductsList());
        this.ShopDate = new Date();
    }
}
