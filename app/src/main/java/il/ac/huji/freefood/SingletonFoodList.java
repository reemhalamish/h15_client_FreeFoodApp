package il.ac.huji.freefood;

import android.os.SystemClock;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ayala on 30/04/2015.
 */
public class SingletonFoodList {

    private  List<FoodListItem> mainList = new ArrayList<FoodListItem>();

    private static SingletonFoodList instance;

    private SingletonFoodList(){
            instance = new SingletonFoodList();
    }

    public static SingletonFoodList getInstance(){
        if (instance == null){
            instance = new SingletonFoodList();
        }
            return instance;
    }


    public void getList(FindCallback<FoodListItem> callback){
        ParseQuery<FoodListItem> query = ParseQuery.getQuery(FoodListItem.class);
        List<FoodListItem> foodList = new LinkedList<FoodListItem>();
        query.findInBackground(callback);
    }

    public static void addToList(FoodListItem item) {
        ParseObject foodItem = item;


    public static void deleteFromList(int id) {

    }
    public static void updateFromList(FoodListItem item) {
        ParseQuery<FoodListItem> query = ParseQuery.getQuery(FoodListItem.class);
        query.getInBackground(item.getId(), new GetCallback<FoodListItem>(){
            @Override
            public void done(List<FoodListItem> foodListItems, ParseException e) {

                }
            });
        }
    }
}
