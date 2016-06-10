package com.dys.instantshopping.objects;

import java.util.List;

/**
 * Created by Sagi on 08/06/2016.
 */
public class Category {
    private String _id;
    private String Name;
    private List<String> Products;

    public Category(String name, List<String> products){
        this.Name = name;
        this.Products = products;
    }

    public String getName(){
        return this.Name;
    }

    public List<String> getProducts(){
        return this.Products;
    }
}
