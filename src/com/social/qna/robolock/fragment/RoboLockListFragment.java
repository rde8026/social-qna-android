package com.social.qna.robolock.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.app.SherlockListFragment;
import com.social.qna.robolock.dialog.DialogBase;
import com.social.qna.robolock.dialog.DialogBaseImpl;
import roboguice.RoboGuice;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 5:04 PM
 */
public class RoboLockListFragment extends SherlockListFragment implements DialogBase {

    private DialogBase dialogBase = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);
        dialogBase = new DialogBaseImpl(getFragmentManager());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectViewMembers(this);
    }

    @Override
    public void displayWarning(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        dialogBase.displayWarning(title, message, positive, negative);
    }

    @Override
    public void displayError(String title, String message, DialogInterface.OnClickListener neutral) {
        dialogBase.displayError(title, message, neutral);
    }

    @Override
    public void displayInfo(String title, String message) {
        dialogBase.displayInfo(title, message);
    }

    @Override
    public void displayConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        dialogBase.displayConfirm(title, message, positive, negative);
    }

}
