package com.droid.backupphone.activity.contact;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.droid.backupphone.R;

/**
 * Created by nikhil1804 on 25-04-2017.
 */

public class BaseContactActivity extends AppCompatActivity {

    protected FloatingActionButton mFabUploadDownload = null;
    protected ListView mLvContact = null;
    protected TextView mTvNoData = null;
    protected View mLoadingProgress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFabUploadDownload = (FloatingActionButton) findViewById(R.id.fab_upload_download);
        mFabUploadDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performUploadDownload(view);
            }
        });

        mLvContact = (ListView) findViewById(R.id.lv_contact);
        mTvNoData = (TextView) findViewById(R.id.tv_no_data);
        mLoadingProgress = findViewById(R.id.view_contact_progress);
    }

    protected void performUploadDownload(View view) {
        // do nothing
    }

    protected void showProgress() {
        mFabUploadDownload.setVisibility(View.GONE);
        mLvContact.setVisibility(View.GONE);
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    protected void hideProgress() {
        mFabUploadDownload.setVisibility(View.VISIBLE);
        mLvContact.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.GONE);
    }
}
