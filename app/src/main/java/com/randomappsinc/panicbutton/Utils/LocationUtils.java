package com.randomappsinc.panicbutton.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexanderchiou on 2/18/16.
 */
public class LocationUtils {
    public static String getCurrentAddress() {
        LocationManager locationManager = (LocationManager) MyApplication.getAppContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Geocoder geocoder = new Geocoder(MyApplication.getAppContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder addressText = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        if (i != 0) {
                            addressText.append("\n");
                        }
                        addressText.append(address.getAddressLine(i));
                    }
                    return addressText.toString();
                }
            }
        }
        catch (SecurityException|IOException ignored) {}
        return "";
    }
}
