package com.arasu.vt.backgroundsmvc;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arasu.vt.backgroundsmvc.application.MyApplication;
import com.arasu.vt.backgroundsmvc.databinding.ActivityFullScreenBinding;
import com.arasu.vt.backgroundsmvc.models.GetPhotoByIdResponse;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class FullScreenActivity extends AppCompatActivity {
    @Inject
    FullScreenViewModel viewModel;
    private CompositeSubscription subscription;
    private GetPhotoByIdResponse response=new GetPhotoByIdResponse();

    private ActivityFullScreenBinding binding;
    private String url=null;
    private String id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_full_screen);
        ((MyApplication)getApplication()).getApplicationComponent().injectAct(this);
            subscription=new CompositeSubscription();
        try {
            Bundle bundle=getIntent().getExtras();
            url=bundle.getString("image");
             id=bundle.getString("id");
            Log.d("Id"," : "+id);
//            if(url!=null){
//                Picasso.with(FullScreenActivity.this)
//                        .load(url)
//                        .placeholder(R.color.colorAccent)
//                        .fit()
//                        .into(binding.imgDisplay);
//            }else {
//                Toast.makeText(getApplicationContext(),"No urls! ",Toast.LENGTH_SHORT).show();
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        initBindings();



    }

    private void loadData() {
        subscription.add(viewModel.getPhotoByIdResponse(id)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("error : ",""+throwable.getMessage()))
        .subscribe(photosl ->{
            binding.imgDisplay.setVisibility(View.VISIBLE);
            try{
                String value=photosl.getUrls().getFull();
                if(value!=null){
                    Picasso.with(FullScreenActivity.this)
                            .load(value)
                            .placeholder(R.color.colorAccent)
                            .fit()
                            .into(binding.imgDisplay);
                }else {
                    Toast.makeText(getApplicationContext(),"No urls on initbindings! ",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }));
    }

    private void initBindings() {
        viewModel.getPhotoData()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("error : ",""+throwable.getMessage()))
                .subscribe((items)->{
                    response=items;



                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }
}
