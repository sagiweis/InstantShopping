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
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.HistoryShoppingList;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ObjectIdTypeAdapter;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dor Albagly on 5/4/2016.
 */
public class ListsHistoryFragment extends Fragment{
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String,List<Product>> expandableListDetail = new HashMap<String, List<Product>>();
    Group group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity context = getActivity();
        final View v = inflater.inflate(R.layout.fragment_lists_history, container, false);
        context.setTitle("הרשימות הישנות שלי");

        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(p.getProperty("ServerApiUrl"))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                .build();
        GroupController groupApi = retrofit.create(GroupController.class);
        Group currentGroup = (Group) AppCache.get("currentGroup");
        Call<Group> call = groupApi.GetGroupById(currentGroup.getId().toString());
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                Group group = response.body();
                List<HistoryShoppingList> historyLists = group.getHistoryLists();

                for(int i=0;i<historyLists.size();i++){
                    expandableListDetail.put(DateFormat.getDateTimeInstance().format(historyLists.get(i).getShopDate()),historyLists.get(i).getProductsList());
                }

                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new ListsHistoryExpandableListAdapter(context, expandableListTitle, expandableListDetail);
                expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
                expandableListView.setAdapter(expandableListAdapter);
            }

            @Override
            public void onFailure(Call<Group> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת הקבוצה מהשרת", Toast.LENGTH_LONG).show();
            }
        });



        return v;

    }
}
