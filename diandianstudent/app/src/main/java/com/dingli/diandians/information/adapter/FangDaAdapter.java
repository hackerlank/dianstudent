package com.dingli.diandians.information.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/10/31.
 */
public class FangDaAdapter extends PagerAdapter {
      ArrayList<View> url;
    public FangDaAdapter(ArrayList<View> url){
        this.url=url;
    }
    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=url.get(position);
        ViewParent vp=view.getParent();
        if (vp!=null){
          ViewGroup vg=(ViewGroup)vp;
            vg.removeView(view);
        }
        ((ViewPager)container).addView(url.get(position));
        return url.get(position);
    }
}
