package com.arasu.vt.backgroundsmvc;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arasu.vt.backgroundsmvc.adapters.PhotListAdapter;
import com.arasu.vt.backgroundsmvc.application.MyApplication;
import com.arasu.vt.backgroundsmvc.databinding.ActivityMainBinding;
import com.arasu.vt.backgroundsmvc.models.PhotoResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements PhotListAdapter.PhotoClickListener {
    @Inject
    PictureViewModel viewModel;
    private CompositeSubscription subscription;
    private List<PhotoResponse>photoResponseList=new ArrayList<>();
    private ActivityMainBinding binding;
    private StaggeredGridLayoutManager layoutManager;
    private PhotListAdapter adapter;
    private int perPage=30;
    private int page=1;
    private String order_By="latest";
    //oldest,latest,popular,trending

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        ((MyApplication)getApplication()).getApplicationComponent().inject(this);
            subscription=new CompositeSubscription();

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerPictures.setLayoutManager(layoutManager);
        adapter=new PhotListAdapter(photoResponseList,this);
        binding.recyclerPictures.setAdapter(adapter);
        initBindings();
        if(photoResponseList.isEmpty()){
            loadPictures();
        }else{
            adapter.setItems(photoResponseList);
        }
    }
    private void initBindings(){
        Observable<Void> infiniteScrollObservable=Observable.create(subscriber -> {
            binding.recyclerPictures.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int pastVisibleItems=0;
                    int totalItemCount=layoutManager.getItemCount();
                    int visibleItemCount=layoutManager.getChildCount();
                    int[] firstVisibleItems=null;
                    firstVisibleItems=layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if(firstVisibleItems!=null&& firstVisibleItems.length>0){
                        pastVisibleItems=firstVisibleItems[0];
                    }
                    if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                        subscriber.onNext(null);
                    }
                }
            });
        });
        viewModel.getPhotos()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((items)->{
                this.photoResponseList=items;
                adapter.setItems(photoResponseList);
                });
        viewModel.getIsLoading().observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean ->{
            adapter.setShowLoadingMore(aBoolean);
        });
        infiniteScrollObservable.subscribe(x-> loadPictures());

    }
    private void loadPictures(){
        subscription.add(viewModel.loadPhotos(perPage,order_By)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",""+throwable.getMessage()))
        .subscribe(photosl ->{
            binding.recyclerPictures.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.GONE);
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }
    public void setData(List<PhotoResponse>photoResponses){
        this.photoResponseList=photoResponses;
    }
    public List<PhotoResponse>getPhotoResponseList(){
        return photoResponseList;
    }

    @Override
    public void onClick(int position, String name) {
        Toast.makeText(getApplicationContext(), "You clicked on " + name, Toast.LENGTH_SHORT).show();

    }
}
