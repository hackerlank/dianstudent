package com.dingli.diandians.firstpage.hybrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;


import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.bean.InJavaScriptInterface;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.HybridShouyeFragment;
import com.dingli.diandians.firstpage.school.SchoolWebsActivity;
import com.dingli.diandians.found.WebViewLianxiActivity;
import com.dingli.diandians.found.WebViewLianxisActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.survey.WebViewSurveyActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/9/29.
 */
public class AndroidObjectInJavascript {
      private Activity activity;
    public AndroidObjectInJavascript(Activity activity){
        this.activity = activity;
    }
    @JavascriptInterface
    public String accessTokenAddTokenType(){
        DianTool.huoqutoken();
        return  DianApplication.user.token;
    }
    @JavascriptInterface
    public void hideNavigation(String navitaga){
    }
    @JavascriptInterface
    public void backNative(){
         activity.finish();
        activity.overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void backExerciseHome(){
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void errorHandle(String unvalid_token){
       Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
          activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void errorHandle(){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void operateType(String url){
        Datas datas= JSON.parseObject(url, Datas.class);
        String id=datas.url.substring(datas.url.indexOf("?") + 1);
        DianApplication.user.wenzhangid=id;
        Intent intent=new Intent(activity, SchoolWebsActivity.class);
        intent.putExtra("url",datas.url);
        intent.putExtra("title",datas.title);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public String homeLisWithID(){
        return DianApplication.user.wenzhangid;
    }
     @JavascriptInterface
    public void examCurrentStateName(String url){
         DianApplication.user.weburl=url;
     }
    @JavascriptInterface
    public void addEventListener(String eventName, String handler, String args){
    }
    @JavascriptInterface
    public void redirectToExercise(){
        Intent intent=new Intent(activity, WebViewLianxisActivity.class);
        intent.putExtra("url", Constant.lianxis);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
}
