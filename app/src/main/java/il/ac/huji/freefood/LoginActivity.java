package il.ac.huji.freefood;

import android.content.Context;
import android.content.Intent;
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
        setContentView(R.layout.activity_login2);
        try {
            InputStreamReader inputStreamReader= new InputStreamReader(this.openFileInput("cred.txt"));
            BufferedReader credentials = new BufferedReader(inputStreamReader);
            String name = credentials.readLine();
            String password = credentials.readLine();
            final Context context = this;
            ParseUser.logInInBackground(name, password, new LogInCallback() {
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
