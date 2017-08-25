package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dingli.diandians.R;
import com.dingli.diandians.common.QingJiaSty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class InfomationAdapter extends BaseAdapter {
    Context context;
    List<QingJiaSty> arrlist;
    public InfomationAdapter(Context context){
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
        View view=null;
        if (convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item,null);
        }else{
            view=convertView;
        }
        ViewHolder holder=null;
        if(holder==null){
            holder=new ViewHolder();
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.tvweitongzhi=(TextView)view.findViewById(R.id.tvweitongzhi);
        holder.rlqingjia=(RelativeLayout)view.findViewById(R.id.rlqingjia);
        holder.tvshuzi=(TextView)view.findViewById(R.id.tvshuzi);
        holder.tvtzsj=(TextView)view.findViewById(R.id.tvtzsj);
        return view;
    }
    public void addWeiDone(List<QingJiaSty> list){
        arrlist.addAll(list);
    }
    public void refreshWeiDone(List<QingJiaSty> list){
        arrlist.clear();
        arrlist.addAll(list);
    }
    class ViewHolder{
        TextView tvweitongzhi;
       RelativeLayout rlqingjia;
        TextView tvshuzi;
        TextView tvtzsj;
    }
}
