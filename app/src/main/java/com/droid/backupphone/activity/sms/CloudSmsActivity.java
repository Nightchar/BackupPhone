package com.droid.backupphone.activity.sms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import com.droid.backupphone.R;
import com.droid.backupphone.helper.DatabaseHelper;
import com.droid.backupphone.model.sms.Sms;
import com.droid.backupphone.util.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The activity class used to download cloud server contacts and write on device.
 */
public class CloudSmsActivity extends BaseSmsActivity {

    private static final String TAG = "CloudSmsActivity";
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
                    String response = dataSnapshot.getValue().toString();
                    Log.d(TAG, "ValueEventListener : " + "Key : " + dataSnapshot.getKey());
                    Log.d(TAG, "ValueEventListener : " + "Response : " + response);

                    if (dataSnapshot.getChildren() != null) {
                        List<Sms> contacts = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "ValueEventListener : " + "Key : " + postSnapshot.getKey());
                            Sms sms = postSnapshot.getValue(Sms.class);
                            if (sms != null) {
                                contacts.add(sms);

                            }
                        }
                        showSms(contacts);
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

    }

    // start fetching contacts from cloud server
    private void startCloudContactDownloadTask() {
        String userId = CommonUtils.getUserId(CloudSmsActivity.this, getApplicationContext());
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserSmsEndPoint(userId);
            addDatabaseListener();
            showProgress();
            // perform a sample write operation on cloud and you will get the data in listener
            CommonUtils.getDatabaseReference().child("app_title").setValue("Realtime Database1");
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

    @Override
    protected void performUploadDownload(View view) {
        super.performUploadDownload(view);

        List<Sms> selectedSms = new ArrayList<Sms>();
        selectedSms.clear();
        boolean selectedContact_cb[] = mListAdapter.getCheckedHolder();

        for (int i = 0; i < mListAdapter.getCount(); i++) {


            if (selectedContact_cb[i]) {

                Log.d("check","check" + selectedContact_cb[i] + " : " + i);
                Sms contact = (Sms) mLvSms.getAdapter().getItem(i);
                selectedSms.add(contact);
            }
        }

        if (CommonUtils.isCollectionNullOrEmpty(selectedSms)) {
            Snackbar.make(view, R.string.select_atleast_one, Snackbar.LENGTH_SHORT).show();
        }
    }

    // start async task to write selected cloud contacts in device


    // show confirmation dialog, whether contacts written on device successfully or not
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
                CloudSmsActivity.this.finish();
            }
        });
        uploadAlertDialog.create().show();
    }
}
