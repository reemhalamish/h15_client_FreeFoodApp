package il.ac.huji.freefood.activity_choose_food;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.List;

import il.ac.huji.freefood.FunnyStringsToRefreshButton;
import il.ac.huji.freefood.R;
import il.ac.huji.freefood.activities_one_class.FoodDetailsActivity;
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
    public static final String INTENT_FROM_CHOOSEFOOD_TAG = "index_of_fooditem_in_singleton";


    protected View.OnTouchListener dismissListener;
    protected View.OnClickListener openDetailsListener;
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
        setContentView(R.layout.activity_choose_food);
        Log.d("no_food", "choose food called");
        lv_foodList = (ListView) findViewById(R.id.lv_FoodShowList);
        final Context context = this;


        // set the touch-listener for every item in the list
        dismissListener = createDismissListener();

        // set the click-listener
        openDetailsListener = createOpeningListener();

        // set the adapter
        listFromSingleton = SingletonFoodList.getInstance().getClientFoodListItems();
        aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, listFromSingleton, dismissListener, openDetailsListener);
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


        //set the buttons
        setButtonsListeners();
    }

    private void setButtonsListeners() {
        // set the refresh button
        refreshButton = (ImageButton) findViewById(R.id.ib_showfood_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            private int pressedTimes = 0;
            @Override
            public void onClick(View view) {
                String funny = FunnyStringsToRefreshButton.getWord(pressedTimes);
                if (funny != null) { // i.e. not pressed so much times that there are no more funny words
                    Toast.makeText(ChooseFoodActivity.this, funny, Toast.LENGTH_SHORT).show();
                    pressedTimes += 1;
                }
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
                //start a nice visualization
                Animation tiltAnimation = AnimationUtils.loadAnimation(
                        ChooseFoodActivity.this,
                        R.anim.tilt_by_rotating_couple_times);
                tiltAnimation.setRepeatCount(1);
                clearAllButton.startAnimation(tiltAnimation);

                // after animation, open the dialog
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        promptTheUserForDeleting(ChooseFoodActivity.this);
                    }
                }, tiltAnimation.computeDurationHint());
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
            public static final int CRITICAL_MOVE_FOR_CLICK = 3;
            private boolean shouldClick = false;
            private final int CRITICAL_PADDING_FOR_OPEN_DETAILS_ACTIVITY = 10;
            private final float screen_width = getScreenWidth();
            private final float CRITICAL_PADDING = screen_width / 4.0f;

            private int paddingx = 0;
            private int initialx = 0;
            private int currentx = 0;
            private int paddingy = 0;
            private int initialy = 0;
            private int currenty = 0;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int index_of_me = (int) v.getTag();

                final ImageView iv_icon = (ImageView) v.findViewById(R.id.choose_food_picture);
                final TextView tv_title = (TextView) v.findViewById(R.id.choose_food_title);
                final TextView tv_dismissText = (TextView) v.findViewById(R.id.tv_choose_food_dismiss);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    paddingx = 0;
                    initialx = (int) event.getX();
                    currentx = (int) event.getX();
                    paddingy = 0;
                    initialy = (int) event.getY();
                    currenty = (int) event.getY();
                    //viewHolder = ((ViewHolder) v.getTag());
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    currentx = (int) event.getX();
                    paddingx = currentx - initialx;
                    currenty = (int) event.getY();
                    paddingy = currenty - initialy;

                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (Math.abs(paddingx) + Math.abs(paddingy) < CRITICAL_MOVE_FOR_CLICK && event.getAction() == MotionEvent.ACTION_UP) { // didn't move at all
                        Log.d("click", "shouldclick summoned");
                        v.performClick();
                    } else if (Math.abs(paddingx) >= CRITICAL_PADDING) {
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

//                        if (Math.abs(padding) < CRITICAL_PADDING_FOR_OPEN_DETAILS_ACTIVITY) {
//                            Intent foodDetails = new Intent(ChooseFoodActivity.this, FoodDetailsActivity.class);
//                            foodDetails.putExtra(INTENT_FROM_CHOOSEFOOD_TAG, index_of_me);
//                            startActivity(foodDetails);
//                        }
                    }
                    paddingx = 0;
                    initialx = 0;
                    currentx = 0;
                    paddingy = 0;
                    initialy = 0;
                    currenty = 0;
                }


                if (paddingx != 0) {
                    tv_dismissText.setAlpha(Math.min(Math.abs(4.0f * paddingx) / screen_width, 1.0f));
                    if (Math.abs(paddingx) >= CRITICAL_PADDING) {
                        tv_dismissText.setTextColor(Color.RED);
                    } else {
                        tv_dismissText.setTextColor(Color.WHITE);
                    }

                }
                return true;
            }
        };
    }

    protected View.OnClickListener createOpeningListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent foodDetails = new Intent(ChooseFoodActivity.this, FoodDetailsActivity.class);
                foodDetails.putExtra(INTENT_FROM_CHOOSEFOOD_TAG, (int) view.getTag());
                startActivity(foodDetails);
            }
        };
    }
    private void promptTheUserForDeleting(Context context) {
        AlertDialog.OnClickListener confirmListener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SingletonFoodList.getInstance().clearAndUnpinAllItems();
                        // it will automatically get inside this list via the handler
                        break;
                }
                dialogInterface.dismiss();
            }
        };
        AlertDialog.OnCancelListener cancel_listener = new AlertDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        };
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("Sure to Dismiss All?");
        dlg.setMessage("You were about to dismiss ALL THE FOOD IN THE LIST. Are you sure?");
        dlg.setPositiveButton("yes", confirmListener);
        dlg.setNegativeButton("NO", confirmListener);
        dlg.setCancelable(true);
        dlg.setOnCancelListener(cancel_listener);
        dlg.create();
        dlg.show();
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
