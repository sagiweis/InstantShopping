package com.dys.instantshopping.objects;

/**
 * Created by Sagi on 26/04/2016.
 */
public class Product {
    private String Name;
    private String Category;
    private double Amount;
    private String Description;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void toggleSelected() {
        isSelected = !isSelected();
    }

    public Product(String name, String category, String description, double amount){
        this.Name = name;
        this.Category = category;
        this.Amount = amount;
        this.Description = description;
    }

    public String getName(){
        return this.Name;
    }

    public String getCategory(){
        return this.Category;
    }

    public double getAmount(){
        return this.Amount;
    }

    public String getDescription(){
        return this.Description;
    }
}
