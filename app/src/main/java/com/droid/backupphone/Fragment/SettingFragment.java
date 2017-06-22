package com.droid.backupphone.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.droid.backupphone.R;
import com.droid.backupphone.activity.Setting.SettingActivity;

/**
 * Created by root on 20/6/17.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    View viewSetting;

    CardView cv_UserProfile, cv_changeUserProfileInfo, cv_passwordUpdate, cv_changeEmail, cv_deleteAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewSetting = inflater.inflate(R.layout.setting_dashboard, container, false);

        cv_UserProfile = (CardView) viewSetting.findViewById(R.id.cv_current_user_profile);
        cv_changeUserProfileInfo = (CardView) viewSetting.findViewById(R.id.cv_user_profile_update);
        cv_passwordUpdate = (CardView) viewSetting.findViewById(R.id.cv_update_password);
        cv_changeEmail = (CardView) viewSetting.findViewById(R.id.cv_change_email);
        cv_deleteAccount = (CardView) viewSetting.findViewById(R.id.cv_delete_account);


        cv_UserProfile.setOnClickListener(this);
        cv_changeUserProfileInfo.setOnClickListener(this);
        cv_passwordUpdate.setOnClickListener(this);
        cv_changeEmail.setOnClickListener(this);
        cv_deleteAccount.setOnClickListener(this);


        return viewSetting;
    }

    @Override
    public void onClick(View v) {

        int clickId = v.getId();
        switch (clickId) {
            case R.id.cv_current_user_profile:
                break;
            case R.id.cv_user_profile_update:
                break;
            case R.id.cv_update_password:

                ((SettingActivity)getActivity()).updatePasswordFragment();
                break;
            case R.id.cv_change_email:
                ((SettingActivity)getActivity()).updateEmailFragment();
                break;
            case R.id.cv_delete_account:
                break;

        }

    }
}
