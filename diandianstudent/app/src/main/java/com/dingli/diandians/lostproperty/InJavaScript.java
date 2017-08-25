package com.dingli.diandians.lostproperty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.Datas;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.personcenter.ActionSheetDialog;
import com.dingli.diandians.service.DaemonService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by dingliyuangong on 2017/5/12.
 */

public class InJavaScript  {
    UpPhone phone;
    Activity context;
    X5WebView webView;
    public InJavaScript(Activity context,UpPhone upPhone,X5WebView webViews){
        this.phone=upPhone;
        this.context=context;
        this.webView=webViews;
    }
    @JavascriptInterface
    public String accessTokenAddTokenType(){
        DianTool.huoqutoken();
        return  DianApplication.user.token;
    }
    @JavascriptInterface
    public void callAppNative(String url){
        Log.d("callAppNative",url);
        Datas datas= JSON.parseObject(url,Datas.class);
        switch (datas.type){
            case "camera":

//                String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//                if (EasyPermissions.hasPermissions(context, perms)) {
//
//                } else {
//                    EasyPermissions.requestPermissions(context, context.getString(R.string.permission_hint),
//                            READ_EXTERNAL_STORAGE_AND_CAMERA, perms);
//                }

                new ActionSheetDialog(context)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        phone.upphone("photograph","");
                                    }
                                })
                        .addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        phone.upphone("album","");
                                    }
                                })
                        .show();
                break;
            case"phone":
                phone.upphone("phone",datas.param1);
                break;
            case "copy":
                phone.upphone("copy",datas.param1);
                break;
        }
    }
    @JavascriptInterface
    public String getUserInfo(){
        return DianApplication.sharedPreferences.getStringValue(Constant.INFO);
    }
    @JavascriptInterface
    public String noticeMessage(String json){

        return json;
    }
    @JavascriptInterface
    public String callAppNativeWithPhotosUrl(String trs){

        return trs;
    }
    @JavascriptInterface
    public void backNative(){
        context.finish();
        context.overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void errorHandle(){
        Intent intent=new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void errorHandle(String str){
        Intent intent=new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    public interface UpPhone{
        void upphone(String name,String str);
    }

    private static final int READ_EXTERNAL_STORAGE_AND_CAMERA = 0x001;
}
