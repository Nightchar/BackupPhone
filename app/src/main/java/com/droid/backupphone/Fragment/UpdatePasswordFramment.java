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
import com.droid.backupphone.util.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 21/6/17.
 */
public class UpdatePasswordFramment extends Fragment implements View.OnClickListener {


    View updatePasswordView;
    TextView tv_currentUserName;
    AutoCompleteTextView actv_newpassword ;
    Button btn_udpatePassword;

    FirebaseAuth firebaseAuth ;
    protected View mProgressView;
    private View mLoginFormView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        updatePasswordView =inflater.inflate(R.layout.password_update_view,container,false);

        tv_currentUserName = (TextView)updatePasswordView.findViewById(R.id.tv_current_user_name);
        actv_newpassword = (AutoCompleteTextView)updatePasswordView.findViewById(R.id.actv_newpassword);
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
                updatePassword();
                break;
        }
    }

    public void updatePassword()
    {
        if (!validatePassword()) {
            return;
        }

        showProgress(true);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        user.updatePassword(actv_newpassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Password is updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update password!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    }
                });
    }

    protected boolean validatePassword() {

        // Reset errors.

        actv_newpassword.setError(null);

        // Store values at the time of the login attempt.
        String password = actv_newpassword.getText().toString();

        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!LoginHelper.isPasswordValid(password)) {
            actv_newpassword.setError(getString(R.string.error_invalid_password));
            focusView = actv_newpassword;
            valid = false;
        }

        if (!valid) {
            focusView.requestFocus();
        }
        return valid;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
