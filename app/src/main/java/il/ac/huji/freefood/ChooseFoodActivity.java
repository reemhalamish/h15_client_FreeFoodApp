package il.ac.huji.freefood;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Elyasaf on 4/30/2015.
 */
public class ChooseFoodActivity extends Activity {
    List<FoodListItem> actualList;
    ChooseFoodActivityAdapter aa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_food_one_row);

        FoodListItem food1 = new FoodListItem(1,20,"kaplan","floor 1", "avocado.jpg","a very ripe avocado","avocado");
        FoodListItem food2 = new FoodListItem(2,15,"shprinzak","floor 3", "rogalachs.jpg", "leftovers from lecture", "rogalachs");
        FoodListItem food3 = new FoodListItem(3,40,"shprinzak","floor 1", "cakes.jpg", "leftover cacks", "cacks");
        actualList.add(food1);
        actualList.add(food2);
        actualList.add(food3);
        ListView lv_todoList = (ListView) findViewById(R.id.lv_FoodShowList);
        aa = new ChooseFoodActivityAdapter(this, R.layout.choose_food_one_row, R.id.choose_food_picture, actualList);
        lv_todoList.setAdapter(aa);

    }
}
