package com.social.qna.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;
import com.social.qna.R;
import com.social.qna.controllers.BusController;
import com.social.qna.robolock.fragment.RoboLockListFragment;
import roboguice.inject.InjectView;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/15/12
 * Time: 7:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuestionListFragment extends RoboLockListFragment {

    private static final String TAG = QuestionListFragment.class.getSimpleName();

    @Inject BusController busController;

    @InjectView(R.id.loaderLayout) private RelativeLayout loaderLayout;
    @InjectView(R.id.listLayout) private LinearLayout listLayout;

    @Override
    public void onResume() {
        super.onResume();
        busController.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        busController.getBus().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                loaderLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_question:
                //TODO: Show new Question Frag and deal w/ back stack.
            return true;

            default:
            return false;
        }
    }
}
