package com.droid.backupphone.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by nikhil1804 on 23-04-2017.
 */

@IgnoreExtraProperties
public class User {
    private String username;
    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
