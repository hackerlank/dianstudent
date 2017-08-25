package com.dingli.diandians.newProject.moudle.eye;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
/**
 * Created by lwq
 */
public class JianJieRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater   inflater;
    private int width;
    private String imageUrl;
    public JianJieRecycleAdapter(Context context, int width) {
        this.context = context;
        this.width=width;
        inflater = LayoutInflater.from(context);
    }

    public void setData(String url){
        imageUrl=url;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(inflater.inflate(R.layout.item_jianjie, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(imageUrl)
                .fitCenter()
                .into(((ImageViewHolder)holder).imageJianJie);
    }

    @Override
    public int getItemCount() {
        return  1;
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {//简介
        ImageView imageJianJie ;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageJianJie = (ImageView) itemView.findViewById(R.id.imageJianJie);
        }
    }
}
