package com.droid.backupphone.activity.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.droid.backupphone.R;
import com.droid.backupphone.util.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * {@link SignUpActvity} for new user signup.
 */
public class SignUpActvity extends BaseSignInSignUpActivity {

    private static final String TAG = "SignUpActvity";

    private View mSignUpFormView;

    private View.OnClickListener mSignUpBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonUtils.hideSoftKeyboard(SignUpActvity.this, view, 0);
            createAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.actv_email);
        mPasswordView = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_create_account).setOnClickListener(mSignUpBtnClickListener);

        mSignUpFormView = findViewById(R.id.view_signup_parent);
        mProgressView = findViewById(R.id.view_signup_progress);

        mAuth = FirebaseAuth.getInstance();
        setAuthListener();
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
                    Log.d(TAG, "onAuthStateChanged:signed_up:");
                    Log.d(TAG, "User Id : " + userId);
                    Log.d(TAG, "User Name : " + user.getDisplayName());
                    Log.d(TAG, "User Email : " + user.getEmail());

                    CommonUtils.signOutFromApp(); // after sign up perform sign out operation is mandatory
                    showProgress(false);
                    showSignUpsuccessDialog();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    // Perform signUp operation
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgress(true);
        clearCredentials();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "createUserWithEmail:failed", task.getException());
                            final Snackbar snackbar = Snackbar.make(mSignUpFormView, R.string.signup_failed,
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
        mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    // show sign up successful dialog
    private void showSignUpsuccessDialog() {
        AlertDialog.Builder signupAlertDialog = new AlertDialog.Builder(SignUpActvity.this);
        signupAlertDialog.setMessage(R.string.signup_success);
        signupAlertDialog.setCancelable(false);
        signupAlertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.signOutFromApp();
                SignUpActvity.this.finish();
            }
        });
        signupAlertDialog.create().show();
    }
}
