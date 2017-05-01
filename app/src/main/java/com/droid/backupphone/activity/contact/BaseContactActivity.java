package com.droid.backupphone.activity.contact;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.droid.backupphone.R;
import com.droid.backupphone.activity.BaseActivity;
import com.droid.backupphone.adapter.MultiSelectListAdapter;
import com.droid.backupphone.model.contact.Contact;

import java.util.List;

/**
 * The base class for {@link CloudContactActivity} and {@link PhoneContactActivity}
 */
public class BaseContactActivity extends BaseActivity {

    protected FloatingActionButton mFabUploadDownload = null;
    protected ListView mLvContact = null;
    protected TextView mTvNoData = null;
    protected View mLoadingProgress = null;
    private MultiSelectListAdapter mListAdapter = null;

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

    // the method detail is given in subclasses.
    protected void performUploadDownload(View view) {
        // do nothing
    }

    // show list of contacts in list view.
    protected void showContacts(List<Contact> contacts) {
        mLvContact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListAdapter = new MultiSelectListAdapter(this, android.R.layout.simple_list_item_multiple_choice, contacts);
        mLvContact.setAdapter(mListAdapter);
        mLvContact.setVisibility(View.VISIBLE);
        mFabUploadDownload.setVisibility(View.VISIBLE);
    }

    // show progress bar & hide other UI components
    protected void showProgress() {
        mFabUploadDownload.setVisibility(View.GONE);
        mLvContact.setVisibility(View.GONE);
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    // hide progress bar and show other UI components
    protected void hideProgress() {
        mFabUploadDownload.setVisibility(View.VISIBLE);
        mLvContact.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.GONE);
    }
}
