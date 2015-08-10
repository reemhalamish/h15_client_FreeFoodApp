package il.ac.huji.freefood.activities_one_class;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    protected ScrollView scrollView;

    protected ImageButton pooh_thumbnail;
    protected ImageButton coffee_thumbnail;
    protected ImageButton pizza_thumbnail;
    protected ImageButton drinks_thumbnail;

    protected List<ImageButton> all_pictures;

    protected ImageButton currentSelection;
    protected Thumbnail image_id_chosen;

    protected EditText et_foodTitle;
    protected EditText et_foodPlace;

    protected ImageButton btn_share;
    protected ImageButton btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ver2);
        findViews();
        initThumbnails();
        setFocusOnPooh();
        createClickListenersForPublishAndBack();

    }

    private void findViews() {
        pooh_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_pooh);
        coffee_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_coffee);
        pizza_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_pizza);
        drinks_thumbnail = (ImageButton) findViewById(R.id.ib_add_thmb_drinks);

        et_foodTitle = (EditText) findViewById(R.id.et_add_title);
        et_foodPlace = (EditText) findViewById(R.id.et_add_description);

        btn_back = (ImageButton) findViewById(R.id.ib_add_back);
        btn_share = (ImageButton) findViewById(R.id.ib_add_publish);
    }

    private void createClickListenersForPublishAndBack() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_foodTitle.getText().toString();
                String place = et_foodPlace.getText().toString();
                FoodListItem newbie = new FoodListItem(image_id_chosen, title, place);
                SingletonFoodList.getInstance().addToList(newbie);
                finish();

                // TODO add intent that succefull so the prev. activity would know that there is something!
                // TODO maybe in the background add something to run and catch yuor gps location
                // FACEBOOK here will be some dialog to ask the user if they want to publish to world \ only fb \ both (with notifications)
            }
        });

        // TODO set those for facebook as well

    }


    private void setFocusOnPooh() {
        scrollView = (ScrollView) findViewById(R.id.sv_add_scroll);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(pooh_thumbnail.getRight() + 50, 0); // scrollTo(x, y);
            }
        });
    }

    private void initThumbnails() {
        pooh_thumbnail.setTag(Thumbnail.POOH);
        coffee_thumbnail.setTag(Thumbnail.COFFEE);
        pizza_thumbnail.setTag(Thumbnail.PIZZA);
        drinks_thumbnail.setTag(Thumbnail.DRINKS);
        // TODO add them all!

        all_pictures = new ArrayList<>(Arrays.asList(pooh_thumbnail, coffee_thumbnail, pizza_thumbnail, drinks_thumbnail));
        View.OnClickListener choose_listener = createListenerForThumbnails();
        for (ImageButton thumbnail : all_pictures) {
            thumbnail.setOnClickListener(choose_listener);
        }
        currentSelection = pooh_thumbnail;
        image_id_chosen = Thumbnail.POOH;
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
                currentSelection.setAlpha(.15f);
                image_id_chosen = (Thumbnail) view.getTag();
                Log.d("add", "" + image_id_chosen);
                currentSelection = (ImageButton) view;
                currentSelection.setAlpha(1.0f);
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