package il.ac.huji.freefood;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Elyasaf on 4/30/2015.
 */

@ParseClassName("FoodList")
public class FoodListItem extends ParseObject {


    public FoodListItem() {
    }


    public int getId() {
        return getInt("Id");
    }

    public int getNumPeople() {
        return getInt("numPeople");
    }

    public String getBuilding() {
        return getString("building");
    }

    public String getInsideBuilding() {
        return getString("insideBuilding");
    }

    public String getPicture() {
        return getString("picture");
    }

    public String getDescription() {
        return  getString("description");
    }

    public String getAllInfo() {
        return getDescription() + " for " + Integer.toString(getNumPeople())+ " people is waiting in "+
                    getBuilding() + " in " +getInsideBuilding();
    }

    public void setId(int id) {
        put("id",id);
    }

    public void setNumPeople(int numPeople) {
        put("numPeople",numPeople);
    }

    public void setBuilding(String building) {
        put("building",building);
    }

    public void setInsideBuilding(String insideBuilding) {
        put("insideBuilding",insideBuilding);
    }

    public void setPicture(String picture) {
        put("picture",picture);
    }

    public void setDescription(String description) {
        put("description",description);
    }
}
