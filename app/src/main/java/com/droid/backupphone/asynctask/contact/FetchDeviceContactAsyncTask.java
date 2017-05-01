package com.droid.backupphone.asynctask.contact;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.model.contact.PhoneDetail;

import java.util.ArrayList;
import java.util.List;

import static com.droid.backupphone.common.CommonConstants.IS_SIM;

/**
 * The async task class to fetch all the device contacts.
 */
public class FetchDeviceContactAsyncTask extends AsyncTask<Void, Void, List<Contact>> {

    private final String TAG = "FetchDeviceContactTask";
    private Context mContext = null;
    private boolean mIsOnlyDeviceContact = false;

    /**
     * The constructor.
     *
     * @param context           the context
     * @param onlyDeviceContact fetch only device contact or sim contact as well
     */
    public FetchDeviceContactAsyncTask(Context context, boolean onlyDeviceContact) {
        mContext = context;
        mIsOnlyDeviceContact = onlyDeviceContact;
    }

    @Override
    protected List<Contact> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null);

        List<Contact> contacts = getContacts(cursor);
        return contacts;
    }

    // get list of contacts from cursor object
    private List<Contact> getContacts(Cursor cursor) {
        List<Contact> contacts = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) { // must check the result to prevent exception
            do { // iterate each contact here
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                List<PhoneDetail> phoneList = new ArrayList<>();

                // fetch phone number for each contact
                if (isContactHasPhoneNumber(cursor)) {
                    Log.e(TAG, "Contact name : " + contactName);
                    retrievePhoneForContact(contactId, phoneList);
                }

                // add contact  if it has at least 1 phone number
                if (phoneList.size() > 0) {
                    Contact contact = new Contact(contactId, contactName, phoneList);
                    contacts.add(contact);
                }
            } while (cursor.moveToNext());
        }

        return contacts;
    }

    // check if contact has phone number or not
    private boolean isContactHasPhoneNumber(Cursor cursor) {
        return Integer
                .parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0;
    }

    // fetch all phone numbers for given contact id
    private void retrievePhoneForContact(String contactId, List<PhoneDetail> phoneList) {
        //the below cursor will give you details for multiple contacts
        Cursor phoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);

        if (phoneCursor != null) {

            // continue till this cursor reaches to all phone numbers which are associated with a contact
            // in the contact list
            if (phoneCursor.moveToFirst()) {
                do {
                    int phoneType = getPhoneType(phoneCursor);
                    String phoneNo = getPhoneNumber(phoneCursor);
                    boolean isSim = checkIsSim(phoneCursor);

                    PhoneDetail phone = null;
                    if (phoneType >= ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                            && phoneType <= ContactsContract.CommonDataKinds.Phone.TYPE_MMS) {
                        phone = new PhoneDetail(phoneType, phoneNo);
                    }
                    showLog(phoneType, phoneNo, isSim);

                    // if onlyDeviceContact = true, only device contact will be saved here
                    if (phone != null && (mIsOnlyDeviceContact ? !isSim : true)) {
                        phoneList.add(phone);
                    }
                } while (phoneCursor.moveToNext());
            }
            phoneCursor.close();
        }
    }

    // get phone type constant from given cursor object
    private int getPhoneType(Cursor phoneCursor) {
        return phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
    }

    // get phone number from given cursor object
    private String getPhoneNumber(Cursor phoneCursor) {
        return phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
    }

    // check if phone number saved in sim or device
    private boolean checkIsSim(Cursor phoneCursor) {
        boolean isSim = true;
        int isSimColumnIndex = phoneCursor.getColumnIndex(IS_SIM);
        if (isSimColumnIndex != -1) {
            // value =0 for device contact and non-zero(1,2) for sim contacts
            if ((phoneCursor.getInt(isSimColumnIndex) == 0)) {
                isSim = false;
            }
        } else {
            // label value be null or 'SIM'
            String label = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract
                    .CommonDataKinds.Phone.LABEL));
            if (label == null) {
                isSim = false;
            }
        }
        return isSim;
    }

    // print log
    private void showLog(int phoneType, String phoneNo, boolean isSim) {
        switch (phoneType) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                Log.e(TAG, ": TYPE_MOBILE : " + phoneNo + " : IS_SIM : " + isSim);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                Log.e(TAG, ": TYPE_HOME : " + phoneNo + " : IS_SIM : " + isSim);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                Log.e(TAG, ": TYPE_WORK : " + phoneNo + " : IS_SIM : " + isSim);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                Log.e(TAG, ": TYPE_WORK_MOBILE : " + phoneNo + " : IS_SIM : " + isSim);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                Log.e(TAG, ": TYPE_OTHER : " + phoneNo + " : IS_SIM : " + isSim);
                break;
            default:
                break;
        }
    }
}