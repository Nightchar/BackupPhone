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
import com.droid.backupphone.activity.contact.ContactSyncActivity;
import com.droid.backupphone.util.CommonUtils;
import com.droid.backupphone.util.PreferenceUtils;

import static android.Manifest.permission.READ_CONTACTS;
import static com.droid.backupphone.common.CommonConstants.REQUEST_READ_CONTACT;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvUser = (TextView) findViewById(R.id.tv_user);
        tvUser.setText(PreferenceUtils.getUserEmail(getApplicationContext()));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        findViewById(R.id.cv_contact).setOnClickListener(this);
        findViewById(R.id.cv_sms).setOnClickListener(this);
        findViewById(R.id.cv_photos).setOnClickListener(this);
        findViewById(R.id.cv_videos).setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        showLogOutDialog();
    }

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
//                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                if (mayRequestContacts()) {
                    openContactSyncScreen();
                }
                break;
            case R.id.cv_sms:
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_photos:
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_videos:
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void openContactSyncScreen() {
        Intent contactIntent = new Intent(DashboardActivity.this, ContactSyncActivity.class);
        startActivity(contactIntent);
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
                openContactSyncScreen();
            }
        }
    }
}
