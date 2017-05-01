package com.droid.backupphone.helper;

import android.content.ContentProviderOperation;
import android.provider.ContactsContract;

import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.util.CommonUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * The helper class for database.
 */
public class DatabaseHelper {

    /**
     * Update user contact.
     *
     * @param userEndPoint the database reference till contact >> user+id
     * @param contact      the contact
     */
    public static void updateContact(DatabaseReference userEndPoint, Contact contact) {
        userEndPoint.child(contact.getContactName()).setValue(contact);
    }

    /**
     * Delete user contact.
     *
     * @param userEndPoint the database reference till contact >> user+id
     * @param contact      the contact
     */
    public static void deleteContact(DatabaseReference userEndPoint, Contact contact) {
        userEndPoint.child(contact.getContactName()).removeValue();
    }

    /**
     * Add new user contact
     *
     * @param userEndPoint the database reference till contact >> user+id
     * @param contacts     the contact list
     */
    public static void writeNewContact(DatabaseReference userEndPoint, List<Contact> contacts) {
        for (Contact contact : contacts) {
            userEndPoint.child(contact.getContactName()).setValue(contact);
        }
    }

    /**
     * Get the database reference till contact >> user+id
     *
     * @param userId the user id
     * @return the database reference
     */
    public static DatabaseReference getUserEndPoint(String userId) {
        return CommonUtils.getContactEndPoint().child("user" + userId);
    }

    // common method to create an operation.
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

    /**
     * Method to get Add Name operation.
     *
     * @param contactRawId   the contact id
     * @param strDisplayName the display name
     * @return return Add Name operation
     */
    public static ContentProviderOperation getAddNameOperation(int contactRawId, String strDisplayName) {
        return getContentProviderOperation(contactRawId,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                strDisplayName,
                null,
                -1);
    }

    /**
     * Method to get Add Phone operation.
     *
     * @param contactRawId the contact id
     * @param number       the number
     * @param type         the phone number type (Home, Work, others)
     * @return return Add Phone operation
     */
    public static ContentProviderOperation getAddPhoneOperation(Integer contactRawId, String number, int type) {
        return getContentProviderOperation(contactRawId,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                number,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                type);
    }
}
