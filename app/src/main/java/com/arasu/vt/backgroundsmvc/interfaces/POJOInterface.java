package com.arasu.vt.backgroundsmvc.interfaces;

import com.arasu.vt.backgroundsmvc.BuildConfig;
import com.arasu.vt.backgroundsmvc.models.PhotoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kyros on 06-11-2017.
 */

public interface POJOInterface {
    @Headers("Authorization: Client-ID "+ BuildConfig.APPLICATION_ID_PICTURE)
    @GET("/photos")
    Observable<List<PhotoResponse>>getPhotosList(@Query("per_page")int per_page, @Query("order_By")String order_By, @Query("page")int page);
}
