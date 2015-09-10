package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.activity_choose_food.ChooseFoodActivity;


public class MainActivity extends Activity {
    final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnHungry = (ImageButton) findViewById(R.id.main_image_hungry);
        ImageButton btnAddFood = (ImageButton) findViewById(R.id.main_image_add_food);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callActivityAdd();
            }
        });
        btnHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callActivityHungry();
            }
        });
    }

    protected void callActivityAdd() {
        Intent addItemIntent = new Intent(this, AddFoodActivity.class);
        startActivity(addItemIntent); // ONE_DAY for result
    }
    protected void callActivityHungry() {
        Intent addItemIntent = new Intent(this, ChooseFoodActivity.class);
        startActivity(addItemIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            // ONE_DAY make the MainActivity extend ActionBarActivity instead
//            // and here startActivityForResult(settingsActivity)
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * back button from home will exit the app. api 5
     */
//    @Override
//    public void onBackPressed() {
//        Log.i("back", "back pressed");
//        Log.d("back", "back pressed!");
//        finish();
//        System.exit(0);
//    }


    /**
     * back button from home will exit the app. api 6+
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
////            Log.i("back", "back pressed");
////            Log.d("back", "back pressed");
////            finish();
////            System.exit(0);
////            return true;
////        }
//        return super.onKeyDown(keyCode, event);
//    }
}
