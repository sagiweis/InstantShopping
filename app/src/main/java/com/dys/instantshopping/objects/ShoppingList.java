package com.dys.instantshopping.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sagi on 26/04/2016.
 */
public class ShoppingList {
    private List<Product> ProductsList;

    public String getListDate() {
        return ListDate;
    }

    public void setListDate(String listDate) {
        ListDate = listDate;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    private String ListDate;
    private String GroupName;

    public ShoppingList() {
        this.ProductsList = new ArrayList<Product>();
    }

    public void addProduct(Product product){
        this.ProductsList.add(product);
    }

    public List<Product> getProductsList(){
        return this.ProductsList;
    }



}
