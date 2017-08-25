package com.dingli.diandians.firstpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.syllabus.CourseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/3/9.
 */
public class ZhuanYeAdapter extends BaseAdapter {
    ArrayList<Course> arrayList;
    Context context;
    private ListItemFirstPageView.onCancelCollectListener mOnCancelInterface;
    public ZhuanYeAdapter(Context context,ListItemFirstPageView.onCancelCollectListener listener){
        this.arrayList=new ArrayList<Course>();
        this.context=context;
        mOnCancelInterface=listener;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Course getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_firstpage_view,null);
        }
        final ListItemFirstPageView listItemFirstPageView=(ListItemFirstPageView)convertView;
        listItemFirstPageView.setListFirst(getItem(position));
        listItemFirstPageView.setTag(getItem(position));
        listItemFirstPageView.setmOnCancelInterface(mOnCancelInterface);
       final  int  id =arrayList.get(position).scheduleId;
        final String location=arrayList.get(position).localtion;
        final String rollcallType=arrayList.get(position).rollcallType;
        final boolean be=arrayList.get(position).canReport;
        final int posi=position;
        final String isAuma=arrayList.get(position).isAutomatic;
        final String rollcallendtime=arrayList.get(position).rollCallEndTime;
        final String courseName=arrayList.get(position).courseName;
        final String type=arrayList.get(position).type;
       listItemFirstPageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(parent.getContext(), CourseActivity.class);
               intent.putExtra(Constant.KE_ID,id);
              parent.getContext().startActivity(intent);
           }
       });
        if(listItemFirstPageView.btxiao.getText().toString().equals("签 到")) {
            listItemFirstPageView.btxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCancelInterface.onCancelCollect(courseName,rollcallType, type, rollcallendtime, isAuma, id, be, location, posi);
                }
            });
        }
        listItemFirstPageView.btxiangqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(parent.getContext(), CourseActivity.class);
                intent.putExtra(Constant.KE_ID,id);
                parent.getContext().startActivity(intent);
            }
        });
        return listItemFirstPageView;
    }
    public void addFirstlist(List<Course> list){
        arrayList.addAll(list);
    }
    public void refreshFirstlist(List<Course> list){
        arrayList.clear();
        arrayList.addAll(list);
    }
}
