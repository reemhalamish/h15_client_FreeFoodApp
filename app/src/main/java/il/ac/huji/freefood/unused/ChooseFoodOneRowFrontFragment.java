package il.ac.huji.freefood.unused;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import il.ac.huji.freefood.FoodListItem;
import il.ac.huji.freefood.R;

/**
 * Created by reem on 7/31/15.
 * <p/>
 * here to try swiping
 */
public class ChooseFoodOneRowFrontFragment extends Fragment {
    protected FoodListItem curFood;

    public ChooseFoodOneRowFrontFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        curFood = (FoodListItem) args.getSerializable("FoodListItem");
        View view = inflater.inflate(R.layout.choose_food_one_row_front, container, false);
        TextView txtTitle = (TextView) view.findViewById(R.id.choose_food_title);
        txtTitle.setText(curFood.getDescription());
        ImageView imgPicture = (ImageView) view.findViewById(R.id.choose_food_picture);
        return view;
    }
}
