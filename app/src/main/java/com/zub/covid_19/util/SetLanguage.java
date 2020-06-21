package com.zub.covid_19.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Extending the Alert Dialog
 */
public class SetLanguage extends AlertDialog {
    /**
     * This class will passing the Alert Dialog to set the language
     * @param context: get the context from the fragment/activity
     * @param activity: get the activity from the fragment/activity
     */
    public SetLanguage(Context context, Activity activity) {
        super(context);
        final String[] listLanguage = {"Indonesia", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Choose Language...");
        builder.setSingleChoiceItems(listLanguage, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    new SetLocale(context, activity, "in");
                } else if (i == 1){
                    new SetLocale(context, activity, "en");
                }
                activity.finish();
                activity.startActivity(activity.getIntent());
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
