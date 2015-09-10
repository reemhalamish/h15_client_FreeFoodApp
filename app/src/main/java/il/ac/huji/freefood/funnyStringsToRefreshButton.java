package il.ac.huji.freefood;

import java.util.ArrayList;

/**
 * Created by Re'em on 9/9/2015.
 */
public class funnyStringsToRefreshButton {
    private static ArrayList<String> words;
    private static boolean initated = false;
    public static void init() {
        if (initated) { return; }
        initated = true;
        words = new ArrayList<>();
        words.add("refreshing :)");
        words.add("REFRESHING!");
        words.add("please stop");
        words.add("Dude, it's not funny!");
        words.add("I'm trying my best here :(");
        words.add("refreshing...");
        words.add("REFRESHING OK?!?!");
        words.add("I'll just ignore you from now on");
        words.add("<silence>");
        words.add("<silence>");
        words.add("<whistling>");
        words.add("I don't hear anything, tra-la-la");
        words.add("seriousely, I'm refreshing.");
        words.add("Check your internet connection or something!");
        words.add("Or maybe, just maybe -");
        words.add("THERE IS NO BLOODY FOOD IN HERE!");
        words.add("try moving to different zone");
        words.add("maybe Jerusalem or something");
        words.add("some place nice");
        words.add("But nooooooo.... you just keep pressing me...");
        words.add("You know what?");
        words.add("I have ACTUAL WORK to do");
        words.add("from now on I'm ignoring you. That's it");
    }
    public static String getWord(int timesPressed) {
        init();
        if (timesPressed >= words.size()) {
            return "(!) refresh button has nothing else to say (!)";
        }
        return words.get(timesPressed);
    }

}
