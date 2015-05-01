package il.ac.huji.freefood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Reem on 30/04/2015.
 */
public class ChooseFoodActivityAdapter extends ArrayAdapter<FoodListItem> {
    protected int _resurce;
    protected List<FoodListItem> _objects;
    protected int _itemId;
    protected Context _context;


    public ChooseFoodActivityAdapter(
                        Context context,
                        int resource,
                        int itemId,
                        List<FoodListItem> objects) {
        super(context, resource, itemId, objects);
        this._context = context;
        this._resurce = resource;
        this._itemId = itemId;
        this._objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.choose_food_one_row, parent, false);
        }

        FoodListItem curItem = this._objects.get(position);
        TextView txtTitle = (TextView) view.findViewById(R.id.choose_food_title);
        txtTitle.setText(curItem.getDescription());
        String info = curItem.getBuilding()+" building, floor: "+curItem.getInsideBuilding() + ". \nSuitable for "+Integer.toString(curItem.getNumPeople()) + " people.";
        TextView txtSubtitle = (TextView) view.findViewById(R.id.choose_food_subtitle);
        txtSubtitle.setText(info);
        return view;
    }

}
