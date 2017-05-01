package com.droid.backupphone.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.droid.backupphone.common.CommonConstants;

/**
 * Created by nikhil1804 on 26-04-2017.
 */

public class PreferenceUtils {

    private static boolean writeData(Context context, String userId, String prefKey) {
        SharedPreferences prefLocaleFile = context.getSharedPreferences(CommonConstants.PREF_LOCALE_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefLocaleFile.edit();
        if (editor != null) {
            editor.putString(prefKey, userId);
            return editor.commit();
        }
        return false;
    }

    private static String getData(Context context, String prefKey) {
        SharedPreferences prefLocaleFile = context.getSharedPreferences(CommonConstants.PREF_LOCALE_FILE,
                Context.MODE_PRIVATE);
        return prefLocaleFile.getString(prefKey, null);
    }

    /**
     * Save user id in shared preference
     *
     * @param context the context
     * @param userId  the user id to save
     * @return returns true if user id saved successfully
     */
    public static boolean saveUserId(Context context, String userId) {
        return writeData(context, userId, CommonConstants.PREF_USER_ID);
    }

    /**
     * Get user id saved in shared preference
     *
     * @param context the context
     * @return returns the user id
     */
    public static String getUserId(Context context) {
        return getData(context, CommonConstants.PREF_USER_ID);
    }

    /**
     * Delete the user id from shared preference
     *
     * @param context the context
     * @return returns true if user id deleted successfully
     */
    public static boolean removeUserId(Context context) {
        return saveUserId(context, null);
    }

    /**
     * Save user email in shared preference
     *
     * @param context   the context
     * @param userEmail the user id to save
     * @return returns true if user email saved successfully
     */
    public static boolean saveUserEmail(Context context, String userEmail) {
        return writeData(context, userEmail, CommonConstants.PREF_USER_EMAIL);
    }

    /**
     * Get user id saved in shared preference
     *
     * @param context the context
     * @return returns the user id
     */
    public static String getUserEmail(Context context) {
        return getData(context, CommonConstants.PREF_USER_EMAIL);
    }

    /**
     * Delete the user id from shared preference
     *
     * @param context the context
     * @return returns true if user id deleted successfully
     */
    public static boolean removeUserEmail(Context context) {
        return saveUserEmail(context, null);
    }
}
