package il.ac.huji.freefood;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
        SingletonFoodList.getInstance().getList(new FindCallback<FoodListItem>() {
            @Override
            public void done(List<FoodListItem> foodListItems, ParseException e) {
                ListView lv_todoList = (ListView) findViewById(R.id.lv_FoodShowList);
                ChooseFoodActivityAdapter aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, foodListItems);
                lv_todoList.setAdapter(aa);
            }
        });
    }
}
