package com.incampusit.staryaar.SimpleClasses;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import iknow.android.utils.BaseUtils;

//import com.crashlytics.android.Crashlytics;

//import io.fabric.sdk.android.Fabric;

/*
 * Created by PANKAJ on 3/18/2019.
 */

public class StarYaar extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        BaseUtils.init(this);
        //Fabric.with(this, new Crashlytics());
    }

}
