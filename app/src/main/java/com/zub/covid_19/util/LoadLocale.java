package com.zub.covid_19.util;

import android.app.Activity;
import android.content.SharedPreferences;

import timber.log.Timber;

public class LoadLocale {

    private Activity activity;

    public LoadLocale(Activity activity) {
        this.activity = activity;
        Timber.d("LoadLocale loaded!");
    }

    public String getLocale() {
        SharedPreferences sharedPreferences = this.activity.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String locale = sharedPreferences.getString("language", "");
        if (locale != null && !locale.isEmpty()) {
            return locale;
        }
        return "-1";
    }

}
