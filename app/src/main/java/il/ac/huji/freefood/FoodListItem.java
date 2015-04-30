package il.ac.huji.freefood;

/**
 * Created by Elyasaf on 4/30/2015.
 */
public class FoodListItem {

    protected int id;
    protected int numPeople;
    protected String building;
    protected String insideBuilding;
    protected String picture;
    protected String description;
    protected String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FoodListItem(int id, int numPeople, String building, String insideBuilding, String picture, String description, String title) {
        this.title = title;
        this.id = id;
        this.numPeople = numPeople;
        this.building = building;
        this.insideBuilding = insideBuilding;
        this.picture = picture;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public String getBuilding() {
        return building;
    }

    public String getInsideBuilding() {
        return insideBuilding;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setInsideBuilding(String insideBuilding) {
        this.insideBuilding = insideBuilding;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
