package il.ac.huji.freefood;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

/**
 * Created by Reem on 30/04/2015.
 */
public class AddFoodActivity extends Activity {
    private static List<Integer> FOOD_PICTURES = Arrays.asList(R.drawable.cookies, R.drawable.beer, R.drawable.cake, R.drawable.coffee, R.drawable.drinks, R.drawable.fruits, R.drawable.pizza, R.drawable.rugelach, R.drawable.sandwich);
    private static List<String> FOOD_BUILDINGS = Arrays.asList("Rothberg", "Kaplan", "Shprintzek", "Ross", "Canada");
    //TODO: onCreate(), spinner for buildings, make it pretty!!

    @Override
    public void onCreate(Bundle unused) {
        super.onCreate(unused);
        setTitle(getResources().getString(R.string.addActivityTitle));
        setContentView(R.layout.activity_add_food);
        Spinner pictureSpinner = (Spinner) findViewById(R.id.activityAddFood_spn_pictures);
        Spinner buildingSpinner = (Spinner) findViewById(R.id.activityAddFood_spn_building);

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

    }

/*

        final EditText edtNewItem =(EditText) findViewById(R.id.et_add_dialog_text);
        final DatePicker datePicker =   (DatePicker) findViewById(R.id.dp_add_dialog_date);
        Button btnOK =         (Button) findViewById(R.id.btn_add_dialog_add);
        Button btnCancel =      (Button) findViewById(R.id.btn_add_dialog_cancel);
        final CheckBox cbxNoDue =        (CheckBox) findViewById(R.id.cbx_add_dialog_no_due);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtNewItem.getText().toString();
                int d_days = datePicker.getDayOfMonth();
                int d_months = datePicker.getMonth();
                int d_years = datePicker.getYear();
                GregorianCalendar dueDate = new GregorianCalendar(d_years, d_months, d_days);
                Date retVal = dueDate.getTime();
                if (cbxNoDue.isChecked()) {
                    retVal = null;
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("title", text);
                returnIntent.putExtra("dueDate", retVal);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
*/


/*

spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // your code here
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
    }

});

TODO:
need to return with success or failure (to add a cancel button)
need to create intent from the main
need to create the main!


 */

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