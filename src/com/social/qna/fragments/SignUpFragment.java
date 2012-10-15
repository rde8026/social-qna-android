package com.social.qna.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import com.kinvey.KinveyUser;
import com.kinvey.util.ScalarCallback;
import com.social.qna.R;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.events.LoginComplete;
import com.social.qna.robolock.fragment.RoboLockFragment;
import roboguice.inject.InjectView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/14/12
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class SignUpFragment extends RoboLockFragment {

    @Inject BusController busController;
    @Inject LoginController loginController;

    @InjectView(R.id.edtEmail) private EditText email;
    @InjectView(R.id.edtPassword) private EditText password;
    @InjectView(R.id.edtPasswordConfirm) private EditText passwordConfirm;
    @InjectView(R.id.edtName) private EditText name;
    @InjectView(R.id.edtCity) private EditText city;
    @InjectView(R.id.btnSignUp) private Button signUp;

    private ProgressDialog pd = null;

    private static final String TAG = SignUpFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        busController.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        busController.getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pd = new ProgressDialog(getActivity());
        pd.setMessage(getResources().getString(R.string.create_user_loading));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        signUp.setOnClickListener(signUpListener);
    }

    private View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<String> errors = validate();
            if (errors.isEmpty()) {

                pd.show();
                loginController.createUser(email.getText().toString(), password.getText().toString(), name.getText().toString(), city.getText().toString(), createCallback);

            } else {
                displayError(getResources().getString(R.string.error_msg_title), flattenErrors(errors), errorListener);
            }
        }
    };

    private ScalarCallback<KinveyUser> createCallback = new ScalarCallback<KinveyUser>() {
        @Override
        public void onSuccess(final KinveyUser kinveyUser) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    busController.getBus().post(new LoginComplete(kinveyUser));
                }
            });
        }

        @Override
        public void onFailure(final Throwable throwable) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    displayError(getResources().getString(R.string.error_msg_title), throwable.getMessage(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            });
        }
    };

    private ArrayList<String> validate() {
        ArrayList<String> errors = new ArrayList<String>();
        String pass = password.getText().toString();
        String confirm = passwordConfirm.getText().toString();

        if (pass.length() == 0) {
            errors.add(getResources().getString(R.string.missing_password_error));
        }

        if (pass != null && confirm != null) {
            if (!pass.equals(confirm)) {
                errors.add(getResources().getString(R.string.mis_match_password_error));
            }
        }

        if (name.getText().length() == 0) {
            errors.add(getResources().getString(R.string.missing_name_error));
        }

        return errors;
    }

    private String flattenErrors(ArrayList<String> errors) {
        StringBuilder sb = new StringBuilder();

        for (String error : errors) {
            sb.append(error + "\n");
        }

        return sb.toString();
    }

    private DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

}
