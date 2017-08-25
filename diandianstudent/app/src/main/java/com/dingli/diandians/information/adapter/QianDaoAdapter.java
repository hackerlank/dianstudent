package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.QingJiaSty;
import com.dingli.diandians.firstpage.FirstPageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class QianDaoAdapter extends BaseAdapter {
    Context context;
    List<Course> arrlist;
    public QianDaoAdapter(Context context){
        this.context=context;
        arrlist=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return arrlist.size();
    }

    @Override
        public Course getItem(int position) {
        return arrlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_qiandao_view,null);
        }
        ListItemQanDaoView listItemWeiDoneView=(ListItemQanDaoView)convertView;
        listItemWeiDoneView.setQanDao(getItem(position));
        listItemWeiDoneView.setTag(getItem(position));
        listItemWeiDoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FirstPageFragment.class);
                context.startActivity(intent);
            }
        });
        return listItemWeiDoneView;
    }
    public void addQingJia(List<Course> list){
        arrlist.addAll(list);
    }
    public void refreshQanDao(List<Course> list){
        arrlist.clear();
        arrlist.addAll(list);
    }
}
