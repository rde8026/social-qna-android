package com.social.qna.controllers;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kinvey.KCSClient;
import com.kinvey.KinveyUser;
import com.kinvey.MappedAppdata;
import com.kinvey.persistence.mapping.MappedEntity;
import com.kinvey.util.ListCallback;
import com.kinvey.util.ScalarCallback;
import com.social.qna.application.SocialApplication;
import com.social.qna.model.QuestionModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 11:30 AM
 */
@Singleton
public class QuestionController {


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = QuestionController.class.getSimpleName();


    @Inject BusController busController;
    @Inject LoginController loginController;
    @Inject Context context;

    public void createQuestion(String questionText, double latitude, double longitude, KinveyUser user, final ScalarCallback<QuestionModel> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();

        QuestionModel model = new QuestionModel();
        model.setQuestionText(questionText);
        model.setLongitude((float)longitude);
        model.setLatitude((float)latitude);
        model.setCreated(sdf.format(new Date()));
        model.setUserId(user.getId());
        model.setCreatedBy(user.getUsername());

        client.mappeddata("questions").save(model, callback);
    }

    public void getQuestions(KinveyUser user, final ListCallback<QuestionModel> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();
        MappedAppdata questions = client.mappeddata("questions");
        questions.addFilterCriteria("userId", "==", user.getId());
        questions.fetchByFilterCriteria(QuestionModel.class, callback);
    }

    public void removeQuestion(String questionId, ScalarCallback<Void> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();
        client.mappeddata("questions").delete(questionId, callback);
    }

    public void getAllQuestions(ListCallback<QuestionModel> callback) {
        KCSClient client = ((SocialApplication)context.getApplicationContext()).getService();
        client.mappeddata("questions").all(QuestionModel.class, callback);
    }

}
