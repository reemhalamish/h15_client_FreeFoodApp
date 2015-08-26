package il.ac.huji.freefood.data;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Elyasaf on 4/30/2015.
 */

@ParseClassName("FoodList")
public class FoodListItem extends ParseObject implements Serializable {
//    @Deprecated
//    public static final String CREATED = "created";
//
//
//    @Deprecated
//    public FoodListItem(int numPeople, String building, String insideBuilding,
//                                                    int picture, String description){
//        super();
//        this.setNumPeople(numPeople);
//        this.setBuilding(building);
//        this.setInsideBuilding(insideBuilding);
//        this.setPicture(picture);
//        this.setDescription(description);
//        this.setCreator();
//    }

    public FoodListItem(Thumbnail picture, String title, String place) {
        this.setBuilding(place);
        this.setThumbnail(picture);
        this.setDescription(title);
        this.setCreator();
    }

    public int getNumPeople() {
        return getInt("numPeople");
    }

    public FoodListItem() {
        this.setCreator();
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


    @Deprecated
    public int getPicture() {
        return getInt("picture");
    }

    @Deprecated
    public void setPicture(int picture) {
        put("picture", picture);
    }
//
//    @Deprecated
//    public Date getCreatedDate() {
//        return getDate(CREATED);
//    }
//
//    @Deprecated
//    public void setCreatedDate(Date created) {
//        put(CREATED, created);
//    }


    public void setCreator() {
        put("creator", ParseUser.getCurrentUser());
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
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

    public Thumbnail getThumbnail() {
        return Thumbnail.fromInt(getInt("thumbnail"));
    }

    public void setThumbnail(Thumbnail thumbnail) {
        Log.d("thumbnail", thumbnail.toString());
        put("thumbnail", thumbnail.toInt());
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setUI_Ready() {
        put("ui_ready", true);
    }
    public void setLocationReady() {
        put("location_ready", true);
    }

    public boolean isReadyToPublish() {
        boolean location_ready, ui_ready;
        location_ready = getBoolean("location_ready");
        ui_ready =       getBoolean("ui_ready");
        return location_ready && ui_ready;
    }
}
