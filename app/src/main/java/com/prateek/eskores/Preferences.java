package com.prateek.eskores;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by prateek on 17/3/15.
 */
public class Preferences {

    public static final String PREF_NAME = "preference_eskore_app";
    public static final String SELECTED_TEAMS = "preference_teams";
    public static final String DELAY_DURATION = "delay_duration";
    public static final String IS_STARTED = "is_started";
    public static final Long MINUTE = (60L*1000);

    private static SharedPreferences getPreferences() {
        return EskoreApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public static void savePreferenceSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static Set<String> getPreference(String key) {
        return getPreferences().getStringSet(key, null);
    }

    public static void savePreferenceString(String key, Long value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static Long getPreferenceLong(String key) {
        return getPreferences().getLong(key, 2*MINUTE);
    }

    public static void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getPreferenceBoolean(String key) {
        return getPreferences().getBoolean(key, false);
    }
}
