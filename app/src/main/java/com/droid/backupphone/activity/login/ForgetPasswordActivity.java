package com.droid.backupphone.activity.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.droid.backupphone.R;
import com.droid.backupphone.helper.LoginHelper;
import com.droid.backupphone.util.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by root on 18/6/17.
 */
public class ForgetPasswordActivity extends BaseSignInSignUpActivity {

    private static final String TAG = ForgetPasswordActivity.class.getName();
    private View mLoginFormView;

    private Button mResetPassword;
    private View.OnClickListener mForgetPassword = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonUtils.hideSoftKeyboard(ForgetPasswordActivity.this, view, 0);
            forgetpassword(mEmailView.getText().toString());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_view);


        mEmailView = (AutoCompleteTextView) findViewById(R.id.actv_email);
        mLoginFormView = findViewById(R.id.view_forget_parent);
        mProgressView = findViewById(R.id.view_reset_progress);
        findViewById(R.id.btn_reset_password).setOnClickListener(mForgetPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    protected boolean validateEmail() {

        // Reset errors.
        mEmailView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();


        boolean valid = true;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            valid = false;
        } else if (!LoginHelper.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            valid = false;
        }

        if (!valid) {
            focusView.requestFocus();
        }
        return valid;
    }

    private void forgetpassword(String email) {
        Log.d(TAG, "signIn:" + email);
        if (!validateEmail()) {
            return;
        }
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                        showProgress(false);

                    }
                });
        showProgress(true);

    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
