package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.dingli.diandians.R;
import com.dingli.diandians.common.QingJiaSty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class WeiDoneAdapter extends BaseAdapter {
    Context context;
    List<QingJiaSty> arrlist;
    ListItemWeiDoneView.onCancelCollectListener listener;
    public WeiDoneAdapter(Context context,ListItemWeiDoneView.onCancelCollectListener listener){
        this.context=context;
        this.listener=listener;
        arrlist=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return arrlist.size();
    }

    @Override
    public QingJiaSty getItem(int position) {
        return arrlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_weidone_view,null);
        }
        final ListItemWeiDoneView listItemWeiDoneView=(ListItemWeiDoneView)convertView;
            listItemWeiDoneView.setWeiDone(getItem(position));

        listItemWeiDoneView.setTag(getItem(position));
        listItemWeiDoneView.setmOnCancelInterface(listener);
        final int id=arrlist.get(position).leaveId;
        final String url=arrlist.get(position).leavePictureUrls;
        listItemWeiDoneView.vtjujue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelCollect("jujue",id,"");
            }
        });
        listItemWeiDoneView.ivleavePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelCollect("tupian", id, url);
            }
        });
        listItemWeiDoneView.ivleavePicone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelCollect("tupianone", id, url);
            }
        });
        listItemWeiDoneView.ivleavePictwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelCollect("tupiantwo", id, url);
            }
        });
        return listItemWeiDoneView;
    }
    public void addWeiDone(List<QingJiaSty> list){
        arrlist.addAll(list);
    }
    public void refreshWeiDone(List<QingJiaSty> list){
        arrlist.clear();
        arrlist.addAll(list);
    }
}
