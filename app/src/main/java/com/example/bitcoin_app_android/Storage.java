package com.example.bitcoin_app_android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Олег on 27.03.2017.
 */

public class Storage {
    public final static String CURRENT_CURRENCY = "Current_currency";
    public final static String NO_DATA = "no data";
    private static Storage instance;
    SharedPreferences preferences;


    public static synchronized Storage getInstance(Context context) {
        if(instance == null) {
            instance = new Storage(context.getApplicationContext());
        }
        return instance;
    }

    private Storage(Context context) {
        preferences = context.getSharedPreferences("Bitcoin", 0);
    }

    public String loadString(String key) {
        return preferences.getString(key, NO_DATA);
    }

    public void saveString(String key, String data) {
        preferences.edit().putString(key, data).apply();
    }
}
