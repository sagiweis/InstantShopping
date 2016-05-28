package com.dys.instantshopping.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;

import java.util.ArrayList;

/**
 * Created by Sagi on 02/05/2016.
 */
public class GroupListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.content_group_list, container, false);
        ListView listView = (ListView) fragmentView.findViewById(R.id.groupListView);
        ShoppingList groupList = new ShoppingList();
        listView.setAdapter(new GroupListAdapter(getActivity(), groupList));

        FloatingActionButton fab = (FloatingActionButton) fragmentView.findViewById(R.id.addProductFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new AddEditProductFragment();
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

        return fragmentView;
    }
}
