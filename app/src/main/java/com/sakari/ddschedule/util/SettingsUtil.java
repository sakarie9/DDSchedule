package com.sakari.ddschedule.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsUtil {
    public static String getString(Context context, String key, String def) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, def);
    }
    public static Boolean getBoolean(Context context, String key, Boolean def) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, def);
    }
}
