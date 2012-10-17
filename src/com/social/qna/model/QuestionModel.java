package com.social.qna.model;

import com.kinvey.KinveyUser;
import com.kinvey.persistence.mapping.MappedEntity;
import com.kinvey.persistence.mapping.MappedField;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 10:50 AM
 */
public class QuestionModel implements MappedEntity, Serializable {

    private String id;
    private String questionText;
    private String created;
    private String updated;
    private String userId;
    /*private float latitude;
    private float longitude;*/
    private JSONObject location;
    private String createdBy;

    @Override
    public List<MappedField> getMapping() {
        return Arrays.asList(new MappedField[]{
                new MappedField("questionText", "questionText"),
                new MappedField("created", "created"),
                new MappedField("updated", "updated"),
                new MappedField("userId", "userId"),
                /*new MappedField("latitude", "latitude"),
                new MappedField("longitude", "longitude"),*/
                new MappedField("location", "location"),
                new MappedField("createdBy", "createdBy"),
                new MappedField("id", "_id")
                }
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public JSONObject getLocation() {
        return location;
    }

    public void setLocation(JSONObject location) {
        this.location = location;
    }

    /*public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }*/
}
