package com.social.qna.robolock.dialog;


import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 4:57 PM
 */
public class DialogBaseImpl implements DialogBase {

    private FragmentManager fragmentManager;

    private static final String DIALOG = "dialog";

    public DialogBaseImpl(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void displayWarning(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        FragmentAlertDialog fragmentAlertDialog = FragmentAlertDialog.newWarningInstance(title, message, positive, negative);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        fragmentAlertDialog.show(fragmentManager, DIALOG);
    }

    @Override
    public void displayError(String title, String message, DialogInterface.OnClickListener neutral) {
        FragmentAlertDialog fragmentAlertDialog = FragmentAlertDialog.newErrorInstance(title, message, neutral);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        fragmentAlertDialog.show(fragmentManager, DIALOG);
    }

    @Override
    public void displayInfo(String title, String message) {
        FragmentAlertDialog fragmentAlertDialog = FragmentAlertDialog.newInfoInstance(title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        fragmentAlertDialog.show(fragmentManager, DIALOG);
    }

    @Override
    public void displayConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        FragmentAlertDialog fragmentAlertDialog = FragmentAlertDialog.newConfirmInstance(title, message, positive, negative);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        fragmentAlertDialog.show(fragmentManager, DIALOG);
    }
}
