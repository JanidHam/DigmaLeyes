package com.digma.digmaleyes.helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by janidham on 10/12/15.
 */
public class Utils {

    Activity context;

    final String CHECK_EMAIL = "check_email";
    final String DEFAULT_EMAIL = "default_email";
    final String URL_SEND_EMAIL = "http://digma.mx/pruebasalex/mail-test.php";

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

    public String getURL_SEND_EMAIL() {
        return this.URL_SEND_EMAIL;
    }



    public void renderMessage(String message, int time) {
        Toast toast = Toast.makeText(this.context, message, time);
        toast.show();
    }

    public boolean isValidEmail(String email) {
        if (email.equals("")) return false;

        return true;
    }
}
