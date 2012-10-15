package com.social.qna.events;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 12:06 PM
 */
public class LocationEvent implements Serializable {

    private String address;
    private String error;
    private int code;

    public LocationEvent(String address) {
        this.address = address;
    }

    public LocationEvent(String error, int code) {
        this.error = error;
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
