package il.ac.huji.freefood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Reem on 30/04/2015.
 * adapter for any row in the foodListItems
 */
public class ChooseFoodActivityAdapter extends ArrayAdapter<FoodListItem> {
    protected int _resurce;
    protected List<FoodListItem> _objects;
    protected int _itemId;
    protected Context _context;
    protected View.OnTouchListener _dismissListener;

    public ChooseFoodActivityAdapter(
                        Context context,
                        int resource,
                        int itemId,
                        List<FoodListItem> objects,
                        View.OnTouchListener listener) {
        super(context, resource, itemId, objects);
        this._context = context;
        this._resurce = resource;
        this._itemId = itemId;
        this._objects = objects;
        this._dismissListener = listener;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.choose_food_one_row_front, parent, false);
        }

        if (_dismissListener != null) {
            view.setOnTouchListener(_dismissListener);

        }

        FoodListItem curItem = this._objects.get(position);
        TextView txtTitle = (TextView) view.findViewById(R.id.choose_food_title);
        txtTitle.setText(curItem.getDescription());
        view.setTag(position);

        return view;
    }

    }
