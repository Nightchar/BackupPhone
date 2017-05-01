package com.droid.backupphone.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;

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
}
