package com.dingli.diandians.common;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.dingli.diandians.R;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by dingliyuangong on 2016/3/9.
 */
public class BaseActivity extends FragmentActivity{
     DianApplication dianApplication=DianApplication.getInstance();
    PopupWindow popupWindowsd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        dianApplication.addActivity(this);
        realizePopups();
    }
    @Override
    protected void onDestroy() {
        dianApplication.remove(this);
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void realizePopups() {
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_rightpopup,null);
        LinearLayout lldiankebiao=(LinearLayout)view.findViewById(R.id.llinfor);
        LinearLayout llinfor=(LinearLayout)view.findViewById(R.id.llfound);
        LinearLayout llshouye=(LinearLayout)view.findViewById(R.id.llshouye);
        LinearLayout llmine=(LinearLayout)view.findViewById(R.id.llmine);
        lldiankebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<DianApplication.activityList.size();i++){
                    if(DianApplication.activityList.get(i)!=DianApplication.user.main){
                        DianApplication.activityList.get(i).finish();
                    }
                }
                DianApplication.user.libiao="infor";
            }
        });
        llshouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<DianApplication.activityList.size();i++){
                    if(DianApplication.activityList.get(i)!=DianApplication.user.main){
                        DianApplication.activityList.get(i).finish();
                    }
                }
                DianApplication.user.libiao=null;
            }
        });
        llinfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<DianApplication.activityList.size();i++){
                    if(DianApplication.activityList.get(i)!=DianApplication.user.main){
                        DianApplication.activityList.get(i).finish();
                    }
                }
                DianApplication.user.libiao="found";
            }
        });
        llmine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<DianApplication.activityList.size();i++){
                    if(DianApplication.activityList.get(i)!=DianApplication.user.main){
                        DianApplication.activityList.get(i).finish();
                    }
                }
                DianApplication.user.libiao="mine";
            }
        });
        popupWindowsd=new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowsd.setOutsideTouchable(true);
        popupWindowsd.setBackgroundDrawable(new BitmapDrawable());
        popupWindowsd.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindowsd.update();
    }
    public void tanchu(View v){
        popupWindowsd.showAsDropDown(v);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
