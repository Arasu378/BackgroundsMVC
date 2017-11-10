package com.arasu.vt.backgroundsmvc.adapters;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.arasu.vt.backgroundsmvc.PictureViewModel;
import com.arasu.vt.backgroundsmvc.R;
import com.squareup.picasso.Picasso;

/**
 * Created by kyros on 06-11-2017.
 */

public class CustomBindingAdapter {
    @BindingAdapter({"app:poster"})
    public static void loadImage(ImageView view,String path){
        Log.d("Url : ",""+path);
        Picasso.with(view.getContext())
                .load(path)
                .placeholder(R.color.colorAccent)
               // .fit()
                .into(view);
    }
    @BindingAdapter({"app:poster_full"})
    public static void loadImageFull(ImageView view,String path){
        Picasso.with(view.getContext())
                .load(path)
                .placeholder(R.color.colorAccent)
                .fit()
                .into(view);
    }
}
