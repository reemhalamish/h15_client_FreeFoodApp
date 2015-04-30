package il.ac.huji.freefood;

import android.app.AlertDialog;
import android.os.SystemClock;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ayala on 30/04/2015.
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
        return null;
    }


    public List<FoodListItem> getList ()
    {
        return mainList;
    }

    public static void addToList(FoodListItem item) {
        //ParseObject foodItem = new ParseObject(item.getId());
        //foodItem.put("")
    }
    public static void deleteFromList(int id) {

    }
    public static void updateFromList(FoodListItem item) {
        item.saveInBackground();
    }

    public static void deleteFromList(String objectID) {
        ParseQuery<FoodListItem> query = new ParseQuery(FoodListItem.class);
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
