package com.arasu.vt.backgroundsmvc;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arasu.vt.backgroundsmvc.application.MyApplication;
import com.arasu.vt.backgroundsmvc.databinding.ActivityFullScreenBinding;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends AppCompatActivity {
    private ActivityFullScreenBinding binding;
    private String url=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_full_screen);
        ((MyApplication)getApplication()).getApplicationComponent().injectAct(this);
        try {
            Bundle bundle=getIntent().getExtras();
            url=bundle.getString("image");
            String id=bundle.getString("id");
            Log.d("Id"," : "+id);
            if(url!=null){
                Picasso.with(FullScreenActivity.this)
                        .load(url)
                        .placeholder(R.color.colorAccent)
                        .fit()
                        .into(binding.imgDisplay);
            }else {
                Toast.makeText(getApplicationContext(),"No urls! ",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
