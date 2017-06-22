package com.droid.backupphone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.backupphone.R;
import com.droid.backupphone.activity.Setting.SettingActivity;
import com.droid.backupphone.activity.contact.ContactSyncActivity;
import com.droid.backupphone.activity.sms.SmsSyncActivity;
import com.droid.backupphone.util.CommonUtils;
import com.droid.backupphone.util.PreferenceUtils;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static com.droid.backupphone.common.CommonConstants.REQUEST_READ_CONTACT;
import static com.droid.backupphone.common.CommonConstants.REQUEST_READ_SMS;

/**
 * Dashboard activity to show all possible type of backup options.
 */
public class DashboardActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvUser = (TextView) findViewById(R.id.tv_user);
        tvUser.setText(PreferenceUtils.getUserEmail(getApplicationContext()));

        findViewById(R.id.cv_contact).setOnClickListener(this);
        findViewById(R.id.cv_sms).setOnClickListener(this);
        findViewById(R.id.cv_photos).setOnClickListener(this);
        findViewById(R.id.cv_setting).setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                showLogOutDialog();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_help:
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        showLogOutDialog();
        DashboardActivity.this.finish();
    }

    // Show log out option to the user with Yes/No option.
    private void showLogOutDialog() {
        AlertDialog.Builder logoutAlertDialog = new AlertDialog.Builder(DashboardActivity.this);
        logoutAlertDialog.setMessage(R.string.logout_message);

        logoutAlertDialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.signOutFromApp();
                CommonUtils.logOut(DashboardActivity.this, getApplicationContext());
                DashboardActivity.this.finish();
            }
        });
        logoutAlertDialog.setPositiveButton(R.string.no, null);
        logoutAlertDialog.create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_contact:
                if (mayRequestContacts()) {
                    openContactSyncScreen();
                }
                break;
            case R.id.cv_sms:
                if (mayRequestSms()) {
                    openSmsSyncScreen();
                }
                break;
            case R.id.cv_photos:
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_setting:

                Intent contactIntent = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(contactIntent);
                break;
        }
    }

    // Open contact sync screen
    private void openContactSyncScreen() {
        Intent contactIntent = new Intent(DashboardActivity.this, ContactSyncActivity.class);
        startActivity(contactIntent);
    }

    // Open sms sync screen
    private void openSmsSyncScreen() {
        Intent contactIntent = new Intent(DashboardActivity.this, SmsSyncActivity.class);
        startActivity(contactIntent);
    }

    // check for contact permission
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

    private boolean mayRequestSms() {

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
        if (requestCode == REQUEST_READ_CONTACT) {
            if (grantResults.length > 0 && READ_CONTACTS.equals(permissions[0])
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContactSyncScreen();
            }
        }else if(requestCode == REQUEST_READ_SMS)
        {
            if (grantResults.length > 0 && READ_SMS.equals(permissions[0])
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContactSyncScreen();
            }
        }
    }
}
