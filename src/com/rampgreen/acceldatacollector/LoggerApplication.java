package com.rampgreen.acceldatacollector;

import android.app.Application;

/**
 * Application class for the demo. Used to ensure that MyVolley is initialized. {@see MyVolley}
 * @author Manish Pathak
 *
 */
public class LoggerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        init();
    }


    private void init() {
        MyVolley.init(this);
    }
}
