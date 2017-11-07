package com.arasu.vt.backgroundsmvc;

import com.arasu.vt.backgroundsmvc.interfaces.POJOInterface;
import com.arasu.vt.backgroundsmvc.models.GetPhotoByIdResponse;

import org.json.JSONObject;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 06-11-2017.
 */

public class FullScreenViewModel {
    private BehaviorSubject<GetPhotoByIdResponse>photoresponse=BehaviorSubject.create(new GetPhotoByIdResponse());
    private POJOInterface pojoInterface;
    @Inject
    public FullScreenViewModel(POJOInterface pojoInterface){
        this.pojoInterface=pojoInterface;
    }
    public Observable<GetPhotoByIdResponse> getPhotoByIdResponse(String id){
        return pojoInterface
                .getPhotoDetailsById(id)
                .doOnNext(photosk ->{
                  //  JSONObject list=new JSONObject(photoresponse.getValue());
                   GetPhotoByIdResponse res= photoresponse.getValue();
                    photoresponse.onNext(res);
                })
                .doOnTerminate(()-> {

                });
    }
    public Observable<GetPhotoByIdResponse>getPhotoData(){
        return photoresponse.asObservable();
    }
}
