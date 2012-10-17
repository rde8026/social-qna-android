package com.social.qna.controllers;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kinvey.KCSClient;
import com.kinvey.KinveyUser;
import com.kinvey.util.ScalarCallback;
import com.social.qna.application.SocialApplication;
import com.social.qna.model.AnswerModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/16/12
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class AnswerController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = AnswerController.class.getSimpleName();

    @Inject BusController busController;
    @Inject Context context;

    public void answerQuestion(String questionId, String answerText, KinveyUser user, double latitude, double longitude, ScalarCallback<AnswerModel> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();

        AnswerModel answerModel = new AnswerModel();
        answerModel.setAnsweredBy(user.getUsername());
        answerModel.setAnswerText(answerText);
        answerModel.setCreatedDate(sdf.format(new Date()));
        answerModel.setQuestionId(questionId);
        try {
            JSONObject location = new JSONObject();
            location.put("latitude", latitude);
            location.put("longitude", longitude);
            answerModel.setLocation(location);
        } catch (JSONException je) {
            Log.e(TAG, "JSONException: " + je.getMessage());
        }
        client.mappeddata("answers").save(answerModel, callback);
    }

}
