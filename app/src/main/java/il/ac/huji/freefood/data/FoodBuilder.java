package il.ac.huji.freefood.data;

import android.util.Log;

import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import il.ac.huji.freefood.FreeFoodApplication;

/**
 * Created by Re'em on 9/2/2015.
 *
 * The meaning of this class is to create order and help talking to the UI addFoodActivity on one side,
 * and the location getter service in the other side
 */
public class FoodBuilder {
    public static FoodBuilder instance = null;
    private HashMap<Long, Food> foods;

    private FoodBuilder() {
        foods = new HashMap<>();
    }

    public synchronized static FoodBuilder getInstance() {
        if (instance == null) {
            instance = new FoodBuilder();
        }
        return instance;
    }

    public void startFood(Long id) {
        Food newbie = new Food();
        foods.put(id, newbie);
    }

    public void cancelFood(Long id) {
        foods.remove(id);
    }

//
//    @Deprecated
//    public boolean isCanceled(Long id) {
//        return (!foods.containsKey(id));
//    }

    public void serviceFoundLocation(ParseGeoPoint location, int accuracy) {
        Log.d("foodbuilder", "got new location. currently waitlist of foods: " + foods.size());
        for (Iterator<Map.Entry<Long, Food>> it = foods.entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<Long, Food> entry = ;
            Food toWork = it.next().getValue();
            toWork.setLocation(location, accuracy);
            if (toWork.isReadyToPublish()) {
                SingletonFoodList.getInstance().addToList(toWork);
                it.remove();
            }

        }
    }
//
//    @Deprecated
//    public void setFoodLocation(Long id, ParseGeoPoint location, int accuracy) {
//        Food toWork = foods.get(id);
//        toWork.setLocation(location, accuracy);
//        checkIfReady(id);
//    }

    public void setFoodUI(Long id, Thumbnail picture, String title, String details) {
        Food toWork = foods.get(id);
        toWork.setUI(picture, title, details);
        checkIfReady(id);
    }

    private void checkIfReady(Long id) {
        Food toPublish = foods.get(id);
        if (toPublish.isReadyToPublish()) {
            SingletonFoodList.getInstance().addToList(toPublish);
            foods.remove(id);
        }
    }



    public boolean dontNeedLocationAnymore() {
        return foods.size() == 0;
    }
}
