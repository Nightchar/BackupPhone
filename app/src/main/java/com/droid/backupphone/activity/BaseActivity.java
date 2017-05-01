package com.droid.backupphone.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

/**
 * The Base class for all the activities in app.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Override this method to cancel any running async task and nullify it to free the memory.
     *
     * @param asyncTasks the async task to reset
     */
    protected void nullifyAsyncTasks(AsyncTask<?, ?, ?>... asyncTasks) {
        if (asyncTasks != null && asyncTasks.length > 0) {
            for (AsyncTask<?, ?, ?> asyncTask : asyncTasks) {
                if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    asyncTask.cancel(true);
                }
            }
        }
    }
}
