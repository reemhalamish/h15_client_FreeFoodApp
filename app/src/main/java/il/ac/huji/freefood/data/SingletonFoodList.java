package il.ac.huji.freefood.data;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import il.ac.huji.freefood.FreeFoodApplication;

/**
 * Created by Ayala on 30/04/2015.
 * Here to make calling the foodlist easier
 */
public class SingletonFoodList {
    private static final int ADD_ONE_ITEM_FIRST = 1;
    private static final int ADD_ONE_ITEM_LAST = 2;
    private static final int ADD_LIST_FIRST = 3;
    private static final int ADD_LIST_LAST = 4;
    private static final int REMOVE_ITEM_IN_POSITION = 5;
    private static final int REMOVE_ITEM_BY_REFERENCE = 6;
    private static final int REPLACE_WHOLE_LIST = 7;
    private static final int REMOVE_WHOLE_LIST = 8;
    private static SingletonFoodList instance;
    private FreeFoodApplication application;
    private List<Food> clientFoodListItems;
    private List<Handler> listChangesHandlers;
    private Date lastUpdated;


    protected SingletonFoodList() {
        listChangesHandlers = new LinkedList<>();
    }

    public static synchronized SingletonFoodList getInstance() {
        if (instance == null) {
            instance = new SingletonFoodList();
        }
        return instance;
    }

    /**
     * this method tries to get the foodListItems that are stored in the local database first
     * <p/>
     * if it fails, it tries to connect to the internet and get the foodList from parse
     * by calling getListFromParse()
     */
    private void getListFromLocal() {
        FindCallback<Food> localCallback = new FindCallback<Food>() {

            @Override
            public void done(List<Food> foodListItems, ParseException e) {
                if (e != null) {
                    Log.e("parse ds", "no food is in local datastore. trying to reach the internet");
                    Log.e("parse ds", e.getMessage());
                    getListFromParse();
                } else {
                    Log.d("parse ds", "found food from local ds - " + foodListItems.size() + " items");

                    ParseObject.fetchAllInBackground(foodListItems);
                    messWithList(REPLACE_WHOLE_LIST, null, foodListItems, 0);
                    if (foodListItems.size() == 0) { // nothing local!
                        getListFromParse();
                    } else {
                        // set the lastUpdated date to be the last food item exist
                        setLastUpdated(foodListItems.get(0).getCreatedAt());
                        getOnlyNewElementsFromParse(); // update the list in background
                    }
                }
            }
        };
        ParseQuery<Food> query = ParseQuery.getQuery(Food.class);
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.findInBackground(localCallback);
    }

    /**
     * gets the items that were uploaded in the last day
     */
    private void getListFromParse() {
        FindCallback<Food> parseCallback = new FindCallback<Food>() {
            @Override
            public void done(List<Food> foodListItems, ParseException e) {
                if (e != null) {
                    Log.e("parse ds", "no food from parse.com");
                    Log.e("parse ds", e.getMessage());

                    // so let's create the list.
                    // an empty list suggests there is no food, while null means we need to sync
                    clientFoodListItems = new ArrayList<>();
                } else {
                    Log.d("parse ds", "got some food from parse.com with " + foodListItems.size() + " items");
                    ParseObject.pinAllInBackground(foodListItems);
                    messWithList(REPLACE_WHOLE_LIST, null, foodListItems, 0);

                    // update lastUpdated Date if needed
                    if (foodListItems.size() > 0) {
                        setLastUpdated(foodListItems.get(0).getCreatedAt());
                    }
                }
            }
        };


        ParseQuery<Food> query = ParseQuery.getQuery(Food.class);
        query.whereGreaterThan("createdAt", lastUpdated);
        query.orderByDescending("createdAt");
        query.findInBackground(parseCallback);


    }

    public void getOnlyNewElementsFromParse() {
        getOnlyNewElementsFromParse(null);
    }

    /**
     * @param toDelete if not null - will be deleted from the ORIGINAL clientlist
     *                 before inserting the results from the query
     */
    private void getOnlyNewElementsFromParse(final Food toDelete) {
        final Date dateToSearchFrom;
        if (lastUpdated != null) {
            dateToSearchFrom = lastUpdated;
        } else {
            dateToSearchFrom = getYesterday();
        }

        FindCallback<Food> parseCallback = new FindCallback<Food>() {
            @Override
            public void done(List<Food> foodListItems, ParseException e) {
                if (e != null) {
                    Log.e("parse ds", "no food from try to update in parse.com");
                    Log.e("parse ds", e.getMessage());
                } else {
                    Log.d("parse ds", "updating, " + foodListItems.size() + " items");
                    if (toDelete != null)
                        messWithList(REMOVE_ITEM_BY_REFERENCE, toDelete, null, 0);

                    messWithList(ADD_LIST_FIRST, null, foodListItems, 0);
                    // update lastUpdated Date if needed
                    if (foodListItems.size() > 0) {
                        setLastUpdated(foodListItems.get(0).getCreatedAt());
                    }
                }
            }
        };


        ParseQuery<Food> query = ParseQuery.getQuery(Food.class);
        query.whereGreaterThan("createdAt", dateToSearchFrom);
        query.orderByDescending("createdAt");
        query.findInBackground(parseCallback);
    }

    public void init(FreeFoodApplication app, Date lastUpdatedFromApp) {
        application = app;
        lastUpdated = getYesterday(); // old enough
        if (lastUpdatedFromApp != null && lastUpdatedFromApp.after(lastUpdated)) //d1.after(d2) == d1.isAfter(d2)
            this.lastUpdated = lastUpdatedFromApp;
        getListFromLocal();
    }

