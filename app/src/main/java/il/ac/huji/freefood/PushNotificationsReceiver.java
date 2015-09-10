package il.ac.huji.freefood;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

import il.ac.huji.freefood.activities_one_class.MainActivity;
import il.ac.huji.freefood.data.SingletonFoodList;

/**
 * Created by reem on 7/29/15.
 * The receiver. One day it will maybe catch notifications and will update the FoodList...
 * For now it just notificate the user that it's good to update the list
 */
public class PushNotificationsReceiver extends ParsePushBroadcastReceiver {
    private static SingletonFoodList singleton = SingletonFoodList.getInstance();
    @Override
    protected void onPushReceive(Context context, Intent intent) {
//        Log.d("ntf", "received!");
        super.onPushReceive(context, intent);
        SingletonFoodList.getInstance().getOnlyNewElementsFromParse();
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
//        Log.d("ntf", "opened!");
        Intent starting_freefood = new Intent(context, MainActivity.class);
        starting_freefood.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        starting_freefood.putExtras(intent.getExtras());
        context.startActivity(starting_freefood);
        super.onPushOpen(context, intent);
        SingletonFoodList.getInstance().getOnlyNewElementsFromParse();
    }
}
