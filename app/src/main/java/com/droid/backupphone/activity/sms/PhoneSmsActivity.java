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
import com.droid.backupphone.asynctask.sms.FetchDeviceSmsAsyncTask;
import com.droid.backupphone.helper.DatabaseHelper;
import com.droid.backupphone.model.contact.Contact;
import com.droid.backupphone.model.sms.Sms;
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
public class PhoneSmsActivity extends BaseSmsActivity {
    private static final String TAG = "PhoneSmsActivity";

    private FetchDeviceSmsAsyncTask mFetchDeviceContactAsyncTask = null;

    // the database reference till contact >> user+id
    private DatabaseReference mUserEndPoint = null;
//
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

        startDeviceSmsTask();
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
    private void startDeviceSmsTask() {
        mFetchDeviceContactAsyncTask = new FetchDeviceSmsAsyncTask(this) {

            @Override
            protected void onPostExecute(List<Sms> smsList) {
                mLoadingProgress.setVisibility(View.GONE);
                if (CommonUtils.isCollectionNullOrEmpty(smsList)) {
                    mTvNoData.setVisibility(View.VISIBLE);
                } else {
                    showSms(smsList);
                }
            }
        };
        mFetchDeviceContactAsyncTask.execute();
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
        } else {
//            uploadSmsToCloud(selectedSms);
        }

    }

    // method performs operation on cloud server to upload selected contacts
    private void uploadSmsToCloud(List<Sms> selectedSms) {
        String userId = CommonUtils.getUserId(PhoneSmsActivity.this, getApplicationContext());
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserSmsEndPoint(userId);
            DatabaseHelper.writeNewSms(mUserEndPoint, selectedSms);
            addDatabaseListener();
            showProgress();
        }
    }

//     add database listener
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

    // show confirmation dialog, whether Sms uploaded on cloud server successfully or not
    private void showConfirmationDialog(boolean uploadSuccessful) {
        AlertDialog.Builder uploadAlertDialog = new AlertDialog.Builder(this);
        if (uploadSuccessful) {
            uploadAlertDialog.setMessage(R.string.sms_upload_success);
        } else {
            uploadAlertDialog.setMessage(R.string.err_sms_upload_fail);
        }
        uploadAlertDialog.setCancelable(false);
        uploadAlertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneSmsActivity.this.finish();
            }
        });
        uploadAlertDialog.create().show();
    }
}
