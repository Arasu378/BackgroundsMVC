package com.arasu.vt.backgroundsmvc.dependencyinjection.module;

import com.arasu.vt.backgroundsmvc.BuildConfig;
import com.arasu.vt.backgroundsmvc.interfaces.POJOInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kyros on 06-11-2017.
 */
@Module
public class RetrofitClient {
  private static final String APPLICATION_ID_PICTURE="application_id_picture";
  @Provides
    @Singleton
    public POJOInterface pojoInterface(){
      Retrofit retrofit=new Retrofit.Builder()
              .baseUrl(BuildConfig.END_POINT)
              .addConverterFactory(GsonConverterFactory.create())
              .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
              .build();
      return retrofit.create(POJOInterface.class);
  }
}
