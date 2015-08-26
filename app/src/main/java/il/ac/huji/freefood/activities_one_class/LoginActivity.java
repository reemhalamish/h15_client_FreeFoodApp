package il.ac.huji.freefood.activities_one_class;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.activity_add_food.AddFoodActivity;
import il.ac.huji.freefood.data.ImportantDataSaver;


public class LoginActivity extends ActionBarActivity {
    private final static int TIME_IN_SCREEN_MS = 800;
    private final static String NO_INTERNET_DIALOG_TITLE = "No Internet Connectivity";
    private final static String NO_INTERNET_DIALOG_MSG = "We can't access the internet,\nCheck your internet connectivity and try again.\n\nFreeFood will close now";
    private final static String NO_INTERNET_DIALOG_BUTTON = "Ok";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        startActivity(new Intent(this, AddFoodActivity.class));
//        if (1 == 1) {finish(); return;} //TODO

        try {
            final Context context = this;

            String uniqueID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            ParseUser.logInInBackground(uniqueID, uniqueID, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        ImportantDataSaver.getInstance().setUser(user);
                        TextView tv_underText = (TextView) findViewById(R.id.tv_login_undertext);
                        String username = user.getString("name");
                        if (username != null) {
                            tv_underText.setText("Hello " + username + "!");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent mainIntent = new Intent(context, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }, TIME_IN_SCREEN_MS);
                        }

                    } else {
                        if (!connectedToInternet()) {
                            exitAppWithDialog(context);
                        } else {
                            startActivity(new Intent(context, SignUpActivity.class));
                            finish();
                        }
                    }
                }
            });
        } catch (Exception e) {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }
    }

    private boolean connectedToInternet() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("internet", "IO " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("internet", "Interrupt " + e.getMessage());
        }

        return false;
    }

    private void exitAppWithDialog(Context context) {
        AlertDialog.OnClickListener button_listener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        };

        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(NO_INTERNET_DIALOG_TITLE);
        dlg.setMessage(NO_INTERNET_DIALOG_MSG);
        dlg.setCancelable(false);
        dlg.setPositiveButton(NO_INTERNET_DIALOG_BUTTON, button_listener);
        dlg.create();
        dlg.show();
    }
}
