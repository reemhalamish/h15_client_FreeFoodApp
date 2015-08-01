package il.ac.huji.freefood.unused;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import il.ac.huji.freefood.FoodListItem;

/**
 * Created by reem on 7/31/15.
 * here to try swiping
 */
public class ChooseFoodOneRowSwipeAdapter extends FragmentPagerAdapter {
    protected static int NUM_ITEMS = 3; // 1 in back left, 1 in front, another in back right
    protected Context context;
    protected FoodListItem curFood;

    public ChooseFoodOneRowSwipeAdapter(FragmentManager fm, Context context, FoodListItem curFood) {
        super(fm);
        this.context = context;
        this.curFood = curFood;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        if (position % 2 == 1) { // the front
            Bundle args = new Bundle();

            args.putSerializable("FoodListItem", curFood);

            ChooseFoodOneRowFrontFragment ret_val = new ChooseFoodOneRowFrontFragment();

            // Set the arguments on the fragment
            // that will be fetched in DemoFragment@onCreateView
            ret_val.setArguments(args);

            return ret_val;
        }
        // else - get the back fragment
        return new ChooseFoodOneRowBackFragment();
    }
}
