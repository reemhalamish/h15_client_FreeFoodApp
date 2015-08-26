package il.ac.huji.freefood;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.parse.ParseGeoPoint;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import il.ac.huji.freefood.data.FoodListItem;
import il.ac.huji.freefood.data.SingletonFoodList;

/**
 * Created by Re'em on 8/22/2015.
 */
public class LocationCaptureService extends Service
        implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final int MIN_PRECISE_OKAY_IN_METERS = 15;
    private static final int TIME_UNTIL_RETREAT_SECONDS = 60; // beyond that time we use the location we got and that's it
    private int precise_cur_level = 999999;
    private Context context;
    private FoodListItem itemToWorkOn;
    private Location curLocation;
    private long startCapturingLocationTime;
    private long stopCapturingLocationTime;


    // needed for the location -
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "request_location_updates";
    private static final String LOCATION_KEY = "location_stored";
    private static final String LAST_UPDATED_TIME_STRING_KEY = "last_updated_at";
    private static final int REQUEST_RESOLVE_ERROR = 35;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 26;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = true;
    private boolean mResolvingError;

    private final String LOG_TAG = "location";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        itemToWorkOn = (FoodListItem) intent.getSerializableExtra("food");
        Log.d(LOG_TAG, "service is working on object: "+ itemToWorkOn);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Toast.makeText(this, "Starting service!", Toast.LENGTH_SHORT).show();

        startCapturingLocationTime = new Date().getTime();
        stopCapturingLocationTime = startCapturingLocationTime + TimeUnit.SECONDS.toMillis(TIME_UNTIL_RETREAT_SECONDS);
        mResolvingError = false;

        initLocationListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

 public void onFindingLocation() {
     SingletonFoodList.getInstance().updatePosition(curLocation);
     Log.d(LOG_TAG, "found sufficient location. stopping...");
     double lat = curLocation.getLatitude();
     double lng = curLocation.getLongitude();
     ParseGeoPoint itemLocation = new ParseGeoPoint(lat, lng);
     itemToWorkOn.setLocation(itemLocation);
     itemToWorkOn.setLocationReady();
     SingletonFoodList.getInstance().addToList(itemToWorkOn);
     Toast.makeText(context, "GPS captured. food shared!\npress \"I'm hungry!\" to see", Toast.LENGTH_LONG).show();
     stopLocationUpdates();
     stopSelf();
 }

    public void onTooLongLocationNotFound() {
        // TODO check if accuracy is good enough, if so then publish as "not to be trusted" and tell the user to help - add some text

        Toast.makeText(this, "couldn't find sufficient location.\nFood won't be shared. Try again!", Toast.LENGTH_LONG).show();
        stopLocationUpdates();
        stopSelf();
    }



    // from now on there will be all the methods releated to the location-capturing

    private void initLocationListener() {
        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

        } else {
            Toast.makeText(context, "cant connect to google api client!", Toast.LENGTH_SHORT).show(); //TODO something better!
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(context,
                        "Google PlayServices is out of date. Please upgrade it.\nFood won't be shared",
                        Toast.LENGTH_LONG)
                        .show();
//
//  TODO add this piece of code to resolve the user's problem by downloading the relevant PlayServices
// GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "connected to google api");
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        Log.d(LOG_TAG , "asking for location updates");
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, "new location arrived!");
        Log.d(LOG_TAG, "precise level: " + location.getAccuracy());
        float precise =location.getAccuracy();
        if (precise > precise_cur_level) {
            this.curLocation = location;
            precise_cur_level = (int) precise;
        }
        if (precise_cur_level <= MIN_PRECISE_OKAY_IN_METERS) {
            onFindingLocation();
        } else if (new Date().getTime() > stopCapturingLocationTime) {
            onTooLongLocationNotFound();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("gps", "connection to google api failed!");
        return;

//        // TODO one day, add this (this is a resolving error code)
//        if (mResolvingError) {
//            // Already attempting to resolve an error.
//            // then do nothing
//            return;
//        } else if (result.hasResolution()) {
//            try {
//                mResolvingError = true;
//                result.startResolutionForResult(context, REQUEST_RESOLVE_ERROR);
//            } catch (IntentSender.SendIntentException e) {
//                // There was an error with the resolution intent. Try again.
//                mGoogleApiClient.connect();
//            }
//        } else {
//            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
//            Log.e("gps", "couldn't connect to google api - " + result.getErrorCode());
//            mResolvingError = true;
//        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("gps", "connection to google api suspended. trying again...");
        mGoogleApiClient.connect();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

}
