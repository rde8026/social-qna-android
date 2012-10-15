package com.social.qna.robolock.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.social.qna.R;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/14/12
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DialogActivityBaseImpl implements DialogBase {

    private Context context;

    public DialogActivityBaseImpl(Context context) {
        this.context = context;
    }

    @Override
    public void displayWarning(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
            new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(context.getResources().getString(R.string.msg_ok), positive)
                .setNegativeButton(context.getResources().getString(R.string.msg_cancel), negative)
                .create().show();
    }

    @Override
    public void displayError(String title, String message, DialogInterface.OnClickListener neutral) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton(context.getResources().getString(R.string.msg_ok), neutral)
                .create().show();
    }

    @Override
    public void displayInfo(String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNeutralButton(context.getResources().getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public void displayConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(context.getResources().getString(R.string.msg_yes), positive)
                .setNegativeButton(context.getResources().getString(R.string.msg_no), negative)
                .create().show();
    }
}
