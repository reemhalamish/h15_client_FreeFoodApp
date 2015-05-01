package il.ac.huji.freefood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

/**
 * Created by Reem on 30/04/2015.
 */
public class AddFoodActivity extends Activity {
    private static List<Integer> FOOD_PICTURES = Arrays.asList(R.drawable.default_picture, R.drawable.cookies, R.drawable.beer, R.drawable.cake, R.drawable.coffee, R.drawable.drinks, R.drawable.fruits, R.drawable.pizza, R.drawable.rugelach, R.drawable.sandwich);
    private static List<String> FOOD_BUILDINGS = Arrays.asList("Rothberg", "Kaplan", "Shprintzek", "Ross", "Canada");
    private Spinner pictureSpinner;
    private Spinner buildingSpinner;
    private EditText placeInBuilding;
    private EditText description;
    private EditText numOfPeople;

    //TODO: onCreate(), spinner for buildings, make it pretty!!

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

        List<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
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

        List<HashMap<String, String>> listText = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapText;
        for (String classroom : FOOD_BUILDINGS) {
            mapText = new HashMap<String, String>();
            mapText.put("Name", classroom);
            listText.add(mapText);
        }
        StringAdapter adapterBuilding = new StringAdapter(context, listText,
                R.layout.simple_adapter_for_text, new String[]{"Name"},
                new int[]{R.id.adapter_for_text_textView});
        buildingSpinner.setAdapter(adapterBuilding);
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
                startActivity(new Intent(context, MainActivity.class));
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
                    .setImageResource((int) data.get("Icon"));

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
            tv.setText((String) data.get("Name"));
           // tv.setTextSize(25);

            return convertView;
        }

    }
}