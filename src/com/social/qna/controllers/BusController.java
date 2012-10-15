package com.social.qna.controllers;

import com.google.inject.Singleton;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created with IntelliJ IDEA.
 * User: ryaneldridge
 * Date: 10/13/12
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */

@Singleton
public class BusController {

    private static Bus bus;

    public Bus getBus() {
        if (bus == null) {
            bus = new Bus(ThreadEnforcer.ANY);
        }
        return bus;
    }

}
