package com.zub.covid_19.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;
import java.util.Objects;

/**
 * Extending the Set Language for setting the locale
 */
public class SetLocale extends SetLanguage {
    /**
     * Set the locale to passing the language
     * @param context: get the context
     * @param activity: get the activity
     * @param language: get the language (STRING)
     */
    protected SetLocale(Context context, Activity activity, String language) {
        super(context, activity);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        Objects.requireNonNull(activity).getBaseContext().getResources().updateConfiguration(configuration, activity.getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("language", language);
        editor.apply();
    }
}
