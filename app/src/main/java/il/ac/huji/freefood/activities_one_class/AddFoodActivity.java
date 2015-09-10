package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import il.ac.huji.freefood.LocationCaptureService;
import il.ac.huji.freefood.R;
import il.ac.huji.freefood.data.FoodBuilder;
import il.ac.huji.freefood.data.LocationSuperviser;
import il.ac.huji.freefood.data.Thumbnail;

/**
 * Created by reem on 8/7/15.
 * <p/>
 * This activity is showing up when the user presses the button "I'm hungry!" in the main activity
 */
public class AddFoodActivity extends Activity {
    private static final int DELAY_FOR_SCROLLING_IN_MS = 1000;
    private static final float ALPHA_FOR_THUMBNAILS = 0.5f;
    protected HorizontalScrollView scrollView;

    protected ImageButton default_goat_thumbnail;
    protected ImageButton coffee_thumbnail;
    protected ImageButton pizza_thumbnail;
    protected ImageButton drinks_thumbnail;
    protected ImageButton beer_thumbnail;
    protected ImageButton rice_thumbnail;
    protected ImageButton cookies_thumbnail;
    protected ImageButton fruits_thumbnail;
    protected ImageButton sandwitch_thumbnail;

    protected List<ImageButton> all_pictures;

    protected ImageButton currentSelection;
    protected Thumbnail image_id_chosen;

    protected EditText et_foodTitle;
    protected EditText et_foodPlace;

    protected ImageButton btn_share;
    protected ImageButton btn_back;
    ProgressDialog progress;
    private AsyncTask locationTask;
    private Long food_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            food_id = savedInstanceState.getLong(LocationCaptureService.FOOD_ID_TAG);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        findLocation();
        findViews();
        initThumbnails();
        setFocusOnDefaultThumbnail();
        createClickListenersForPublishAndBack();

    }

    private boolean findLocation() {
//        ONE_DAY:
//              int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//              if (ConnectionResult.SUCCESS == resultCode) {
//        then call some other service to catch the location

        try {
            if (food_id == null) {
                food_id = Long.valueOf(new Date().getTime());
                FoodBuilder.getInstance().startFood(food_id);
            }

            LocationSuperviser.startLocationServiceIfNeeded(this);
            return true;
        } catch (Exception error) {
//            Log.e("location", error.getMessage());
            return false;
        }
    }

    private void findViews() {
        coffee_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_coffee);
        drinks_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_drinks);
        beer_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_beer);
        default_goat_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_default);
        pizza_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_pizza);
        sandwitch_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_sandwitch);
        rice_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_rice);
        cookies_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_cookies);
        fruits_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_fruits);


        et_foodTitle = (EditText) findViewById(R.id.et_add_title);
        et_foodPlace = (EditText) findViewById(R.id.et_add_description);

        btn_back = (ImageButton) findViewById(R.id.ib_add_back);
        btn_share = (ImageButton) findViewById(R.id.ib_add_publish);
    }

    private void createClickListenersForPublishAndBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodBuilder.getInstance().cancelFood(food_id);
                food_id = null;
                finish();
            }
        });

        // ONE_DAY add some visuality?
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_foodTitle.getText().toString();
                String details = et_foodPlace.getText().toString();
                FoodBuilder.getInstance().setFoodUI(food_id, image_id_chosen, title, details);
                food_id = null;
                if (ParseUser.getCurrentUser().getBoolean("prompt_on_share")) {
                    promptTheUser(AddFoodActivity.this);
                } else {
                    Toast.makeText(AddFoodActivity.this, "working on it...", Toast.LENGTH_SHORT).show();
                    finish();
                }
                // FACEBOOK here will be some dialog to ask the user if they want to publish to world \ only fb \ both (with notifications)
            }
        });

    }
