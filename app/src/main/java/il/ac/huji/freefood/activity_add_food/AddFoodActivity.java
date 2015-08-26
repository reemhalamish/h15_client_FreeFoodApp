package il.ac.huji.freefood.activity_add_food;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import il.ac.huji.freefood.Application;
import il.ac.huji.freefood.LocationCaptureService;
import il.ac.huji.freefood.R;
import il.ac.huji.freefood.data.FoodListItem;
import il.ac.huji.freefood.data.SingletonFoodList;
import il.ac.huji.freefood.data.Thumbnail;

/**
 * Created by reem on 8/7/15.
 * <p/>
 * This activity is showing up when the user presses the button "I'm hungry!" in the main activity
 */
public class AddFoodActivity extends Activity {
    private static final int ONE_SECOND_IN_MS = 1000;
    private static final float ALPHA_FOR_THUMBNAILS = 0.5f;
    protected HorizontalScrollView scrollView;

    protected ImageButton pooh_thumbnail;
    protected ImageButton coffee_thumbnail;
    protected ImageButton pizza_thumbnail;
    protected ImageButton drinks_thumbnail;
    protected ImageButton beer_thumbnail;
    protected ImageButton cake_thumbnail;
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
    private AsyncTask locationTask;
    private FoodListItem foodTOWorkOn;

    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        findLocation();
        findViews();
        initThumbnails();
        setFocusOnPooh();
        createClickListenersForPublishAndBack();

    }

    private boolean findLocation() {
        try {
            foodTOWorkOn = new FoodListItem();

            Intent findLocationIntent = new Intent(AddFoodActivity.this, LocationCaptureService.class);
            findLocationIntent.putExtra("food", foodTOWorkOn);
            startService(findLocationIntent);
            return true;
        } catch (Exception error) {
            Log.e("location", error.getMessage());
            return false;
        }
    }

    private void findViews() {
        coffee_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_coffee);
        drinks_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_drinks);
        beer_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_beer);
        pooh_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_pooh);
        pizza_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_pizza);
        sandwitch_thumbnail= (ImageButton) findViewById(R.id.ib_add_thmb_sandwitch);
        cake_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_cake);
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
                locationTask.cancel(false);
                finish();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_foodTitle.getText().toString();
                String place = et_foodPlace.getText().toString();
//                FoodListItem newbie = new FoodListItem(image_id_chosen, title, place);
                foodTOWorkOn.setThumbnail(image_id_chosen);
                foodTOWorkOn.setDescription(title);
                foodTOWorkOn.setBuilding(place);
                foodTOWorkOn.setUI_Ready();
//                SingletonFoodList.getInstance().addToList(foodTOWorkOn); TODO add later
//                finish();
                displayProgressDialogToCatchLocation();
                // TODO add intent that succefull so the prev. activity would know that there is something!
                // TODO maybe in the background add something to run and catch yuor gps location
                // FACEBOOK here will be some dialog to ask the user if they want to publish to world \ only fb \ both (with notifications)
            }
        });

    }

    private void displayProgressDialogToCatchLocation() {
        progress = new ProgressDialog(this);
        progress.setMax(100);
        progress.setTitle("title");
        progress.setMessage("please wait...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d("progress", "dismissed");
            }
        });
        progress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO manipulation stuf.. computation...
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.incrementProgressBy(1);
                        }
                    });

                    try {
                        Thread.sleep(1000);                 //1000 milliseconds is one second.
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }


    private void setFocusOnPooh() {
        scrollView = (HorizontalScrollView) findViewById(R.id.hsv_add_thumbnails);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                scrollView.scrollTo(pooh_thumbnail.getBottom(), 0); // scrollTo(x, y);
                Log.d("scroll", "calling");
                scrollView.smoothScrollTo(pooh_thumbnail.getLeft() - 80, 0);
            }
        }, ONE_SECOND_IN_MS);
    }

    private void initThumbnails() {
        coffee_thumbnail.setTag(Thumbnail.COFFEE);
        drinks_thumbnail.setTag(Thumbnail.DRINKS);
        beer_thumbnail.setTag(Thumbnail.BEER);
        pooh_thumbnail.setTag(Thumbnail.POOH);
        pizza_thumbnail.setTag(Thumbnail.PIZZA);
        sandwitch_thumbnail.setTag(Thumbnail.SANDVITCH);
        cookies_thumbnail.setTag(Thumbnail.COOKIES);
        cake_thumbnail.setTag(Thumbnail.CAKE);
        fruits_thumbnail.setTag(Thumbnail.FRUITS);


        all_pictures = new ArrayList<>(Arrays.asList(pooh_thumbnail, coffee_thumbnail, drinks_thumbnail, beer_thumbnail, pizza_thumbnail, sandwitch_thumbnail, cake_thumbnail, cookies_thumbnail, fruits_thumbnail));
        View.OnClickListener choose_listener = createListenerForThumbnails();
        for (ImageButton thumbnail : all_pictures) {
            thumbnail.setOnClickListener(choose_listener);
            thumbnail.setAlpha(ALPHA_FOR_THUMBNAILS);
        }
        currentSelection = pooh_thumbnail;
        image_id_chosen = Thumbnail.POOH;
        pooh_thumbnail.setAlpha(1f);
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
                Log.d("add", "" + image_id_chosen);
                currentSelection = (ImageButton) view;
                currentSelection.setAlpha(1f);
            }
        };
    }

}


/*
TODO
maybe a whole different look:
publish only with one button
then pop-up appears to ask you if you want to:
 [button] publish to the world(default, <countdown>)
 [button] publish only to facebook friends
 [button] publish to the world with notifications for facebook friends


 with the last two will open a facebook confirmation activityForResult() if not in facebook already
 */