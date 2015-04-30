package il.ac.huji.freefood;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Activity {

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
        setContentView(R.layout.activity_login);
        Button signIn = (Button) findViewById(R.id.email_sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener(){
                                      @Override
                                      public void onClick(View v) {
                                          String email = ((TextView) findViewById(R.id.activityLogin_enterYourName)).getText().toString();
                                          String password = email;
                                          ParseUser user = new ParseUser();
                                          user.setUsername("my name");
                                          user.setPassword("my pass");
                                          user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
                                          user.put("phone", "650-253-0000");

                                          user.signUpInBackground(new SignUpCallback() {
                                              public void done(ParseException e) {
                                                  if (e == null) {
                                                      // Hooray! Let them use the app now.
                                                  } else {
                                                      // Sign up didn't succeed. Look at the ParseException
                                                      // to figure out what went wrong
                                                  }
                                              }
                                          });
                                      }

                                  }

            );
    }


}



