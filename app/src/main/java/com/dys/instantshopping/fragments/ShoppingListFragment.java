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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.dys.instantshopping.GroupActivity;
import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.adapters.ShoppingListAdapter;
import com.dys.instantshopping.objects.Category;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Market;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ProductOrderReport;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.serverapi.CategoriesController;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.serverapi.SuperMarketsOrderController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ObjectIdTypeAdapter;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import okhttp3.ResponseBody;
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
    Group group;
    ShoppingList currentShoppingList;

    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity context = getActivity();
        market = (Market) AppCache.get("selectedMarket");
        group = (Group) AppCache.get("currentGroup");
        context.setTitle("רשימת הקניות שלי");
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        listView = (ListView) v.findViewById(R.id.slistView);

        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(p.getProperty("ServerApiUrl"))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        SuperMarketsOrderController orderApi = retrofit.create(SuperMarketsOrderController.class);
        Call<ShoppingList> call = orderApi.GetOrderedList(group.getId().toString(), market.getId().toString());
        call.enqueue(new Callback<ShoppingList>() {
            @Override
            public void onResponse(Call<ShoppingList> call, Response<ShoppingList> response) {
                currentShoppingList = response.body();
                listView.setAdapter(new ShoppingListAdapter(context, currentShoppingList));
                listView.setItemsCanFocus(false);

                if (AppCache.get("categories") == null) {
                    AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
                    Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
                    Retrofit retrofit2 = new Retrofit.Builder()
                            .baseUrl(p.getProperty("ServerApiUrl"))
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                            .build();
                    CategoriesController categoriesApi = retrofit2.create(CategoriesController.class);
                    Call<List<Category>> callCategories = categoriesApi.GetCategories();
                    callCategories.enqueue(new Callback<List<Category>>() {
                        @Override
                        public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                            final List<Category> allCategories = response.body();
                            AppCache.put("categories", allCategories);
                            handleTakeProduct();
                        }

                        @Override
                        public void onFailure(Call<List<Category>> call, Throwable throwable) {
                            Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת רשימת הקטגוריות", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handleTakeProduct();
                }
            }

            @Override
            public void onFailure(Call<ShoppingList> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת רשימת הקניות המסודרת", Toast.LENGTH_LONG).show();
            }
        });
        return v;

    }

    private void handleTakeProduct() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Product prd = (Product) parent.getItemAtPosition(position);
                prd.toggleSelected();
                CheckBox c=(CheckBox)view.findViewById(R.id.checkBox);
                c.setChecked(prd.isSelected());
                List<Category> categories = (List<Category>)AppCache.get("categories");

                if(prd.isSelected()){
                    // get current category name
                    String currentCategoryName = "";
                    for(int i=0;i<categories.size();i++){
                        if(categories.get(i).getProducts().contains(prd.getName())){
                            currentCategoryName = categories.get(i).getName();
                        }
                    }

                    // get all selected products
                    List<Product> allSelectedProducts = new ArrayList<Product>();
                    for(int i=0;i<currentShoppingList.getProductsList().size();i++){
                        Product product = (Product) parent.getItemAtPosition(i);
                        if(product.isSelected())
                            allSelectedProducts.add(product);
                    }

                    // get all selected items category names
                    List<String> before = new ArrayList();
                    for(int i=0;i<allSelectedProducts.size();i++){
                        for(int k=0;k<categories.size();k++){
                            String currentProductName = allSelectedProducts.get(i).getName();
                            if(categories.get(k).getProducts().contains(currentProductName)){
                                if(currentCategoryName != categories.get(k).getName())
                                    before.add(categories.get(k).getName());
                            }
                        }
                    }

                    if(before.size() > 0){
                        ProductOrderReport report = new ProductOrderReport(market.getId(),before,currentCategoryName);

                        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
                        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(p.getProperty("ServerApiUrl"))
                                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                                .build();
                        SuperMarketsOrderController orderApi = retrofit.create(SuperMarketsOrderController.class);
                        Call<ResponseBody> call = orderApi.AddNewProductOrder(report);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בדיווח לקיחת המוצר לשרת", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

            }
        });
    }

    }

