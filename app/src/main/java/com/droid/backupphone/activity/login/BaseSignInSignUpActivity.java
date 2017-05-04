package com.droid.backupphone.activity.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.droid.backupphone.R;
import com.droid.backupphone.activity.BaseActivity;
import com.droid.backupphone.helper.LoginHelper;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The common activity class for {@link LoginActivity} and {@link SignUpActvity}.
 */
public class BaseSignInSignUpActivity extends BaseActivity {

    // UI references.
    protected AutoCompleteTextView mEmailView;
    protected EditText mPasswordView;
    protected View mProgressView;

    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // validate form fields
    protected boolean validateForm() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!LoginHelper.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            valid = false;
        }

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

    // clear password
    protected void clearCredentials() {
        mPasswordView.setText(R.string.empty);
    }
}
