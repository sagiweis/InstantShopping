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
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dys.instantshopping.Comparators.MarketDistanceComparator;
import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.MarketSpinnerAdapter;
import com.dys.instantshopping.objects.Market;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class ChooseMarketDialogFragment extends DialogFragment implements Spinner.OnItemSelectedListener {

    private Market chosenMarket;
    ArrayList<Market> mlist;
    ChooseMarketDialogFragment mHost = (ChooseMarketDialogFragment)getTargetFragment();

    public Market getMarket(){
        return this.chosenMarket;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setTargetFragment(this, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.content_group_list, container, false);
        final Activity context = getActivity();

        Location l = new Location("MyLocation");
        l.setLatitude(32.033470);
        l.setLongitude(34.881564);

        Location l1 = new Location("שופרסל"); l1.setLatitude(32.030124); l1.setLongitude(34.878045);
        Location l2 = new Location("חצי חינם"); l2.setLatitude(32.003929); l2.setLongitude( 34.803373);
        ArrayList<Market> list = new ArrayList<Market>();
        list.add(new Market(l1, "שופרסל"));
        list.add(new Market(l2, "חצי חינם"));
        mlist = list;
        Collections.sort(mlist, new MarketDistanceComparator(l));
        View view = inflater.inflate(R.layout.choose_market_dialog, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.marketSpinner);
        spinner.setAdapter(new MarketSpinnerAdapter(context, R.id.marketSpinner, list, l));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chosenMarket = mlist.get(position);
                ChooseMarketDialogListener activity = (ChooseMarketDialogListener) getActivity();
                activity.onFinishChooseMarketDialog(chosenMarket);
                dismiss();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("בחר", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //chosenMarket = (Market) ((Spinner)getActivity().findViewById(R.id.marketSpinner)).getSelectedItem();
                        //ChooseMarketDialogListener activity = (ChooseMarketDialogListener) getActivity();
                        //activity.onFinishChooseMarketDialog(chosenMarket);
                    }
                }).setNegativeButton("בטל", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
        });
        //builder.setAdapter(new MarketSpinnerAdapter(context, R.id.marketSpinner, list, l));
        return builder.create();

    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (EditorInfo.IME_ACTION_DONE == id) {
            // Return input text to activity
            this.chosenMarket = mlist.get(position);
            ChooseMarketDialogListener activity = (ChooseMarketDialogListener) getActivity();
            activity.onFinishChooseMarketDialog(this.chosenMarket);
            this.dismiss();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface ChooseMarketDialogListener {
        void onFinishChooseMarketDialog(Market market);
    }

    /*@Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            ChooseMarketDialogListener activity = (ChooseMarketDialogListener) getActivity();
            activity.onFinishChooseMarketDialog(this.chosenMarket);
            this.dismiss();
            return true;
        }
        return false;
    }*/

    // TODO: started working on the location
    /*public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_group_list, container, false);
        final Activity context = getActivity();
        SensorManager s = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return view;
    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }*/
}
