package com.social.qna.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/15/12
 * Time: 7:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeyboardUtil {

    public static void requestKeyboard(final Activity activity, int editViewId) {
        requestKeyboard(activity.findViewById(editViewId));
    }

    public static void requestKeyboard(final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(view, 0);
            }
        }, 200);
    }

    public static void dismissKeyboard(final Activity activity, int viewId) {
        final View view = activity.findViewById(viewId);
        dismissKeyboard(view);
    }

    public static void dismissKeyboard(View view) {
        InputMethodManager keyboard = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
