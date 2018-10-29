package com.triassi.queuemanager;

import android.app.Application;

import com.parse.Parse;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("478cc9f4a46f487344745645ca0bffebc1410a26")
                .clientKey("c3c691408d44ec63d8addd54e6d2f84dfbbf1abd")
                .server("http://52.28.191.143:80/parse/")
                .build()
        );
    }
}
