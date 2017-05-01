package com.droid.backupphone.activity.contact;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.droid.backupphone.R;

import static android.Manifest.permission.READ_CONTACTS;
import static com.droid.backupphone.common.CommonConstants.REQUEST_READ_CONTACT;

public class ContactSyncActivity extends AppCompatActivity implements View.OnClickListener {

    private int mClickedButtonId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btn_phone_contact).setOnClickListener(this);
        findViewById(R.id.btn_cloud_contact).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_phone_contact:
            case R.id.btn_cloud_contact:
                mClickedButtonId = view.getId();
                if (mayRequestContacts()) {
                    openNextScreen();
                }
                break;
            default:
                break;
        }
    }

    private boolean mayRequestContacts() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACT);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACT) {
            if (grantResults.length > 0 && READ_CONTACTS.equals(permissions[0])
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openNextScreen();
            }
        }
    }

    protected void openNextScreen() {
        switch (mClickedButtonId) {
            case R.id.btn_phone_contact:
                Intent phoneContactIntent = new Intent(ContactSyncActivity.this, PhoneContactActivity.class);
                startActivity(phoneContactIntent);
                mClickedButtonId = -1;
                break;
            case R.id.btn_cloud_contact:
                Intent cloudContactIntent = new Intent(ContactSyncActivity.this, CloudContactActivity.class);
                startActivity(cloudContactIntent);
                mClickedButtonId = -1;
            default:
                break;
        }
    }
}
