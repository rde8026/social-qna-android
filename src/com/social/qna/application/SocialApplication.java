package com.social.qna.application;

import android.app.Application;
import com.kinvey.KCSClient;
import com.kinvey.KinveySettings;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 5:08 PM
 */
public class SocialApplication extends Application {

    private KCSClient service;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        KinveySettings settings = KinveySettings.loadFromProperties(getApplicationContext());
        service = KCSClient.getInstance(getApplicationContext(), settings);
    }

    public KCSClient getService() {
        return service;
    }
}
