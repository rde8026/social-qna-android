package com.social.qna.model;

import com.kinvey.persistence.mapping.MappedEntity;
import com.kinvey.persistence.mapping.MappedField;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/16/12
 * Time: 4:55 PM
 */
public class AnswerModel implements MappedEntity, Serializable {

    private String id;
    private String answeredBy;
    private String questionId;
    private String answerText;
    private String createdDate;
    private String updatedDate;
    private String latitude;
    private String longitude;


    @Override
    public List<MappedField> getMapping() {
        return Arrays.asList(new MappedField[]{
                new MappedField("answeredBy", "answeredBy"),
                new MappedField("questionId", "questionId"),
                new MappedField("answerText", "answerText"),
                new MappedField("createdDate", "createdDate"),
                new MappedField("updatedDate", "updatedDate"),
                new MappedField("latitude", "latitude"),
                new MappedField("longitude", "longitude"),
                new MappedField("id", "_id")
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnsweredBy() {
        return answeredBy;
    }

    public void setAnsweredBy(String answeredBy) {
        this.answeredBy = answeredBy;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
