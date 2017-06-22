package com.droid.backupphone.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.droid.backupphone.R;
import com.droid.backupphone.helper.LoginHelper;
import com.droid.backupphone.util.CommonUtils;
import com.droid.backupphone.util.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 21/6/17.
 */
public class UpdateEmailFramment extends Fragment implements View.OnClickListener {


    View updatePasswordView;
    TextView tv_currentUserName;
    AutoCompleteTextView actv_newEmail;
    Button btn_udpatePassword;

    FirebaseAuth firebaseAuth ;

    protected View mProgressView;
    private View mLoginFormView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        updatePasswordView =inflater.inflate(R.layout.password_update_view,container,false);

        tv_currentUserName = (TextView)updatePasswordView.findViewById(R.id.tv_current_user_name);
        actv_newEmail = (AutoCompleteTextView)updatePasswordView.findViewById(R.id.actv_newpassword);
        btn_udpatePassword = (Button) updatePasswordView.findViewById(R.id.btn_update_password);


        mLoginFormView =(View)updatePasswordView.findViewById(R.id.view_forget_parent);
        mProgressView =(View)updatePasswordView.findViewById(R.id.view_update_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        tv_currentUserName.setText(PreferenceUtils.getUserEmail(getActivity().getApplicationContext()));

        btn_udpatePassword.setOnClickListener(this);
        return updatePasswordView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_update_password :
                CommonUtils.hideSoftKeyboard(getActivity().getApplicationContext(), v, 0);
                break;
        }
    }

    public void updateEmail(String Email)
    {
        if (!validateEmail()) {

            return;
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

        user.updateEmail(actv_newEmail.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email address is updated.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update email!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    protected boolean validateEmail() {

        // Reset errors.
        actv_newEmail.setError(null);


        // Store values at the time of the login attempt.
        String email = actv_newEmail.getText().toString();


        boolean valid = true;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            actv_newEmail.setError(getString(R.string.error_field_required));
            focusView = actv_newEmail;
            valid = false;
        } else if (!LoginHelper.isEmailValid(email)) {
            actv_newEmail.setError(getString(R.string.error_invalid_email));
            focusView = actv_newEmail;
            valid = false;
        }

        if (!valid) {
            focusView.requestFocus();
        }
        return valid;
    }
}
