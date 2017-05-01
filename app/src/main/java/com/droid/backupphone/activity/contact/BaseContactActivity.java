package com.droid.backupphone.activity.contact;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        final MenuItem selectAll = menu.findItem(R.id.action_select_all);
        CheckBox cbSelectAll = (CheckBox) selectAll.getActionView().findViewById(R.id.action_item_checkbox);
        if (cbSelectAll != null) {
            // Set the text to match the item.
            //cbSelectAll.setText(selectAll.getTitle());

            // Add the onClickListener because the CheckBox doesn't automatically trigger onOptionsItemSelected.
            cbSelectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // update the menu item checked state based on checkbox checked state
                    onOptionsItemSelected(selectAll.setChecked(((CheckBox) v).isChecked()));
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_all:
                selectAllContact(item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // select / deselect all contacts in listview.
    private void selectAllContact(boolean checked) {
        if (mLvContact.getVisibility() == View.VISIBLE && mLvContact.getCount() > 0) {
            for (int i = 0; i < mLvContact.getChildCount(); i++) {
                mLvContact.setItemChecked(i, checked);
            }
        }
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
