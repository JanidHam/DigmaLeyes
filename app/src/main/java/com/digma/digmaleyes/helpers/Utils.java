package com.digma.digmaleyes.helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by janidham on 10/12/15.
 */
public class Utils {

    Activity context;

    final String CHECK_EMAIL = "check_email";
    final String DEFAULT_EMAIL = "default_email";

    public Utils(Activity context) {
        this.context = context;
    }

    public String isDefaultEmail() {
        SharedPreferences settings = this.context.getPreferences(0);

        if (settings.getBoolean(CHECK_EMAIL, false))
            return settings.getString(DEFAULT_EMAIL, "");

        return "";
    }

    public Activity getContext() {
        return this.context;
    }

    public void renderMessage(String message, int time) {
        Toast toast = Toast.makeText(this.context, message, time);
        toast.show();
    }

    public boolean isValidEmail(String email) {
        if (email.equals("")) return false;

        return true;
    }

    public void setTimeOutCloseBanner(int time) {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //toogleRecyclerView(true);
                        //toogleBannerCenter(false);
                        Log.i("tag", "This'll run 300 milliseconds later");
                    }
                },
                time);
    }
}
