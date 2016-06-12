package com.dys.instantshopping.objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagi on 26/04/2016.
 */
public class Group {
    private ObjectId _id;
    private String Name;
    private String ImageURL;
    private List<String> Participents;
    private ShoppingList CurrentList;
    private List<HistoryShoppingList> HistoryLists;

    public Group(String name, String imageURL, List<String> participents) {
        this._id = new ObjectId();
        this.Name = name;
        this.ImageURL = imageURL;
        this.Participents = participents;
        this.CurrentList = new ShoppingList();
        this.HistoryLists = new ArrayList<HistoryShoppingList>();
    }

    public String getImageURL(){
        return this.ImageURL;
    }

    public String getName(){
        return this.Name;
    }

    public ObjectId getId(){
        return this._id;
    }

    public List<String> getParticipents(){
        return this.Participents;
    }

    public List<HistoryShoppingList> getHistoryLists(){
        return this.HistoryLists;
    }

    public ShoppingList getCurrentList(){
        return this.CurrentList;
    }

    public void setCurrentList(ShoppingList lst){
        this.CurrentList = lst;
    }
}
