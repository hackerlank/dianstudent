package com.dingli.diandians.qingjia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/8/2.
 */
public class NumClassAdapter extends BaseAdapter{
    Context context;
    List<Course> arraylist;
    public NumClassAdapter(Context context){
        this.context=context;
        arraylist=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Course getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class_view,null);
        }
        ListItemClassView listItemClassView=(ListItemClassView)convertView;
        listItemClassView.setNumClass(getItem(position));
        listItemClassView.setTag(getItem(position));
        return listItemClassView;
    }
    public void addNumClass(List<Course> list){
        arraylist.addAll(list);
    }
    public void refreshNumClass(List<Course> list){
        arraylist.clear();
        arraylist.addAll(list);
    }
}
