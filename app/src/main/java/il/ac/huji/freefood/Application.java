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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.util.Date;

import il.ac.huji.freefood.data.FoodListItem;
import il.ac.huji.freefood.data.SingleShotLocationProvider;
import il.ac.huji.freefood.data.SingletonFoodList;

public class Application extends android.app.Application {
    private static final String APP_ID = "dKryMiFlnWz1NQLyS6Jt2uG3YVf5nqtuQd1iffxb";
    private static final String CLIENT_KEY = "2Hg8c7CUgwNLMrnDS82BpJa3tIMK3Q7CFNUgSYrA";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (1 == 1) {finish(); return;} //TODO


        final Context context = this;



        Log.d("app", "reached1");

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Log.d("app", "reached2");

        ParseObject.registerSubclass(FoodListItem.class);
        Log.d("app", "reached3");

        Parse.initialize(this);
//        Parse.initialize(this, APP_ID, CLIENT_KEY);
        Log.d("app", "reached4");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        Log.d("app", "reached5");



        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("app", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("app", "failed to subscribe for push", e);
                }
            }
        });


        // get my location
        Log.d("app", "reached_a");
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(Location location) {
                        Log.d("Location", "my location is " + location.toString());
                        //prepare the singleton
                        Date lastUpdated;
                        try {
                            prefs = PreferenceManager.getDefaultSharedPreferences(Application.this);
                            lastUpdated = new Date(prefs.getLong("freefood.lastUpdated", 0));
                            Log.d("last updated", "got from sharedPref" + lastUpdated);
                        } catch (Exception e) {
                            Log.e("last updated", e.getMessage());
                            e.printStackTrace();
                            lastUpdated = null;
                        }
                        SingletonFoodList.getInstance().init(Application.this, lastUpdated, location);

                    }
                });


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
