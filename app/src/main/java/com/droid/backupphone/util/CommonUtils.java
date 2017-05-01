package com.droid.backupphone.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.droid.backupphone.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;

/**
 * Created by nikhil1804 on 17-04-2017.
 */

public class CommonUtils {

    public static void hideSoftKeyboard(Context context, View view, int flags) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), flags);
        } else {
            inputManager.hideSoftInputFromWindow(null, flags);
        }
    }

    public static void signOutFromApp() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Checks whether the list is empty or null.
     *
     * @param collection the passed collection object
     * @return true, if the list is empty or null
     */
    public static boolean isCollectionNullOrEmpty(Collection<?> collection) {

        return (collection == null || collection.isEmpty());
    }

    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getContactEndPoint() {
        return getDatabaseReference().child("contacts");
    }

    public static String getUserId(Activity activity, Context applicationContext) {
        String userId = PreferenceUtils.getUserId(applicationContext);
        if (userId == null) {
            // logout from app and move to login screen
            logOut(activity, applicationContext);
        }
        return userId;
    }

    public static void logOut(Activity activity, Context applicationContext) {
        PreferenceUtils.removeUserId(applicationContext);
        PreferenceUtils.removeUserEmail(applicationContext);
        Intent intentNextActivity = new Intent(activity, LoginActivity.class);
        intentNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intentNextActivity);
    }
}
