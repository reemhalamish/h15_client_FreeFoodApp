package il.ac.huji.freefood;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import il.ac.huji.freefood.activities_one_class.ErrorDialog;
import il.ac.huji.freefood.data.FoodBuilder;
import il.ac.huji.freefood.data.LocationSuperviser;

/**
 * Created by Re'em on 8/22/2015.
 */
public class LocationCaptureService extends Service
        implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String FOOD_ID_TAG = "food_id";
    public static final String USER_LOCATION_TAG = "last_known_location";

    private static final int MIN_PRECISE_OKAY_IN_METERS = LocationSuperviser.MIN_PRECISE_TO_PUBLISH;
    private static final int MIN_PRECISE_GOOD_ENOUGH_FOR_IMMIDIATE_PUBLISH_IN_METERS = 20;
    private static final int TIME_UNTIL_RETREAT_SECONDS = 180; // beyond that time we use the location we got and that's it
    private static final int TIME_UNTIL_PARSE_RETREAT_MILISECONDS = 30 * 1000; // beyond that time we use the location we got and that's it

    private final String LOG_TAG = "location";
    private int precise_cur_level = 999999;
    private Context context;
    private Location curLocation;
    private long startCapturingLocationTime;
    private long stopCapturingLocationTime;
    // needed for the location -
//    private static final String REQUESTING_LOCATION_UPDATES_KEY = "request_location_updates";
//    private static final String LOCATION_KEY = "location_stored";
//    private static final String LAST_UPDATED_TIME_STRING_KEY = "last_updated_at";
//    private static final int REQUEST_RESOLVE_ERROR = 35;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 26;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = true;
    private boolean mResolvingError;
    private long food_id;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationSuperviser.updateServiceState(true); // need to tell him that the service is working
//        Log.d(LOG_TAG, "service is starting");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        Toast.makeText(this, "Capturing your location!", Toast.LENGTH_SHORT).show();

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
        ParseGeoPoint parse_location = LocationSuperviser.location_to_ParseGeoPoint(curLocation);
        int accuracy = (int) curLocation.getAccuracy();

        updateParse(parse_location, accuracy);

//        Log.d(LOG_TAG, "found sufficient location. stopping...");
        FoodBuilder.getInstance().serviceFoundLocation(parse_location, accuracy);
        quitImmediately();
    }

    private void updateParse(ParseGeoPoint parse_location, int accuracy) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put(USER_LOCATION_TAG, parse_location); // for other uses
        user.saveEventually();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(USER_LOCATION_TAG, parse_location);
        installation.saveEventually();
    }

    public void onTooLongLocationNotFound() {
//        Toast.makeText(this, "couldn't find sufficient location. Try connecting to a wifi or get out to the open air :)", Toast.LENGTH_LONG).show();
        if (! FoodBuilder.getInstance().dontNeedLocationAnymore()) { // service was called to update some food
            ErrorDialog.callErrorDialog(this,
                    "couldn't find sufficient location",
                    "Try connecting to a wifi or get out to the open air, and then share your food again!");
        }
        quitImmediately();
    }


    // from now on there will be all the methods releated to the location-capturing

    private void initLocationListener() {
        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

        } else {
            if (!FoodBuilder.getInstance().dontNeedLocationAnymore()) { // service was called to update some food

                ErrorDialog.callErrorDialog(this, "We couldn't reach GoogleServices", "So you can't share any food until you will install it. meanwhile you can browse the food that other people shared!");
//            startParseLocation();
            }
        }
    }

//    private void startItOldWay() {
//        startService(new Intent(this, LocationServiceOldWay.class));
//    }

