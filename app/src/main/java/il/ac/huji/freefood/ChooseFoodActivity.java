package il.ac.huji.freefood;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Elyasaf on 4/30/2015.
 */
public class ChooseFoodActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_show_food);

        ImageView img = (ImageView)findViewById(R.id.iv_loader);
        img.setBackgroundResource(R.drawable.loading_animation);

        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();


        SingletonFoodList.getInstance().getList(new FindCallback<FoodListItem>() {
            public void done(List<FoodListItem> foodListItems, ParseException e) {
                ListView lv_foodList = (ListView) findViewById(R.id.lv_FoodShowList);
                FoodListItem food1 = new FoodListItem(2,15,"shprinzak","floor 3", "", "leftovers from lecture");
                FoodListItem food2 = new FoodListItem(3,40,"shprinzak","floor 1", "", "leftover cakes");
                foodListItems.add(food1);
                foodListItems.add(food2);
                ChooseFoodActivityAdapter aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, foodListItems);
                lv_foodList.setAdapter(aa);

            }
        });
    }
}
