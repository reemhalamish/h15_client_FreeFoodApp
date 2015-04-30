package il.ac.huji.freefood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;

import com.parse.LogInCallback;
import com.parse.*;


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
                        SingletonFoodList.getInstance().addToList(new FoodListItem(2, "shprinzak", "floor 2",
                                "rogalachs.jpg", "Yummy rogalachs"));
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
