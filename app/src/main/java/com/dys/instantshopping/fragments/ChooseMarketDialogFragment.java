package com.dys.instantshopping.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dys.instantshopping.Comparators.MarketDistanceComparator;
import com.dys.instantshopping.GroupActivity;
import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.adapters.MarketSpinnerAdapter;
import com.dys.instantshopping.objects.Category;
import com.dys.instantshopping.objects.Market;
import com.dys.instantshopping.serverapi.CategoriesController;
import com.dys.instantshopping.serverapi.MarketsController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.MyLocationListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class ChooseMarketDialogFragment extends DialogFragment{
    View view;
    List<Market> mlist;
    MyLocationListener locationListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setTargetFragment(this, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.content_group_list, container, false);
        view = inflater.inflate(R.layout.choose_market_dialog, null);

        final Activity context = getActivity();

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת המיקום הנוכחי של המשתמש", Toast.LENGTH_LONG).show();
            return null;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
            Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
            Retrofit retrofit = new Retrofit.Builder().baseUrl(p.getProperty("ServerApiUrl")).addConverterFactory(GsonConverterFactory.create()).build();
            MarketsController marketsApi = retrofit.create(MarketsController.class);
            Call<List<Market>> call = marketsApi.GetMarkets();
            call.enqueue(new Callback<List<Market>>() {
                @Override
                public void onResponse(Call<List<Market>> call, Response<List<Market>> response) {
                    mlist = response.body();
                    Spinner spinner = (Spinner) view.findViewById(R.id.marketSpinner);

                /*Location l = new Location("MyLocation");
                l.setLatitude(currentLocation.getla);
                l.setLongitude(currentLongitude);*/

                    Location currentUserLocation = null;
                    if(locationListener.currentLocation != null){
                        currentUserLocation = locationListener.currentLocation;
                        Collections.sort(mlist, new MarketDistanceComparator(currentUserLocation));
                        spinner.setAdapter(new MarketSpinnerAdapter(context, R.id.marketSpinner, mlist, currentUserLocation));
                    }else{
                        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת המיקום הנוכחי של המשתמש", Toast.LENGTH_LONG).show();


                        } else {
                            currentUserLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Collections.sort(mlist, new MarketDistanceComparator(currentUserLocation));
                            spinner.setAdapter(new MarketSpinnerAdapter(context, R.id.marketSpinner, mlist, currentUserLocation));
                        }
                    }




                }

                @Override
                public void onFailure(Call<List<Market>> call, Throwable throwable) {
                    Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת רשימת הסופרמרקטים", Toast.LENGTH_LONG).show();
                }
            });


            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Add action buttons
                    .setPositiveButton("בחר", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Spinner spinner = ((Spinner) ((AlertDialog) dialog).findViewById(R.id.marketSpinner));
                            Market selectedMarket = (Market)spinner.getSelectedItem();
                            AppCache.put("selectedMarket", selectedMarket);

                            ((GroupActivity)getActivity()).setFragment(new ShoppingListFragment());

                            //chosenMarket = (Market) ((Spinner)getActivity().findViewById(R.id.marketSpinner)).getSelectedItem();
                            //ChooseMarketDialogListener activity = (ChooseMarketDialogListener) getActivity();
                            //activity.onFinishChooseMarketDialog(chosenMarket);
                        }
                    }).setNegativeButton("בטל", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            //builder.setAdapter(new MarketSpinnerAdapter(context, R.id.marketSpinner, list, l));
            return builder.create();
        }
    }
}
