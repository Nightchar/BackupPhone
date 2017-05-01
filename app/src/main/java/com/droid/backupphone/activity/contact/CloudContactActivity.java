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
import com.droid.backupphone.asynctask.contact.WriteDeviceContactAsyncTask;
import com.droid.backupphone.helper.PhoneContactActivityHelper;
import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.util.CommonUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CloudContactActivity extends BaseContactActivity {

    private static final String TAG = "CloudContactActivity";
    // the database reference till contact >> user+id
    private DatabaseReference mUserEndPoint = null;
    private WriteDeviceContactAsyncTask mWriteDeviceContactAsyncTask = null;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "ChildEventListener : " + "onChildAdded:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "ChildEventListener : " + "onChildChanged:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG, "ChildEventListener : " + "onChildRemoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d(TAG, "ChildEventListener : " + "onChildMoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
                    String response = dataSnapshot.getValue().toString();
                    Log.d(TAG, "ValueEventListener : " + "Key : " + dataSnapshot.getKey());
                    Log.d(TAG, "ValueEventListener : " + "Response : " + response);

                    // TODO parse this data to Contact list and call showContacts() method
                    if (dataSnapshot.getChildren() != null) {
                        List<Contact> contacts = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "ValueEventListener : " + "Key : " + postSnapshot.getKey());
                            Contact contact = postSnapshot.getValue(Contact.class);
                            if (contact != null) {
                                contacts.add(contact);
                                Log.d(TAG, "ValueEventListener : " + "contact : " + contact.getContactName() + "," +
                                        contact.getId());
                            }
                        }
                        showContacts(contacts);
                    }
                }
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
        mFabUploadDownload.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_download_done));

        startCloudContactDownloadTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeDatabaseListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nullifyAsyncTasks(mWriteDeviceContactAsyncTask);
    }

    private void startCloudContactDownloadTask() {
        String userId = CommonUtils.getUserId(CloudContactActivity.this, getApplicationContext());
        if (userId != null) {
            mUserEndPoint = PhoneContactActivityHelper.getUserEndPoint(userId);
            addDatabaseListener();
            showProgress();
            // perform a sample write operation on cloud and you will get the data in listener
            CommonUtils.getDatabaseReference().child("app_title").setValue("Realtime Database1");
        }
    }

    private void addDatabaseListener() {
        mUserEndPoint.addValueEventListener(valueEventListener);
        mUserEndPoint.addChildEventListener(childEventListener);
    }

    private void removeDatabaseListener() {
        if (mUserEndPoint != null) {
            if (valueEventListener != null) {
                mUserEndPoint.removeEventListener(valueEventListener);
            }

            if (childEventListener != null) {
                mUserEndPoint.removeEventListener(childEventListener);
            }
        }
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
            writeContactsInDevice(selectedContacts);
        }
    }

    private void writeContactsInDevice(final List<Contact> selectedContacts) {
        mWriteDeviceContactAsyncTask = new WriteDeviceContactAsyncTask(getApplicationContext(), selectedContacts) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected void onPostExecute(Integer successFullWriteCount) {
                hideProgress();
                showConfirmationDialog(selectedContacts.size() == successFullWriteCount);
            }
        };
        mWriteDeviceContactAsyncTask.execute();
    }

    private void showConfirmationDialog(boolean uploadSuccessful) {
        AlertDialog.Builder uploadAlertDialog = new AlertDialog.Builder(this);
        if (uploadSuccessful) {
            uploadAlertDialog.setMessage(R.string.contact_write_success);
        } else {
            uploadAlertDialog.setMessage(R.string.contact_write_partially_success);
        }
        uploadAlertDialog.setCancelable(false);
        uploadAlertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CloudContactActivity.this.finish();
            }
        });
        uploadAlertDialog.create().show();
    }
}
