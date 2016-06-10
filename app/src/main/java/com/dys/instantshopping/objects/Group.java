package com.dys.instantshopping.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagi on 26/04/2016.
 */
public class Group {
    private String Id;
    private String Name;
    private String ImageURL;
    private List<String> Participents;
    private ShoppingList CurrentList;
    private List<ShoppingList> HistoryLists;

    public Group(String name, String imageURL, List<String> participents) {
        this.Name = name;
        this.ImageURL = imageURL;
        this.Participents = participents;
        this.CurrentList = new ShoppingList();
        this.HistoryLists = new ArrayList<ShoppingList>();
    }

    public String getImageURL(){
        return this.ImageURL;
    }

    public String getName(){
        return this.Name;
    }

    public String getId(){
        return this.Id;
    }

    public List<String> getParticipents(){
        return this.Participents;
    }

}
