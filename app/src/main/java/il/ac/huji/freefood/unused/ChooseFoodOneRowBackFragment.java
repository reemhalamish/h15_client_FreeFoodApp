package il.ac.huji.freefood.unused;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import il.ac.huji.freefood.R;

/**
 * Created by reem on 7/31/15.
 * here to try swiping
 */
public class ChooseFoodOneRowBackFragment extends Fragment {
    public ChooseFoodOneRowBackFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_food_one_row_back, container, false);
        TextView dismissText = (TextView) view.findViewById(R.id.tv_choose_food_one_row_back_dismiss);
        dismissText.setTextSize(72);
        dismissText.setTextColor(Color.WHITE);
        dismissText.setBackgroundResource(R.drawable.food_list_item_back);
        return view;
    }
}
