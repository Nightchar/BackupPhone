package com.droid.backupphone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid.backupphone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneContactFragment extends Fragment {

    public PhoneContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_contact, container, false);
    }
}
