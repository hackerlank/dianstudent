package com.dingli.diandians.firstpage.hybrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.AbsoluteLayout;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.MainActivity;
import com.dingli.diandians.MainActivy;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.firstpage.FirstPageFragment;
import com.dingli.diandians.firstpage.HybridShouyeFragment;
import com.dingli.diandians.firstpage.WebViewActivity;
import com.dingli.diandians.firstpage.WebViewOneActivity;
import com.dingli.diandians.firstpage.WebViewTwoActivity;
import com.dingli.diandians.firstpage.WebViewsActivity;
import com.dingli.diandians.firstpage.school.SchoolWebActivity;
import com.dingli.diandians.found.WebViewLianxiActivity;
import com.dingli.diandians.information.instructor.SignNoteActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.personcenter.ActionSheetDialog;
import com.dingli.diandians.qingjia.VacateActivity;
import com.dingli.diandians.schedule.SyllFragment;
import com.dingli.diandians.survey.WebViewSurveyActivity;

import org.json.JSONObject;

import java.util.Objects;

import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by dingliyuangong on 2016/9/29.
 */
public class AndroidObjectInJavascripts {
      private Activity activity;
    Datas datas;
    public AndroidObjectInJavascripts( Activity activity){
        this.activity = activity;
    }
    @JavascriptInterface
    public String accessTokenAddTokenType(){
        DianTool.huoqutoken();
        return DianApplication.user.token;
    }
    /**
     * 设置 对应fragment的标题. 通过 消息机制来实现.
     */
    @JavascriptInterface
    public String getClientType(){
        return "student";
    }
    @JavascriptInterface
    public String BundleShortVersion(){
        return DianApplication.code;
    }
    @JavascriptInterface
    public void operateType(String url){
        datas=new Datas(url);
        Datas datas= JSON.parseObject(url, Datas.class);
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        if (datas.operateType.equals("DLHomeDianMing")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                }else {
                Initoken.signId(activity);
                }
        }else if (datas.operateType.equals("DLHomeKeBiao")){
                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                }else {
                    intent.setClass(activity, SyllFragment.class);
                    activity.startActivity(intent);
                }
        }else if (datas.operateType.equals("DLHomeReMen")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity,LoginActivity.class);
                activity.startActivity(intent);
            }else {
                intent.setClass(activity, FirstPageFragment.class);
                activity.startActivity(intent);
            }
        }else if (datas.operateType.equals("DLHomeHeadList")){
                intent.setClass(activity, WebViewActivity.class);
                bundle.putString("url", datas.url);
                bundle.putString("list","list");
                    intent.putExtras(bundle);
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeHeadListV2")){
            if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                intent.setClass(activity, LoginActivity.class);
            }else {
                String id = datas.url.substring(datas.url.indexOf("?") + 1);
                intent.setClass(activity, WebViewActivity.class);
                bundle.putString("url", datas.url);
                bundle.putString("id", id);
                bundle.putString("list","listv2");
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeBanner")){
            intent.setClass(activity, WebViewsActivity.class);
            intent.putExtra("url", datas.url);
            intent.putExtra("title", datas.title);
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeExercise")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity,LoginActivity.class);
                activity.startActivity(intent);
            }else {
                intent.setClass(activity, WebViewLianxiActivity.class);
                bundle.putString("url", Constant.weblianxi+"/mobileui/");
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        }else if (datas.operateType.equals("DLHomeLeave")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity,LoginActivity.class);
            }else {
                intent.setClass(activity, VacateActivity.class);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeArticleList")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewsActivity.class);
                intent.putExtra("url", datas.url);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash1")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash2")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash3")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else  if (datas.operateType.equals("DLHomeReserveNoRefrash4")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if(datas.operateType.equals("DLHomeReserveNoRefrash5")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeRedirect")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                if (datas.domainName.equals("dd_mobile")){
                    if (datas.url.contains("questatuslist")){
                        intent.setClass(activity, WebViewSurveyActivity.class);
                        bundle.putString("url",Constant.webdiaocha+"/mobileui/questatuslist");
                        intent.putExtras(bundle);
                    }else {
                        intent.setClass(activity, SchoolWebActivity.class);
                        intent.putExtra("url", datas.url);
                        intent.putExtra("title", datas.title);
                        intent.putExtra("isRefresh", datas.isRefresh);
                        intent.putExtra("isStatusBar", datas.isStatusBar);
                        intent.putExtra("domainName", datas.domainName);
                    }
                }else if (datas.domainName.equals("hy_mobile")){
                    intent.setClass(activity, WebViewOneActivity.class);
                    intent.putExtra("url",datas.url);
                    intent.putExtra("title",datas.title);
                }
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("assistantCall")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, SignNoteActivity.class);
            }
            activity.startActivity(intent);
        }
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void operateTypeV2(String url){
        Datas datas= JSON.parseObject(url, Datas.class);
        Intent intent=new Intent();
        if (datas.operateType.equals("DLHomeReserveNoRefrash1")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash2")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash3")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else  if (datas.operateType.equals("DLHomeReserveNoRefrash4")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if(datas.operateType.equals("DLHomeReserveNoRefrash5")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public String homeLisWithID(){
        return DianApplication.user.wenzhangid;
    }
    @JavascriptInterface
    public void backNative(){
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void errorHandle(){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void errorHandle(String str){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public String getUserInfo(){
        return DianApplication.sharedPreferences.getStringValue(Constant.INFO);
    }
}
