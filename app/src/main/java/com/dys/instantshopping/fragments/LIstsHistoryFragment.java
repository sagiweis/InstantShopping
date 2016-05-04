package com.dys.instantshopping.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.ShoppingList;

import java.util.ArrayList;

/**
 * Created by Dor Albagly on 5/4/2016.
 */
public class ListsHistoryFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<ShoppingList> lst = new ArrayList<ShoppingList>();

        ShoppingList lst1 = new ShoppingList();


        return inflater.inflate(R.layout.content_group_list, container, false);

    }
}
