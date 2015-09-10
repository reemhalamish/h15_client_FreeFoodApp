package il.ac.huji.freefood.data;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by Elyasaf on 4/30/2015.
 */

@ParseClassName("Food")
public class Food extends ParseObject implements Serializable {

    public Food() {}

    @Deprecated
    public Food(Thumbnail picture, String title, String details) {
        this.setDetails(details);
        this.setThumbnail(picture);
        this.setTitle(title);
        this.setCreator();
    }

    public String getDetails() {
        return getString("details");
    }

    public void setDetails(String details) {
        put("details", details);
    }

    public void setCreator() {
        put("creator", ParseUser.getCurrentUser());
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public Thumbnail getThumbnail() {
        return Thumbnail.fromInt(getInt("thumbnail"));
    }

    public void setThumbnail(Thumbnail thumbnail) {
//        Log.d("thumbnail", thumbnail.toString());
        put("thumbnail", thumbnail.toInt());
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }
    public int getAccuracy() {
        return getInt("accuracy");
    }

    public void setLocation(ParseGeoPoint location, int accuracy) {
        put("accuracy", accuracy);
        put("location", location);
        put("location_ready", true);
    }

    public void setUI(Thumbnail picture, String title, String details) {
        this.setDetails(details);
        this.setThumbnail(picture);
        this.setTitle(title);
        this.setCreator();
        put("ui_ready", true);
    }

    public boolean isReadyToPublish() {
        boolean location_ready, ui_ready;
        location_ready = getBoolean("location_ready");
        ui_ready = getBoolean("ui_ready");
        return location_ready && ui_ready;
    }
}
