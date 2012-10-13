package com.social.qna;

import android.os.Bundle;
import com.social.qna.robolock.RoboLockFragmentActivity;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/12/12
 * Time: 4:23 PM
 */
public class SocialMain extends RoboLockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}