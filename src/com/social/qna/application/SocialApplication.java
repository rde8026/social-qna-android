package com.social.qna.application;

import android.app.Application;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import com.kinvey.KCSClient;
import com.kinvey.KinveySettings;
import roboguice.RoboGuice;
import roboguice.inject.SharedPreferencesName;

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
        setModule(new ApplicationModule());
        init();
    }


    public void setModule(Module module) {
        RoboGuice.setBaseApplicationInjector(
                this,
                RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(this)).with(module)
        );
    }

    public static class ApplicationModule extends AbstractModule {
        @Override
        protected void configure() {
            bindConstant()
                    .annotatedWith(SharedPreferencesName.class)
                    .to("com.socialqna.perfs");
        }

        /*@Provides
        MainThread provideMainThread() {
            AndroidMainThread mainThread = new AndroidMainThread();
            return mainThread;
        }*/

    }

    private void init() {
        KinveySettings settings = KinveySettings.loadFromProperties(getApplicationContext());
        service = KCSClient.getInstance(getApplicationContext(), settings);
    }

    public KCSClient getService() {
        return service;
    }
}
