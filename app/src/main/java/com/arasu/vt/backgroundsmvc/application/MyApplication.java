package com.arasu.vt.backgroundsmvc.application;

import android.app.Application;

import com.arasu.vt.backgroundsmvc.dependencyinjection.module.ApplicationComponent;
import com.arasu.vt.backgroundsmvc.dependencyinjection.module.DaggerApplicationComponent;

/**
 * Created by kyros on 06-11-2017.
 */

public class MyApplication extends Application {
    private ApplicationComponent applicationComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent= DaggerApplicationComponent.create();
    }
    public ApplicationComponent getApplicationComponent(){
        return this.applicationComponent;
    }

}
