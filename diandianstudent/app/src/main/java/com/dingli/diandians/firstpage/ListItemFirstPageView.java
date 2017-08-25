package com.dingli.diandians.firstpage;

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

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by dingliyuangong on 2016/3/8.
 */
public class ListItemFirstPageView extends RelativeLayout {
    TextView dididian,dishijia,dijie,kechengming,latertv,tvpand;
    private onCancelCollectListener mOnCancelInterface;
    SimpleDateFormat formatter;
    int id;
    String auto;
    Button btxiao,btxiangqi;
    View viewxian;
    ImageView ivgaizhang;
    LinearLayout lilater;
    long secod;
    public ListItemFirstPageView(Context context) {
        super(context);
    }
    public ListItemFirstPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemFirstPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dididian=getView(R.id.dididian);
        dishijia=getView(R.id.dishijia);
        dijie=getView(R.id.dijie);
        kechengming=getView(R.id.kechengming);
        btxiao=getView(R.id.btxiao);
        viewxian=getView(R.id.viewxian);
        latertv=getView(R.id.latertv);
        ivgaizhang=getView(R.id.ivgaizhang);
        tvpand=getView(R.id.tvpand);
        lilater=getView(R.id.lilater);
        btxiangqi=getView(R.id.btxiangqi);
    }
    public void setListFirst(Course result){
        formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm");
        Date curDate    =   new    Date();
        secod=curDate.getTime();
        dididian.setText(result.classRoom);
        dishijia.setText(result.classBeginTime+"-"+result.classEndTime);
        dijie.setText(result.whichLesson);
        kechengming.setText(result.courseName);
        if (result.lateTime!=0) {
            lilater.setVisibility(VISIBLE);
            latertv.setText("上课" + result.lateTime + "分钟之后");
        }else{
            lilater.setVisibility(GONE);
        }
//        if(result.canReport==true){
//            viewxian.setVisibility(VISIBLE);
//            btxiao.setVisibility(VISIBLE);
//            btxiao.setClickable(true);
//        }else {
//            viewxian.setVisibility(GONE);
//            btxiao.setVisibility(GONE);
//        }
        if (!TextUtils.isEmpty(result.type)) {
            switch (result.type){
                case "1":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_yidao);
                    tvpand.setText("已到");
                    tvpand.setTextColor(getResources().getColor(R.color.yidao));
                    break;
                case "2":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("旷课");
                    tvpand.setTextColor(getResources().getColor(R.color.dianmingzhong));
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_kuangke);
                    break;
                case "3":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("迟到");
                    tvpand.setTextColor(getResources().getColor(R.color.chidao));
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_chidao);
                    break;
                case "4":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("请假");
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_qingjia);
                    tvpand.setTextColor(getResources().getColor(R.color.qingjiae));
                    break;
                case "5":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("早退");
                    tvpand.setTextColor(getResources().getColor(R.color.zaotui));
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_zaotui);
                    break;
                case "6":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("签到确认中");
                    tvpand.setTextColor(getResources().getColor(R.color.dianmingzhong));
                    ivgaizhang.setBackgroundResource(R.mipmap.icon_qdz);
                    break;
                case "8":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("异常 >" + result.deviation + "m");
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_yic);
                    tvpand.setTextColor(getResources().getColor(R.color.yid));
                    break;
                case "9":
                    ivgaizhang.setVisibility(VISIBLE);
                    tvpand.setVisibility(VISIBLE);
                    tvpand.setText("已取消考勤");
                    ivgaizhang.setBackgroundResource(R.mipmap.lieb_quxiao);
                    tvpand.setTextColor(getResources().getColor(R.color.yid));
                    break;
                default:
                    ivgaizhang.setVisibility(GONE);
                    tvpand.setVisibility(GONE);
                    break;
            }
        }else{
            ivgaizhang.setVisibility(GONE);
            tvpand.setVisibility(GONE);
        }
     dateCompares(result.assessScore,result.type, result.canReport,result.localtion, result.teach_time + " " + result.classBeginTime, result.teach_time + " " + result.classEndTime);
    }
    void dateCompares(String score,String type,boolean b,String loca,String one,String two){
        try {
            Date d1 = formatter.parse(one);
            Date d2 = formatter.parse(two);
            if(d1.getTime() - secod-4*60*1000>0) {
                viewxian.setVisibility(GONE);
                btxiao.setVisibility(GONE);
                btxiangqi.setVisibility(GONE);
            }else if(d2.getTime()-secod<0){
                if (!TextUtils.isEmpty(score)){
                    if (score.equals("0")){
                        if(type.equals("4")){
                            viewxian.setVisibility(GONE);
                            btxiangqi.setVisibility(GONE);
                        }else if (type.equals("2")){
                            viewxian.setVisibility(GONE);
                            btxiangqi.setVisibility(GONE);
                        }else {
                            viewxian.setVisibility(VISIBLE);
                            btxiangqi.setVisibility(VISIBLE);
                        }
                    }else if(!score.equals("0")){
                        viewxian.setVisibility(GONE);
                        btxiangqi.setVisibility(GONE);
                    }
                }else{
                    viewxian.setVisibility(VISIBLE);
                    btxiangqi.setVisibility(VISIBLE);
                }
                if(b==true){
                    btxiao.setVisibility(VISIBLE);
                    viewxian.setVisibility(VISIBLE);
                }else {
                    btxiao.setVisibility(GONE);
                    viewxian.setVisibility(GONE);
                }
            }else{
                if(b==true){
                    viewxian.setVisibility(VISIBLE);
                    btxiao.setVisibility(VISIBLE);
                    btxiangqi.setVisibility(GONE);
                }else {
                    btxiao.setVisibility(GONE);
                    viewxian.setVisibility(GONE);
                    btxiangqi.setVisibility(GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setId(int id) {
        this.id = id;
    }
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
    public void setmOnCancelInterface(onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
    public interface onCancelCollectListener {
        void onCancelCollect(String courseName,String rollcallType,String type,String rollendtime,String isAu,int id,boolean bd,String location,int position);
    }
}
