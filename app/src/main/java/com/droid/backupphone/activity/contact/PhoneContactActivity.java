package com.droid.backupphone.activity.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import com.droid.backupphone.R;
import com.droid.backupphone.asynctask.contact.FetchDeviceContactAsyncTask;
import com.droid.backupphone.helper.DatabaseHelper;
import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.util.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used to fetch device contacts and upload selected contact on cloud server.
 */
public class PhoneContactActivity extends BaseContactActivity {
    private static final String TAG = "PhoneContactActivity";
    private boolean isOnlyDeviceContact = true;
    private FetchDeviceContactAsyncTask mFetchDeviceContactAsyncTask = null;

    // the database reference till contact >> user+id
    private DatabaseReference mUserEndPoint = null;

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            if (dataSnapshot != null) {
                if (dataSnapshot.getKey() != null && dataSnapshot.getValue() == null) {
                    Log.d("ValueEventListener", "Account : " + dataSnapshot.getKey() + " does not exist.");
                } else if (dataSnapshot.getValue() != null) {
                    removeDatabaseListener();
                    hideProgress();
                    showConfirmationDialog(true);
                    String response = dataSnapshot.getValue().toString();
                    Log.d(TAG, "ValueEventListener : " + "Key : " + dataSnapshot.getKey());
                    Log.d(TAG, "ValueEventListener : " + "Response : " + response);
                } else {
                    showConfirmationDialog(false);
                }
            } else {
                showConfirmationDialog(false);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFabUploadDownload.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_upload_done));

        startDeviceContactTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeDatabaseListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nullifyAsyncTasks(mFetchDeviceContactAsyncTask);
    }

    // start async task to fetch the contact list from device.
    private void startDeviceContactTask() {
        mFetchDeviceContactAsyncTask = new FetchDeviceContactAsyncTask(this, isOnlyDeviceContact) {

            @Override
            protected void onPostExecute(List<Contact> contacts) {
                mLoadingProgress.setVisibility(View.GONE);
                if (CommonUtils.isCollectionNullOrEmpty(contacts)) {
                    mTvNoData.setVisibility(View.VISIBLE);
                } else {
                    showContacts(contacts);
                }
            }
        };
        mFetchDeviceContactAsyncTask.execute();
    }

    @Override
    protected void performUploadDownload(View view) {
        super.performUploadDownload(view);
        SparseBooleanArray checked = mLvContact.getCheckedItemPositions();
        List<Contact> selectedContacts = new ArrayList<Contact>();
        for (int i = 0; i < checked.size(); i++) {
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i)) {
                Contact contact = (Contact) mLvContact.getAdapter().getItem(checked.keyAt(i));
                selectedContacts.add(contact);
            }
        }

        for (int i = 0; i < selectedContacts.size(); i++) {
            Log.d(TAG, "Selected contact : - " + selectedContacts.get(i).getContactName());
        }

        if (CommonUtils.isCollectionNullOrEmpty(selectedContacts)) {
            Snackbar.make(view, R.string.select_atleast_one, Snackbar.LENGTH_SHORT).show();
        } else {
            uploadContactsToCloud(selectedContacts);
        }
    }

    // method performs operation on cloud server to upload selected contacts
    private void uploadContactsToCloud(List<Contact> selectedContacts) {
        String userId = CommonUtils.getUserId(PhoneContactActivity.this, getApplicationContext());
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserEndPoint(userId);
            DatabaseHelper.writeNewContact(mUserEndPoint, selectedContacts);
            addDatabaseListener();
            showProgress();
        }
    }

    // add database listener
    private void addDatabaseListener() {
        mUserEndPoint.addValueEventListener(valueEventListener);
    }

    // remove database listener
    private void removeDatabaseListener() {
        if (mUserEndPoint != null) {
            if (valueEventListener != null) {
                mUserEndPoint.removeEventListener(valueEventListener);
            }
        }
    }

    // show confirmation dialog, whether contacts uploaded on cloud server successfully or not
    private void showConfirmationDialog(boolean uploadSuccessful) {
        AlertDialog.Builder uploadAlertDialog = new AlertDialog.Builder(this);
        if (uploadSuccessful) {
            uploadAlertDialog.setMessage(R.string.contact_upload_success);
        } else {
            uploadAlertDialog.setMessage(R.string.err_contact_upload_fail);
        }
        uploadAlertDialog.setCancelable(false);
        uploadAlertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneContactActivity.this.finish();
            }
        });
        uploadAlertDialog.create().show();
    }
}
