package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import il.ac.huji.freefood.R;

/**
 * Created by Re'em on 9/8/2015.
 */
public class ErrorDialog extends Activity {
    public static void callErrorDialog(Context context, String title, String msg) {
        Intent errorIntent = new Intent(context, ErrorDialog.class);
        errorIntent.putExtra("title", title);
        errorIntent.putExtra("msg", msg);
        errorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(errorIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_error);
        String title = getIntent().getStringExtra("title");
        String msg = getIntent().getStringExtra("msg");

        ((TextView) findViewById(R.id.tv_error_title)).setText(title);
        ((TextView) findViewById(R.id.tv_error_msg)).setText(msg);

        findViewById(R.id.btn_error_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ErrorDialog.this, MainActivity.class));
                finish();
            }
        });
    }
}
