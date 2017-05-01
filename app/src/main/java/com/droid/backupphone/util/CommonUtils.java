package com.droid.backupphone.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.droid.backupphone.activity.login.LoginActivity;
import com.droid.backupphone.activity.login.SignUpActvity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;

/**
 * The common utils class.
 */
public class CommonUtils {

    /**
     * Hides the keyboard.
     *
     * @param context the context
     * @param view    the view that is making request.
     * @param flags   Provides additional operating flags. Currently may be 0 or have the HIDE_IMPLICIT_ONLY bit set.
     */
    public static void hideSoftKeyboard(Context context, View view, int flags) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), flags);
        } else {
            inputManager.hideSoftInputFromWindow(null, flags);
        }
    }

    /**
     * Concatenate the elements of string array to one string.
     *
     * @param args the passed string arguments to be concatenated
     * @return the concatenated string
     */
    public static String concatenateString(Object... args) {
        StringBuilder sbConcatenated = new StringBuilder();

        for (Object arg : args) {
            sbConcatenated.append(arg);
        }
        return sbConcatenated.toString();
    }

    /**
     * The method to sign out from Firebase auth.
     */
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

    /**
     * Method to get firebase database parent reference.
     *
     * @return the firebase database parent reference
     */
    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Method to get firebase database reference of contact end point.
     *
     * @return the firebase database reference of contact end point
     */
    public static DatabaseReference getContactEndPoint() {
        return getDatabaseReference().child("contacts");
    }

    /**
     * Get user id from shared preference else logout.
     *
     * @param activity           the source activity reference
     * @param applicationContext the application context
     * @return return the user id of login session, saved in shared preference
     */
    public static String getUserId(Activity activity, Context applicationContext) {
        String userId = PreferenceUtils.getUserId(applicationContext);
        if (userId == null) {
            // logout from app and move to login screen
            logOut(activity, applicationContext);
        }
        return userId;
    }

    /**
     * Logout user and move to login screen.
     *
     * @param activity           the activity reference
     * @param applicationContext the application context
     */
    public static void logOut(Activity activity, Context applicationContext) {
        PreferenceUtils.removeUserId(applicationContext);
        PreferenceUtils.removeUserEmail(applicationContext);
        Intent intentNextActivity = new Intent(activity, LoginActivity.class);
        intentNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intentNextActivity);
    }

    /**
     * Function linkify a text view and make a phone call on touch.
     *
     * @param context   the context
     * @param tvLinkify Linkify text view
     * @param sequence  Spanned sequence
     */
    public static void linkifyCallTextView(final Context context, TextView tvLinkify, Spanned sequence) {
        ClickableSpan clickableSpan;
        int start;
        int end;
        int flags;

        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        UnderlineSpan[] underlines = strBuilder.getSpans(0, sequence.length(), UnderlineSpan.class);

        for (UnderlineSpan span : underlines) {
            start = strBuilder.getSpanStart(span);
            end = strBuilder.getSpanEnd(span);
            flags = strBuilder.getSpanFlags(span);
            clickableSpan = new ClickableSpan() {
                public void onClick(View view) {
                    Intent callIntent = new Intent(context, SignUpActvity.class);
                    context.startActivity(callIntent);
                }
            };

            strBuilder.setSpan(clickableSpan, start, end, flags);
        }

        tvLinkify.setText(strBuilder);
        tvLinkify.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Check if user logged in or not.
     *
     * @param context the context
     * @return return true if user logged in
     */
    public static boolean isUserLogin(Context context) {
        return PreferenceUtils.getUserId(context) != null;
    }
}
