package com.dys.instantshopping.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;

/**
 * Created by Sagi on 02/05/2016.
 */
public class GroupListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_group_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.groupListView);

        ShoppingList groupList = new ShoppingList();
        groupList.addProduct(new Product("חלב","מוצרי חלב","3%",5));
        groupList.addProduct(new Product("מלפפון","ירקות","",0.5));
        groupList.addProduct(new Product("עגבניה","ירקות","",2));
        groupList.addProduct(new Product("לחם","קונדיטוריה","חיטה מלאה",2));
        groupList.addProduct(new Product("חזה עוף","בשר","",2.5));
        listView.setAdapter(new GroupListAdapter(getActivity(), groupList));

        return view;
    }
}
