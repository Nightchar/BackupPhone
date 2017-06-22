package com.droid.backupphone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.droid.backupphone.R;
import com.droid.backupphone.model.sms.Sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The list adapter for multi select ListView.
 */
public class MultiSelectSmsListAdapter extends BaseAdapter {
    public boolean[] checkedHolder;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Sms> mSmsList;
    private boolean contactCheckedBox = false;


    public MultiSelectSmsListAdapter(Context context, List<Sms> contactList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mSmsList = contactList;

        createCheckedHolder();
    }

    @Override
    public int getCount() {
        return mSmsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSmsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        final ViewHolder holder;

//        int listPosition=((ListView)parent).getPositionForView(view);


        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.content_sms, null);
            holder.sms_cb = (CheckBox) view.findViewById(R.id.cb_sms);

            holder.sms_address = (TextView) view.findViewById(R.id.tv_sms_address);
            holder.sms_date = (TextView) view.findViewById(R.id.tv_sms_date);
            holder.sms_message = (TextView) view.findViewById(R.id.tv_sms_message);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        Sms sms = mSmsList.get(position);

        holder.sms_address.setText(sms.getAddress());
        holder.sms_date.setText("Date : " +smsDateFormated(Long.parseLong(sms.getTime())));
        holder.sms_message.setText(sms.getMsg());


        holder.sms_cb.setChecked(contactCheckedBox);


        final int pos = position;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean currentStatus = checkedHolder[position];
                Log.d("check", "on view click : " + position + currentStatus);

                if (currentStatus) {
                    setCheckHolder(position, false);
                    final ViewHolder tempHolder = (ViewHolder) v.getTag();
                    tempHolder.sms_cb.setChecked(false);
                    v.invalidate();
                } else {
                    setCheckHolder(position, true);
                    final ViewHolder tempHolder = (ViewHolder) v.getTag();
                    tempHolder.sms_cb.setChecked(true);
                    v.invalidate();
                }

            }
        });
        holder.sms_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setCheckHolder(position, isChecked);
            }
        });


        return view;
    }

    public String smsDateFormated(Long currentDate)
    {
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(currentDate));
        return dateString;
    }

    private void createCheckedHolder() {
        checkedHolder = new boolean[mSmsList.size()];
        for (int i = 0; i < checkedHolder.length; i++) {
            setCheckHolder(i, false);
        }

    }

    public void setCheckHolder(int position, boolean currentStatus) {
        checkedHolder[position] = currentStatus;
    }

    public boolean[] getCheckedHolder() {
        return checkedHolder;
    }

    public void updateCheckBox(boolean currentStatus) {

        contactCheckedBox = currentStatus;
        for (int i = 0; i < checkedHolder.length; i++) {
            setCheckHolder(i, currentStatus);
        }
        notifyDataSetChanged();
    }

    // The view holder class
    private static class ViewHolder {
        CheckBox sms_cb;
        TextView sms_address,sms_date, sms_message;
    }
}