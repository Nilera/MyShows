package me.myshows.android.model;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

import me.myshows.android.R;

/**
 * Created by Whiplash on 13.08.2015.
 */
public enum Action {
    WATCH(R.drawable.action_watch, 0xff78b531),
    NEW(R.drawable.action_new, 0xffcc0000),
    RATING(R.drawable.action_rating, 0xfffcb647),
    WATCH_LATER(R.drawable.action_watch_later, 0xff3894e8),
    STOP_WATCH(R.drawable.action_stop_watch, 0xffcc0000),
    ACHIEVEMENT(R.drawable.action_achievement, 0xfffcb647);

    private static Map<String, Action> strToAction = new HashMap<>(Action.values().length);

    static {
        strToAction.put("watch", WATCH);
        strToAction.put("new", NEW);
        strToAction.put("rating", RATING);
        strToAction.put("later", WATCH_LATER);
        strToAction.put("stop", WATCH_LATER);
        strToAction.put("achievement", WATCH_LATER);
    }

    private final int drawableId;
    private final int color;

    Action(@DrawableRes int drawableId, @ColorInt int color) {
        this.drawableId = drawableId;
        this.color = color;
    }

    @JsonCreator
    public static Action fromString(String value) {
        return strToAction.get(value);
    }

    @DrawableRes
    public int getDrawableId() {
        return drawableId;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    @JsonValue
    @Override
    public String toString() {
        for (Map.Entry<String, Action> entry : strToAction.entrySet()) {
            if (entry.getValue() == this) {
                return entry.getKey();
            }
        }
        return null;
    }
}
