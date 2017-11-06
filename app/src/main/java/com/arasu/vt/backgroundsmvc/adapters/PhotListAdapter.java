package com.arasu.vt.backgroundsmvc.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arasu.vt.backgroundsmvc.FullScreenActivity;
import com.arasu.vt.backgroundsmvc.R;
import com.arasu.vt.backgroundsmvc.databinding.AdapterListItemBinding;
import com.arasu.vt.backgroundsmvc.databinding.InfiniteLoaderBinding;
import com.arasu.vt.backgroundsmvc.models.PhotoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyros on 06-11-2017.
 */

public class PhotListAdapter extends RecyclerView.Adapter<PhotListAdapter.MyViewHolder> {
    private List<PhotoResponse>photoResponses;
    private static final int LIST_ITEM=0;
    private static final int LOAD_MORE=1;
    private boolean showLoadingMore=false;
    private PhotoClickListener mListener;
    public PhotListAdapter(List<PhotoResponse>photoResponses,PhotoClickListener  listener){
        this.photoResponses=photoResponses;
        this.mListener=listener;
    }
   abstract class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       MyViewHolder(View view){
           super(view);
       }
       abstract void bindTo(PhotoResponse photoResponse);
   }
   public void setItems(List<PhotoResponse>photoResponses){
       if(photoResponses==null){
           return;
       }
       this.photoResponses=new ArrayList<>(photoResponses);
       notifyDataSetChanged();
   }
   public void setShowLoadingMore(boolean isLoading){
       showLoadingMore=isLoading;
   }
   public class PhotosViewHolderBackdrop extends MyViewHolder{
       AdapterListItemBinding binding;

       PhotosViewHolderBackdrop(AdapterListItemBinding view) {
           super(view.getRoot());
           this.binding=view;
       }

       @Override
       void bindTo(PhotoResponse photoResponse) {
           binding.setPhotos(photoResponse.getUrls());
           binding.executePendingBindings();
       }

       @Override
       public void onClick(View view) {
           mListener.onClick(getLayoutPosition(),photoResponses.get(getAdapterPosition()).getUser().getName());

       }
   }
   public class LoadingMoreHolder extends MyViewHolder{

       LoadingMoreHolder(InfiniteLoaderBinding view) {
           super(view.getRoot());
       }

       @Override
       void bindTo(PhotoResponse photoResponse) {


       }

       @Override
       public void onClick(View view) {
           mListener.onClick(getLayoutPosition(),photoResponses.get(getAdapterPosition()).getUser().getName());

       }
   }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        if(viewType==LOAD_MORE){
            InfiniteLoaderBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.infinite_loader,parent,false);
            return new LoadingMoreHolder(binding);
        }else{
            AdapterListItemBinding binding=DataBindingUtil.inflate(layoutInflater,R.layout.adapter_list_item,parent,false);
            return new PhotosViewHolderBackdrop(binding);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            switch (getItemViewType(position)){
                case LIST_ITEM:
                    holder.bindTo(photoResponses.get(position));
                    holder.itemView.setOnClickListener(v -> {
                        Context context = holder.itemView.getContext();
                        Intent intent=new Intent(context,FullScreenActivity.class);
                        intent.putExtra("id",photoResponses.get(position).getId());
                        intent.putExtra("image",photoResponses.get(position).getUrls().getFull());
                        context.startActivity(intent);
                    });
                    break;
                case LOAD_MORE:
                    break;
            }
    }

    @Override
    public int getItemCount() {
        return photoResponses.size();
    }
    public interface PhotoClickListener{
       void onClick(int position,String name);
    }
}
