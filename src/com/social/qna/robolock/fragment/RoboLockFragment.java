package com.social.qna.robolock.fragment;

import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.app.SherlockFragment;
import roboguice.RoboGuice;
import roboguice.util.RoboContext;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 5:03 PM
 */
public class RoboLockFragment extends SherlockFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectViewMembers(this);
    }

}
