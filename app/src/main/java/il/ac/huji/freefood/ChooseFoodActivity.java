package il.ac.huji.freefood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.*;

import java.util.List;

/**
 * Created by Elyasaf on 4/30/2015.
 */
public class ChooseFoodActivity extends Activity {

    private List<FoodListItem> localCopyOfTheList;
    ChooseFoodActivityAdapter aa;

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
                if (foodListItems == null || foodListItems.size() == 0) {
                    ImageView img = (ImageView) findViewById(R.id.iv_loader);
                    img.setBackgroundResource(R.drawable.no_food_found);
                } else {
                    setContentView(R.layout.activity_show_food);
                    ListView lv_foodList = (ListView) findViewById(R.id.lv_FoodShowList);
                    aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, foodListItems);
                    lv_foodList.setAdapter(aa);
                    localCopyOfTheList = foodListItems;
                    registerForContextMenu(lv_foodList);
                }
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.lv_FoodShowList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            FoodListItem curItem = localCopyOfTheList.get(info.position);
            menu.setHeaderTitle(curItem.getDescription());
            menu.add(ContextMenu.NONE, 0, 0, "Report gone");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        // 0 for delete, 1 for calling. [as written in onCreateContextMenu() ]

        switch (menuItemIndex) {
            case 0:
                openDialogDeleteItem(info.position);
                break;
        }
        return true;
    }

    protected void openDialogDeleteItem(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final FoodListItem item = localCopyOfTheList.get(index);
        String text = item.getDescription();
        String msg = "This will remove this item from the list.";
        builder.setTitle("Are you sure?");
        builder.setMessage(msg);
        // Set up the buttons
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //update the db and the view
                SingletonFoodList.getInstance().deleteFromList(item.getObjectId());
                localCopyOfTheList.remove(index);
                aa.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
