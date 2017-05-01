package com.droid.backupphone.helper;

import com.droid.backupphone.model.User;
import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.util.CommonUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by nikhil1804 on 25-04-2017.
 */

public class PhoneContactActivityHelper {

    public static void updateUser(DatabaseReference databaseReference, User user) {
        databaseReference.child("contacts").child("user3").child(user.getUsername()).setValue(user);
    }

    public static void deleteUser(DatabaseReference databaseReference, String user) {
        databaseReference.child("contacts").child("user3").child(user).removeValue();
    }

    public static void writeNewUser(DatabaseReference userEndPoint, String userId, List<Contact> contacts) {
        for (Contact contact : contacts) {
            userEndPoint.child(contact.getContactName()).setValue(contact);
        }
    }

    public static DatabaseReference getUserEndPoint(String userId) {
        return CommonUtils.getContactEndPoint().child("user" + userId);
    }
}
