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
import com.kinvey.util.KinveyCallback;
import com.social.qna.R;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.events.LoginComplete;
import com.social.qna.events.SignUpEvent;
import com.social.qna.robolock.fragment.RoboLockFragment;
import com.social.qna.util.KeyboardUtil;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 5:13 PM
 */
public class LoginFragment extends RoboLockFragment {

    @Inject BusController busController;
    @InjectView(R.id.edtEmail) private EditText email;
    @InjectView(R.id.edtPassword) private EditText password;
    @InjectView(R.id.btnLogin) private Button btnLogin;
    @InjectView(R.id.btnSignUp) private Button btnSignUp;

    @Inject LoginController loginController;

    private static final String TAG = LoginFragment.class.getSimpleName();
    private ProgressDialog pd = null;

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
        return inflater.inflate(R.layout.fragment_login, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        btnLogin.setOnClickListener(logInListener);
        btnSignUp.setOnClickListener(signUpListener);
        pd = new ProgressDialog(getActivity());
        pd.setMessage(getResources().getString(R.string.login_loading));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

    }

    private View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            busController.getBus().post(new SignUpEvent());
        }
    };

    private View.OnClickListener logInListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pd.show();
            KeyboardUtil.dismissKeyboard(btnLogin);
            loginController.login(email.getText().toString(), password.getText().toString(), new KinveyCallback<KinveyUser>() {
                @Override
                public void onSuccess(KinveyUser kinveyUser) {
                    pd.dismiss();
                    loginController.setUser(kinveyUser);
                    busController.getBus().post(new LoginComplete(kinveyUser));
                }

                @Override
                public void onFailure(Throwable throwable) {
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

}
