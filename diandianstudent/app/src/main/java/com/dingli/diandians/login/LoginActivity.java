package com.dingli.diandians.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingli.diandians.MainActivity;
import com.dingli.diandians.MainActivy;
import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.CheatDialog;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.GuanBiDialog;
import com.dingli.diandians.common.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.setting.ForgetActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class LoginActivity extends BaseActivity {

    EditText etphoneid,etpassowrd;
    Button btlogin;
    TextView tvforgert;
    String phone;
    String password;
    HttpHeaders headers;
    ResultInfo resultInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DianApplication.user.login=this;
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
                init();
    }
    public void init(){
        tvforgert=(TextView)findViewById(R.id.tvforgert);
        btlogin=(Button)findViewById(R.id.btlogin);
        etphoneid=(EditText)findViewById(R.id.etphoneid);
        etpassowrd=(EditText)findViewById(R.id.etpassowrd);
        String accounts=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNTS);
        String passwords=DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORDS);
        if(!TextUtils.isEmpty(accounts)){
            etphoneid.setText(accounts);
        }
        if(!TextUtils.isEmpty(passwords)){
            etpassowrd.setText(passwords);
        }
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etphoneid.getText().toString().trim();
                password = etpassowrd.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    DianTool.showTextToast(LoginActivity.this, getResources().getString(R.string.shoukong));
                } else if (TextUtils.isEmpty(password)) {
                    DianTool.showTextToast(LoginActivity.this, getResources().getString(R.string.mikong));
                } else if (DianTool.isConnectionNetWork(LoginActivity.this)) {
                    btlogin.setClickable(false);
                    DianTool.showMyDialog(LoginActivity.this, "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btlogin.setClickable(true);
                            DianTool.dissMyDialog();
                        }
                    },10000);
                    HttpParams params = new HttpParams();
                    params.put("username", phone);
                    params.put("password", password);
                    params.put("grant_type", "password");
                    params.put("scope", "read write");
                    params.put("client_secret", "mySecretOAuthSecret");
                    params.put("client_id", "dleduApp");
                    headers.put("Authorization", "Basic ZGxlZHVBcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==");
                    OkGo.post(HostAdress.getRequestUrl("/oauth/token")).tag(this)
                            .headers(headers).params(params).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            btlogin.setClickable(true);
                            if (!s.equals("{}")) {
                                resultInfo = JSON.parseObject(s, ResultInfo.class);
                                DianApplication.user.account = phone;
                                DianApplication.user.password = password;
                                DianApplication.user.token = resultInfo.access_token;
                                DianApplication.user.token_type = resultInfo.token_type;
                                DianApplication.user.refresh_token = resultInfo.refresh_token;
                                DianApplication.user.strtoken = resultInfo.access_token.split("-");
                                DianApplication.sharedPreferences.saveString(Constant.SPLITONE, DianApplication.user.strtoken[0]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, DianApplication.user.strtoken[1]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, DianApplication.user.strtoken[2]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, DianApplication.user.strtoken[3]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, DianApplication.user.strtoken[4]);
                                DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORDS, password);

                                DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, resultInfo.access_token);
                                DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, resultInfo.token_type);
                                DianApplication.sharedPreferences.saveString(Constant.REFRESHED, resultInfo.refresh_token);

                                DianApplication.getInstance().setAuthorization(resultInfo.token_type+ "" + resultInfo.access_token);//保存tgt
                                DianApplication.getInstance().setRefreshToken(resultInfo.refresh_token);//保存tgt
                                DianApplication.getInstance().setUserInfo(DianApplication.user);
                                yanzhen();
                            } else {
                                DianTool.dissMyDialog();
                                DianTool.kongtoken();
                                DianTool.showTextToast(LoginActivity.this, "账号或密码错误");
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            btlogin.setClickable(true);
                           DianTool.kongtoken();
                            if(response!=null) {
                                if (response.code()==400) {
                                    DianTool.showTextToast(LoginActivity.this, "账号或密码错误");
                                }else{
                                    DianTool.showTextToast(LoginActivity.this, "登录失败");
                                }
                            }else{
                                DianTool.showTextToast(LoginActivity.this, "登录失败");
                            }
                        }
                    });
                } else {
                    DianTool.dismissProgressDialog();
                    DianTool.showTextToast(LoginActivity.this, getResources().getString(R.string.wangxi));
                }
            }
        });
        tvforgert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });
    }
    void yanzhen(){
        if (DianTool.isConnectionNetWork(this)) {
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/user/info")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianApplication.sharedPreferences.saveString(Constant.INFO,s);
                    btlogin.setClickable(true);
                    DianTool.dissMyDialog();
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("{}")) {
                            ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                            if (resultInfo.role.equals("ROLE_STUDENT")) {
                                DianApplication.sharedPreferences.saveString(Constant.ORGAINER, resultInfo.organName);
                                zhixin();
                            } else {
                                DianTool.kongtoken();
                                DianTool.showTextToast(LoginActivity.this, "这不是学生账号,请输入学生账号!");
                            }
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                    btlogin.setClickable(true);
                    DianTool.kongtoken();
                    if (response != null) {
                        if (response.code() == 400) {
                            DianTool.showTextToast(LoginActivity.this, "账号或密码错误");
                        } else {
                            DianTool.showTextToast(LoginActivity.this, "登录失败");
                        }
                    }else{
                        DianTool.showTextToast(LoginActivity.this, "登录失败");
                    }
                }
            });
        }
        }
    void zhixin(){
        DianApplication.sharedPreferences.saveString(Constant.USER_ACCOUNTS, phone);
        if (password.equals("123456")) {
            Intent intent = new Intent(LoginActivity.this, FirstAlterActivity.class);
            startActivity(intent);
        } else {
            Intent intenttwo = new Intent(LoginActivity.this, MainActivy.class);
            startActivity(intenttwo);
            for (int i = 0; i < DianApplication.activityList.size(); i++) {
                DianApplication.activityList.get(i).finish();
            }
        }
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
        @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
