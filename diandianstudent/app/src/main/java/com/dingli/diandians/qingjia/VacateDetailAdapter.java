package com.dingli.diandians.qingjia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.dingli.diandians.qingjia.picture.PictrueFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/10/24.
 */
public class VacateDetailAdapter extends FragmentPagerAdapter{
    private ArrayList<String> stringimage;

    public VacateDetailAdapter(FragmentManager fm,ArrayList<String> stringimage) {
        super(fm);
        this.stringimage=stringimage;
    }
    @Override
    public int getCount() {
        return stringimage.size();
    }
    @Override
    public Fragment getItem(int position) {
        String path = stringimage.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
       Bitmap bm = BitmapFactory.decodeFile(path,options);
        return new PictrueFragment(position,bm);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
