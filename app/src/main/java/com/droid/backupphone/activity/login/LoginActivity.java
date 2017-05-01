package com.droid.backupphone.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.droid.backupphone.R;
import com.droid.backupphone.activity.DashboardActivity;
import com.droid.backupphone.common.CommonConstants;
import com.droid.backupphone.util.CommonUtils;
import com.droid.backupphone.util.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseSignInSignUpActivity {
    private static final String TAG = "LoginActivityTag";

    private View mLoginFormView;

    private View.OnClickListener mSignInBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonUtils.hideSoftKeyboard(LoginActivity.this, view, 0);
            signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.actv_email);
        mPasswordView = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_email_sign_in).setOnClickListener(mSignInBtnClickListener);


        mLoginFormView = findViewById(R.id.view_login_parent);
        mProgressView = findViewById(R.id.view_login_progress);

        mAuth = FirebaseAuth.getInstance();
        setAuthListener();

        TextView tvNoAccount = (TextView) findViewById(R.id.tv_no_account);
        Spanned sequenceCall = Html.fromHtml(CommonUtils.concatenateString(CommonConstants.UNDER_LINE_OPEN_TAG,
                getString(R.string.dont_have_account), CommonConstants.UNDER_LINE_CLOSE_TAG));
        CommonUtils.linkifyCallTextView(this, tvNoAccount, sequenceCall);
    }

    // set auth listener to listen if signIn or signUp operation get successful
    private void setAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String userId = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:");
                    Log.d(TAG, "User Id : " + userId);
                    Log.d(TAG, "User Name : " + user.getDisplayName());
                    Log.d(TAG, "User Email : " + user.getEmail());

                    if (userId != null && PreferenceUtils.saveUserId(getApplicationContext(), userId)) {
                        PreferenceUtils.saveUserEmail(getApplicationContext(), user.getEmail());
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                    showProgress(false);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO remove it after use
        mEmailView.setText("abc@gmail.com");
        mPasswordView.setText("123456");
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // perform login operation
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgress(true);
        clearCredentials();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithEmail:failed", task.getException());
                            final Snackbar snackbar = Snackbar.make(mLoginFormView, R.string.auth_failed,
                                    Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            snackbar.setAction(R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            showProgress(false);
                        }
                    }
                });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}

