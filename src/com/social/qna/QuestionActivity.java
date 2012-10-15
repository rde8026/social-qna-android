package com.social.qna;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import com.google.inject.Inject;
import com.social.qna.controllers.BusController;
import com.social.qna.controllers.LoginController;
import com.social.qna.events.AllQuestionsEvent;
import com.social.qna.events.LogoutEvent;
import com.social.qna.events.NewQuestionEvent;
import com.social.qna.events.QuestionCreatedEvent;
import com.social.qna.fragments.AllQuestionsFragment;
import com.social.qna.fragments.QuestionCreateFragment;
import com.social.qna.fragments.QuestionListFragment;
import com.social.qna.robolock.RoboLockFragmentActivity;
import com.squareup.otto.Subscribe;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 11:21 AM
 */
public class QuestionActivity extends RoboLockFragmentActivity {


    private static final String TAG = QuestionActivity.class.getSimpleName();

    @Inject BusController busController;
    @Inject LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Fragment listFragment = new QuestionListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.questionFragRoot, listFragment).commit();

    }

    @Subscribe
    public void addQuestionEvent(NewQuestionEvent newQuestionEvent) {
        QuestionCreateFragment askQuestion = new QuestionCreateFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.questionFragRoot, askQuestion).addToBackStack(null).commit();
    }

    @Subscribe
    public void questionCreatedEvent(QuestionCreatedEvent questionCreatedEvent) {
        getSupportFragmentManager().popBackStack();
    }

    @Subscribe
    public void allQuestionsEvent(AllQuestionsEvent allQuestionsEvent) {
        AllQuestionsFragment fragment = new AllQuestionsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.questionFragRoot, fragment).addToBackStack(null).commit();
    }

    @Subscribe
    public void logoutEvent(LogoutEvent logoutEvent) {
        loginController.setUser(null);
        finish();
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
