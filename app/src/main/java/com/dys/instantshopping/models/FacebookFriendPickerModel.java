package com.dys.instantshopping.models;

/**
 * Created by Sagi on 25/04/2016.
 */
public class FacebookFriendPickerModel implements Comparable{
    private String name;
    private String id;
    private String imageURL;
    private boolean isSelected;

    public FacebookFriendPickerModel(String id, String name, String imageURL){
        this.name = name;
        this.id = id;
        this.imageURL = imageURL;
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

    public String getImageURL(){
        return this.imageURL;
    }

    @Override
    public int compareTo(Object another) {
        return this.name.compareTo(((FacebookFriendPickerModel) another).getName());
    }
}
