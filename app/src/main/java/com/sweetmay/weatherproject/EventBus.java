package com.sweetmay.weatherproject;

import com.squareup.otto.Bus;

public class EventBus {
    private static Bus bus = new Bus();

    public static Bus getBus(){
        return bus;
    }
}
