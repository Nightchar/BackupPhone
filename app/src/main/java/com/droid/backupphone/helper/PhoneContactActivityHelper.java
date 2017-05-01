package com.droid.backupphone.helper;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;

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

    private static ContentProviderOperation getContentProviderOperation(Integer contactRawId, String mimeType,
                                                                        String whatToAdd, String data,
                                                                        String typeKey, int type) {
        ContentProviderOperation.Builder contentProviderOperation =
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        if (contactRawId != null) {
            contentProviderOperation.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactRawId);

        }
        if (mimeType != null) {
            contentProviderOperation.withValue(ContactsContract.Data.MIMETYPE, mimeType);
        }
        if (whatToAdd != null && data != null) {
            contentProviderOperation.withValue(whatToAdd, data);
        }
        if (typeKey != null && type != -1) {
            contentProviderOperation.withValue(typeKey, type);
        }

        return contentProviderOperation.build();
    }

    public static ContentProviderOperation getAddNameOperation(int contactRawId, String strDisplayName) {
        return getContentProviderOperation(contactRawId,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                strDisplayName,
                null,
                -1);
    }

    public static ContentProviderOperation getAddPhoneOperation(Integer contactRawId, String strNumber, int type) {
        return getContentProviderOperation(contactRawId,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                strNumber,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                type);
    }
}
