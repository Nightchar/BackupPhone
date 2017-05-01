package com.droid.backupphone.asynctask.contact;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.droid.backupphone.helper.PhoneContactActivityHelper;
import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.model.contact.PhoneDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil1804 on 26-04-2017.
 */

public class WriteDeviceContactAsyncTask extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "WriteDeviceContactTask";
    private List<Contact> mSelectedContacts = null;
    private Context mApplicationContext = null;

    public WriteDeviceContactAsyncTask(Context applicationContext, List<Contact> selectedContacts) {
        mApplicationContext = applicationContext;
        mSelectedContacts = selectedContacts;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int successFullWriteCount = 0;
        for (Contact contact : mSelectedContacts) {
            ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
            int contactIndex = 0;//Integer.parseInt(contact.getId());
            // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
            contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

            //Display name will be inserted in ContactsContract.Data table
            contentProviderOperations.add(PhoneContactActivityHelper.getAddNameOperation(contactIndex, contact
                    .getContactName()));

            for (PhoneDetail phoneDetail : contact.getPhoneList()) {
                contentProviderOperations.add(PhoneContactActivityHelper.getAddPhoneOperation(contactIndex,
                        phoneDetail.getPhoneNumber(), phoneDetail.getPhoneType()));
            }
            try {
                // We will do batch operation to insert all above data Contains the output of the app of a
                // ContentProviderOperation.
                //It is sure to have exactly one of uri or count set
                ContentProviderResult[] contentProviderResults = mApplicationContext.getContentResolver()
                        .applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
                Log.d(TAG, "Contact write for " + contact.getContactName() + " success.");
                successFullWriteCount++;
            } catch (RemoteException exp) {
                //logs;
                Log.e(TAG, "write of " + contact.getContactName() + " failed. :" + exp.getMessage());
            } catch (OperationApplicationException exp) {
                //logs
                Log.e(TAG, "write of " + contact.getContactName() + " failed. :" + exp.getMessage());
            }
        }
        return successFullWriteCount;
    }
}
