package com.droid.backupphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.droid.backupphone.R;
import com.droid.backupphone.util.PreferenceUtils;

/**
 * The app splash screen.
 */
public class SplashActivity extends BaseActivity {

    private Handler mSplashHandler = null;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (PreferenceUtils.getUserId(getApplicationContext()) != null) {
                startNextActivity(DashboardActivity.class);
            } else {
                startNextActivity(LoginActivity.class);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay in splash
        long splashDelay = Long.parseLong(getString(R.string.splash_time_delay));

        // Delay the closing of splash screen
        mSplashHandler = new Handler();
        mSplashHandler.postDelayed(mRunnable, splashDelay);
    }

    /*
     * The method to push next activity to the top of the activity stack clearing other activities at
     * its top.
     */
    private void startNextActivity(Class<?> cls) {
        mSplashHandler.removeCallbacks(mRunnable);
        Intent intentNextActivity = new Intent(SplashActivity.this, cls);
        intentNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentNextActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Splash screen need not be destroyed using this key
    }
}
