package com.dingli.diandians;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.dingli.diandians.bean.BaseTipsView;
import com.dingli.diandians.bean.ShowMemberTipsView;
import com.dingli.diandians.bean.ShowMoreTipsView;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.GengXiDialog;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.QingJiaSty;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.login.FindActivity;
import com.dingli.diandians.login.FirstAlterActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.service.DaemonService;
import com.dingli.diandians.setting.ForgetActivity;
import com.dingli.diandians.view.NoScrollViewPager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.umeng.message.PushAgent;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/11.
 */
public class MainActivy extends BaseActivity implements View.OnClickListener{

    NoScrollViewPager vpdiandian;
    LinearLayout home_tab_main,home_tab_kebiao,home_tab_found,home_tab_wo;
    TextView tvinfom;
    ImageView ivfirstpage,ivkebiao,ivfound,ivmine;
    String deviceId;
    HttpHeaders headers;
    int studeshu,qiandaoshu,suiqiandao;
    int zione=0;
    private ShowMoreTipsView mShowMoreTipsView;
    private ShowMemberTipsView mShowMemberTipsView;
    int zizeng;
    private AMapLocationClient mLocationClient;
    private AMapLocation aMapLocations;
    TextView tvfirstpage,tvkebiao,tvxiaoxi,tvmine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        headers = new HttpHeaders();
        DianApplication.user.main=this;
        boolean mFirst = DianTool.isFirstEnter(MainActivy.this, MainActivy.this.getClass().getName());
        if(mFirst) {
            Intent mIntent = new Intent();
            mIntent.setClass(MainActivy.this, YiDaoTuActivity.class);
            this.startActivity(mIntent);
            this.finish();
            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
        }else {
            initview();
            MainAdapter adapter=new MainAdapter(getSupportFragmentManager());
            vpdiandian.setAdapter(adapter);
            shouviewTip();
            if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                DianTool.huoqutoken();
                initUMeng();
                initforma();
//                serive();
                inbanben();
                if (DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORDS).equals("123456")){
                    Intent intent = new Intent(MainActivy.this, FirstAlterActivity.class);
                    startActivity(intent);
                }
            }

        }
    }
    void initview(){
        tvfirstpage=(TextView) findViewById(R.id.tvfirstpage);
        tvkebiao=(TextView) findViewById(R.id.tvkebiao);
        tvxiaoxi=(TextView) findViewById(R.id.tvfound);
        tvmine=(TextView) findViewById(R.id.tvmine);
        ivfirstpage=(ImageView)findViewById(R.id.ivfirstpage);
        ivkebiao=(ImageView)findViewById(R.id.ivkebiao);
        ivfound=(ImageView)findViewById(R.id.ivfound);
        ivmine=(ImageView)findViewById(R.id.ivmine);
        vpdiandian=(NoScrollViewPager)findViewById(R.id.vpDianDian);
        tvinfom=(TextView)findViewById(R.id.tvinfom);
        home_tab_main=(LinearLayout)findViewById(R.id.home_tab_main);
        home_tab_kebiao=(LinearLayout)findViewById(R.id.home_tab_kebiao);
        home_tab_found=(LinearLayout)findViewById(R.id.home_tab_found);
        home_tab_wo=(LinearLayout)findViewById(R.id.home_tab_wo);
        ivfirstpage.setBackgroundResource(R.mipmap.firstpage);
        ivkebiao.setBackgroundResource(R.mipmap.xiaoxian);
        ivfound.setBackgroundResource(R.mipmap.getjobed);
        ivmine.setBackgroundResource(R.mipmap.mine);
        home_tab_wo.setOnClickListener(this);
        home_tab_found.setOnClickListener(this);
        home_tab_kebiao.setOnClickListener(this);
        home_tab_main.setOnClickListener(this);
        vpdiandian.setOffscreenPageLimit(4);
    }
    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.home_tab_main:
              DianApplication.user.libiao=null;
              vpdiandian.setCurrentItem(0);
              tabone();
              if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                  if(tvinfom.getVisibility()!=View.VISIBLE) {
                      initforma();
                  }
              }
              break;
          case R.id.home_tab_kebiao:
              if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                  Intent intent=new Intent(this,LoginActivity.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
              }else {
                  DianApplication.user.libiao = "infor";
                  v.clearAnimation();
                  vpdiandian.setCurrentItem(2);
                  tabtwo();
              }
              break;
          case R.id.home_tab_found:
              if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                  Intent intent=new Intent(this,LoginActivity.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
              }else {
                  DianApplication.user.libiao = "found";
                  vpdiandian.setCurrentItem(1);
                  tabthree();
                  if(tvinfom.getVisibility()!=View.VISIBLE) {
                      initforma();
                  }
              }
              break;
          case R.id.home_tab_wo:
              if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                  Intent intent=new Intent(this,LoginActivity.class);
                  startActivity(intent);
                  overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
              }else {
                  DianApplication.user.libiao = "mine";
                  vpdiandian.setCurrentItem(3);
                  tabfour();
                  if(tvinfom.getVisibility()!=View.VISIBLE) {
                      initforma();
                  }
              }
              break;
      }
    }
    private void initUMeng() {
                   final PushAgent mPushAgent = PushAgent.getInstance(this);
                    deviceId=mPushAgent.getRegistrationId();
                    DianApplication.sharedPreferences.saveString(Constant.DEVICEID,deviceId);
                    DianApplication.user.deviceId=deviceId;
                    if (!TextUtils.isEmpty(deviceId)) {
                        inithttp();
                    }

    }
    void inithttp() {
        //教师端的推送(请假)
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            HttpParams params = new HttpParams();
            params.put("android", deviceId);
            headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/postdevicetoken")).tag(this).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                }
            });
        }
    }
    void initforma(){
        tvinfom.setVisibility(View.GONE);
        studeshu=0;
        qiandaoshu=0;
        suiqiandao=0;
        if(DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            HttpParams params=new HttpParams();
            OkGo.get(HostAdress.getRequest("/api/phone/v1/getstatus")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    zione=1;
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            if (!TextUtils.isEmpty(arg.get(i).module)) {
                                if (arg.get(i).module.equals("leave")) {
                                    if (arg.get(i).function.equals("student_notice")) {
                                        studeshu=arg.get(i).notRead;
                                    }
                                }
                                if (arg.get(i).module.equals("questionnaire")){
                                    if (arg.get(i).function.equals("que_student_notice")) {
                                        qiandaoshu=arg.get(i).notRead;
                                    }
                                }
                                if (arg.get(i).module.equals("rollcallever")) {
                                    if (arg.get(i).function.equals("rollCallEver_student_notice")) {
                                        suiqiandao=arg.get(i).notRead;
                                    }
                                }
                                if(studeshu+qiandaoshu+suiqiandao!=0) {
                                    tvinfom.setVisibility(View.VISIBLE);
                                    tvinfom.setText(String.valueOf(studeshu + qiandaoshu+suiqiandao));
                                }else{
                                    tvinfom.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                }
            });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    void tabone(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpage);
        ivkebiao.setBackgroundResource(R.mipmap.xiaoxian);
        ivfound.setBackgroundResource(R.mipmap.getjobed);
        ivmine.setBackgroundResource(R.mipmap.mine);
        tvfirstpage.setTextColor(getResources().getColor(R.color.qianblued));
        tvkebiao.setTextColor(getResources().getColor(R.color.wenzis));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.wenzis));
        tvmine.setTextColor(getResources().getColor(R.color.wenzis));
    }
    void tabtwo(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpagexuan);
        ivkebiao.setBackgroundResource(R.mipmap.xiaoxiliang);
        ivfound.setBackgroundResource(R.mipmap.getjobed);
        ivmine.setBackgroundResource(R.mipmap.mine);
        tvfirstpage.setTextColor(getResources().getColor(R.color.wenzis));
        tvkebiao.setTextColor(getResources().getColor(R.color.qianblued));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.wenzis));
        tvmine.setTextColor(getResources().getColor(R.color.wenzis));
        tvinfom.setVisibility(View.GONE);
    }
    void tabthree(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpagexuan);
        ivkebiao.setBackgroundResource(R.mipmap.xiaoxian);
        ivfound.setBackgroundResource(R.mipmap.getjobing);
        ivmine.setBackgroundResource(R.mipmap.mine);
        tvfirstpage.setTextColor(getResources().getColor(R.color.wenzis));
        tvkebiao.setTextColor(getResources().getColor(R.color.wenzis));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.qianblued));
        tvmine.setTextColor(getResources().getColor(R.color.wenzis));
    }
    void tabfour(){
        ivfirstpage.setBackgroundResource(R.mipmap.firstpagexuan);
        ivkebiao.setBackgroundResource(R.mipmap.xiaoxian);
        ivfound.setBackgroundResource(R.mipmap.getjobed);
        ivmine.setBackgroundResource(R.mipmap.minexuan);
        tvfirstpage.setTextColor(getResources().getColor(R.color.wenzis));
        tvkebiao.setTextColor(getResources().getColor(R.color.wenzis));
        tvxiaoxi.setTextColor(getResources().getColor(R.color.wenzis));
        tvmine.setTextColor(getResources().getColor(R.color.qianblued));
    }
    @Override
    protected void onResume() {
        super.onResume();
         if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.KEY))){
            DianApplication.user.libiao=DianApplication.sharedPreferences.getStringValue(Constant.KEY);
        }
        if(TextUtils.isEmpty(DianApplication.user.libiao)){
            vpdiandian.setCurrentItem(0);
            tabone();
        }else if(DianApplication.user.libiao.equals("infor")){
            vpdiandian.setCurrentItem(2);
            tabtwo();
            DianApplication.sharedPreferences.saveString(Constant.KEY,"");
            tvinfom.setVisibility(View.GONE);
        }else if(DianApplication.user.libiao.equals("found")){
            vpdiandian.setCurrentItem(1);
            tabthree();
        }else if(DianApplication.user.libiao.equals("mine")){
            vpdiandian.setCurrentItem(3);
            tabfour();
        }
}
//    void serive() {
//        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
//            headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
//            if (DianApplication.user.jiangeshijian == 0) {
//                OkGo.get(HostAdress.getRequest("/api/phone/v1/queryTimeInterval")).tag(this)
//                        .headers(headers).execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        if (!TextUtils.isEmpty(s)) {
//                            Result result = JSON.parseObject(s, Result.class);
//                            sevice(result.timeInterval);
//                        }else{
//                            sevice(1800);
//                        }
//                    }
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        sevice(1800);
//                    }
//                });
//            }
//        }
//    }
//
//    void sevice(int timeInterval){
//        DianApplication.sharedPreferences.saveInt(Constant.TIMER,timeInterval);
//        startService(new Intent(MainActivy.this,DaemonService.class));
//     }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }else{
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (zizeng==0) {
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage("该应用需要赋予访问定位的权限，不开启将无法正常工作！")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                        zizeng=1;
                    }
                    return;
                }
            }
        }
    }
    void shouviewTip(){
        if (mShowMoreTipsView == null) {
            mShowMoreTipsView = new ShowMoreTipsView(this);
        }
        mShowMoreTipsView.setOnCloseListener(new ShowMoreTipsView.OnCloseListener() {
            @Override
            public void onClose(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MainActivy.this);
            }
        });
        mShowMoreTipsView.setOnSureListener(new ShowMoreTipsView.OnSureListener() {
            @Override
            public void onSure(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MainActivy.this);
                showMemberTipsView();
            }
        });
        mShowMoreTipsView.show(this);
    }
    private void showMemberTipsView() {
        if (mShowMemberTipsView == null) {
            mShowMemberTipsView = new ShowMemberTipsView(this);
        }
        mShowMemberTipsView.setOnCloseListener(new ShowMemberTipsView.OnCloseListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClose(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MainActivy.this);
                if (DianTool.getsdkbanbe()>22){
                    if (DianTool.getsdkbanbe()>22){
                        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivy.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    100);
                            return;
                        }
                    }
                }
            }
        });
        mShowMemberTipsView.setOnSureListener(new ShowMemberTipsView.OnSureListener() {
            @Override
            public void onSure(BaseTipsView baseTipsView) {
                baseTipsView.dismiss(MainActivy.this);
            }
        });
        mShowMemberTipsView.show(this);
    }
    void inbanben(){
        if (DianTool.isConnectionNetWork(this)) {
            HttpParams params = new HttpParams();
            HttpHeaders headers = new HttpHeaders();
            headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
            params.put("type", "android");
            params.put("role", "student");
            OkGo.get(HostAdress.getBanben("/api/web/v1/upgrade/getInfo")).tag(this)
                    .params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("{}")) {
                            Result result = JSON.parseObject(s, Result.class);
                            boolean isRequire=result.isRequired;
                            String str = result.version.substring(0, 1) + result.version.substring(2, 3) + result.version.substring(4, 5);
                            String strs = DianApplication.code.substring(0, 1) + DianApplication.code.substring(2, 3) + DianApplication.code.substring(4, 5);
                            if (Integer.parseInt(str) > Integer.parseInt(strs)) {
                                GengXiDialog dialog = new GengXiDialog(MainActivy.this,isRequire, result.versionDescrip, new GengXiDialog.SelectDialogButtonListener() {
                                    @Override
                                    public void checkButton(int id) {
                                        switch (id) {
                                            case R.id.btnSelectDialogDetermineguan:
                                                DianTool.genxin(MainActivy.this);
                                                DianTool.kongtoken();
                                                break;
                                        }
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                }
            });
        }
    }
}
