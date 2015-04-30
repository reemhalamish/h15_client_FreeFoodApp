package il.ac.huji.freefood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Reem on 30/04/2015.
 */
public class AdapterForPictures extends ArrayAdapter {
    private Context context;
    private List<String> itemList;
    public AdapterForPictures(Context context, int textViewResourceId,List<String> itemList) {

        super(context, textViewResourceId);
        this.context=context;
        this.itemList=itemList;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.simple_adapter_for_picture, parent, false);
        ImageView picture = (ImageView) row.findViewById(R.id.adapter_for_image_imageView);
        picture.setImageDrawable(Drawable.createFromPath(itemList.get(position)));
        return row;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.simple_adapter_for_picture, parent, false);
        ImageView picture = (ImageView) row.findViewById(R.id.adapter_for_image_imageView);
        picture.setImageDrawable(Drawable.createFromPath(itemList.get(position)));
        return row;
    }
}
