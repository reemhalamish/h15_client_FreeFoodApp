package il.ac.huji.freefood;

/**
 * Created by reem on 7/29/15.
 * It's here in order to maintain the application state
 * (after getting closed, the app needs to restore communication to parse next time it gets opened)
 */

import android.content.SharedPreferences;
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
import il.ac.huji.freefood.data.SingletonFoodList;

public class Application extends android.app.Application {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(FoodListItem.class);
        Parse.initialize(this, "dKryMiFlnWz1NQLyS6Jt2uG3YVf5nqtuQd1iffxb", "2Hg8c7CUgwNLMrnDS82BpJa3tIMK3Q7CFNUgSYrA");
        ParseInstallation.getCurrentInstallation().saveInBackground();
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

        //prepare the singleton
        Date lastUpdated;
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            lastUpdated = new Date(prefs.getLong("freefood.lastUpdated", 0));
            Log.d("last updated", "got from sharedPref" + lastUpdated);
        } catch (Exception e) {
            e.printStackTrace();
            lastUpdated = null;
            Log.d("last updated", e.getMessage());
        }
        SingletonFoodList.getInstance().init(this, lastUpdated);


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
