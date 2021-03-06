package com.dys.instantshopping.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dys.instantshopping.GroupActivity;
import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.objects.Category;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.serverapi.CategoriesController;
import com.dys.instantshopping.serverapi.GroupController;
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
 * Created by Sagi on 04/05/2016.
 */
public class AddEditProductFragment extends DialogFragment {
    View view;
    int index;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        index = -1;
        if(getArguments() != null)
            index = getArguments().getInt("itemIndex");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.add_product_dialog, null);

        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(p.getProperty("ServerApiUrl"))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                .build();        CategoriesController categoriesApi = retrofit.create(CategoriesController.class);
        Call<List<Category>> call = categoriesApi.GetCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                final List<Category> allCategories = response.body();
                AppCache.put("categories",allCategories);
                ArrayList<String> categoriesNames = new ArrayList<String>();
                for(int i=0;i<allCategories.size();i++){
                    categoriesNames.add(allCategories.get(i).getName());
                }

                Spinner categorySpinner = (Spinner) view.findViewById(R.id.addProductCategorySpinner);
                ArrayAdapter<String> categorySpinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesNames);
                categorySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categorySpinnerArrayAdapter);

                Spinner productSpinner = (Spinner) view.findViewById(R.id.addProductSpinner);
                ArrayAdapter<String> productSpinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>());
                productSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productSpinner.setAdapter(productSpinnerArrayAdapter);

                if(index > -1){
                    ListView listView = (ListView) getActivity().findViewById(R.id.groupListView);
                    GroupListAdapter listAdapter = ((GroupListAdapter) listView.getAdapter());
                    Product productToEdit = listAdapter.getShoppingList().getProductsList().get(index);

                    // get relevant category
                    for(int categoryIndex=0;categoryIndex < allCategories.size();categoryIndex++){
                        if(allCategories.get(categoryIndex).getProducts().contains((productToEdit.getName()))) {
                            categorySpinner.setSelection(categoryIndex);
                            ((ArrayAdapter)productSpinner.getAdapter()).clear();
                            ((ArrayAdapter)productSpinner.getAdapter()).addAll(allCategories.get(categoryIndex).getProducts());
                        }
                    }


                    productSpinner.setSelection(((ArrayAdapter)productSpinner.getAdapter()).getPosition(productToEdit.getName()));

                    TextView description = (TextView) view.findViewById(R.id.addProductDescriptionText);
                    description.setText(productToEdit.getDescription());

                    TextView amount = (TextView) view.findViewById(R.id.addProductAmount);
                    amount.setText(String.valueOf(productToEdit.getAmount()));
                }

                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Spinner productSpinner = (Spinner) view.findViewById(R.id.addProductSpinner);
                        productSpinner.setClickable(true);
                        ((ArrayAdapter)productSpinner.getAdapter()).clear();
                        ((ArrayAdapter)productSpinner.getAdapter()).addAll(allCategories.get(position).getProducts());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת רשימת הקטגוריות", Toast.LENGTH_LONG).show();
            }
        });

        builder.setView(view);
        if(index > -1)
            builder.setTitle("ערוך פריט");
        else
            builder.setTitle("הוסף פריט");
        builder.setPositiveButton("אישור",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Spinner spinner = ((Spinner) ((AlertDialog) dialog).findViewById(R.id.addProductSpinner));
                        String amount = (((EditText) ((AlertDialog) dialog).findViewById(R.id.addProductAmount))).getText().toString();
                        String description = (((EditText) ((AlertDialog) dialog).findViewById(R.id.addProductDescriptionText))).getText().toString();
                        String name = spinner.getSelectedItem().toString();

                        ListView listView = (ListView) getActivity().findViewById(R.id.groupListView);
                        final GroupListAdapter listAdapter = ((GroupListAdapter) listView.getAdapter());

                        double amountAsDouble = 1.0;
                        if (amount.compareTo("") != 0)
                            amountAsDouble = Double.parseDouble(amount);


                        if(index > -1){
                            if(amountAsDouble == 0.0)
                                listAdapter.getShoppingList().removeProduct(index);
                            else
                                listAdapter.getShoppingList().editProduct(index, new Product(name, description, amountAsDouble));
                        }
                        else{
                            listAdapter.getShoppingList().addProduct(new Product(name, description, amountAsDouble));

                        }

                        final Activity activity = getActivity();
                        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
                        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(p.getProperty("ServerApiUrl"))
                                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                                .build();
                        final GroupController groupApi = retrofit.create(GroupController.class);
                        Group currentGroup = (Group) AppCache.get("currentGroup");
                        Call<Group> call = groupApi.GetGroupById(currentGroup.getId().toString());
                        call.enqueue(new Callback<Group>() {
                            @Override
                            public void onResponse(Call<Group> call, Response<Group> response) {
                                final Group newGroup = response.body();
                                newGroup.setCurrentList(listAdapter.getShoppingList());
                                Call<Void> updateCall = groupApi.UpdateGroup(newGroup);
                                updateCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        AppCache.put("currentGroup",newGroup);
                                        listAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable throwable) {
                                        Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בשמירת הקבוצה", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            @Override
                            public void onFailure(Call<Group> call, Throwable throwable) {
                                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת הקבוצה מהשרת", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
        );
        builder.setNegativeButton("ביטול",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );
        return builder.create();
    }
}
