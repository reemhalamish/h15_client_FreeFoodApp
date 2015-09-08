package il.ac.huji.freefood;

/**
 * Created by reem on 7/29/15.
 * It's here in order to maintain the application state
 * (after getting closed, the app needs to restore communication to parse next time it gets opened)
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.io.File;
import java.util.Date;

import il.ac.huji.freefood.data.Food;
import il.ac.huji.freefood.data.LocationSuperviser;
import il.ac.huji.freefood.data.SingletonFoodList;

public class FreeFoodApplication extends android.app.Application {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public static boolean deleteInstallationCache(Context context) {
        boolean deletedParseFolder = false;
        File cacheDir = context.getCacheDir();
        File parseApp = new File(cacheDir.getParent(), "app_Parse");
        File installationId = new File(parseApp, "installationId");
        File currentInstallation = new File(parseApp, "currentInstallation");
        if (installationId.exists()) {
            deletedParseFolder = installationId.delete();
        }
        if (currentInstallation.exists()) {
            deletedParseFolder = deletedParseFolder && currentInstallation.delete();
        }

        return deletedParseFolder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = this;


        // parse-related actions
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Enable Local Datastore.
        Parse.enableLocalDatastore(context);
        ParseObject.registerSubclass(Food.class);
        Log.d("app", "reached1");
        Parse.initialize(context, "dKryMiFlnWz1NQLyS6Jt2uG3YVf5nqtuQd1iffxb", "2Hg8c7CUgwNLMrnDS82BpJa3tIMK3Q7CFNUgSYrA");
        Log.d("app", "reached2");
        deleteInstallationCache(this);
        Log.d("app", "reached3.0");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Log.d("app", "reached3.1");
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        Log.d("app", "reached4");


        //prepare the singleton
        Date lastUpdated;
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(FreeFoodApplication.this);
            lastUpdated = new Date(prefs.getLong("freefood.lastUpdated", 0));
            Log.d("last updated", "got from sharedPref" + lastUpdated);
        } catch (Exception e) {
            e.printStackTrace();
            lastUpdated = null;
            Log.d("last updated", e.getMessage());
        }
        SingletonFoodList.getInstance().init(FreeFoodApplication.this, lastUpdated);


        //location-related actions
        ParseGeoPoint lastParseKnown = ParseInstallation.getCurrentInstallation().getParseGeoPoint(LocationCaptureService.USER_LOCATION_TAG);
        if (lastParseKnown != null ) {
            Location lastKnownLocation = LocationSuperviser.parseToLocation(lastParseKnown);
            Log.d("app","sending location: " + lastKnownLocation);
            LocationSuperviser.updateLocation(lastKnownLocation);
        }
//        LocationSuperviser.startLocationServiceIfNeeded(this);

//
//        }}).start();

    }

    public void setLastUpdated(Date lastUpdated) {
        // TODO maybe use the ParseUser object (just add some date other there.
        // TODO maybe even save the user locally! and then create local use to this app!
        editor = prefs.edit();
        editor.putLong("freefood.lastUpdated", lastUpdated.getTime());
        editor.apply();
        Log.d("last updated", "put into sharedPref" + lastUpdated);
    }
}
