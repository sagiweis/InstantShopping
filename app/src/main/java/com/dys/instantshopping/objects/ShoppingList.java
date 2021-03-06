package com.dys.instantshopping.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sagi on 26/04/2016.
 */
public class ShoppingList {
    private List<Product> ProductsList;

    public ShoppingList() {
        this.ProductsList = new ArrayList<Product>();
    }

    public ShoppingList(List<Product> list) {
        this.ProductsList = list;
    }

    public void addProduct(Product product){
        this.ProductsList.add(product);
    }

    public void editProduct(int index, Product product){
        this.ProductsList.set(index, product);

    }

    public void removeProduct(int index){
        this.ProductsList.remove(index);

    }


    public List<Product> getProductsList(){
        return this.ProductsList;
    }



}
