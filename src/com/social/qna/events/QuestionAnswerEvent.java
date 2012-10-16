package com.social.qna.events;

import com.social.qna.model.QuestionModel;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/16/12
 * Time: 5:23 PM
 */
public class QuestionAnswerEvent implements Serializable {

    private QuestionModel model;

    public QuestionAnswerEvent(QuestionModel model) {
        this.model = model;
    }

    public QuestionModel getModel() {
        return model;
    }

    public void setModel(QuestionModel model) {
        this.model = model;
    }
}
