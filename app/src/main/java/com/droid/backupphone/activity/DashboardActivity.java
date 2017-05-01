package com.droid.backupphone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.backupphone.R;
import com.droid.backupphone.common.CommonConstants;
import com.droid.backupphone.util.CommonUtils;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            TextView tvUser = (TextView) findViewById(R.id.tv_user);
            tvUser.setText(intent.getStringExtra(CommonConstants.USER_EMAIL));
        }

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
                Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show();
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
}
