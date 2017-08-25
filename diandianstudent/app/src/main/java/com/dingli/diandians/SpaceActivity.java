package com.dingli.diandians;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/8/23.
 */
public class SpaceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_space);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (DianTool.isConnectionNetWork(this)){
            HttpParams params=new HttpParams();
            params.put("role","student");
            params.put("version","V2");
            OkGo.get(HostAdress.getRequest("/api/phone/v1/listHomePageV2")).tag(this).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianApplication.sharedPreferences.saveString("homepage",s);
                            jumpto();
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            jumpto();
                        }
                    });
        }else{
            jumpto();
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    void jumpto(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpaceActivity.this, MainActivy.class);
                startActivity(intent);
                SpaceActivity.this.finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        },2000);
    }
}
