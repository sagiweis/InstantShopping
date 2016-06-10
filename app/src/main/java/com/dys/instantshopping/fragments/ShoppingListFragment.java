package com.dys.instantshopping.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.dys.instantshopping.GroupActivity;
import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.adapters.ShoppingListAdapter;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Market;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.facebook.AccessToken;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dor Albagly on 5/5/2016.
 */
public class ShoppingListFragment extends Fragment {

    ListView listView;
    Market market;

    public void setMarket(Market market) {
        this.market = market;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity context = getActivity();
        market = (Market) AppCache.get("selectedMarket");
        context.setTitle("רשימת הקניות שלי");
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ShoppingList lst3 = new ShoppingList();
        lst3.setGroupName("Retro");
        lst3.setListDate("2.5.2016");

        if(market.getName() == "שופרסל") {
            lst3.addProduct(new Product("חלב", "3%", 2));
            lst3.addProduct(new Product("דובדבן", "", 5));
            lst3.addProduct(new Product("תפוח עץ", "אדום", 2));
        }else{
            lst3.addProduct(new Product("תפוח עץ", "אדום", 2));
            lst3.addProduct(new Product("דובדבן", "", 5));
            lst3.addProduct(new Product("חלב", "3%", 2));
        }

        listView = (ListView) v.findViewById(R.id.slistView);
        listView.setAdapter(new ShoppingListAdapter(context, lst3));


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Product prd = (Product) parent.getItemAtPosition(position);
                Toast.makeText(context,
                        "Clicked on Row: " + prd.getName(),
                        Toast.LENGTH_LONG).show();
                prd.toggleSelected();
            }
        });*/

        return v;
    }
}
