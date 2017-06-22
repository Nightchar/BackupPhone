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
import com.droid.backupphone.model.contact.Contact;

import java.util.List;

/**
 * The list adapter for multi select ListView.
 */
public class MultiSelectContactListAdapter extends BaseAdapter {
    public boolean[] checkedHolder;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Contact> mContactList;


    public MultiSelectContactListAdapter(Context context, List<Contact> contactList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mContactList = contactList;

        createCheckedHolder();
    }
    private void createCheckedHolder() {
        checkedHolder = new boolean[mContactList.size()];
        for (int i = 0; i < checkedHolder.length; i++) {
            setCheckHolder(i, false);
        }

    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
       final ViewHolder holder;



        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.content_contact, null);
            holder.contact_cb = (CheckBox) view.findViewById(R.id.cb_contact);

            holder.contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            holder.contact_number = (TextView) view.findViewById(R.id.tv_contact_number);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        final Contact contact = mContactList.get(position);

        holder.contact_name.setText(contact.getContactName());
        holder.contact_number.setText(contact.getPhoneList().get(0).getPhoneNumber());
        holder.contact_cb.setChecked(checkedHolder[position]);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check","on listener " + " : "  + checkedHolder[position]);
                setCheckHolder(position, !checkedHolder[position]);
                ((ViewHolder) v.getTag()).contact_cb.setChecked(checkedHolder[position]);
                v.invalidate();
            }
        });

        holder.contact_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent();
                parent.performClick();
            }
        });
        return view;
    }



    public void setCheckHolder(int position, boolean currentStatus) {

        checkedHolder[position] = currentStatus;
    }

    public void setClickedHolder(int position, boolean currentStatus) {


        checkedHolder[position] = currentStatus;
        notifyDataSetChanged();
    }

    public boolean[] getCheckedHolder() {
        return checkedHolder;
    }

    public void updateCheckBox(boolean currentStatus) {


        for (int i = 0; i < checkedHolder.length; i++) {
            setCheckHolder(i, currentStatus);
        }
        notifyDataSetChanged();
    }

    // The view holder class
    private class ViewHolder {
        CheckBox contact_cb;
        TextView contact_name, contact_number;
    }
}