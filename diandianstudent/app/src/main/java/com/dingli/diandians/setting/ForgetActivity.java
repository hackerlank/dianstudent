package com.dingli.diandians.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.login.FindActivity;
import com.dingli.diandians.login.LoginActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

public class ForgetActivity extends BaseActivity {

    private EditText zhanghao;
    private Button next;
    private String yanzhen;
    HttpHeaders headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();


    }

    private void initView() {
        zhanghao = ((EditText) findViewById(R.id.zhanghao));
        ImageView forgetback = ((ImageView) findViewById(R.id.forgetback));
        forgetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgetActivity.this,LoginActivity.class);
                startActivity(intent);
                ForgetActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
            }
        });
        next = (Button) findViewById(R.id.next);
        next.setClickable(false);
        next.setBackgroundResource(R.drawable.check_btn_not_click);
        zhanghao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                yanzhen = zhanghao.getText().toString().trim();
                if (!TextUtils.isEmpty(yanzhen)) {
                    next.setClickable(true);
                    next.setBackgroundResource(R.color.qianblue);
                } else {
                    next.setClickable(false);
                    next.setBackgroundResource(R.drawable.check_btn_not_click);
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DianTool.isConnectionNetWork(ForgetActivity.this)) {
                    DianTool.showMyDialog(ForgetActivity.this, "");
                    HttpParams params = new HttpParams();
                    params.put("account", yanzhen);
                    OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/checkuserisexist")).tag(this)
                            .headers(headers).params(params).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            if (!s.equals("{}")) {
                                Result result = JSON.parseObject(s, Result.class);
                                if(result.phone!=null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("account", result.phone);
                                    Intent intent = new Intent(ForgetActivity.this, FindActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                }else{
                                    DianTool.showTextToast(ForgetActivity.this, "对不起,该账号没有绑定手机,不能使用忘记密码功能");
                                }
                            } else {
                                DianTool.showTextToast(ForgetActivity.this, "对不起,该账号没有绑定手机,不能使用忘记密码功能");
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(ForgetActivity.this, "对不起,该账号没有绑定手机,不能使用忘记密码功能");
                        }
                    });
                } else {
                    DianTool.showTextToast(ForgetActivity.this, "请检查网络");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
