package com.droid.backupphone.activity.contact;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class CloudContactActivity extends BaseContactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFabUploadDownload.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_download_done));
    }

    @Override
    protected void performUploadDownload(View view) {
        super.performUploadDownload(view);
    }

}
