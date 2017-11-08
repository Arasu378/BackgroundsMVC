package com.arasu.vt.backgroundsmvc;

import com.arasu.vt.backgroundsmvc.interfaces.POJOInterface;
import com.arasu.vt.backgroundsmvc.models.PhotoResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 06-11-2017.
 */

public class PictureViewModel {
    private int page=1;
    private BehaviorSubject<List<PhotoResponse>>photos=BehaviorSubject.create(new ArrayList<PhotoResponse>());
    private BehaviorSubject<Boolean>isLoading=BehaviorSubject.create(false);
    private POJOInterface pojoInterface;
    @Inject
    public PictureViewModel(POJOInterface pojoInterface){
        this.pojoInterface=pojoInterface;
    }
    public Observable<List<PhotoResponse>> loadPhotos(int per_page,String order_By){
        if(isLoading.getValue()){
            return Observable.empty();
        }
        isLoading.onNext(true);

        return pojoInterface
                .getPhotosList(per_page,order_By,page)
                .doOnNext(photosl ->{
                    List<PhotoResponse>fullList=new ArrayList<>(photos.getValue());
                    fullList.addAll(photosl);
                    photos.onNext(fullList);
                    page++;
                })
                .doOnTerminate(()-> isLoading.onNext(false));
    }
    public BehaviorSubject<Boolean>getIsLoading(){
        return isLoading;
    }
    public Observable<List<PhotoResponse>>getPhotos(){
        return photos.asObservable();
    }
}
