package com.droid.backupphone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.droid.backupphone.model.contact.Contact;

import java.util.List;

/**
 * The list adapter for multi select ListView.
 */
public class MultiSelectListAdapter extends ArrayAdapter<Contact> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Contact> mContactList;

    public MultiSelectListAdapter(Context context, int resourceId, List<Contact> contactList) {
        super(context, resourceId, contactList);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mContactList = contactList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
            holder.itemName = (CheckedTextView) view.findViewById(android.R.id.text1);
            holder.itemName.setPadding(10, 10, 10, 10);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Contact contact = mContactList.get(position);
        holder.itemName.setText(contact.getContactName() + "\n" + contact.getPhoneList().get(0).getPhoneNumber());
        return view;
    }

    // The view holder class
    private static class ViewHolder {
        CheckedTextView itemName;
    }
}