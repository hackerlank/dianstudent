package com.dingli.diandians.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.MainActivy;
import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.GengXiDialog;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.login.AlterActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    TextView lloutlogin,tvshoujihm,banbenmin;
    ImageView ddsetts;
    LinearLayout llalter;
    LinearLayout faxian;
    TextView huancundaxiao;
    View viset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        initdata();
    }
    public void init(){
        banbenmin=(TextView)findViewById(R.id.banbenmin);
        ImageView setingback=(ImageView)findViewById(R.id.setingback);
        llalter=(LinearLayout)findViewById(R.id.llalter);
        LinearLayout llbindphone=(LinearLayout)findViewById(R.id.llbindphone);
        LinearLayout llhelpcenter=(LinearLayout)findViewById(R.id.llhelpcenter);
        huancundaxiao=(TextView) findViewById(R.id.huancundaxiao);
        viset=findViewById(R.id.viset);
        try {
            if (DataCleanManager.getTotalCacheSize(this).equals("0K")){
                huancundaxiao.setText("");
            }else {
                huancundaxiao.setText(DataCleanManager.getTotalCacheSize(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        faxian=(LinearLayout) findViewById(R.id.faxian);
        lloutlogin=(TextView)findViewById(R.id.outlogin);
        ddsetts=(ImageView)findViewById(R.id.ddsetts);
        tvshoujihm=(TextView)findViewById(R.id.tvshoujihm);
        ddsetts.setOnClickListener(this);
        llalter.setOnClickListener(this);
        llbindphone.setOnClickListener(this);
        lloutlogin.setOnClickListener(this);
        setingback.setOnClickListener(this);
        llhelpcenter.setOnClickListener(this);
        faxian.setOnClickListener(this);
        banbenmin.setText("V" + DianApplication.code);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.BANGDING))){
            tvshoujihm.setText(DianApplication.sharedPreferences.getStringValue(Constant.BANGDING));
        }
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(SettingActivity.this)) {
            DianApplication.user.account=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNTS);
            HttpParams params = new HttpParams();
            params.put("account", DianApplication.user.account);
            OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/checkuserisexist")).tag(this)
                    .params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (s.equals("{}")) {
                        tvshoujihm.setText("未绑定");
                    } else {
                        Result result = JSON.parseObject(s, Result.class);
                        if(result.phone!=null) {
                            tvshoujihm.setText(result.phone.substring(0, 3) + "****" + result.phone.substring(7, 11));
                            DianApplication.sharedPreferences.saveString(Constant.BANGDING,result.phone.substring(0, 3) + "****" + result.phone.substring(7, 11));
                        }
                    }
                    initbanben();
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    tvshoujihm.setText("未绑定");
                    initbanben();
                }
            });
        } else {
            DianTool.showTextToast(SettingActivity.this, "请检查网络");
        }
    }
    void initbanben(){
        DianTool.huoqutoken();
        HttpParams params=new HttpParams();
        HttpHeaders headers=new HttpHeaders();
        headers.put("Authorization",DianApplication.user.token_type + " " + DianApplication.user.token);
        params.put("type","android");
        params.put("role","student");
        OkGo.get(HostAdress.getBanben("/api/web/v1/upgrade/getInfo")).tag(this)
                .params(params).headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (!TextUtils.isEmpty(s)) {
                    if (!s.equals("{}")) {
                        Result result = JSON.parseObject(s, Result.class);
                        String str = result.version.substring(0, 1) + result.version.substring(2, 3) + result.version.substring(4, 5);
                        String strs = DianApplication.code.substring(0, 1) + DianApplication.code.substring(2, 3) + DianApplication.code.substring(4, 5);
                        if (Integer.parseInt(str) > Integer.parseInt(strs)) {
                            faxian.setVisibility(View.VISIBLE);
                            viset.setVisibility(View.VISIBLE);
                        } else {
                            faxian.setVisibility(View.GONE);
                            viset.setVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                faxian.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setingback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.llbindphone:
                Intent intent=new Intent(SettingActivity.this,BindPhoneActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.llalter:
                Intent intentone=new Intent(SettingActivity.this,AlterActivity.class);
                startActivity(intentone);
                this.finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.outlogin:
                DianApplication.sharedPreferences.saveString("percenters","");
                DianApplication.sharedPreferences.saveString("touxiangs", "");
                DianApplication.sharedPreferences.saveString(Constant.SPLITONE, "");
                DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, "");
                DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, "");
                DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, "");
                DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, "");
                DianApplication.sharedPreferences.saveString(Constant.ORGAINER,"");
                DianApplication.sharedPreferences.saveString(Constant.BANGDING,"");
                DianApplication.sharedPreferences.saveString(Constant.INFO,"");
                DianApplication.sharedPreferences.saveString("phone","");
                DianApplication.sharedPreferences.saveString("personId","");
                DianApplication.sharedPreferences.saveString("mingName","");
                DianApplication.user.libiao=null;
                DianTool.showTextToast(SettingActivity.this, "退出登录");
                Intent intents=new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intents);
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
            case R.id.llhelpcenter:
                if (!huancundaxiao.getText().toString().equals("")) {
                    DianTool.showTextToast(this, "清除成功");
                    DataCleanManager.clearAllCache(this);
                    huancundaxiao.setText("");
                }
                break;
            case R.id.faxian:
               DianTool.genxin(this);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
