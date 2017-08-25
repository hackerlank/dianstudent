package com.dingli.diandians.yichangnv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.Data;
import com.dingli.diandians.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/3/14.
 */

public class ListFirstRecordAdapter extends BaseAdapter{
    ViewHolder viewHolder;
    ArrayList<Course> arrayList;
    Context context;
    ViewHolder.onCancelCollectListener listener;
    public ListFirstRecordAdapter(Context context,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        this.arrayList=new ArrayList<>();
        this.listener=listener;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recordfirst,parent,false);
            viewHolder=new ViewHolder(convertView);
            viewHolder.benyue=(TextView) convertView.findViewById(R.id.benyue);
            viewHolder.lvs=(NoScrollListView) convertView.findViewById(R.id.lvs);
            viewHolder.lvyue=(TextView) convertView.findViewById(R.id.lvyue);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
            viewHolder.lvyue.setText(arrayList.get(position).month+"月");
            viewHolder.benyue.setText("本月("+arrayList.get(position).year+"/");
            ListSecondRecordAdapter adapter=new ListSecondRecordAdapter(parent.getContext(),arrayList.get(position).data,listener);
            viewHolder.lvs.setAdapter(adapter);
        return convertView;
    }
    public void refreshRecord(List<Course> arrayLists){
        arrayList.clear();
        arrayList.addAll(arrayLists);
    }
    public void addRecord(List<Course> arrayLists){
        arrayList.addAll(arrayLists);
    }
}
