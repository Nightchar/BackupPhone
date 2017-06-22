package com.droid.backupphone.activity.Setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.droid.backupphone.Fragment.SettingFragment;
import com.droid.backupphone.Fragment.UpdatePasswordFramment;
import com.droid.backupphone.R;
import com.droid.backupphone.activity.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by root on 19/6/17.
 */
public class SettingActivity extends BaseActivity {


    FragmentTransaction ft;
    FragmentManager mFragmentManager ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = this.getSupportFragmentManager();

        ft = mFragmentManager.beginTransaction();

        ft.add(R.id.setting_container,new SettingFragment(),"main")
//        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .commit();


    }

    public void updatePasswordFragment()
    {

        mFragmentManager.beginTransaction().replace(R.id.setting_container, new UpdatePasswordFramment(), "update Passwod")
                .addToBackStack("update Passwod")
                .commit();
    }


    public void updateEmailFragment()
    {

        mFragmentManager.beginTransaction().replace(R.id.setting_container, new UpdatePasswordFramment(), "update Email")
                .addToBackStack("update Email")
                .commit();
    }

    @Override
    public void onBackPressed() {

        int count = mFragmentManager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            mFragmentManager.popBackStack();
        }

    }
}
