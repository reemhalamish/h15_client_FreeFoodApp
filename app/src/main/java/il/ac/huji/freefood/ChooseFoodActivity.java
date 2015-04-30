package il.ac.huji.freefood;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyasaf on 4/30/2015.
 */
public class ChooseFoodActivity extends Activity {
    List<FoodListItem> actualList = new ArrayList<FoodListItem>();//SingletonFoodList.getInstance().getList(); TODO
    ChooseFoodActivityAdapter aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);

        FoodListItem food1 = new FoodListItem(1,20,"kaplan","floor 1", "","a very ripe avocado");
        FoodListItem food2 = new FoodListItem(2,15,"shprinzak","floor 3", "", "leftovers from lecture");
        FoodListItem food3 = new FoodListItem(3,40,"shprinzak","floor 1", "", "leftover cakes");
        //SingletonFoodList.getInstance().addToList(food1); TODO
        actualList.add(food2);
        actualList.add(food3);
        ListView lv_todoList = (ListView) findViewById(R.id.lv_FoodShowList);
        aa = new ChooseFoodActivityAdapter(this, R.layout.choose_food_one_row, R.id.choose_food_picture, actualList);
        lv_todoList.setAdapter(aa);

    }
}
