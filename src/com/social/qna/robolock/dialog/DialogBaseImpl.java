package com.social.qna.robolock.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 4:57 PM
 */
public class DialogBaseImpl implements DialogBase {

    @Inject FragmentManager fragmentManager;

    @Override
    public void displayWarning(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        DialogFragment fragment = new DialogFragment();
    }

    @Override
    public void displayError(String title, String message, DialogInterface.OnClickListener neutral) {

    }

    @Override
    public void displayInfo(String title, String message) {

    }

    @Override
    public void displayConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {

    }
}
