package il.ac.huji.freefood.unused;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import il.ac.huji.freefood.FoodListItem;
import il.ac.huji.freefood.R;
import il.ac.huji.freefood.SingletonFoodList;

//import android.support.v4.app.Fragment;

/**
 * Created by reem on 7/31/15.
 * <p/>
 * here to try swiping
 */
public class SwipeFragmentsActivity extends FragmentActivity {
    ChooseFoodOneRowSwipeAdapter mCustomPagerAdapter;
    ViewPager mViewPager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        setContentView(R.layout.activity_loading);

        ImageView img = (ImageView) findViewById(R.id.iv_loader);
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
                    onCreate(foodListItems);
                }
            }
        });

    }

    protected void onCreate(List<FoodListItem> food) {
        setContentView(R.layout.choose_food_one_row_swipeble);

        // == Setting up the ViewPager ==
        FragmentManager mngr = getSupportFragmentManager();

        mCustomPagerAdapter = new ChooseFoodOneRowSwipeAdapter(mngr, this, food.get(0));

        mViewPager = (ViewPager) findViewById(R.id.vp_choose_food_swipeable);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(1); // middle item - the front
    }
}

