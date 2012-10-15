package com.social.qna.robolock.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import com.social.qna.R;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/14/12
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentAlertDialog extends DialogFragment {

    private static enum Type {
        ERROR,
        INFO,
        CONFIRM,
        WARNING
    }


    private DialogInterface.OnClickListener neutralListener;
    private DialogInterface.OnClickListener positiveListener;
    private DialogInterface.OnClickListener negativeListener;

    private static final String TITLE = "TITLE";
    private static final String MESSAGE = "MESSAGE";
    private static final String ENUM_TYPE = "TYPE";

    public static FragmentAlertDialog newErrorInstance(String title, String message, DialogInterface.OnClickListener neutral) {
        FragmentAlertDialog fragmentAlertDialog = new FragmentAlertDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putString(ENUM_TYPE, Type.ERROR.name());
        fragmentAlertDialog.setArguments(args);

        fragmentAlertDialog.setNeutralListener(neutral);
        return fragmentAlertDialog;
    }

    public static FragmentAlertDialog newConfirmInstance(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        FragmentAlertDialog fragmentAlertDialog = new FragmentAlertDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putString(ENUM_TYPE, Type.CONFIRM.name());
        fragmentAlertDialog.setArguments(args);

        fragmentAlertDialog.setPositiveListener(positive);
        fragmentAlertDialog.setNegativeListener(negative);
        return fragmentAlertDialog;
    }

    public static FragmentAlertDialog newInfoInstance(String title, String message, DialogInterface.OnClickListener neutral) {
        FragmentAlertDialog fragmentAlertDialog = new FragmentAlertDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putString(ENUM_TYPE, Type.INFO.name());
        fragmentAlertDialog.setArguments(args);

        fragmentAlertDialog.setNeutralListener(neutral);
        return fragmentAlertDialog;
    }

    public static FragmentAlertDialog newWarningInstance(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        FragmentAlertDialog fragmentAlertDialog = new FragmentAlertDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putString(ENUM_TYPE, Type.WARNING.name());
        fragmentAlertDialog.setArguments(args);

        fragmentAlertDialog.setPositiveListener(positive);
        fragmentAlertDialog.setNegativeListener(negative);
        return fragmentAlertDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);
        Type type = Type.valueOf(getArguments().getString(ENUM_TYPE));

        if (type == Type.ERROR) {
            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton(getResources().getString(R.string.msg_ok), getNeutralListener())
                    .create();
        } else if (type == Type.INFO) {
            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(getResources().getString(R.string.msg_ok), getNeutralListener())
                    .create();
        } else if (type == Type.CONFIRM) {
            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.msg_yes), getPositiveListener())
                    .setNegativeButton(getResources().getString(R.string.msg_no), getNegativeListener())
                    .create();
        } else {
            return new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.msg_ok), getPositiveListener())
                    .setNegativeButton(getResources().getString(R.string.msg_cancel), getNegativeListener())
                    .create();
        }

    }


    public DialogInterface.OnClickListener getPositiveListener() {
        return positiveListener;
    }

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public DialogInterface.OnClickListener getNegativeListener() {
        return negativeListener;
    }

    public void setNegativeListener(DialogInterface.OnClickListener negativeListener) {
        this.negativeListener = negativeListener;
    }

    public DialogInterface.OnClickListener getNeutralListener() {
        return neutralListener;
    }

    public void setNeutralListener(DialogInterface.OnClickListener neutralListener) {
        this.neutralListener = neutralListener;
    }
}
