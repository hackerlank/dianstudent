package com.dingli.diandians.firstpage.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.ResultOne;
import com.dingli.diandians.firstpage.FirstPageFragment;
import com.dingli.diandians.firstpage.WebViewOneActivity;
import com.dingli.diandians.firstpage.WebViewTwoActivity;
import com.dingli.diandians.firstpage.WebViewsActivity;
import com.dingli.diandians.firstpage.school.SchoolWebActivity;
import com.dingli.diandians.found.WebViewLianxiActivity;
import com.dingli.diandians.information.instructor.SignNoteActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.lostproperty.WebViewLostActivity;
import com.dingli.diandians.newProject.moudle.home.Schedule.ScheduleActivity;
import com.dingli.diandians.qingjia.VacateActivity;
import com.dingli.diandians.schedule.SyllFragment;
import com.dingli.diandians.survey.WebViewSurveyActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dingliyuangong on 2017/5/4.
 */

public class ShouYeGridAdapter extends BaseAdapter{
    ResultOne[] listgride;
    public ShouYeGridAdapter(ResultOne[] listgr){
        this.listgride=listgr;
    }
    @Override
    public int getCount() {
        return listgride.length;
    }

    @Override
    public Object getItem(int position) {
        return listgride[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ShouYeHolder shouYeHolder=null;
        if (convertView==null){
            shouYeHolder=new ShouYeHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shouyes,null);
            shouYeHolder.iv=(ImageView) convertView.findViewById(R.id.ivd);
            shouYeHolder.tv=(TextView) convertView.findViewById(R.id.titlepage);
            shouYeHolder.grideitem=(LinearLayout) convertView.findViewById(R.id.grideitem);
            convertView.setTag(shouYeHolder);
        }else {
            shouYeHolder=(ShouYeHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(listgride[position].iconUrl).into(shouYeHolder.iv);
        shouYeHolder.tv.setText(listgride[position].title);
        final String title=listgride[position].title.trim();
        final String targetTypes=listgride[position].targetType.trim();
        final String targetUrl=listgride[position].targetUrl.trim();
        final boolean isNeedLogin=listgride[position].isNeedLogin;
        final String targetTitle=listgride[position].targetTitle;
        shouYeHolder.grideitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                MobclickAgent.onEvent(parent.getContext(),title);
                if (targetTypes.equals("app")) {
                    switch (targetUrl) {
                        case "DLHomeDianMing":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                                parent.getContext().startActivity(intent);
                            }else {
                                Initoken.signId(parent.getContext());
                            }
                        break;
                        case "DLHomeLeave":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), VacateActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "DLHomeKeBiao":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), SyllFragment.class);
                             //   intent.setClass(parent.getContext(), ScheduleActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "DLHomeReMen":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), FirstPageFragment.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "assistantCall":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                                parent.getContext().startActivity(intent);
                            }else {
                                Initoken.signNote(parent.getContext());
                            }
                            break;
                        case "DLHomeExercise":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), WebViewLianxiActivity.class);
                                bundle.putString("url", Constant.weblianxi+"/mobileui/");
                                intent.putExtras(bundle);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        default:
                            DianTool.showTextToast(parent.getContext(),"当前版本不支持此功能，请升级最新版本");
                            break;
                    }
                }else if (targetTypes.equals("web")){
                    switch (title) {
                        case "校园动态":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), SchoolWebActivity.class);
                                intent.putExtra("url", "/mobileui/schoolNews");
                                intent.putExtra("title", "校园动态");
                                intent.putExtra("isRefresh", false);
                                intent.putExtra("isStatusBar", true);
                                intent.putExtra("domainName", "dd_mobile");
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "失物招领":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), WebViewLostActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "点点心理":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), WebViewsActivity.class);
                                intent.putExtra("url", "/mobileui/allArticle");
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        default:
                            if (isNeedLogin){
                                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                    intent.setClass(parent.getContext(), LoginActivity.class);
                                }else {
                                    intent.setClass(parent.getContext(), WebViewTwoActivity.class);
                                    intent.putExtra("targetTitle",targetTitle);
                                    intent.putExtra("targetUrl",targetUrl);
                                }
                            }else{
                                intent.setClass(parent.getContext(), WebViewTwoActivity.class);
                                intent.putExtra("targetTitle",targetTitle);
                                intent.putExtra("targetUrl",targetUrl);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                    }
                }
            }
        });
        return convertView;
    }
    class ShouYeHolder{
        ImageView iv;
        TextView tv;
        LinearLayout grideitem;
    }
}
