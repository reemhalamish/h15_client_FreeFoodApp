package il.ac.huji.freefood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.*;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class SignUpActivity extends Activity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signIn = (Button) findViewById(R.id.button);
        final Context context = this;
        signIn.setOnClickListener(new View.OnClickListener(){
                                      @Override
                                      public void onClick(View v) {
                                          String uniqueID = Settings.Secure.getString(context.getContentResolver(),
                                                  Settings.Secure.ANDROID_ID);
                                          ParseUser user = new ParseUser();
                                          user.setUsername(uniqueID);
                                          user.setPassword(uniqueID);

                                          user.signUpInBackground(new SignUpCallback() {
                                              public void done(ParseException e) {
                                                  if (e == null) {
                                                      startActivity(new Intent(context, ChooseFoodActivity.class));
                                                  } else {
                                                      AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                                                      dlgAlert.setTitle("Unable to sign up");
                                                      dlgAlert.setMessage(e.getMessage());
                                                      dlgAlert.create().show();
                                                  }
                                              }
                                          });
                                      }

                                  }

            );
    }


}



