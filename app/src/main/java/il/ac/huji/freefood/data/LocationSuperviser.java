package il.ac.huji.freefood.data;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import il.ac.huji.freefood.LocationCaptureService;

/**
 * Created by Re'em on 9/7/2015.
 */
public class LocationSuperviser { // ONE_DAY make it Singleton
    private static final float FACTOR_TO_ACCEPT_BAD_ACCURACY = 10f;
    public static final int MIN_PRECISE_TO_PUBLISH = 50;
    private static final double FOOD_CLOSE_CRITERIA_IN_METERS = 10;
    private static boolean serviceIsWorking = false;
    private static Location lastKnownLocation = null;
    public static boolean locationServiceIsActive() {
        return serviceIsWorking;
    }
    public static void updateServiceState(boolean workingState) {
        serviceIsWorking = workingState;
    }

    public static synchronized void updateLocation(Location location) {
        if (location == null) { return; }
        // Log.d("location", "new location to superviser! " + location);
        // Log.d("location", "superviser was " + lastKnownLocation);
        if (lastKnownLocation == null || location.getAccuracy() < lastKnownLocation.getAccuracy() * FACTOR_TO_ACCEPT_BAD_ACCURACY)
            lastKnownLocation = location;
        // Log.d("location", "superviser now " + lastKnownLocation);
    }


    public static ParseGeoPoint location_to_ParseGeoPoint(Location l) {
        return new ParseGeoPoint(l.getLatitude(), l.getLongitude());
    }

    @Nullable
    public static synchronized LatLng getLastLocation() {
        if (lastKnownLocation == null) {
            ParseGeoPoint lastParseKnown = ParseUser.getCurrentUser().getParseGeoPoint(LocationCaptureService.USER_LOCATION_TAG);
            if (lastParseKnown != null) {
                lastKnownLocation = parseToLocation(lastParseKnown);
                return parseToLatLng(lastParseKnown);
            }
            else {
                // couldn't get user location
                return null;
            }
        }
        return new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    public static LatLng parseToLatLng(ParseGeoPoint foodLocation) {
        return new LatLng(foodLocation.getLatitude(), foodLocation.getLongitude());
    }

    public static void startLocationServiceIfNeeded(Context context) {
        if (!serviceIsWorking) {
            Intent findLocationIntent = new Intent(context, LocationCaptureService.class);
            context.startService(findLocationIntent);
        }
    }

    public static boolean imWithinCloseArea(ParseGeoPoint foodLocation) {
        return calculateDistanceFromMyself(foodLocation) < FOOD_CLOSE_CRITERIA_IN_METERS;
    }

    public static double calculateDistanceFromMyself(ParseGeoPoint foodLocation) {
        if (lastKnownLocation == null)
            return Double.POSITIVE_INFINITY;
        double lat1 = lastKnownLocation.getLatitude();
        double lng1 = lastKnownLocation.getLongitude();
        double lat2 = foodLocation.getLatitude();
        double lng2 = foodLocation.getLongitude();
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;

    }

    public static boolean OkToStart() {
        return lastKnownLocation != null;
    }

    public static Location parseToLocation(ParseGeoPoint lastParseKnown) {
        Location ret_val = new Location("");
        ret_val.setLatitude(lastParseKnown.getLatitude());
        ret_val.setLongitude(lastParseKnown.getLongitude());
        return ret_val;
    }
}
