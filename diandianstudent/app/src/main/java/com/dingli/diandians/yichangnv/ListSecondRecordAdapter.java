package com.dingli.diandians.yichangnv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by dingliyuangong on 2017/3/14.
 */

public class ListSecondRecordAdapter extends BaseAdapter{
    ViewHolder viewHolder;
    Context context;
    List<Data> list;
    ViewHolder.onCancelCollectListener listener;
    public ListSecondRecordAdapter(Context context,List<Data> list,ViewHolder.onCancelCollectListener listener){
        this.context=context;
        this.list=list;
        this.listener=listener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Data getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_record_view,null);
            viewHolder=new ViewHolder(convertView);
            viewHolder.secondzhuangtai=(TextView) convertView.findViewById(R.id.secondzhuangtai);
            viewHolder.llsecordre=(LinearLayout) convertView.findViewById(R.id.llsecordre);
            viewHolder.recorddetail=(TextView) convertView.findViewById(R.id.recorddetail);
            viewHolder.recordriqi=(TextView) convertView.findViewById(R.id.recordriqi);
            viewHolder.recordjieshu=(TextView) convertView.findViewById(R.id.recordjieshu);
            viewHolder.recordtea=(TextView) convertView.findViewById(R.id.recordtea);
            viewHolder.recordweek=(TextView) convertView.findViewById(R.id.recordweek);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        switch (list.get(position).typeId){
            case 1:
                viewHolder.secondzhuangtai.setText("已到");
                viewHolder.secondzhuangtai.setBackgroundResource(R.mipmap.yuanyidao);
                break;
            case 2:
                viewHolder.secondzhuangtai.setText("旷课");
                viewHolder.secondzhuangtai.setBackgroundResource(R.mipmap.yuankuangke);
                break;
            case 3:
                viewHolder.secondzhuangtai.setText("迟到");
                viewHolder.secondzhuangtai.setBackgroundResource(R.mipmap.yuanchidao);
                break;
            case 4:
                viewHolder.secondzhuangtai.setText("请假");
                viewHolder.secondzhuangtai.setBackgroundResource(R.mipmap.yuanqingjia);
                break;
            case 5:
                viewHolder.secondzhuangtai.setText("早退");
                viewHolder.secondzhuangtai.setBackgroundResource(R.mipmap.yuanzaotui);
                break;
        }
        viewHolder.recorddetail.setText(list.get(position).courseName);
        viewHolder.recordriqi.setText(list.get(position).teach_time);
        viewHolder.recordjieshu.setText(list.get(position).periodName);
        viewHolder.recordtea.setText(list.get(position).name);
        switch (list.get(position).dayWeek){
            case 1:
                viewHolder.recordweek.setText("周一");
                break;
            case 2:
                viewHolder.recordweek.setText("周二");
                break;
            case 3:
                viewHolder.recordweek.setText("周三");
                break;
            case 4:
                viewHolder.recordweek.setText("周四");
                break;
            case 5:
                viewHolder.recordweek.setText("周五");
                break;
            case 6:
                viewHolder.recordweek.setText("周六");
                break;
            case 7:
                viewHolder.recordweek.setText("周日");
                break;
            default:
                viewHolder.recordweek.setText("周日");
                break;
        }
        final String scheduleId=list.get(position).scheduleId;
        viewHolder.llsecordre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecordListener(scheduleId);
            }
        });
        return convertView;
    }
}
