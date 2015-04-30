package il.ac.huji.freefood;

import android.os.SystemClock;

import com.parse.ParseObject;

import java.util.ArrayList;
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


    public List<FoodListItem> getList ()
    {
        return mainList;
    }

    public static void addToList(FoodListItem item) {
        ParseObject foodItem = new ParseObject(item.getId());
        foodItem.put("")
    }
    public static void deleteFromList(int id) {

    }
    public static void updateFromList(FoodListItem item) {

    }
}
