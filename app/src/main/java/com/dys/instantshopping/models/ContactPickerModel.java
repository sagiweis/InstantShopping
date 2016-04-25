package com.dys.instantshopping.models;

/**
 * Created by Sagi on 24/04/2016.
 */
public class ContactPickerModel implements Comparable{
    private String name;
    private boolean isSelected;

    public ContactPickerModel(String name){
        this.name = name;
        this.isSelected = false;
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public int compareTo(Object another) {
        return this.name.compareTo(((ContactPickerModel) another).getName());
    }
}
