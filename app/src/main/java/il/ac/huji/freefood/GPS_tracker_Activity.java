package il.ac.huji.freefood;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Re'em on 8/13/2015.
 */
public class GPS_tracker_Activity extends Activity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "request_location_updates";
    private static final String LOCATION_KEY = "location_stored";
    private static final String LAST_UPDATED_TIME_STRING_KEY = "last_updated_at";
    private static final int REQUEST_RESOLVE_ERROR = 35;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 26;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;

    private TextView tv_lat, tv_lon, tv_precise, tv_last_updated;
    private Button btn_toggleRequest;
    private String mLastUpdateTime;
    private boolean mResolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        tv_lat = (TextView) findViewById(R.id.tv_gps_lat);
        tv_lon = (TextView) findViewById(R.id.tv_gps_lon);
        tv_precise = (TextView) findViewById(R.id.tv_gps_precise);
        tv_last_updated = (TextView) findViewById(R.id.tv_gps_last_updated);
        btn_toggleRequest = (Button) findViewById(R.id.btn_gps_toggle_request);

        updateValuesFromBundle(savedInstanceState);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        } else {
            Toast.makeText(this, "cant connect to google api client!", Toast.LENGTH_SHORT).show(); //TODO something better!
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
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
    


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            updateUI();
        }
    }

    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            btn_toggleRequest.setText("stop requesting");
            btn_toggleRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopLocationUpdates();
                    setButtonsEnabledState();
                }
            });
        } else {
            btn_toggleRequest.setText("start requestiong updates!");
            btn_toggleRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLocationUpdates();
                    setButtonsEnabledState();
                }
            });
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("gps", "connected to google api");
        Toast.makeText(this, "connected to google api!", Toast.LENGTH_SHORT).show();
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mCurrentLocation != null) {
            Toast.makeText(this, mCurrentLocation.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "cant find out last location!", Toast.LENGTH_SHORT).show();
        }
        if (mRequestingLocationUpdates) {
            createLocationRequest();
            startLocationUpdates();
        }
        setButtonsEnabledState();
        updateUI();
    }

    protected void startLocationUpdates() {
        Log.d("gps", "asking for location updates");
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d("gps", "new location arrived!");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        if (mCurrentLocation == null) {
            return;
        }
        tv_lat.setText("lat: " + String.valueOf(mCurrentLocation.getLatitude()));
        tv_lon.setText("lon: " + String.valueOf(mCurrentLocation.getLongitude()));
        tv_precise.setText("precise: " + String.valueOf(mCurrentLocation.getAccuracy()));
        tv_last_updated.setText(mLastUpdateTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }



    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("gps", "connection to google api suspended. trying again...");
        mGoogleApiClient.connect();


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("gps", "connection to google api failed!");
        mResolvingError = false;
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            Log.e("gps","couldn't connect to google api - " + result.getErrorCode());
            mResolvingError = true;
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
}
