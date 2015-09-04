package il.ac.huji.freefood.activity_choose_food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.activities_one_class.NoFoodFoundActivity;
import il.ac.huji.freefood.data.Food;
import il.ac.huji.freefood.data.SingletonFoodList;

/**
 * Created by Elyasaf on 4/30/2015.
 *
 * the activity that chooses food
 */
public class ChooseFoodActivity extends Activity// implements LoaderManager.LoaderCallbacks<List<FoodListItem>> {
{
    public View.OnTouchListener dismissListener;
    protected ChooseFoodActivityAdapter aa;
    protected Handler listChangedHandler;
    protected ImageButton refreshButton;
    protected ImageButton clearAllButton;
    protected ImageButton backButton;
    protected ListView lv_foodList;
    protected List<Food> listFromSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);
        Log.d("no_food", "choose food called");
        lv_foodList = (ListView) findViewById(R.id.lv_FoodShowList);
        final Context context = this;


        // set the touch-listener for every item in the list
        dismissListener = createDismissListener();

        // set the adapter
        listFromSingleton = SingletonFoodList.getInstance().getClientFoodListItems();
        aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, listFromSingleton, dismissListener);
        lv_foodList.setAdapter(aa);

        // set the handler
        listChangedHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aa.updatedList();
                        if (aa.getCount() == 0) {
                            goto_no_food_activity();
                        }
                    }
                });
            }
        };
        SingletonFoodList.getInstance().registerHandler(listChangedHandler);


        // set the refresh button
        refreshButton = (ImageButton) findViewById(R.id.ib_showfood_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonFoodList.getInstance().getOnlyNewElementsFromParse();
                // it will automatically get inside this list via the handler
                // TODO add some graphic of work?
                // i can add them at the layer of the button. radio-waves-inside-out style :)
            }
        });

        //set the clear-all button
        clearAllButton = (ImageButton) findViewById(R.id.ib_showfood_clear);
        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonFoodList.getInstance().clearAndUnpinAllItems();
                // it will automatically get inside this list via the handler
                // TODO add an alert!!

                // TODO add some visuality?
                // i can add somthing like the garbage-can is tilting
            }
        });

        //set the back button
        backButton = (ImageButton) findViewById(R.id.ib_showfood_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void goto_no_food_activity() {
        Intent noFoodFound = new Intent(this, NoFoodFoundActivity.class);
        startActivity(noFoodFound);
        finish(); // to get it out of the activities stack
    }

    protected int getScreenWidth() {
        final int version = android.os.Build.VERSION.SDK_INT;
        final int width;
        Display display = getWindowManager().getDefaultDisplay();
        if (version >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            //noinspection deprecation
            width = display.getWidth();
        }
        return width;
    }

    protected void deleteFoodListItem(int index) {
        Log.d("delete", "start");
        SingletonFoodList.getInstance().removeItem(index);
        // update will be via the singleton and the handler
    }

    protected View.OnTouchListener createDismissListener() {
        final Context context = this;
        return new View.OnTouchListener() {
            private final float screen_width = getScreenWidth();
            private final float CRITICAL_PADDING = screen_width / 4.0f;
            private int padding = 0;
            private int initialx = 0;
            private int currentx = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int index_of_me = (int) v.getTag();

                final ImageView iv_icon = (ImageView) v.findViewById(R.id.choose_food_picture);
                final TextView tv_title = (TextView) v.findViewById(R.id.choose_food_title);
                final TextView tv_dismissText = (TextView) v.findViewById(R.id.tv_choose_food_dismiss);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    padding = 0;
                    initialx = (int) event.getX();
                    currentx = (int) event.getX();
                    //viewHolder = ((ViewHolder) v.getTag());
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    currentx = (int) event.getX();
                    padding = currentx - initialx;
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (Math.abs(padding) >= CRITICAL_PADDING) {
                        // create fade-out animation and call delete() at the end
                        Animation shrinkAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_to_middle);
                        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation arg0) {
                            }

                            public void onAnimationRepeat(Animation arg0) {
                            }
                            public void onAnimationEnd(Animation arg0) {
                                tv_dismissText.setTextColor(Color.WHITE);
                                tv_dismissText.setAlpha(0);
                                deleteFoodListItem(index_of_me);
                            }
                        });
                        v.startAnimation(shrinkAnimation);
                    } else {
                        tv_dismissText.setTextColor(Color.WHITE);
                        tv_dismissText.setAlpha(0);
                    }
                    padding = 0;
                    initialx = 0;
                    currentx = 0;
                }


                if (padding != 0) {
                    tv_dismissText.setAlpha(Math.min(Math.abs(4.0f * padding) / screen_width, 1.0f));
                    if (Math.abs(padding) >= CRITICAL_PADDING) {
                        tv_dismissText.setTextColor(Color.RED);
                    } else {
                        tv_dismissText.setTextColor(Color.WHITE);
                    }

                }
                return true;
            }
        };
    }

    @Override
    protected void onResume() {
        if (aa.getCount() == 0) {
            goto_no_food_activity();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SingletonFoodList.getInstance().removeHandler(listChangedHandler);
        listChangedHandler = null;
        listFromSingleton = null;
        dismissListener = null;
        aa = null;
        super.onDestroy();
    }

}
