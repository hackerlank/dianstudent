package com.dingli.diandians.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by dingliyuangong on 2016/3/14.
 */
public class ModificationActivity extends BaseActivity {
    ImageView ddmodi;
    EditText etnewmodification,etnewagainmodification;
    Button btrequiremodification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        String phone=getIntent().getStringExtra("phone");
        String code=getIntent().getStringExtra("code");
        DianTool.huoqutoken();
        init(phone, code);
    }
    public void init(final String phone,final String code){
        ImageView modificationorderback=(ImageView)findViewById(R.id.modificationorderback);
        etnewmodification=(EditText)findViewById(R.id.etnewmodification);
        etnewagainmodification=(EditText)findViewById(R.id.etnewagainmodification);
        btrequiremodification=(Button)findViewById(R.id.btrequiremodification);
        ddmodi=(ImageView)findViewById(R.id.ddmodi);
        btrequiremodification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newmodification = etnewmodification.getText().toString().trim();
                String againmodification = etnewagainmodification.getText().toString().trim();
                if (TextUtils.isEmpty(newmodification)) {
                    DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.xinmi));
                } else if (TextUtils.isEmpty(againmodification)) {
                    DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.zaixinmi));
                }else if(newmodification.equals("123456")){
                    DianTool.showTextToast(ModificationActivity.this,"新密码设置过于简单,请重设");
                }else if (newmodification.getBytes().length < 6) {
                    DianTool.showTextToast(ModificationActivity.this, "密码不能小于6位");
                } else if (newmodification.equals(againmodification)) {
                    if (DianTool.isConnectionNetWork(ModificationActivity.this)) {
                        DianTool.showMyDialog(ModificationActivity.this,"");
                        HttpParams params = new HttpParams();
                        HttpHeaders headers=new HttpHeaders();
                        params.put("code", code);
                        params.put("pwd", againmodification);
                        params.put("phone", phone);
                        params.put("module", "dd_fp");
                        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
                        headers.put("Encoding", "UTF-8");
                        headers.put("Accept", Constant.APPLICATION_JSON);
                        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                        OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/findandsetpwd")).tag(this)
                                .headers(headers).params(params).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.xiugong));
                                DianApplication.user.account = null;
                                DianApplication.user.password = null;
                                Intent intent = new Intent(ModificationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                for (int i = 0; i < DianApplication.activityList.size(); i++) {
                                    if (DianApplication.activityList.get(i) != DianApplication.user.login) {
                                        DianApplication.activityList.get(i).finish();
                                    }
                                }
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                            }
                        });
                    } else {
                        DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.wangxi));
                    }
                } else {
                    DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.shubuyi));
                }

            }
        });
        modificationorderback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificationActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
            }
        });
    }
}
