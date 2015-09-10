package il.ac.huji.freefood;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import il.ac.huji.freefood.data.FoodBuilder;
import il.ac.huji.freefood.data.LocationSuperviser;

public class LocationServiceOldWay extends Service {
        private static final String TAG = "BOOMBOOMTESTGPS";
        private LocationManager mLocationManager = null;
        private static final int LOCATION_INTERVAL = 1000;
        private static final float LOCATION_DISTANCE = 5f; // the amount of meters the user will move in order to call onLocationChanged() again

        private static boolean wifi_only = false;

        private class LocationListener implements android.location.LocationListener{
            Location mLastLocation;
            public LocationListener(String provider)
            {
                // log.e(TAG, "LocationListener " + provider);
                mLastLocation = new Location(provider);
            }
            @Override
            public void onLocationChanged(final Location location)
            {
                // log.d(TAG, "location changed. providor: " + location.getProvider());
                if (location.getProvider().toLowerCase() == "network") {
                    //  so maybe the gps can't work and we stuck with this netwoek forever :\
                    //     i.e. no further updates
                    // so we will just publish "as is"
                    foundLocation(location);
                    } else {
                    // Gps. publish if you're good with the results
                    if (location.getAccuracy() < LocationSuperviser.MIN_PRECISE_TO_PUBLISH)
                        foundLocation(location);
                }
                // log.e(TAG, "onLocationChanged: " + location);
                mLastLocation.set(location);
            }

            private void foundLocation(Location location) {
                FoodBuilder.getInstance().serviceFoundLocation(LocationSuperviser.location_to_ParseGeoPoint(location), (int) location.getAccuracy());
                quit();
            }

            @Override
            public void onProviderDisabled(String provider)
            {
                // log.e(TAG, "onProviderDisabled: " + provider);
            }
            @Override
            public void onProviderEnabled(String provider)
            {
                // log.e(TAG, "onProviderEnabled: " + provider);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                // log.e(TAG, "onStatusChanged: " + provider);
            }
        }
        LocationListener[] mLocationListeners = new LocationListener[] {
                new LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };
        @Override
        public IBinder onBind(Intent arg0)
        {
            return null;
        }
        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            // log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);
            return START_STICKY;
        }
        @Override
        public void onCreate()
        {
            // log.e(TAG, "onCreate");
            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                // log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                // log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
                wifi_only = false;
            } catch (java.lang.SecurityException ex) {
                // log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                // log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }

        public void quit() {
            LocationSuperviser.updateServiceState(false); // getting out
            cleanUp();
        }

    private void cleanUp() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    // log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Override
        public void onDestroy()
        {
            // log.e(TAG, "onDestroy");
            super.onDestroy();
            cleanUp();
        }
        private void initializeLocationManager() {
            // log.e(TAG, "initializeLocationManager");
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
        }
    }