package com.social.qna.fragments.callbacks;

import android.util.SparseBooleanArray;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/16/12
 * Time: 12:57 PM
 */
public interface RemoveQuestionsCallback {

    void itemSelected(SparseBooleanArray array);

    void itemDeSelected(SparseBooleanArray array);

}
