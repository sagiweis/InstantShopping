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
import android.widget.Toast;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.serverapi.SuperMarketsOrderController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ObjectIdTypeAdapter;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sagi on 02/05/2016.
 */
public class GroupListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.content_group_list, container, false);
        final ListView listView = (ListView) fragmentView.findViewById(R.id.groupListView);

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
                Group newGroup = response.body();
                AppCache.put("currentGroup",newGroup);
                listView.setAdapter(new GroupListAdapter(getActivity(), newGroup.getCurrentList()));
            }

            @Override
            public void onFailure(Call<Group> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת הקבוצה מהשרת", Toast.LENGTH_LONG).show();
            }
        });

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
