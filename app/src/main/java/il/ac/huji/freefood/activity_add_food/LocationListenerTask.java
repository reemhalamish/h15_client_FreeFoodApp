package il.ac.huji.freefood.activity_add_food;

//
//import android.content.Context;
//import android.content.IntentSender;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.parse.ParseGeoPoint;
//
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//import il.ac.huji.freefood.Application;
//import il.ac.huji.freefood.data.FoodListItem;
//import il.ac.huji.freefood.data.SingletonFoodList;
//
///**
// * Created by Re'em on 8/17/2015.
// */
//public class LocationListenerTask extends AsyncTask<Void, Void, Location>
//        implements
//        LocationListener,
//            GoogleApiClient.ConnectionCallbacks,
//            GoogleApiClient.OnConnectionFailedListener {
//    private static final int MIN_PRECISE_OKAY_IN_METERS = 15;
//    private static final int TIME_UNTIL_RETREAT_SECONDS = 60; // beyond that time we use the location we got and that's it
//    private int precise_cur_level = 999999;
//    private Context context;
//    private FoodListItem itemToWorkOn;
//    private Location curLocation;
//
//
//    // needed for the location -
//    private static final String REQUESTING_LOCATION_UPDATES_KEY = "request_location_updates";
//    private static final String LOCATION_KEY = "location_stored";
//    private static final String LAST_UPDATED_TIME_STRING_KEY = "last_updated_at";
//    private static final int REQUEST_RESOLVE_ERROR = 35;
//    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 26;
//    private Location mLastKnownLocation;
//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
//    private boolean mRequestingLocationUpdates;
//    private boolean mResolvingError;
//    private android.app.Activity activity;
//
//    private final String LOG_TAG = "location";
//
//
//    public LocationListenerTask(android.app.Activity senderActivity, FoodListItem itemToWorkOn) {
//        super();
//        this.activity = senderActivity;
//        this.context = senderActivity;
//        this.itemToWorkOn = itemToWorkOn;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        Toast.makeText(context, "capturing your GPS location. Food will be shared shortly!", Toast.LENGTH_LONG).show();
//        initLocationListener();
//    }
//
//
//    @Override
//    protected Location doInBackground(Void... voids) {
//    /*
//        get gps, wait up to one minute or until the area is close enougth (lonely meters) TODO
//        publish to parse and close.
//     */
//        Date now = new Date();
//        long now_l = now.getTime();
//        final long expirationDate = (now_l) + (TimeUnit.SECONDS.toMillis(TIME_UNTIL_RETREAT_SECONDS));
//        while (precise_cur_level > MIN_PRECISE_OKAY_IN_METERS || now_l > expirationDate) {
//            if (isCancelled())
//                return null;
//            now = new Date();
//            now_l = now.getTime();
//        }
//        return curLocation;
//    }
//
//    @Override
//    protected void onPostExecute(Location location) {
//        super.onPostExecute(location);
//
//        double lat = location.getLatitude();
//        double lng = location.getLongitude();
//        ParseGeoPoint itemLocation = new ParseGeoPoint(lat, lng);
//        itemToWorkOn.setLocation(itemLocation);
//        itemToWorkOn.setLocationReady();
//        SingletonFoodList.getInstance().addToList(itemToWorkOn);
//        Toast.makeText(context, "GPS captured. food shared!\npress \"I'm hungry!\" to see", Toast.LENGTH_LONG).show();
//        stopLocationUpdates();
//    }
//
//    @Override
//    protected void onCancelled() {
//        Toast.makeText(context, "food was not shared", Toast.LENGTH_LONG).show();
//        stopLocationUpdates();
//    }
//
//
//
//
//
//
//
//
//
//    // from now on there will be all the methods releated to the location-capturing
//
//    private void initLocationListener() {
//        // First we need to check availability of play services
//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//
//        } else {
//            Toast.makeText(context, "cant connect to google api client!", Toast.LENGTH_SHORT).show(); //TODO something better!
//        }
//    }
//
//    /**
//     * Method to verify google play services on the device
//     * */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Toast.makeText(context,
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build()
//                .connect();
//    }
//
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.d(LOG_TAG, "connected to google api");
//        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mRequestingLocationUpdates) {
//            createLocationRequest();
//            startLocationUpdates();
//        }
//    }
//
//    protected void startLocationUpdates() {
//        Log.d(LOG_TAG , "asking for location updates");
//        createLocationRequest();
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Log.d(LOG_TAG, "new location arrived!");
//        this.curLocation = location;
//        precise_cur_level = (int) location.getAccuracy();
//    }
//
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.e("gps", "connection to google api failed!");
//        mResolvingError = false;
//        if (mResolvingError) {
//            // Already attempting to resolve an error.
//            return;
//        } else if (result.hasResolution()) {
//            try {
//                mResolvingError = true;
//                result.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
//            } catch (IntentSender.SendIntentException e) {
//                // There was an error with the resolution intent. Try again.
//                mGoogleApiClient.connect();
//            }
//        } else {
//            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
//            Log.e("gps","couldn't connect to google api - " + result.getErrorCode());
//            mResolvingError = true;
//        }
//
//
//    }
//}
