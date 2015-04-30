package il.ac.huji.freefood;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayala on 30/04/2015.
 */
public class SingletonFoodList {

    private static List<FoodListItem> mainList = new ArrayList<FoodListItem>();

    public SingletonFoodList() {}

    public static List<FoodListItem> getList ()
    {
        return mainList;
    }

    public static List<FoodListItem> updateList ( List<FoodListItem> newList)
    {
            mainList = newList;
            return mainList;
    }


}
