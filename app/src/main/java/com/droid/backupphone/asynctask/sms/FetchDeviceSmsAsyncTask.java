package com.droid.backupphone.asynctask.sms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import com.droid.backupphone.model.sms.Sms;

import java.util.ArrayList;
import java.util.List;

/**
 * The async task class to fetch all the device contacts.
 */
public class FetchDeviceSmsAsyncTask extends AsyncTask<Void, Void, List<Sms>> {

    private final String TAG = "FetchDeviceSmsAsyncTask";
    private Context mContext = null;


    /**
     * The constructor.
     *
     * @param context the context
     */
    public FetchDeviceSmsAsyncTask(Context context) {
        mContext = context;

    }

    @Override
    protected List<Sms> doInBackground(Void... params) {
        // create cursor for sms
        Cursor cursor = mContext.getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
        List<Sms> smsList = getSms(cursor);
        return smsList;
    }

    // get list of sms object from cursor object
    private List<Sms> getSms(Cursor cursor) {
        List<Sms> totalSms = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Sms objSms = new Sms();
                objSms.setAddress(cursor.getString(cursor
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(cursor.getString(cursor.getColumnIndexOrThrow("body")));
                objSms.setTime(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                totalSms.add(objSms);
            } while (cursor.moveToNext());
        } else {
            throw new RuntimeException("You have no SMS");
        }
        cursor.close();

        return totalSms;


    }


}