    public List<Food> getClientFoodListItems() {
        return clientFoodListItems;
    }
    public Food getCliendFoodAtIndex(int index) {
        if (index < 0 || index > clientFoodListItems.size()) { return null; }
        return clientFoodListItems.get(index);
    }

    public synchronized void removeItem(int position) {
        messWithList(REMOVE_ITEM_IN_POSITION, null, null, position);
    }

    public void registerHandler(Handler newbie) {
        listChangesHandlers.add(newbie);
    }

    public void removeHandler(Handler toRemove) {
        listChangesHandlers.remove(toRemove);
    }

    public void notifyHandlersNewList() {
        for (Handler handler : listChangesHandlers) {
            Message msg = Message.obtain();
            handler.sendMessage(msg);
        }
    }

    /**
     * changes in the list will go from here
     *
     * @param action      the action to take with the list:
     *                    1. add item to the start of the list WITHOUT pin nor save (taken care at addToList())
     *                    2. add item to the end of the list WITHUOT pin nor save
     *                    3. add list<items> to the start of the list and pin
     *                    4. add list<items> to the end of the list and pin
     *                    5. remove item in this position from the list and unpin
     *                    6. remove the item from the reference, WITHOUT unpin (taken care outside)
     *                    7. replace the whole list with the new list WITHOUT PINNING (maybe it got from local db)
     *                    8. clear the whole list, WITHOUT unpinning (taken care at clearAndUnpinAll() )
     * @param oneItem     if needed
     * @param listOfItems if needed
     * @param position    if needed
     */
    private synchronized void messWithList(int action, Food oneItem, List<Food> listOfItems, int position) {
        switch (action) {
            case ADD_ONE_ITEM_FIRST:
                if (oneItem == null)
                    return;
                clientFoodListItems.add(0, oneItem);
                break;
            case ADD_ONE_ITEM_LAST:
                if (oneItem == null)
                    return;
                clientFoodListItems.add(oneItem);
                break;
            case ADD_LIST_FIRST:
                if (listOfItems == null || listOfItems.size() == 0)
                    return;
                ParseObject.pinAllInBackground(listOfItems);
                clientFoodListItems.addAll(0, listOfItems);
                break;
            case ADD_LIST_LAST:
                if (listOfItems == null || listOfItems.size() == 0)
                    return;
                clientFoodListItems.addAll(listOfItems);
                ParseObject.pinAllInBackground(listOfItems);
                break;
            case REMOVE_ITEM_IN_POSITION:
                if (position < 0 || clientFoodListItems == null || position >= clientFoodListItems.size())
                    return;
                Food toRemove = clientFoodListItems.get(position);
                toRemove.unpinInBackground();
                clientFoodListItems.remove(position);
                break;
            case REMOVE_ITEM_BY_REFERENCE:
                if (oneItem == null)
                    return;
                clientFoodListItems.remove(oneItem);
                break;
            case REPLACE_WHOLE_LIST:
                if (listOfItems == null)
                    return;
                clientFoodListItems = listOfItems;
                break;
            case REMOVE_WHOLE_LIST:
                clientFoodListItems.clear();
                break;
            default: // something else
                return;
        }
        // there WAS a change. notify the listeners!
        notifyHandlersNewList();
        Log.d("mess_with_list", "" + action + ": was completed successfully!");
    }

    public void addToList(final Food item) {
        messWithList(ADD_ONE_ITEM_FIRST, item, null, 0);
        item.saveEventually(new SaveCallback() {
            /*
                the thing is, It can be downloaded from parse now. so next time of update it will get
                as one of the results.

                Solution: remove it from the list now,
                TODO jump a little window that "food is shared!"
                then re-query parse
             */
            @Override
            public void done(ParseException e) {
                getOnlyNewElementsFromParse(item);
            }
        });
    }

    public void clearAndUnpinAllItems() {
        List<Food> allElements = new ArrayList<>(clientFoodListItems);
        // save a copy to unpin, the original will be cleared in a sec...
        ParseObject.unpinAllInBackground(allElements, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("unpin", "removed all items!");
            }
        });
        messWithList(REMOVE_WHOLE_LIST, null, null, 0);
    }


    private void setLastUpdated(Date newTime) {
        lastUpdated = newTime;
        application.setLastUpdated(newTime);
    }

    private Date getYesterday() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now); // convert date to Calendar object
        int daysToDecrement = -1;
        cal.add(Calendar.DATE, daysToDecrement);
        return cal.getTime(); // again get back date object
    }
}

/*
TODO:
* implement such a way that when new food is getting, it is valid for an hour and then gets deleted from parse
* option to report food as "over"?
* get the food from gps?
* update the entire AddFoodActivity to be normal
* save locally the last-updated time ( == when the user pressed "refresh" last time? when the last query came from parse?)
* add facebook connection
* each facebook client will hae their own channel, each of your facebook friends is subscribed to your channel,
    when you publish new food, it gets to parse to let everyone see, plus to your facebook friends via your channel
    the user will be able to decide where to publish - everyone, fb friends only, or both of lists
* add activity on pressing the food with: description, how many people viewed it by now, creator, aaaaaand that's it I guess
    (maybe facebook profile if he's in your friends)

* add points for every user for his shares? so that users that share a lot will receive funny pictures or something?


* what would I do with the GPS??? the simple solution is to add gps tracking and wait with the send until it's tracked or time passed
* also for signing in every time, I need to track down the user (at least to the city level) and show him\her only suitable food.

* parse would have to know to send notifications only in  certain delta for the user's lon\lat




 */
