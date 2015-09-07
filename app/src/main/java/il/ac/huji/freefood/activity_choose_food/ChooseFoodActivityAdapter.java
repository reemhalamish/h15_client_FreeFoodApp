package il.ac.huji.freefood.activity_choose_food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.data.Food;

/**
 * Created by Reem on 30/04/2015.
 * adapter for any row in the foodListItems
 */
public class ChooseFoodActivityAdapter extends ArrayAdapter<Food> {
    protected int _resurce;
    protected List<Food> _objects;
    protected int _itemId;
    protected Context _context;
    protected View.OnTouchListener _dismissListener;
    protected Format formatter;

    public ChooseFoodActivityAdapter(
                        Context context,
                        int resource,
                        int itemId,
                        List<Food> objects,
                        View.OnTouchListener listener) {
        super(context, resource, itemId, objects);
        this._context = context;
        this._resurce = resource;
        this._itemId = itemId;
        this._objects = objects;
        this._dismissListener = listener;

//        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter = new SimpleDateFormat("EEEE HH:mm", Locale.getDefault());

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) { // create the view from scratch (else - view exists, just update)
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.choose_food_one_row, parent, false);
        }

        if (_dismissListener != null) {
            view.setOnTouchListener(_dismissListener);

        }

        Food curItem = this._objects.get(position);
        TextView txtTitle = (TextView) view.findViewById(R.id.choose_food_title);
        TextView txtOther = (TextView) view.findViewById(R.id.choose_food_other);
        ImageView thumbnail = (ImageView) view.findViewById(R.id.choose_food_picture);

        txtTitle.setText(curItem.getTitle());
        Date createdAt = curItem.getCreatedAt();
        if (createdAt != null)
            txtOther.setText(formatter.format(createdAt));
        else {
            txtOther.setText("long ago");
        }
        thumbnail.setImageResource(curItem.getThumbnail().toDrawableID());

        view.setTag(position);

        return view;
    }

    public void updatedList() {
        notifyDataSetChanged();
    }

}
