package com.social.qna.events;

import com.kinvey.KinveyUser;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/13/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginComplete implements Serializable {

    private KinveyUser user;

    public LoginComplete(KinveyUser user) {
        this.user = user;
    }

}
