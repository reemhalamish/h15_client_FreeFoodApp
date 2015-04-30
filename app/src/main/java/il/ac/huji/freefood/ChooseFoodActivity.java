package il.ac.huji.freefood;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.*;

import java.util.List;

/**
 * Created by Elyasaf on 4/30/2015.
 */
public class ChooseFoodActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        setContentView(R.layout.activity_loading);

        ImageView img = (ImageView)findViewById(R.id.iv_loader);
        img.setBackgroundResource(R.drawable.loading_animation);

        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();


        SingletonFoodList.getInstance().getList(new FindCallback<FoodListItem>() {

            @Override
            public void done(List<FoodListItem> foodListItems, ParseException e) {
                if (foodListItems == null){
                    ImageView img = (ImageView)findViewById(R.id.iv_loader);
                    img.setBackgroundResource(R.drawable.hungry);
                }
                else{
                    setContentView(R.layout.activity_show_food);
                    ListView lv_foodList = (ListView) findViewById(R.id.lv_FoodShowList);
                    ChooseFoodActivityAdapter aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, foodListItems);
                    lv_foodList.setAdapter(aa);
                }
            }
        });

    }
}
