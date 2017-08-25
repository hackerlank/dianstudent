package com.dingli.diandians.weipingjiao;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.syllabus.CourseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/8/2.
 */
public class WeiPJAdapter extends BaseAdapter {
    private List<WeiPJ.WeiPJDataBean> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public WeiPJAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_firstpage_view, parent, false);
            vh = new ViewHolder();
            vh.kechengming = (TextView) convertView.findViewById(R.id.kechengming);
            vh.dijie = (TextView) convertView.findViewById(R.id.dijie);
            vh.dishijia = (TextView) convertView.findViewById(R.id.dishijia);
            vh.dididian = (TextView) convertView.findViewById(R.id.dididian);
            vh.btxiao = (Button) convertView.findViewById(R.id.btxiao);

            vh.btxiao.setBackgroundResource(R.drawable.circlefirst_corner);
            vh.btxiao.setTextColor(context.getResources().getColor(R.color.bg_White));

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final WeiPJ.WeiPJDataBean weiPJData = ((WeiPJ.WeiPJDataBean) getItem(position));

        vh.kechengming.setText(weiPJData.getCourseName());
        vh.dijie.setText(weiPJData.getPeriodName());
        vh.dishijia.setText(weiPJData.getBeginTime() + "-" + weiPJData.getEndTime());
        vh.dididian.setText(weiPJData.getClassroom());
        vh.btxiao.setText("去评教");
        vh.btxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseActivity.class);
                intent.putExtra(Constant.KE_ID, ""+weiPJData.getScheduleId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView kechengming;
        private TextView dijie;
        private TextView dishijia;
        private TextView dididian;
        private Button btxiao;

    }

    public void refresh(boolean isClear, List<WeiPJ.WeiPJDataBean> list) {
        if (isClear) {
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
