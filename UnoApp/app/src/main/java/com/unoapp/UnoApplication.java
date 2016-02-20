package com.unoapp;


import android.app.Application;

import com.shaojin.unoapp.R;
import com.squareup.otto.Bus;

import retrofit.RestAdapter;

public class UnoApplication extends Application {
    private RestAdapter adapter;
    public static UnoApplication application;
    Bus bus;



    @Override
    public void onCreate() {
        super.onCreate();
        bus = new MainThreadBus();
        application = this;
        adapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.base_url)).build();
    }
    public static UnoApplication getInstance() {
        return application;
    }

    public RestAdapter getAdapter() {
        return adapter;
    }

    public Bus getBus() {
        return bus;
    }
}
