package il.ac.huji.freefood.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import il.ac.huji.freefood.R;

/**
 * Created by reem on 8/8/15.
 */
public enum Thumbnail implements Serializable {
    DEFAULT(0),
    DRINKS(10),
    COFFEE(11),
    BEER(12),

    PIZZA(20),
    SANDVITCH(21),

    COOKIES(30),
    CAKE(31),
    RUGELACH(32),
    FRUITS(33);

    private static final Map<Integer, Thumbnail> intToTypeMap = new HashMap<>();

    static {
        for (Thumbnail type : Thumbnail.values()) {
            intToTypeMap.put(type.id, type);
        }
    }

    private int id;

    Thumbnail(int id) {
        this.id = id;
    }

    public static Thumbnail fromInt(int i) {
        Thumbnail type = intToTypeMap.get(i);
        if (type == null)
            return Thumbnail.DEFAULT;
        return type;
    }

    public int getId() {
        return id;
    }

    public int toInt() {
        return id;
    }

    public int toDrawableID() {
        switch (this) {
            case DEFAULT:
                return R.drawable.default_thumbnail;
            case DRINKS:
                return R.drawable.drinks;
            case COFFEE:
                return R.drawable.coffee;
            case BEER:
                return R.drawable.beer;
            case PIZZA:
                return R.drawable.pizza;
            case SANDVITCH:
                return R.drawable.sandwich;
            case COOKIES:
                return R.drawable.cookies;
            case CAKE:
                return R.drawable.cake;
            case RUGELACH:
                return R.drawable.rugelach;
            case FRUITS:
                return R.drawable.fruits;
            default:
                return R.drawable.default_thumbnail;
        }
    }
}
