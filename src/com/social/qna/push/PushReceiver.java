package com.social.qna.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/16/12
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "********** Recieved message ***************");
    }
}
