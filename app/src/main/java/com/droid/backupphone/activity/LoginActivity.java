package com.droid.backupphone.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.droid.backupphone.R;
import com.droid.backupphone.common.CommonConstants;
import com.droid.backupphone.helper.LoginHelper;
import com.droid.backupphone.model.User;
import com.droid.backupphone.util.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivityTag";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private int mClickedButtonId = -1;
    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            //String value = dataSnapshot.getValue(String.class);
            //Log.d(TAG, "Value is: " + value);
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                String response = dataSnapshot.getValue().toString();
                Log.d("ValueEventListener", "Key : " + dataSnapshot.getKey());
                Log.d("ValueEventListener", "Response : " + response);


                if ("contacts".equals(dataSnapshot.getKey()) && dataSnapshot.getChildren() != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.d("ValueEventListener", "Key : " + postSnapshot.getKey());
                        //String response1 = postSnapshot.getValue().toString();
                        //Log.d("ValueEventListener", "Response : " + response1);

                        for (DataSnapshot postSnapshot1 : postSnapshot.getChildren()) {
                            User user = postSnapshot1.getValue(User.class);
                            if (user != null) {
                                Log.d("ValueEventListener", "user : " + user.getUsername() + "," + user.getEmail());
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("ChildEventListener", "onChildAdded:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("ChildEventListener", "onChildChanged:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d("ChildEventListener", "onChildRemoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("ChildEventListener", "onChildMoved:" + dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        Button emailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button emailLogInButton = (Button) findViewById(R.id.create_account_button);
        emailSignInButton.setOnClickListener(this);
        emailLogInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        setAuthListener();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        LoginHelper.setUpDataBase(mDatabase, valueEventListener, childEventListener);
    }

    private void setAuthListener() {
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:");
                    Log.d(TAG, "User Id : " + user.getUid());
                    Log.d(TAG, "User Name : " + user.getDisplayName());
                    Log.d(TAG, "User Email : " + user.getEmail());

                    switch (mClickedButtonId) {
                        case R.id.email_sign_in_button:
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra(CommonConstants.USER_EMAIL, user.getEmail());
                            startActivity(intent);
                            break;
                        case R.id.create_account_button:
                            Snackbar snackbar = Snackbar.make(mLoginFormView, R.string.signup_success,
                                    Snackbar.LENGTH_LONG);
                            snackbar.show();
                            CommonUtils.signOutFromApp();
                            break;
                    }
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

        if (mDatabase != null) {
            if (valueEventListener != null) {
                mDatabase.removeEventListener(valueEventListener);
            }

            if (childEventListener != null) {
                mDatabase.removeEventListener(childEventListener);
            }
        }
    }

    @Override
    public void onClick(View view) {
        CommonUtils.hideSoftKeyboard(LoginActivity.this, view, 0);
        mClickedButtonId = view.getId();
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
                break;
            case R.id.create_account_button:
                createAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());
                break;
        }
    }

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
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                        }

                        showProgress(false);
                    }
                });
    }

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
                            final Snackbar snackbar = Snackbar.make(mLoginFormView, R.string.signup_failed,
                                    Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            snackbar.setAction(R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                        }

                        showProgress(false);
                    }
                });
    }

    private boolean validateForm() {

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

    private void clearCredentials() {
        // mEmailView.setText("");
        mPasswordView.setText("");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

