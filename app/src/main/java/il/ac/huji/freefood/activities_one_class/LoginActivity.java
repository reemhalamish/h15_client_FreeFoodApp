package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import il.ac.huji.freefood.data.LocationSuperviser;


public class LoginActivity extends Activity {
    private final static int TIME_IN_SCREEN_MS = 800;
    private final static String NO_INTERNET_DIALOG_TITLE = "No Internet Connectivity";
    private final static String NO_INTERNET_DIALOG_MSG = "We can't access the internet,\nCheck your internet connectivity and try again.\n\nFreeFood will close now";
    private final static String NO_INTERNET_DIALOG_BUTTON = "Ok";

    private final static String TAG = "login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "connection test1 " + test1());
//        Log.d(TAG, "connection test2 " + test2());
//        Log.d(TAG, "connection test3 " + test3());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        try {
            final Context context = this;
            if (!connectedToInternet()) {
                exitAppWithDialog(context);
            }

            String uniqueID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            ParseUser.logInInBackground(uniqueID, uniqueID, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
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
                        startActivity(new Intent(context, SignUpActivity.class));
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }
    }

    private boolean connectedToInternet() {
        if (1 == 1) {
            return true;
        }
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

    public boolean test1() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public Boolean test2() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {
                return reachable;
            } else {
                Log.d(TAG, "No network available!");
            }
        } catch (Exception e) {

            Log.e(TAG, "Error checking internet connection", e);
        }
        return false;
    }

    private boolean test3() {
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

}
