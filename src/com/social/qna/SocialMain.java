package com.social.qna;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.google.inject.Inject;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.events.LoginComplete;
import com.social.qna.events.SignUpEvent;
import com.social.qna.fragments.LoginFragment;
import com.social.qna.fragments.QuestionListFragment;
import com.social.qna.fragments.SignUpFragment;
import com.social.qna.robolock.RoboLockFragmentActivity;
import com.squareup.otto.Subscribe;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 4:23 PM
 */
public class SocialMain extends RoboLockFragmentActivity {

    @Inject BusController busController;
    @Inject LoginController loginController;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fragmentManager = getSupportFragmentManager();

        if (loginController.getUser() == null) {
            Fragment fragment = new LoginFragment();
            fragmentManager.beginTransaction().replace(R.id.fragmentRoot, fragment).commit();
        } else {
            //startQuestionFragment();
            startQuestionActivity();
        }

    }

    private void startQuestionActivity() {
        Intent questionIntent = new Intent(this, QuestionActivity.class);
        questionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        questionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(questionIntent);
        finish();
    }

    @Subscribe
    public void loginEvent(final LoginComplete loginComplete) {
        startQuestionActivity();
    }

    @Subscribe
    public void signUpEvent(final SignUpEvent signUpEvent) {
        Fragment signUpFragment = new SignUpFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentRoot, signUpFragment).addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        busController.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        busController.getBus().unregister(this);
    }

}