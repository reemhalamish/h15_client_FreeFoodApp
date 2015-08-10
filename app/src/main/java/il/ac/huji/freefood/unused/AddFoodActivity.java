package il.ac.huji.freefood.unused;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.huji.freefood.R;
import il.ac.huji.freefood.data.FoodListItem;
import il.ac.huji.freefood.data.SingletonFoodList;

/**
 * Created by Reem on 30/04/2015.
 *
 * the activity to add one more foodListItem
 */
public class AddFoodActivity extends Activity {
    private static List<Integer> FOOD_PICTURES = Arrays.asList(R.drawable.default_picture, R.drawable.cookies, R.drawable.beer, R.drawable.cake, R.drawable.coffee, R.drawable.drinks, R.drawable.fruits, R.drawable.pizza, R.drawable.rugelach, R.drawable.sandwich);
    private static List<String> FOOD_BUILDINGS = Arrays.asList("Rothberg", "Kaplan", "Shprintzek", "Ross", "Canada");
    private Spinner pictureSpinner;
    private Spinner buildingSpinner;
    private EditText placeInBuilding;
    private EditText description;
    private EditText numOfPeople;

    @Override
    public void onCreate(Bundle unused) {
        super.onCreate(unused);
        setTitle(getResources().getString(R.string.addActivityTitle));
        setContentView(R.layout.activity_add_food);
        pictureSpinner = (Spinner) findViewById(R.id.activityAddFood_spn_pictures);
        buildingSpinner = (Spinner) findViewById(R.id.activityAddFood_spn_building);
        placeInBuilding = (EditText) findViewById(R.id.activityAddFood_edt_inside_building);
        description = (EditText) findViewById(R.id.activityAddFood_edt_description);
        numOfPeople = (EditText) findViewById(R.id.activityAddFood_num_people);
        Button btnAdd = (Button) findViewById(R.id.activityAddFood_send_button);
        Button btnCancel = (Button) findViewById(R.id.activityAddFood_cancel_button);

        List<HashMap<String, Integer>> list = new ArrayList<>();
        HashMap<String, Integer> map;
        for (int Rdrowable : FOOD_PICTURES) {
            map = new HashMap<String, Integer>();
            map.put("Icon", Rdrowable);
            list.add(map);
        }
        final Context context = this;
        PictureAdapter adapterPictures = new PictureAdapter(context, list,
                R.layout.simple_adapter_for_picture, new String[]{"Name", "Icon"},
                new int[]{R.id.adapter_for_image_name, R.id.adapter_for_image_imageView});

        pictureSpinner.setAdapter(adapterPictures);

        List<HashMap<String, String>> listText = new ArrayList<>();
        HashMap<String, String> mapText;
        for (String classroom : FOOD_BUILDINGS) {
            mapText = new HashMap<>();
            mapText.put("Name", classroom);
            listText.add(mapText);
        }
        StringAdapter adapterBuilding = new StringAdapter(context, listText,
                R.layout.simple_adapter_for_text, new String[]{"Name"},
                new int[]{R.id.adapter_for_text_textView});
        buildingSpinner.setAdapter(adapterBuilding);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int RETURN_WITHOUT_SUCCESS = 0;
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodListItem foodListItem = new FoodListItem(
                        Integer.parseInt(numOfPeople.getText().toString()),
                        ((HashMap<String, String>)buildingSpinner.getSelectedItem()).get("Name"),
                        placeInBuilding.getText().toString(),
                        ((HashMap<String, Integer>)pictureSpinner.getSelectedItem()).get("Icon"),
                        description.getText().toString());
                SingletonFoodList.getInstance().addToList(foodListItem);
                int SUCCESS = 1;

            }
        });
    }

    public class PictureAdapter extends SimpleAdapter {

        public PictureAdapter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.simple_adapter_for_picture,
                        null);
            }

            HashMap<String, Integer> data = (HashMap<String, Integer>) getItem(position);
            ((ImageView) convertView.findViewById(R.id.adapter_for_image_imageView))
                    .setImageResource(data.get("Icon"));

            return convertView;
        }

    }
    public class StringAdapter extends SimpleAdapter {

        public StringAdapter(Context context, List<? extends Map<String, ?>> data,
                               int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.simple_adapter_for_text,
                        null);
            }

            HashMap<String, String> data = (HashMap<String, String>) getItem(position);
            TextView tv = (TextView) convertView.findViewById(R.id.adapter_for_text_textView);
            tv.setText(data.get("Name"));
           // tv.setTextSize(25);

            return convertView;
        }

    }
}