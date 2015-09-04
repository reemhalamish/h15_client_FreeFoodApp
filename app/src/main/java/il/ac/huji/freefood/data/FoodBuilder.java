package il.ac.huji.freefood.data;

import com.parse.ParseGeoPoint;

import java.util.HashMap;

/**
 * Created by Re'em on 9/2/2015.
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

    public void cancleFood(Long id) {
        foods.remove(id);
    }

    public boolean isCanceled(Long id) {
        return (!foods.containsKey(id));
    }

    public void setFoodLocation(Long id, ParseGeoPoint location) {
        Food toWork = foods.get(id);
        toWork.setLocation(location);
        checkIfReady(id);
    }

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


}
