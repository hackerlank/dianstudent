package com.dingli.diandians.login;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/3/7.
 */
public class FindActivity extends BaseActivity {

    TextView donerequire,tvsendword;
    EditText  etphone,yanet;
    TimeCount waitTime;
    ImageView ddfp;
    String etphonenum;
    String code;
    HttpHeaders headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        waitTime=new TimeCount(60000,1000);
          init();
    }
    public void init(){
        ddfp=(ImageView)findViewById(R.id.ddfp);
        ImageView dianback=(ImageView)findViewById(R.id.dianback);
        yanet=(EditText)findViewById(R.id.yanet);
        donerequire=(TextView)findViewById(R.id.donerequiref);
        etphone=(EditText)findViewById(R.id.etphone);
        tvsendword=(TextView)findViewById(R.id.tvsendword);
        etphone.setFocusable(false);
        etphone.setFocusableInTouchMode(false);
        Bundle bundle=getIntent().getExtras();
        String phone=bundle.getString("account");
        etphone.setText(phone);
        tvsendword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etphone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.shoukong));
                } else if (DianTool.isConnectionNetWork(FindActivity.this)) {
                        waitTime.start();
                        HttpParams params = new HttpParams();
                        params.put("phone", phone);
                        params.put("module", "dd_fp");
                        OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/sendmessagecodebak")).tag(this)
                                .headers(headers).params(params).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.fagong));
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.showTextToast(FindActivity.this, "手机号需要绑定");
                            }
                        });
                } else {
                    DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.wangxi));
                }
            }
        });
        donerequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etphonenum=etphone.getText().toString().trim();
                code=yanet.getText().toString().trim();
                if(TextUtils.isEmpty(etphonenum)){
                    DianTool.showTextToast(FindActivity.this,getResources().getString(R.string.shoukong));
                }else if(TextUtils.isEmpty(code)){
                    DianTool.showTextToast(FindActivity.this,getResources().getString(R.string.yankong));
                }else{
                    if(DianTool.isConnectionNetWork(FindActivity.this)){
                        DianTool.showMyDialog(FindActivity.this, "");
                        HttpParams requestParams=new HttpParams();
                        requestParams.put("phone",etphonenum);
                        requestParams.put("code",code);
                        requestParams.put("module", "dd_fp");
                        OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/validmessagecode")).tag(this)
                                .headers(headers).params(requestParams).execute(new StringCallback() {
                            //40022001
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                if (!TextUtils.isEmpty(s)) {
                                    if (s.contains("true")) {
                                        Intent intent = new Intent(FindActivity.this, ModificationActivity.class);
                                        intent.putExtra("phone", etphonenum);
                                        intent.putExtra("code", code);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                    } else {
                                        DianTool.showTextToast(FindActivity.this, "验证码错误");
                                    }
                                }else{
                                    DianTool.showTextToast(FindActivity.this, "验证码错误");
                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.showTextToast(FindActivity.this, "验证码或手机号错误");
                            }
                        });
                    }else{
                        DianTool.showTextToast(FindActivity.this, "请检查网络");
                    }
                }
            }
        });
        dianback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tvsendword.setText(R.string.send);
            tvsendword.setClickable(true);
            tvsendword.setBackgroundResource(R.color.qianblue);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvsendword.setClickable(false);
            tvsendword.setBackgroundResource(R.drawable.check_btn_not_click);
            tvsendword.setText("(" + millisUntilFinished / 1000 + "S)"+getResources().getString(R.string.chongfa));
        }
    }
}
