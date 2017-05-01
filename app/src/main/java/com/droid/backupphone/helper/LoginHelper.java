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
    public static void setUpDataBase(DatabaseReference databaseReference, ValueEventListener valueEventListener, ChildEventListener childEventListener) {
        // Write a message to the database

//        databaseReference.setValue("Hello, World!");

        databaseReference.child("app_title").setValue("Realtime Database1");
        // Read from the database
        databaseReference.child("contacts").child("user" + 3).addValueEventListener(valueEventListener);
        //databaseReference.child("contacts").addChildEventListener(childEventListener);

        /*List<User> user1 = new ArrayList<User>();
        user1.add(new User("u1", "id1"));
        user1.add(new User("u2", "id2"));
        user1.add(new User("u3", "id3"));
        writeNewUser(databaseReference, "1", user1);

        List<User> user2 = new ArrayList<User>();
        user2.add(new User("u4", "id4"));
        user2.add(new User("u5", "id5"));
        user2.add(new User("u6", "id6"));
        writeNewUser(databaseReference, "2", user2);*/

        //updateUser(databaseReference, new User("u6", "change_id6"));
        //deleteUser(databaseReference, "u5");

    }

    private static void updateUser(DatabaseReference databaseReference, User user) {
        databaseReference.child("contacts").child("user3").child(user.getUsername()).setValue(user);
    }

    private static void deleteUser(DatabaseReference databaseReference, String user) {
        databaseReference.child("contacts").child("user3").child(user).removeValue();
    }

    private static void writeNewUser(DatabaseReference databaseReference, String userId, List<User> users) {
        for (User user : users) {
            databaseReference.child("contacts").child("user" + userId).child(user.getUsername()).setValue(user);
        }
    }
}
