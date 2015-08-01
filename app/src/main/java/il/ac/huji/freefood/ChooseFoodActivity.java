package il.ac.huji.freefood;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Elyasaf on 4/30/2015.
 *
 * the activity that chooses food
 */
public class ChooseFoodActivity extends Activity {

    public View.OnTouchListener dismissListener;
    protected List<FoodListItem> foodList;
    protected ChooseFoodActivityAdapter aa;

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
                if (foodListItems == null) {
                    ImageView img = (ImageView) findViewById(R.id.iv_loader);
                    img.setBackgroundResource(R.drawable.hungry);
                } else {
                    onLoadList(foodListItems);
                }
            }
        });

    }

    protected void onLoadList(List<FoodListItem> foodListItems) {
        final Context context = this;
        this.foodList = foodListItems;

        setContentView(R.layout.activity_show_food);
        ListView lv_foodList = (ListView) findViewById(R.id.lv_FoodShowList);

        // set the listener

        dismissListener = createDismissListener();

        aa = new ChooseFoodActivityAdapter(context, R.layout.choose_food_one_row, R.id.choose_food_picture, foodListItems, dismissListener);
        lv_foodList.setAdapter(aa);
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
        this.foodList.remove(index);
        Log.d("delete", "len:" + foodList.size());
        aa.notifyDataSetChanged();
        Log.d("delete", "finished");
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
                            @Override
                            public void onAnimationStart(Animation arg0) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation arg0) {
                            }

                            @Override
                            public void onAnimationEnd(Animation arg0) {
                                Log.d("animation", "calling delete");
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
//                    deleteFoodListItem(index_of_me);
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
}
