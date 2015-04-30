package il.ac.huji.freefood;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(FoodListItem.class);
        Parse.initialize(this, "dKryMiFlnWz1NQLyS6Jt2uG3YVf5nqtuQd1iffxb", "2Hg8c7CUgwNLMrnDS82BpJa3tIMK3Q7CFNUgSYrA");


        setContentView(R.layout.activity_login2);
        try {
            final Context context = this;
            String uniqueID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            ParseUser.logInInBackground(uniqueID, uniqueID, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        startActivity(new Intent(context, ChooseFoodActivity.class));
                    } else {
                        startActivity(new Intent(context, SignUpActivity.class));
                    }
                }
            });
        } catch (Exception e) {
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
