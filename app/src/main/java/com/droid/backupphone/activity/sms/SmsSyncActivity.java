package com.droid.backupphone.activity.sms;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.droid.backupphone.R;
import com.droid.backupphone.activity.BaseActivity;
import com.droid.backupphone.activity.contact.CloudContactActivity;
import com.droid.backupphone.activity.contact.ContactSyncActivity;
import com.droid.backupphone.activity.contact.PhoneContactActivity;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static com.droid.backupphone.common.CommonConstants.REQUEST_READ_CONTACT;
import static com.droid.backupphone.common.CommonConstants.REQUEST_READ_SMS;

/**
 * Created by root on 18/6/17.
 */
public class SmsSyncActivity extends BaseActivity implements View.OnClickListener{

    private int mClickedButtonId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btn_phone_sms).setOnClickListener(this);
        findViewById(R.id.btn_cloud_sms).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_phone_sms:
            case R.id.btn_cloud_sms:
                mClickedButtonId = view.getId();
                if (mayRequestContacts()) {
                    openNextScreen();
                }
                break;
            default:
                break;
        }
    }

    // check for contact permission
    private boolean mayRequestContacts() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        requestPermissions(new String[]{READ_SMS}, REQUEST_READ_SMS);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_SMS) {
            if (grantResults.length > 0 && READ_SMS.equals(permissions[0])
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openNextScreen();
            }
        }
    }

    // open next screen based on clicked button id.
    protected void openNextScreen() {
        switch (mClickedButtonId) {
            case R.id.btn_phone_sms:
                Intent phoneContactIntent = new Intent(SmsSyncActivity.this, PhoneSmsActivity.class);
                startActivity(phoneContactIntent);
                mClickedButtonId = -1;
                break;
            case R.id.btn_cloud_sms:
                Intent cloudContactIntent = new Intent(SmsSyncActivity.this, CloudSmsActivity.class);
                startActivity(cloudContactIntent);
                mClickedButtonId = -1;
            default:
                break;
        }
    }
}
