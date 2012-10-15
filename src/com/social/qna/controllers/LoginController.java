package com.social.qna.controllers;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kinvey.KCSClient;
import com.kinvey.KinveyUser;
import com.kinvey.exception.KinveyException;
import com.kinvey.util.KinveyCallback;
import com.kinvey.util.ScalarCallback;
import com.social.qna.application.SocialApplication;
import com.social.qna.model.UserModel;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/13/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class LoginController {

    private static final String TAG = LoginController.class.getSimpleName();

    @Inject Context context;

    private KinveyUser user;

    public void createUser(String email, String password, final String name, final String city, final ScalarCallback<KinveyUser> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();

        try {
            client.createUserWithUsername(email, password, new KinveyCallback<KinveyUser>() {
                @Override
                public void onSuccess(KinveyUser kinveyUser) {
                    kinveyUser.setAttribute(UserModel.CITY_ATTRIBUTE, city);
                    kinveyUser.setAttribute(UserModel.NAME_ATTRIBUTE, name);
                    kinveyUser.saveUser(callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        } catch (KinveyException ke) {
            Log.e(TAG, "", ke);
        }

    }


    public void login(String email, String password, final KinveyCallback<KinveyUser> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();

        client.loginWithUsername(email, password, callback);

    }

    public KinveyUser getUser() {
        return user;
    }

    public void setUser(KinveyUser user) {
        this.user = user;
    }
}
