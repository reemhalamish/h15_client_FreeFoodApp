package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import il.ac.huji.freefood.R;

public class SignUpActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signIn = (Button) findViewById(R.id.btn_sign_done);
        final Context context = this;
        final EditText userInput = (EditText) findViewById(R.id.edt_sign_input);

        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String name = userInput.getText().toString();
                if (name == null || name.length() == 0) {
                    userInput.setHint("any nickname you want!");
                    return;
                }
                String uniqueID = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                final ParseUser user = new ParseUser();
                user.setUsername(uniqueID);
                user.setPassword(uniqueID);
                user.put("name", name);
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) { // no errors!
                            Intent mainIntent = new Intent(context, MainActivity.class);
                            mainIntent.putExtra("name", name);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                            dlgAlert.setTitle("Unable to sign up");
                            dlgAlert.setMessage(e.getMessage());
                            dlgAlert.create().show();
                        }
                    }
                });
            }

        });
    }


}



