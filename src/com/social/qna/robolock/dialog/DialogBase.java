package com.social.qna.robolock.dialog;

import android.content.DialogInterface;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 4:51 PM
 */
public interface DialogBase {

    /**
     * Shows waring dialog
     * if positive or negative are null they will default to close the dialog
     * @param title
     * @param message
     * @param positive
     * @param negative
     */
    void displayWarning(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative);

    /**
     * Show error
     * If neutral is null the action will just close the dialog
     * @param title
     * @param message
     * @param neutral
     */
    void displayError(String title, String message, DialogInterface.OnClickListener neutral);

    /**
     * Shows an informational message
     * @param title
     * @param message
     */
    void displayInfo(String title, String message);


    /**
     * Shows a confirmation message
     * You must provide implementation of positive and negative
     * @param title
     * @param message
     * @param positive
     * @param negative
     */
    void displayConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative);

}
