package com.dingli.diandians.yichangnv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.irecyclerview.IViewHolder;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.Data;
import com.dingli.diandians.view.NoScrollListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/3/23.
 */

public class ListRecyAdapter extends RecyclerView.Adapter<IViewHolder> {
    ViewHolder viewHolder;
    List<Course> arrayList;
    Context context;
    ViewHolder.onCancelCollectListener listener;
    List<Data> listda;
    public ListRecyAdapter(Context context,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        this.arrayList=new ArrayList<>();
        this.listener=listener;
        this.listda=new ArrayList<>();
    }

    @Override
    public IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recordfirst,parent,false);
        viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IViewHolder holder, int position) {
            viewHolder.lvyue.setText(arrayList.get(position).month + "月");
            viewHolder.benyue.setText("本月(" + arrayList.get(position).year + "/");
            ListSecondRecordAdapter adapter = new ListSecondRecordAdapter(context, arrayList.get(position).data, listener);
            viewHolder.lvs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void refreshRecord(List<Course> arrayLists){
        arrayList.clear();
        arrayList.addAll(arrayLists);
    }
    public void addRecord(List<Course> arrayLists){
        arrayList.addAll(arrayLists);
//        }
    }
}
