package il.ac.huji.freefood;

import android.app.Activity;

/**
 * Created by Reem on 30/04/2015.
 */
public class AddFoodActivity extends Activity {
    private static String[] FOOD_PICTURES = {"<choose picture>", "beer", "cake", "coffee", "cookies", "drinks", "fruits", "pizza", "rugelach", "sandwich"};
    private static String[] FOOD_BUILDINGS = {"<choose building>", "Kaplan", "Shprintzek", "Ross", "Canada"};
    //TODO: onCreate(), spinner for buildings, make it pretty!!
}
/*
    @Override
    public void onCreate(Bundle unused){
        super.onCreate(unused);
        setTitle(getResources().getString(R.string.addDialogTitle));
        setContentView(R.layout.dialog_adding_item);
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