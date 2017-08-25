package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
public class ListItemWeiDoneView extends RelativeLayout{
    TextView tvti,tvjs,tvnm,tvrq,tvzj,vtlix,vtshijia,weidonxiangguan;
    Button vtjujue;
    ImageView ivleavePic,ivleavePicone,ivleavePictwo;
    LinearLayout llweidone,llweids;
    View viewzhao;
    private onCancelCollectListener mOnCancelInterface;
    public ListItemWeiDoneView(Context context) {
        super(context);
    }
    public ListItemWeiDoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemWeiDoneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvti=getView(R.id.tvti);
        tvrq=getView(R.id.tvrq);
        tvzj=getView(R.id.tvzj);
        tvnm=getView(R.id.tvnm);
        vtlix=getView(R.id.vtlix);
        vtshijia=getView(R.id.vtshijia);
        vtjujue=getView(R.id.vtjujuew);
        tvjs=getView(R.id.tvjs);
        ivleavePic=getView(R.id.ivleavePic);
        ivleavePicone=getView(R.id.ivleavePicone);
        ivleavePictwo=getView(R.id.ivleavePictwo);
        llweids=getView(R.id.llweids);
        llweidone=getView(R.id.llweidone);
        weidonxiangguan=getView(R.id.weidonxiangguan);
        viewzhao=getView(R.id.viewzhao);
    }
    public void setWeiDone(QingJiaSty resultInfo){
        int dayForWeek = 0;
        SimpleDateFormat  matter1=new SimpleDateFormat("yyyy-MM-dd");
        if(TextUtils.isEmpty(resultInfo.endDate)){
            tvnm.setVisibility(GONE);
            tvzj.setVisibility(VISIBLE);
            tvrq.setText(resultInfo.startDate);
                tvjs.setVisibility(VISIBLE);
               tvjs.setText(resultInfo.name);
        }else{
            tvzj.setVisibility(GONE);
            tvrq.setText(resultInfo.startDate+"--"+resultInfo.endDate);
            tvjs.setVisibility(GONE);
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
                tvnm.setVisibility(VISIBLE);
                tvnm.setText("共" + lgo + "天");
            }
        }
        Calendar calendar=Calendar.getInstance();
        Date datesd = null;
        try {
            datesd = matter1.parse(resultInfo.startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(datesd);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            dayForWeek = 7;
        }else{
            dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        tvti.setText(resultInfo.createdDate);
        vtshijia.setText(resultInfo.requestContent);
        if(resultInfo.leaveSchool==true){
            vtlix.setText("是");
        }else{
            vtlix.setText("否");
        }
      switch (dayForWeek){
          case 1:
              tvzj.setText("星期一");
              break;
          case 2:
              tvzj.setText("星期二");
              break;
          case 3:
              tvzj.setText("星期三");
              break;
          case 4:
              tvzj.setText("星期四");
              break;
          case 5:
              tvzj.setText("星期五");
              break;
          case 6:
              tvzj.setText("星期六");
              break;
          case 7:
              tvzj.setText("星期日");
              break;
      }
        if(TextUtils.isEmpty(resultInfo.leavePictureUrls)){
            llweids.setVisibility(GONE);
            viewzhao.setVisibility(GONE);
            llweidone.setVisibility(GONE);
        }else if (resultInfo.leavePictureUrls!=null){
            llweidone.setVisibility(VISIBLE);
            if(!resultInfo.leavePictureUrls.contains(";")){
                ivleavePic.setVisibility(VISIBLE);
                ivleavePicone.setVisibility(GONE);
                ivleavePictwo.setVisibility(GONE);
                llweids.setVisibility(VISIBLE);
                weidonxiangguan.setText("(1)");
                viewzhao.setVisibility(VISIBLE);
                Glide.with(getContext()).load(resultInfo.leavePictureUrls).into(ivleavePic);
            }else {
                String[] http=resultInfo.leavePictureUrls.split(";");
                switch (http.length){
                    case 2:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(GONE);
                        llweids.setVisibility(VISIBLE);
                        weidonxiangguan.setText("(2)");
                        viewzhao.setVisibility(VISIBLE);
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        break;
                    case 3:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(VISIBLE);
                        weidonxiangguan.setVisibility(VISIBLE);
                        llweids.setVisibility(VISIBLE);
                        weidonxiangguan.setText("(3)");
                        viewzhao.setVisibility(VISIBLE);
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        Glide.with(getContext()).load(http[2]).into(ivleavePictwo);
                        break;
                }
            }
        }else{
            ivleavePic.setVisibility(GONE);
            weidonxiangguan.setVisibility(GONE);
            llweidone.setVisibility(GONE);
            llweids.setVisibility(GONE);
            viewzhao.setVisibility(GONE);
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
    public void setmOnCancelInterface(onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
    public interface onCancelCollectListener {
        void onCancelCollect(String url,int id,String urls);
    }
}
