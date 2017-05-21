package com.aquafadas.tagthebus.station.manager;

import android.content.Context;
import android.content.SharedPreferences;

import static com.aquafadas.tagthebus.utils.AppStaticValues.PREFS_NAME;
import static com.aquafadas.tagthebus.utils.AppStaticValues.PRE_LOAD;

/**
 * Created by Oussama on 21/05/2017.
 */

public class SharedManager {

    private static SharedManager instance;
    private final SharedPreferences sharedPreferences;

    public SharedManager(Context context) {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedManager with(Context context) {

        if (instance == null) {
            instance = new SharedManager(context);
        }
        return instance;
    }

    public void setPreLoad(boolean totalTime) {

        sharedPreferences
                .edit()
                .putBoolean(PRE_LOAD, totalTime)
                .apply();
    }

    public boolean getPreLoad(){
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }
}
