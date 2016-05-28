package com.dys.instantshopping.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.ListsHistoryExpandableListAdapter;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dor Albagly on 5/4/2016.
 */
public class ListsHistoryFragment extends Fragment{
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String,List<Product>> expandableListDetail = new HashMap<String, List<Product>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity context = getActivity();
        View v = inflater.inflate(R.layout.fragment_lists_history, container, false);
        context.setTitle("הרשימות הישנות שלי");

        ShoppingList lst1 = new ShoppingList();
        lst1.addProduct(new Product("apple","Just red apples", 12));
        lst1.addProduct(new Product("חלב",  "3%", 2));
        lst1.setGroupName("Retro");
        lst1.setListDate("29.4.2016");

        ShoppingList lst2 = new ShoppingList();
        lst2.addProduct(new Product("עגבניה", "", 5));
        lst2.addProduct(new Product("בטטה", "גדולות", 5));
        lst2.addProduct(new Product("חלב",  "3%", 2));
        lst2.setGroupName("Retro");
        lst2.setListDate("21.4.2016");

        ShoppingList lst3 = new ShoppingList();
        lst3.addProduct(new Product("apple","Just red apples", 12));
        lst3.addProduct(new Product("חלב",  "3%", 2));
        lst3.addProduct(new Product("בטטה",  "גדולות", 5));
        lst3.addProduct(new Product("סבון", "סבון Dove", 2));
        lst3.setGroupName("Retro");
        lst3.setListDate("2.5.2016");

        expandableListDetail.put(lst1.getListDate(),lst1.getProductsList());
        expandableListDetail.put(lst2.getListDate(),lst2.getProductsList());
        expandableListDetail.put(lst3.getListDate(),lst3.getProductsList());

        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ListsHistoryExpandableListAdapter(context, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        /*expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(context.getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(context.getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        context.getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });*/

        return v;

    }
}
