package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dingli.diandians.R;
import com.dingli.diandians.common.QingJiaSty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class ListItemQingJIAView extends RelativeLayout{
    TextView tvqingjias,tvschotvs,tvnams,tvdates,tvzhoujis,tvjijies,foulixiaos,qingjiayous,tvsg,shenpiren,tvjujueyuanying;
    ImageView ivstatuss;
    LinearLayout lljujue,lldivs;
    public ListItemQingJIAView(Context context) {
        super(context);
    }
    public ListItemQingJIAView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemQingJIAView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvqingjias=getView(R.id.tvqingjias);
        tvschotvs=getView(R.id.tvschotvs);
        tvnams=getView(R.id.tvnams);
        tvdates=getView(R.id.tvdates);
        tvzhoujis=getView(R.id.tvzhoujis);
        tvjijies=getView(R.id.tvjijies);
        foulixiaos=getView(R.id.foulixiaos);
        qingjiayous=getView(R.id.qingjiayous);
        tvsg=getView(R.id.tvsg);
        shenpiren=getView(R.id.shenpiren);
        ivstatuss=getView(R.id.ivstatuss);
        lljujue=getView(R.id.lljujues);
        tvjujueyuanying=getView(R.id.tvjujueyuanying);
        lldivs=getView(R.id.lldivs);
    }
    public void setQingJia(QingJiaSty resultInfo){
        tvqingjias.setText(resultInfo.createdDate);
        shenpiren.setText(resultInfo.teacherName);
        int dayForWeek = 0;
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
//        tvnams.setText(resultInfo.studentName);
          tvnams.setVisibility(GONE);
//        tvschotvs.setText(resultInfo.collegeName+" "+resultInfo.majorName+" "+resultInfo.className);
        tvschotvs.setVisibility(GONE);
        if(resultInfo.leaveSchool==true){
            foulixiaos.setText("是");
        }else{
            foulixiaos.setText("否");
        }
        if(resultInfo.status.equals("pass")){
            ivstatuss.setBackgroundResource(R.mipmap.shenpi_tongguo);
            lljujue.setVisibility(GONE);
            lldivs.setBackgroundResource(R.drawable.circleyidone_corner);
        }else{
            ivstatuss.setBackgroundResource(R.mipmap.shenpi_wei);
            lljujue.setVisibility(VISIBLE);
            tvjujueyuanying.setText(resultInfo.rejectContent);
            lldivs.setBackgroundResource(R.drawable.circleyidoneno_corner);
        }
        qingjiayous.setText(resultInfo.requestContent);
        if(TextUtils.isEmpty(resultInfo.endDate)){
            tvsg.setVisibility(GONE);
            tvzhoujis.setVisibility(VISIBLE);
            tvdates.setText(resultInfo.startDate);
                tvjijies.setVisibility(VISIBLE);
            tvjijies.setText(resultInfo.name);
            Date datesd = null;
            try {
                datesd = matter1.parse(resultInfo.startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setTime(datesd);
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1){
                dayForWeek = 7;
            }else{
                dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            }
            switch (dayForWeek){
                case 1:
                    tvzhoujis.setText("星期一");
                    break;
                case 2:
                    tvzhoujis.setText("星期二");
                    break;
                case 3:
                    tvzhoujis.setText("星期三");
                    break;
                case 4:
                    tvzhoujis.setText("星期四");
                    break;
                case 5:
                    tvzhoujis.setText("星期五");
                    break;
                case 6:
                    tvzhoujis.setText("星期六");
                    break;
                case 7:
                    tvzhoujis.setText("星期日");
                    break;
            }
        }else{
            tvzhoujis.setVisibility(GONE);
            tvdates.setText(resultInfo.startDate+"--"+resultInfo.endDate);
            tvjijies.setVisibility(GONE);
            Date dates = null;
            Date datess=null;
            try {
                dates = matter1.parse(resultInfo.startDate);
                datess=matter1.parse(resultInfo.endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long lo=datess.getTime()-dates.getTime();
            if(lo>0) {
                long log = lo / 3600000 / 24;
                long lgo=log+1;
                tvsg.setVisibility(VISIBLE);
                tvsg.setText("共" + lgo + "天");
            }
        }
    }
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(ex.toString(), "Could not cast View to concrete class");
            throw ex;
        }
    }
}
