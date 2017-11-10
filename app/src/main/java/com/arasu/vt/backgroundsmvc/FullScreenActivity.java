package com.arasu.vt.backgroundsmvc;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arasu.vt.backgroundsmvc.application.MyApplication;
import com.arasu.vt.backgroundsmvc.databinding.ActivityFullScreenBinding;
import com.arasu.vt.backgroundsmvc.models.GetPhotoByIdResponse;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
    private ProgressDialog pDialog;

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
    private void showPDialog(){
        if(pDialog==null){
            pDialog=new ProgressDialog(this);
            pDialog.setMessage("Please wait!...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
        }
        pDialog.show();

    }
    private void dismissPDialog(){
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        initBindings();



    }

    private void loadData() {
        showPDialog();
        subscription.add(viewModel.getPhotoByIdResponse(id)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable ->

                Log.e("error : ",""+throwable.getMessage()))
        .subscribe(photosl ->{
            dismissPDialog();
            binding.imgDisplay.setVisibility(View.VISIBLE);
            try{
                String value=photosl.getUrls().getFull();
                if(value!=null){
                    Log.d("Value: ",""+value);
                    SubsamplingScaleImageView imageView = findViewById(R.id.imgDisplay);
                //    imageView.setImage(ImageSource.resource(R.drawable.monkey));
                    Picasso.with(FullScreenActivity.this)
                            .load(value)
                            .placeholder(R.color.colorAccent)
                           // .fit()
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    binding.imgDisplay.setImage(ImageSource.bitmap(bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
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
        dismissPDialog();
    }
}
