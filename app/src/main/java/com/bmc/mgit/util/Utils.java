package com.bmc.mgit.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.bmc.mgit.activity.LoginActivity;
import com.bmc.mgit.misc.Constants;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;


public class Utils {

    public static String getAuthLogin(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(Constants.User.LOGIN, null);
    }

    public static String getAuthToken(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(Constants.User.AUTH_TOKEN, null);
    }

    public static boolean isAuthorized(Context context) {
        return getAuthLogin(context) != null && getAuthToken(context) != null;
    }

    public static void logout(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit()
                .remove(Constants.User.LOGIN)
                .remove(Constants.User.AUTH_TOKEN)
                .apply();

        // take user to login screen
        Intent timeToLogin = new Intent(context, LoginActivity.class);
        timeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(timeToLogin);
    }

    public static Bitmap urlToBitmap(Context context, String imageUrl) {
        try {
            return Ion.with(context).load(imageUrl).asBitmap().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
