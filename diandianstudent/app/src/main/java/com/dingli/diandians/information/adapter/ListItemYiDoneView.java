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
public class ListItemYiDoneView extends RelativeLayout{
    TextView tvtime,tvdate,tvzhouji,tvjijie,qingjiayou,foulixiao,vtzs,tvjujueyuanying,yidonexiangguan;
    ImageView ivstatus;
    Button vtjujue;
    LinearLayout lljujue;
    RelativeLayout rlziyi;
    ImageView ivleavePic,ivleavePicone,ivleavePictwo;
    LinearLayout llyids,lld;
    View llvieresyis;
    private onCancelCollectListener mOnCancelInterface;
    public ListItemYiDoneView(Context context) {
        super(context);
    }
    public ListItemYiDoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemYiDoneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvtime=getView(R.id.tvtime);
        tvdate=getView(R.id.tvdate);
        tvzhouji=getView(R.id.tvzhoujiy);
        tvjijie=getView(R.id.tvjijie);
        qingjiayou=getView(R.id.qingjiayou);
        foulixiao=getView(R.id.foulixiao);
        vtzs=getView(R.id.vtzs);
        ivstatus=getView(R.id.ivstatus);
        vtjujue=getView(R.id.vtjujue);
        lljujue=getView(R.id.lljujue);
        tvjujueyuanying=getView(R.id.tvjujueyuanying);
        rlziyi=getView(R.id.rlwdonedy);
        ivleavePic=getView(R.id.ivleavePicd);
        ivleavePicone=getView(R.id.ivleavePiconed);
        ivleavePictwo=getView(R.id.ivleavePictwod);
        yidonexiangguan=getView(R.id.yidonexiangguan);
        llyids=getView(R.id.llyids);
        llvieresyis=getView(R.id.llvieresyis);
        lld=getView(R.id.lld);
    }
    public void setYiDone(QingJiaSty resultInfo){
        vtjujue.setVisibility(GONE);
        int dayForWeek = 0;
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
       tvtime.setText(resultInfo.lastModifyDate);
        qingjiayou.setText(resultInfo.requestContent);
        if(resultInfo.leaveSchool==true){
            foulixiao.setText("是");
        }else{
            foulixiao.setText("否");
        }
        if(resultInfo.status.equals("pass")){
            ivstatus.setBackgroundResource(R.mipmap.shenpi_tongguo);
            lljujue.setVisibility(GONE);
            rlziyi.setBackgroundColor(getResources().getColor(R.color.lixiaos));
        }else{
            ivstatus.setBackgroundResource(R.mipmap.shenpi_wei);
            lljujue.setVisibility(VISIBLE);
            tvjujueyuanying.setText(resultInfo.rejectContent);
            rlziyi.setBackgroundColor(getResources().getColor(R.color.weid));
        }
        if(TextUtils.isEmpty(resultInfo.endDate)){
            vtzs.setVisibility(GONE);
            tvzhouji.setVisibility(VISIBLE);
            tvdate.setText(resultInfo.startDate);
                tvjijie.setVisibility(VISIBLE);
            tvjijie.setText(resultInfo.name);
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
                    tvzhouji.setText("星期一");
                    break;
                case 2:
                    tvzhouji.setText("星期二");
                    break;
                case 3:
                    tvzhouji.setText("星期三");
                    break;
                case 4:
                    tvzhouji.setText("星期四");
                    break;
                case 5:
                    tvzhouji.setText("星期五");
                    break;
                case 6:
                    tvzhouji.setText("星期六");
                    break;
                case 7:
                    tvzhouji.setText("星期日");
                    break;
            }
        }else{
            tvzhouji.setVisibility(GONE);
            tvdate.setText(resultInfo.startDate + "--" + resultInfo.endDate);
            tvjijie.setVisibility(GONE);
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
                vtzs.setVisibility(VISIBLE);
                vtzs.setText("共" + lgo + "天");
            }
        }
        if(TextUtils.isEmpty(resultInfo.leavePictureUrls)){
            llvieresyis.setVisibility(GONE);
            llyids.setVisibility(GONE);
            lld.setVisibility(GONE);
        }else if (resultInfo.leavePictureUrls!=null){
            llyids.setVisibility(VISIBLE);
            if(!resultInfo.leavePictureUrls.contains(";")){
                ivleavePic.setVisibility(VISIBLE);
                ivleavePicone.setVisibility(GONE);
                ivleavePictwo.setVisibility(GONE);
                llvieresyis.setVisibility(VISIBLE);
                yidonexiangguan.setText("(1):");
                lld.setVisibility(VISIBLE);
                Glide.with(getContext()).load(resultInfo.leavePictureUrls).into(ivleavePic);
            }else {
                String[] http=resultInfo.leavePictureUrls.split(";");
                switch (http.length){
                    case 2:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(GONE);
                        llvieresyis.setVisibility(VISIBLE);
                        yidonexiangguan.setText("(2):");
                        lld.setVisibility(VISIBLE);
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        break;
                    case 3:
                        ivleavePic.setVisibility(VISIBLE);
                        ivleavePicone.setVisibility(VISIBLE);
                        ivleavePictwo.setVisibility(VISIBLE);
                        llvieresyis.setVisibility(VISIBLE);
                        yidonexiangguan.setText("(3):");
                        lld.setVisibility(VISIBLE);
                        Glide.with(getContext()).load(http[0]).into(ivleavePic);
                        Glide.with(getContext()).load(http[1]).into(ivleavePicone);
                        Glide.with(getContext()).load(http[2]).into(ivleavePictwo);
                        break;
                }
            }
        }else{
            ivleavePic.setVisibility(GONE);
            lld.setVisibility(GONE);
            llyids.setVisibility(GONE);
            llvieresyis.setVisibility(GONE);
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
        void onCancelCollect(int id);
//        void onCancelCollect(View v,int  id,String type);
    }
}
