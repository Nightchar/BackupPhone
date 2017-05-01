package com.droid.backupphone.helper;

import android.util.Log;

import com.droid.backupphone.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil1804 on 22-04-2017.
 */

public class LoginHelper {

    public static boolean isEmailValid(String email) {
        return email != null && email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() > 4;
    }


    // TODO remove it after useø
    public static void setUpDataBase(DatabaseReference mDatabase, ValueEventListener valueEventListener, ChildEventListener childEventListener) {
        // Write a message to the database

//        mDatabase.setValue("Hello, World!");

        // Read from the database
        mDatabase.child("contacts").addValueEventListener(valueEventListener);
        //mDatabase.child("contacts").addChildEventListener(childEventListener);

        List<User> user1 = new ArrayList<User>();
        user1.add(new User("u1", "id1"));
        user1.add(new User("u2", "id2"));
        writeNewUser(mDatabase, "1", user1);

        List<User> user2 = new ArrayList<User>();
        user2.add(new User("u3", "id3"));
        user2.add(new User("u4", "id4"));
        writeNewUser(mDatabase, "2", user2);

        List<User> user3 = new ArrayList<User>();
        user3.add(new User("u5", "id5"));
        user3.add(new User("u6", "id6"));
        writeNewUser(mDatabase, "3", user3);

        updateUser(mDatabase, new User("u6", "change_id6"));
        deleteUser(mDatabase, "u5");
    }

    private static void updateUser(DatabaseReference mDatabase, User user) {
        mDatabase.child("contacts").child("user3").child(user.getUsername()).setValue(user);
    }

    private static void deleteUser(DatabaseReference mDatabase, String user) {
        mDatabase.child("contacts").child("user3").child(user).removeValue();
    }

    private static void writeNewUser(DatabaseReference mDatabase, String userId, List<User> users) {
        for (User user : users) {
            mDatabase.child("contacts").child("user" + userId).child(user.getUsername()).setValue(user);
        }
    }

}
