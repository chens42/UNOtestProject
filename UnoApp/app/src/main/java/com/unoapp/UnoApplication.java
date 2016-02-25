package com.unoapp;


import android.app.Application;

import com.shaojin.unoapp.R;
import com.squareup.otto.Bus;

import retrofit.RestAdapter;

public class UnoApplication extends Application {
    public static UnoApplication application;
    Bus bus;



    @Override
    public void onCreate() {
        super.onCreate();
        bus = new MainThreadBus();
        application = this;
    }
    public static UnoApplication getInstance() {
        return application;
    }



    public Bus getBus() {
        return bus;
    }
}
