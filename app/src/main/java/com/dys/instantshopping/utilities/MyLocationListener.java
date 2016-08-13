package com.dys.instantshopping.utilities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Sagi on 13/08/2016.
 */
public class MyLocationListener implements LocationListener {

    public Location currentLocation;
    @Override
    public void onLocationChanged(Location loc) {
        currentLocation = loc;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
