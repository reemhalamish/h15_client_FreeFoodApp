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
 */

public class AdapterForStrings extends ArrayAdapter {
    private Context context;
    private List<String> itemList;
    public AdapterForStrings(Context context, int textViewResourceId,List<String> itemList) {

        super(context, textViewResourceId);
        this.context=context;
        this.itemList=itemList;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.simple_adapter_for_text, parent, false);
        TextView txt = (TextView) row.findViewById(R.id.adapter_for_text_textView);
        txt.setText(itemList.get(position));
        return row;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.simple_adapter_for_picture, parent, false);
        TextView txt = (TextView) row.findViewById(R.id.adapter_for_text_textView);
        txt.setText(itemList.get(position));
        return row;
    }
}