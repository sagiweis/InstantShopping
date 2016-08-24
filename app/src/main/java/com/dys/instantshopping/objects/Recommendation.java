package com.dys.instantshopping.objects;

/**
 * Created by Sagi on 24/08/2016.
 */
public class Recommendation {
    private String name;
    private double amount;

    public Recommendation(String name,double amount){
        this.amount = amount;
        this.name = name;
    }

    public String getName(){return this.name;}
    public double getAmount(){return this.amount;}
}
