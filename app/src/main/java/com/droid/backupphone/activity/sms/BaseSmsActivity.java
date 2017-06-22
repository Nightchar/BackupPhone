package com.droid.backupphone.activity.sms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.droid.backupphone.R;
import com.droid.backupphone.activity.BaseActivity;
import com.droid.backupphone.activity.contact.CloudContactActivity;
import com.droid.backupphone.activity.contact.PhoneContactActivity;
import com.droid.backupphone.adapter.MultiSelectSmsListAdapter;
import com.droid.backupphone.model.sms.Sms;

import java.util.List;

/**
 * The base class for {@link CloudContactActivity} and {@link PhoneContactActivity}
 */
public class BaseSmsActivity extends BaseActivity {

    protected FloatingActionButton mFabUploadDownload = null;
    protected ListView mLvSms = null;
    protected TextView mTvNoData = null;
    protected View mLoadingProgress = null;
    protected MultiSelectSmsListAdapter mListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFabUploadDownload = (FloatingActionButton) findViewById(R.id.fab_upload_download);
        mFabUploadDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performUploadDownload(view);
            }
        });

        mLvSms = (ListView) findViewById(R.id.lv_sms);
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
                selectAllSms(item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // select / deselect all contacts in listview.
    private void selectAllSms(boolean checked) {
        mListAdapter.updateCheckBox(checked);
    }

    // the method detail is given in subclasses.
    protected void performUploadDownload(View view) {
        // do nothing
    }

    // show list of contacts in list view.
    protected void showSms(List<Sms> contacts) {
        mLvSms.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListAdapter = new MultiSelectSmsListAdapter(this,  contacts);
        mLvSms.setAdapter(mListAdapter);

        mLvSms.setVisibility(View.VISIBLE);
        mFabUploadDownload.setVisibility(View.VISIBLE);
    }

    // show progress bar & hide other UI components
    protected void showProgress() {
        mFabUploadDownload.setVisibility(View.GONE);
        mLvSms.setVisibility(View.GONE);
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    // hide progress bar and show other UI components
    protected void hideProgress() {
        mFabUploadDownload.setVisibility(View.VISIBLE);
        mLvSms.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.GONE);
    }
}
