package com.arasu.vt.backgroundsmvc.dependencyinjection.module;

import com.arasu.vt.backgroundsmvc.FullScreenActivity;
import com.arasu.vt.backgroundsmvc.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kyros on 06-11-2017.
 */
@Singleton
@Component(modules = {RetrofitClient.class})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void injectAct(FullScreenActivity fullScreenActivity);

}
