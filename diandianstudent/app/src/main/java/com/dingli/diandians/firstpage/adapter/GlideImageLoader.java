package com.dingli.diandians.firstpage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dingli.diandians.common.ResultOne;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Created by dingliyuangong on 2017/5/11.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
//     for (int i=0;i<imgUrls.length;i++){
         Glide.with(context).load(path).into(imageView);
//     }
    }
}
