package il.ac.huji.freefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {
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
        startActivityForResult(addItemIntent, REQUEST_CODE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
