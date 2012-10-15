package com.social.qna.robolock.dialog;

import android.content.DialogInterface;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/14/12
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DailogActivityBaseImpl implements DialogBase {

    @Override
    public void displayWarning(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void displayError(String title, String message, DialogInterface.OnClickListener neutral) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void displayInfo(String title, String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void displayConfirm(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