//
//    private void displayProgressDialogToCatchLocation() {
//        progress = new ProgressDialog(this);
//        progress.setMax(100);
//        progress.setTitle("title");
//        progress.setMessage("please wait...");
//        progress.setCancelable(false);
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
////                Log.d("progress", "dismissed");
//            }
//        });
//        progress.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // manipulation stuf.. computation...
//                for (int i = 0; i < 100; i++) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progress.incrementProgressBy(1);
//                        }
//                    });
//
//                    try {
//                        Thread.sleep(1000);                 //1000 milliseconds is one second.
//                    } catch (InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
//            }
//        }).start();
//    }


    private void setFocusOnDefaultThumbnail() {
        scrollView = (HorizontalScrollView) findViewById(R.id.hsv_add_thumbnails);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                scrollView.scrollTo(default_goat_thumbnail.getBottom(), 0); // scrollTo(x, y);
//                Log.d("scroll", "calling");
                scrollView.smoothScrollTo(default_goat_thumbnail.getLeft() - 80, 0);
            }
        }, DELAY_FOR_SCROLLING_IN_MS);
    }

    private void initThumbnails() {
        coffee_thumbnail.setTag(Thumbnail.COFFEE);
        drinks_thumbnail.setTag(Thumbnail.DRINKS);
        beer_thumbnail.setTag(Thumbnail.BEER);
        default_goat_thumbnail.setTag(Thumbnail.DEFAULT);
        pizza_thumbnail.setTag(Thumbnail.PIZZA);
        sandwitch_thumbnail.setTag(Thumbnail.SANDVITCH);
        rice_thumbnail.setTag(Thumbnail.RICE);
        cookies_thumbnail.setTag(Thumbnail.COOKIES);
        fruits_thumbnail.setTag(Thumbnail.FRUITS);


        all_pictures = new ArrayList<>(Arrays.asList(default_goat_thumbnail, coffee_thumbnail, drinks_thumbnail, beer_thumbnail, pizza_thumbnail, sandwitch_thumbnail, rice_thumbnail, cookies_thumbnail, fruits_thumbnail));
        View.OnClickListener choose_listener = createListenerForThumbnails();
        for (ImageButton thumbnail : all_pictures) {
            thumbnail.setOnClickListener(choose_listener);
            thumbnail.setAlpha(ALPHA_FOR_THUMBNAILS);
        }
        currentSelection = default_goat_thumbnail;
        image_id_chosen = Thumbnail.DEFAULT;
        default_goat_thumbnail.setAlpha(1f);
    }

    View.OnClickListener createListenerForThumbnails() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    steps:
                        get the alpha back on the prev. selection
                        highlight this selection
                        set the image Thumbnail accordingly
                 */
                currentSelection.setAlpha(ALPHA_FOR_THUMBNAILS);
                image_id_chosen = (Thumbnail) view.getTag();
//                Log.d("add", "" + image_id_chosen);
                currentSelection = (ImageButton) view;
                currentSelection.setAlpha(1f);
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(LocationCaptureService.FOOD_ID_TAG, food_id);
        super.onSaveInstanceState(outState);
    }
    private void promptTheUser(Context context) {
        AlertDialog.OnClickListener show_once_listener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("prompt_on_share", false);
                        user.saveEventually();
                        dialogInterface.dismiss();
                        finish();
                        break;
                }
            }
        };
        AlertDialog.OnCancelListener cancel_listener = new AlertDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        };


        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("share new food");
        dlg.setMessage("The food you just shared need more proccessing.\nWe will capture your location, and then we will upload the food to the cloud.\nMeanwhile you can travel in the app :)");
        dlg.setPositiveButton("Got it! never show again", show_once_listener);
        dlg.setCancelable(true);
        dlg.setOnCancelListener(cancel_listener);
        dlg.create();
        dlg.show();
    }


}


/*
ONE_DAY
publish only with one button
then pop-up appears to ask you if you want to:
 [button] publish to the world(default, <countdown>)
 [button] publish only to facebook friends
 [button] publish to the world with notifications for facebook friends
 [button] publish only to app-followers <--- and add the ability to follow other people with their consent ("reem wants to follow you! do you agree?")


 with 2sc and 3rd options will open a facebook confirmation activityForResult() if not in facebook already
 */