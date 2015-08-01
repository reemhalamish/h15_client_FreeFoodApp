package il.ac.huji.freefood;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Elyasaf on 4/30/2015.
 */

@ParseClassName("FoodList")
public class FoodListItem extends ParseObject implements Serializable {


    public FoodListItem() {
    }

    public FoodListItem(int numPeople, String building, String insideBuilding,
                                                    int picture, String description){
        super();
        this.setNumPeople(numPeople);
        this.setBuilding(building);
        this.setInsideBuilding(insideBuilding);
        this.setPicture(picture);
        this.setDescription(description);
    }

    public int getNumPeople() {
        return getInt("numPeople");
    }

    public void setNumPeople(int numPeople) {
        put("numPeople", numPeople);
    }

    public String getBuilding() {
        return getString("building");
    }

    public void setBuilding(String building) {
        put("building", building);
    }

    public String getInsideBuilding() {
        return getString("insideBuilding");
    }

    public void setInsideBuilding(String insideBuilding) {
        put("insideBuilding", insideBuilding);
    }

    public int getPicture() {
        return getInt("picture");
    }

    public void setPicture(int picture) {
        put("picture", picture);
    }

    public String getDescription() {
        return  getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getAllInfo() {
        return getDescription() + " for " + Integer.toString(getNumPeople())+ " people is waiting in "+
                    getBuilding() + " in " +getInsideBuilding();
    }
}
