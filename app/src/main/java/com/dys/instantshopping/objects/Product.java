package com.dys.instantshopping.objects;

/**
 * Created by Sagi on 26/04/2016.
 */
public class Product {
    private String Name;
    private double Amount;
    private String Description;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void toggleSelected() {
        isSelected = !isSelected();
    }
    
    public Product(String name,String description, double amount){
        this.Name = name;
        this.Amount = amount;
        this.Description = description;
    }

    public String getName(){
        return this.Name;
    }

    public double getAmount(){
        return this.Amount;
    }

    public void setAmount(double amount){this.Amount = amount;}

    public String getDescription(){
        return this.Description;
    }
}
