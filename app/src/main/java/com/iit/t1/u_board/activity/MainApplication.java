package com.iit.t1.u_board.activity;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;


/**
 * Created by Nivash on 11/29/2015.
 */
public class MainApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "opPCtRyCuUCK9B9hYh23krbXexd0MBNLQDq7BKnq", "WonphSDkk8nB5D4Qz4pR52v5ohBVJ31VXqcFdbbk");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        FacebookSdk.sdkInitialize(getApplicationContext());

    }
}

