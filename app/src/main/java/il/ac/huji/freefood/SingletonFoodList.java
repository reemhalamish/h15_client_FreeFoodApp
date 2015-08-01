package il.ac.huji.freefood;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ayala on 30/04/2015.
 * Here to make calling the foodlist easier
 */
public class SingletonFoodList {

    private static SingletonFoodList instance;

    protected SingletonFoodList(){
        Log.d("a","a");
    }

    public static synchronized SingletonFoodList getInstance(){
        if (instance == null){
            instance = new SingletonFoodList();
        }
            return instance;
    }


    public void getList(FindCallback<FoodListItem> callback){
        ParseQuery<FoodListItem> query = ParseQuery.getQuery(FoodListItem.class);
        List<FoodListItem> foodList = new LinkedList<>();
        query.findInBackground(callback);
    }

    public void addToList(FoodListItem item) {
        item.saveInBackground();
    }

    public void deleteFromList(String objectID) {
        ParseQuery<FoodListItem> query;
        query = new ParseQuery(FoodListItem.class);
        query.getInBackground(objectID, new GetCallback<FoodListItem>() {
            public void done(FoodListItem foodListItem, ParseException e) {
                if (e == null) {
                    foodListItem.deleteInBackground();
                } else {
                    Logger.getLogger("FreeFoodParse").warning("Didn't find foodItem to delete"+e.getMessage());
                }
            }
        });
    }
}
