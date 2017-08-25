package com.dingli.diandians.qingjia.picture;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dingli.diandians.R;

/**
 * Created by dingliyuangong on 2016/10/24.
 */
@SuppressLint("ValidFragment")
public class PictrueFragment extends Fragment{
    ImageView ivpages;
    Bitmap bm;
    int position;
    @SuppressLint("ValidFragment")
    public PictrueFragment(int position,Bitmap bm){
        this.position=position;
        this.bm=bm;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewpage=inflater.inflate(R.layout.fragment_picture,container,false);
        ivpages=(ImageView)viewpage.findViewById(R.id.ivpages);
        ivpages.setImageBitmap(bm);
        return viewpage;
    }
}
