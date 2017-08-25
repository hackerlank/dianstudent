package com.dingli.diandians.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.firstpage.FirstPageFragment;
import com.dingli.diandians.firstpage.submit.SubminActivity;
import com.dingli.diandians.information.instructor.InsLocationActivity;
import com.dingli.diandians.information.instructor.SignNoteActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.numbertest.NumberTestActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/12/8.
 */
public class Initoken {
    public static void initoken(final Context context){
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITONE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, "");
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, "");
        DianApplication.user.token="";
        DianApplication.user.token_type="";
        if (DianTool.isConnectionNetWork(context)){
            DianApplication.user.account=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNTS);
            DianApplication.user.password=DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORDS);
            DianApplication.user.refresh_token=DianApplication.sharedPreferences.getStringValue(Constant.REFRESHED);
            HttpParams params1=new HttpParams();
            HttpHeaders headers=new HttpHeaders();
            headers.put("Content-Type", Constant.APPLICATION_FORMURL);
            headers.put("Encoding", "UTF-8");
            headers.put("Accept", Constant.APPLICATION_JSON);
            headers.put("Authorization", "Basic ZGxlZHVBcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==");
            params1.put("username", DianApplication.user.account);
            params1.put("password", DianApplication.user.password);
            params1.put("grant_type", "refresh_token");
            params1.put("refresh_token",DianApplication.user.refresh_token);
            params1.put("scope", "read write");
            params1.put("client_secret", "mySecretOAuthSecret");
            params1.put("client_id", "dleduApp");
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.post(HostAdress.getRequestUrl("/oauth/token"))     // 请求方式和请求url
                    .tag(context).params(params1)                       // 请求的 tag, 主要用于取消对应的请求
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            // s 即为所需要的结果
                            ResultInfo resultInfotoken = JSON.parseObject(s, ResultInfo.class);
                            DianApplication.user.token = resultInfotoken.access_token;
                            DianApplication.user.token_type = resultInfotoken.token_type;
                            DianApplication.user.refresh_token=resultInfotoken.refresh_token;
                            String[] strings=resultInfotoken.access_token.split("-");
                            DianApplication.sharedPreferences.saveString(Constant.SPLITONE,strings[0]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITTWO,strings[1]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE,strings[2]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR,strings[3]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE,strings[4]);
                            DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, resultInfotoken.access_token);
                            DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, resultInfotoken.token_type);
                            DianApplication.sharedPreferences.saveString(Constant.REFRESHED,resultInfotoken.refresh_token);

                            /**新加*/
                            DianApplication.getInstance().setAuthorization(resultInfotoken.token_type+ "" + resultInfotoken.access_token);//保存tgt
                            DianApplication.getInstance().setRefreshToken(resultInfotoken.refresh_token);//保存tgt
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Intent intent=new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    });
        }else{
            DianTool.showTextToast(context,"请检查网络");
        }
    }
    public static void signId(final Context activity){
        if (DianTool.isConnectionNetWork(activity)){
            final List<Integer> alist=new ArrayList<>();
            DianApplication.user.token_type=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
            DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE)+"-"+
                    DianApplication.sharedPreferences.getStringValue(Constant.SPLITTWO)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITTHREE)
                    +"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFOUR)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFIVE);
            HttpHeaders headers=new HttpHeaders();
            HttpParams params=new HttpParams();
            headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
            params.put("offset", 1);
            params.put("limit",500);
            OkGo.get(HostAdress.getZheRe("/api/phone/v1/students/courselist/get")).tag(activity)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if(!TextUtils.isEmpty(s)) {
                        ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                        for (int i = 0; i < resultInfo.data[0].courseList.size(); i++) {
                            if (resultInfo.data[0].courseList.get(i).canReport == true) {
                                alist.add(i);
                            }
                        }
                        Intent intent = new Intent();
                        if (alist.size() == 1) {
                            if (!resultInfo.data[0].courseList.get(alist.get(0)).type.equals("4")) {
                                if (!TextUtils.isEmpty(resultInfo.data[0].courseList.get(alist.get(0)).rollcallType)) {
                                    if (resultInfo.data[0].courseList.get(alist.get(0)).rollcallType.equals("automatic")) {
                                        intent.setClass(activity, SubminActivity.class);
                                        intent.putExtra(Constant.COURSENAME, resultInfo.data[0].courseList.get(alist.get(0)).courseName);
                                        intent.putExtra(Constant.KE_ID, resultInfo.data[0].courseList.get(alist.get(0)).scheduleId);
                                        intent.putExtra(Constant.ROLLCALLTYPE, resultInfo.data[0].courseList.get(alist.get(0)).rollcallType);
                                        intent.putExtra("intoken", "intoken");
                                        activity.startActivity(intent);
                                    } else {
                                        intent.setClass(activity, NumberTestActivity.class);
                                        intent.putExtra("schedu", resultInfo.data[0].courseList.get(alist.get(0)).scheduleId);
                                        intent.putExtra("location", resultInfo.data[0].courseList.get(alist.get(0)).localtion);
                                        intent.putExtra(Constant.COURSENAME, resultInfo.data[0].courseList.get(alist.get(0)).courseName);
                                        intent.putExtra("intoken", "intoken");
                                        activity.startActivity(intent);
                                    }
                                }
                            } else {
                                intent.setClass(activity, FirstPageFragment.class);
                                activity.startActivity(intent);
                            }
                        } else {
                            intent.setClass(activity, FirstPageFragment.class);
                            activity.startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    Intent intent=new Intent();
                    intent.setClass(activity, FirstPageFragment.class);
                    activity.startActivity(intent);
                }
            });
        } else {
            DianTool.showTextToast(activity, "请检查网络");
        }
    }
    public static void signNote(final Context activity){
        if (DianTool.isConnectionNetWork(activity)){
            HttpHeaders headers=new HttpHeaders();
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/students/getRollCallEver")).tag(activity)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    List<ResultInfoCall> resultInfoCall= JSON.parseArray(s,ResultInfoCall.class);
                    Intent intent=new Intent();
                    if (resultInfoCall.size()!=0) {
                        if (resultInfoCall.get(0).status == true) {
                            intent.setClass(activity, InsLocationActivity.class);
                            intent.putExtra(Constant.SUISIGN, resultInfoCall.get(0).id);
                            intent.putExtra(Constant.SUISICI, "1");
                        } else {
                            intent.setClass(activity, SignNoteActivity.class);
                        }
                    }else{
                        intent.setClass(activity, SignNoteActivity.class);
                    }
                    activity.startActivity(intent);
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.response(response,activity);
                }
            });
        }else{
            DianTool.showTextToast(activity, "请检查网络");
        }
    }

    private static void successorfail() {

    }
}