//    private void startParseLocation() {
//        if (true) {
//            LocationSuperviser.updateServiceState(false);
////            startItOldWay();
////            stopSelf();
//            return;
//        }
////        Log.d(LOG_TAG, "google play won't work. Using parse");
//        Toast.makeText(context, "location capturing will take a while...", Toast.LENGTH_LONG).show();
//        ParseGeoPoint.getCurrentLocationInBackground(TIME_UNTIL_PARSE_RETREAT_MILISECONDS, new LocationCallback() {
//            @Override
//            public void done(ParseGeoPoint location, ParseException e) {
//                if (e != null) {
////                    Log.e(LOG_TAG, "using parse to find the location failed: " + e.getMessage());
//                    stopSelf();
//                    return;
//                }
////                Log.d(LOG_TAG, "using parse to find the location SUCCEEDED! " + location);
//
//                // a slightly changed version of onFindingLocation();
//                ParseUser user = ParseUser.getCurrentUser();
//                user.put(USER_LOCATION_TAG, location); // for other uses
//                user.saveEventually();
//                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//                installation.put(USER_LOCATION_TAG, location);
//                installation.saveEventually();
//
////                Log.d(LOG_TAG, "found sufficient location. stopping...");
//                FoodBuilder.getInstance().serviceFoundLocation(location, 100);
//                stopSelf();
//            }
//        });
//    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                Toast.makeText(context,
//                        "Google PlayServices is out of date. Please upgrade it.\nFood won't be shared",
//                        Toast.LENGTH_LONG)
//                        .show();
                if (! FoodBuilder.getInstance().dontNeedLocationAnymore()) { // service was called to update some food

                    ErrorDialog.callErrorDialog(this, "Google PlayServices is out of date", "So you can't share any food until you will update it. meanwhile you can browse the food that other people shared!");
//                startParseLocation();
                }
//
//  ONE_DAY add this piece of code to resolve the user's problem by downloading the relevant PlayServices
// GooglePlayServicesUtil.getErrorDialog(resultCode, getRunningActivitySomehow(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
//                Toast.makeText(context,
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//                startParseLocation();
                if (!FoodBuilder.getInstance().dontNeedLocationAnymore()) { // service was called to update some food

                    ErrorDialog.callErrorDialog(this, "We couldn't reach GoogleServices", "So you can't get your location, meaning you can't share any food until you will install it. meanwhile you can browse the food that other people shared!");
                }
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
//        Log.d(LOG_TAG, "connected to google api");
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
//        Log.d(LOG_TAG, "asking for location updates");
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        float precise = location.getAccuracy();

//        Log.d(LOG_TAG, "new location arrived!");
//        Log.d(LOG_TAG, "precise level: " + precise + ", current: " + precise_cur_level);

        if (FoodBuilder.getInstance().dontNeedLocationAnymore()) {
            quitImmediately();
        }
//        if one day I will use the location service for other stuff, I will need to check that as well so the checks will get to the locationSuperviser
//          for now there is no such a thing. Even then this whole app is based on location...


        if (location != null) {
            LocationSuperviser.updateLocation(curLocation);
        }
        if (precise < precise_cur_level) {

            this.curLocation = location;
            precise_cur_level = (int) precise;
            if (precise_cur_level <= MIN_PRECISE_GOOD_ENOUGH_FOR_IMMIDIATE_PUBLISH_IN_METERS) {
                onFindingLocation();
            }
        } else { // not having better precision over time anymore, time to decide!
            // first update parse
            ParseGeoPoint parse_location = LocationSuperviser.location_to_ParseGeoPoint(location);
            int accuracy = (int) curLocation.getAccuracy();
            updateParse(parse_location, accuracy);

            if (precise_cur_level < MIN_PRECISE_OKAY_IN_METERS) {
                onFindingLocation();
            } else if (new Date().getTime() > stopCapturingLocationTime) {
                onTooLongLocationNotFound();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
//        Log.e("gps", "connection to google api failed!");
        return;

//        // ONE_DAY, add this (this is a resolving error code)
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
//        Log.d("gps", "connection to google api suspended. trying again...");
        mGoogleApiClient.connect();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void quitImmediately() {
//        Log.d(LOG_TAG, "quitting... " + this);
        LocationSuperviser.updateServiceState(false); // need to tell him that the service isn't working anymore
        stopLocationUpdates();
        stopSelf();
    }
}
