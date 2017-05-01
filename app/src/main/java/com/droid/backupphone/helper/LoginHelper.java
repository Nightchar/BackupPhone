package com.droid.backupphone.helper;

/**
 * The helper class for login operations.
 */
public class LoginHelper {

    /**
     * Check if email is valid or not.
     *
     * @param email the email
     * @return return true if email passes the validation criteria
     */
    public static boolean isEmailValid(String email) {
        return email != null && email.contains("@");
    }

    /**
     * Check if password is valid or not.
     *
     * @param password the password
     * @return return true if password passes the validation criteria
     */
    public static boolean isPasswordValid(String password) {
        return password != null && password.length() > 4;
    }
}
