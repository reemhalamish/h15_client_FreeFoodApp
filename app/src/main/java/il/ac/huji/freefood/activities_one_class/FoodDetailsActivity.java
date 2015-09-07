package il.ac.huji.freefood.activities_one_class;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.activity_choose_food.ChooseFoodActivity;
import il.ac.huji.freefood.data.Food;
import il.ac.huji.freefood.data.LocationSuperviser;
import il.ac.huji.freefood.data.SingletonFoodList;
import il.ac.huji.freefood.data.Thumbnail;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Re'em on 9/7/2015.
 */
public class FoodDetailsActivity extends FragmentActivity implements
        OnMapReadyCallback {
    public static final String MY_PLACE_INTENT_TAG = "my_place_parcelable";
    public static final String FOOD_PLACE_INTENT_TAG = "food_place_parcelable";
    public static final java.lang.String INDEX_OF_ME_INTENT_TAG = "index_of_me at the client food list";

    private Format formatter;
    private TextView title, details, timestamp;
    private GoogleMap map;

    private Food food = null;
    private LatLng myPlace = null;
    private LatLng foodPlace = null;
    private int index_of_me = -1;
    private Marker markerFood;
    private Marker markerMyself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);


        getThePrivateVarsReady(savedInstanceState);

        updateUiFoodDetails();
        createListenersForButtons();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_details);
        mapFragment.getMapAsync(this);
    }

    private void updateUiFoodDetails() {
        TextView title, details, timestamp;
        title = (TextView) findViewById(R.id.tv_details_title);
        details = (TextView) findViewById(R.id.tv_details_details);
        timestamp = (TextView) findViewById(R.id.tv_details_timestamp);
        ImageView iv_thumbnail = (ImageView) findViewById(R.id.iv_details_thumbnail);

        title.setText(food.getTitle());
        iv_thumbnail.setImageResource(food.getThumbnail().toDrawableID());
        timestamp.setText("created: " + formatter.format(food.getCreatedAt()));

        if (LocationSuperviser.imWithin50Meters(food.getLocation())) {
            double distance = LocationSuperviser.calculateDistanceFromMyself(food.getLocation());
            NumberFormat formatter = new DecimalFormat("#0.00");
            String distanceAsString = formatter.format(distance);
            details.setText("FOOD IS NEAR! :)\nDistance: " + distanceAsString + "meters!\n\n" + food.getDetails());
        } else {
            details.setText(food.getDetails());
        }
    }

    private void createListenersForButtons() {
        // set the "refresh" button on the screen
        ImageButton check_again_ib = (ImageButton) findViewById(R.id.ib_details_refresh);
        check_again_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationSuperviser.startLocationServiceIfNeeded(getApplicationContext());
                updateUiFoodDetails(); // if location is close enough, it will be reflected in the Title
            }
        });

        // set the "back" button
        ImageButton back_ib = (ImageButton) findViewById(R.id.ib_details_back);
        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // finally, set the "share location outside" button
        Button btn_shareExternal = (Button) findViewById(R.id.btn_details_open_external);
        final float lat, lng;
        lat = (float) food.getLocation().getLatitude();
        lng = (float) food.getLocation().getLongitude();
        btn_shareExternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                Uri outsideUri = Uri.parse(uri);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(outsideUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(FoodDetailsActivity.this, "You have no app that can handle this info, sorry!", LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (mapIsReady()) {
            // update the current position
            // TODO one day, i can register some handlers to the LocationSuperviser the same as with the Singleton so I can update it regullarly
            map.clear();
            addMarkersToMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.getUiSettings().setZoomControlsEnabled(false);
//        map.getUiSettings().setCompassEnabled(false);
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//
        map.setContentDescription("Map with lots of markers.");
        addMarkersToMap();

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map_fragment_details).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(myPlace)
                            .include(foodPlace)
                            .build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }
            });
        }
    }

    private void addMarkersToMap() {
        markerFood = map.addMarker(new MarkerOptions()
                .position(foodPlace)
                .title("food")
                .snippet("created: " + formatter.format(food.getCreatedAt()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        );
        myPlace = LocationSuperviser.getLastLocation();
        if (myPlace != null) {
            markerMyself = map.addMarker(new MarkerOptions()
                            .position(myPlace)
                            .title("you")
                            .snippet("click at the 'refresh' to update")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
        }
    }

    public void getThePrivateVarsReady(Bundle savedInstanceState) {
        formatter = new SimpleDateFormat("EEEE HH:mm:ss", Locale.getDefault());
        if (savedInstanceState != null) {
            myPlace = savedInstanceState.getParcelable(MY_PLACE_INTENT_TAG);
            foodPlace = savedInstanceState.getParcelable(FOOD_PLACE_INTENT_TAG);
            index_of_me = savedInstanceState.getInt(INDEX_OF_ME_INTENT_TAG);
            food = SingletonFoodList.getInstance().getCliendFoodAtIndex(index_of_me);
        } else {
            Intent senderIntent = getIntent();
            index_of_me = senderIntent.getIntExtra(ChooseFoodActivity.INTENT_FROM_CHOOSEFOOD_TAG, -1);
            if (index_of_me == -1) {
                Log.e("details", "not getting index of item to show details!!");
                finish();
            }
            food = SingletonFoodList.getInstance().getCliendFoodAtIndex(index_of_me);
            foodPlace = LocationSuperviser.parseToLatLng(food.getLocation());
            myPlace = LocationSuperviser.getLastLocation();

        }
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MY_PLACE_INTENT_TAG, myPlace);
        outState.putParcelable(FOOD_PLACE_INTENT_TAG, foodPlace);
        super.onSaveInstanceState(outState);
    }

    private boolean mapIsReady() {
        return map != null;
    }
}
