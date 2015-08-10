package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.activity_choose_food.ChooseFoodActivity;
import il.ac.huji.freefood.data.SingletonFoodList;

/**
 * Created by reem on 8/5/15.
 * Shows the user some nice UI when there is no food around, suggesting the user to share some food to friends as well
 */
public class NoFoodFoundActivity extends Activity {
    protected Handler listChangedHandler; // to be used if new food is arrived


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_no_food_found);

        // set the "add food" button on the screen
        ImageButton no_food_foond_ib = (ImageButton) findViewById(R.id.ib_no_food_addfood_button);
        no_food_foond_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFood = new Intent(context, AddFoodActivity.class);
                startActivity(addFood);
            }
        });

        // set the "refresh" button on the screen
        ImageButton check_again_ib = (ImageButton) findViewById(R.id.ib_no_food_checkagain_button);
        check_again_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonFoodList.getInstance().getOnlyNewElementsFromParse();
            }
        });

        // set the "back" button
        ImageButton back_ib = (ImageButton) findViewById(R.id.ib_no_food_back);
        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // set the handler
        listChangedHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (SingletonFoodList.getInstance().getClientFoodListItems().size() > 0) {
                            // new food! so go back to ChooseFood
                            Intent goToChooseFood = new Intent(NoFoodFoundActivity.this, ChooseFoodActivity.class);
                            startActivity(goToChooseFood);
                            finish(); // to get it out of the activities stack
                            Log.d("no food", "msg got!");
                        }
                    }
                });
            }
        };
        SingletonFoodList.getInstance().registerHandler(listChangedHandler);
    }

    @Override
    protected void onDestroy() {
        SingletonFoodList.getInstance().removeHandler(listChangedHandler);
        listChangedHandler = null;
        Log.d("no food", "destroyed");
        super.onDestroy();
    }
}
