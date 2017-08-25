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
public class InSurveyAdapter extends BaseAdapter {
    Context context;
    List<QingJiaSty> arrlist;
    public InSurveyAdapter(Context context){
        this.context=context;
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_insurvey_view,null);
        }
        ListItemInSurveyView listItemInSurveyView=(ListItemInSurveyView)convertView;
        listItemInSurveyView.setInSuvey(getItem(position));
        listItemInSurveyView.setTag(getItem(position));
//        listItemWeiDoneView.setmOnCancelInterface(listener);
//        final int id=arrlist.get(position).leaveId;
//        listItemWeiDoneView.vtjujue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onCancelCollect(id);
//            }
//        });
        return listItemInSurveyView;
    }
    public void addWeiDone(List<QingJiaSty> list){
        arrlist.addAll(list);
    }
    public void refreshWeiDone(List<QingJiaSty> list){
        arrlist.clear();
        arrlist.addAll(list);
    }
}
