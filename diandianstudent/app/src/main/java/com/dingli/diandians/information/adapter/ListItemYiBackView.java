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

import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.common.QingJiaSty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class ListItemYiBackView extends RelativeLayout{
    TextView vtti,vtscho,vtzgong,vtdate,vtzhouji,vtjijie,vtfoulixiao,vtqingjiayou,yibackxiangguan;
    ImageView ivleavePic,ivleavePicone,ivleavePictwo;
    LinearLayout llyibacks,llbc;
    View viewbc;
    public ListItemYiBackView(Context context) {
        super(context);
    }
    public ListItemYiBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemYiBackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        vtti=getView(R.id.vtti);
        vtdate=getView(R.id.vtdate);
        vtzhouji=getView(R.id.vtzhouji);
        vtjijie=getView(R.id.vtjijie);
        vtfoulixiao=getView(R.id.vtfoulixiao);
        vtqingjiayou=getView(R.id.vtqingjiayou);
        vtzgong=getView(R.id.vtzgong);
        ivleavePic=getView(R.id.ivleavePicyi);
        ivleavePicone=getView(R.id.ivleavePiconeyi);
        ivleavePictwo=getView(R.id.ivleavePictwoyi);
        yibackxiangguan=getView(R.id.yibackxiangguan);
        llyibacks=getView(R.id.llyibacks);
        llbc=getView(R.id.llbc);
        viewbc=getView(R.id.viewbc);
    }
    public void setYiBack(QingJiaSty resultInfo){
        int dayForWeek = 0;
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if(TextUtils.isEmpty(resultInfo.endDate)){
            vtzgong.setVisibility(GONE);
            vtzhouji.setVisibility(VISIBLE);
            vtdate.setText(resultInfo.startDate);
                vtjijie.setVisibility(VISIBLE);
            vtjijie.setText(resultInfo.name);
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
                    vtzhouji.setText("星期一");
                    break;
                case 2:
                    vtzhouji.setText("星期二");
                    break;
                case 3:
                    vtzhouji.setText("星期三");
                    break;
                case 4:
                    vtzhouji.setText("星期四");
                    break;
                case 5:
                    vtzhouji.setText("星期五");
                    break;
                case 6:
                    vtzhouji.setText("星期六");
                    break;
                case 7:
                    vtzhouji.setText("星期日");
                    break;
            }
        }else {
            vtzhouji.setVisibility(GONE);
            vtdate.setText(resultInfo.startDate+"--"+resultInfo.endDate);
            vtjijie.setVisibility(GONE);
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
                vtzgong.setVisibility(VISIBLE);
                vtzgong.setText("共" + lgo + "天");
            }
        }
        vtti.setText(resultInfo.lastModifyDate);
        if(resultInfo.leaveSchool==true){
            vtfoulixiao.setText("是");
        }else{
            vtfoulixiao.setText("否");
        }
        vtqingjiayou.setText(resultInfo.requestContent);
        if(TextUtils.isEmpty(resultInfo.leavePictureUrls)){
            llbc.setVisibility(GONE);
            viewbc.setVisibility(GONE);
            llyibacks.setVisibility(GONE);
        }else if (resultInfo.leavePictureUrls!=null){
            llyibacks.setVisibility(VISIBLE);
            if(!resultInfo.leavePictureUrls.contains(";")){
                ivleavePic.setVisibility(VISIBLE);
                ivleavePicone.setVisibility(GONE);
                ivleavePictwo.setVisibility(GONE);
                llbc.setVisibility(VISIBLE);
                viewbc.setVisibility(VISIBLE);
                yibackxiangguan.setText("(1):");
                Glide.with(getContext()).load(resultInfo.leavePictureUrls).into(ivleavePic);
            }else {
                String[] http=resultInfo.leavePictureUrls.split(";");
                switch (http.length){
                    case 2:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(GONE);
                        llbc.setVisibility(VISIBLE);
                        viewbc.setVisibility(VISIBLE);
                        yibackxiangguan.setText("(2):");
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        break;
                    case 3:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(VISIBLE);
                        llbc.setVisibility(VISIBLE);
                        viewbc.setVisibility(VISIBLE);
                        yibackxiangguan.setText("相关照片(3):");
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        Glide.with(getContext()).load(http[2]).into(ivleavePictwo);
                        break;
                }
            }
        }else{
            ivleavePic.setVisibility(GONE);
            llbc.setVisibility(GONE);
            viewbc.setVisibility(GONE);
            llyibacks.setVisibility(GONE);
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
