package com.dingli.diandians.numbertest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.bean.SysInfoUtil;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.CheatDialog;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.firstpage.FirstPageFragment;
import com.dingli.diandians.firstpage.submit.SubminActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/10/19.
 */
public class NumberTestActivity extends BaseActivity implements View.OnClickListener {
    private EditText etnumbtest;
    private Button btup;
    int id;
    String location;
    String intoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbertest);
         id=getIntent().getIntExtra("schedu", 0);
        location=getIntent().getStringExtra("location");
        initView();
    }

    private void initView() {
        intoken=getIntent().getStringExtra("intoken");
        String coursename=getIntent().getStringExtra(Constant.COURSENAME);
        ImageView numbertestback = (ImageView) findViewById(R.id.numbertestback);
       ImageView ddtestnumber = (ImageView) findViewById(R.id.ddtestnumber);
        TextView qiandaokechens=(TextView)findViewById(R.id.qiandaokechens);
            qiandaokechens.setText("签到课程:  "+coursename);
        ddtestnumber.setOnClickListener(this);
        etnumbtest = (EditText) findViewById(R.id.etnumbtest);
        btup = (Button) findViewById(R.id.btup);
        btup.setOnClickListener(this);
        numbertestback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btup:
                    btup.setClickable(false);
                    submit();
                break;
            case R.id.numbertestback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }

    private void submit() {
        // validate
        if(DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            String etnumbtestString = etnumbtest.getText().toString().trim();
            if (TextUtils.isEmpty(etnumbtestString)) {
                btup.setClickable(true);
                DianTool.showTextToast(this,"数字验证不能为空");
            } else if(DianTool.isNumber(etnumbtestString)){
                if(etnumbtestString.length()==6) {
                    if(etnumbtestString.equals(location)) {
                        MobclickAgent.onEvent(this,"signNumber");
                        DianTool.showMyDialog(this, "");
                        JSONObject jsonObject=new JSONObject();
                        HttpHeaders headers=new HttpHeaders();
                        try{
                            jsonObject.put("authCode",etnumbtestString);
                            jsonObject.put("gps","");
                            jsonObject.put("gpsDetail","");
                            jsonObject.put("gpsType","");
                            jsonObject.put("rollCallType","digital");
                            jsonObject.put("scheduleId",id);
                            jsonObject.put("deviceToken",DianApplication.sharedPreferences.getStringValue(Constant.DEVICEID));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                        OkGo.post(HostAdress.getRequest("/api/phone/v1/student/signIn")).tag(this)
                                .upJson(jsonObject).headers(headers).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                btup.setClickable(true);
                                DianTool.dissMyDialog();
                                if (!TextUtils.isEmpty(s)) {
                                    if (!s.equals("{}")) {
                                        Result result = JSON.parseObject(s, Result.class);
                                        if (result.success == false) {
                                            if (result.code.equals("40025004")) {
                                                DianTool.showTextToast(NumberTestActivity.this, "老师已修改您的考勤,不能再次签到。请联系老师 !");
                                            } else if (result.code.equals("40025005")) {
                                                DianTool.showTextToast(NumberTestActivity.this, "老师将您的考勤修改为请假,所以不能签到");
                                            } else if (result.code.equals("40025007")) {
                                                DianTool.showTextToast(NumberTestActivity.this, "老师没有开启点名");
                                            } else if (result.code.equals("40025006")) {
                                                CheatDialog dialog = new CheatDialog(NumberTestActivity.this, result.message, new CheatDialog.SelectDialogButtonListener() {
                                                    @Override
                                                    public void checkButton(int id) {
                                                    }
                                                });
                                                dialog.show();
                                            } else if (result.code.equals("40025008")) {
                                                DianTool.showTextToast(NumberTestActivity.this, "老师更改了点名方式");
                                                Initoken.signId(NumberTestActivity.this);
                                            } else if (result.code.equals("40025009")) {
                                                DianTool.showTextToast(NumberTestActivity.this, "老师已关闭该点名");
                                            }
                                        } else {
                                            DianTool.showTextToast(NumberTestActivity.this, "提交成功");
                                            if (!TextUtils.isEmpty(intoken)) {
                                                Intent intent = new Intent(NumberTestActivity.this, FirstPageFragment.class);
                                                startActivity(intent);
                                            }
                                            finish();
                                            overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                                btup.setClickable(true);
                                DianTool.response(response,NumberTestActivity.this);
                            }
                        });
                    }else{
                        btup.setClickable(true);
                        DianTool.showTextToast(this,"请输入正确的数字");
                    }
                }else{
                    btup.setClickable(true);
                    DianTool.showTextToast(this,"请输入6位数字");
                }
            }else{
                btup.setClickable(true);
                DianTool.showTextToast(this,"你输入的不全是数字");
            }
        }else{
            btup.setClickable(true);
            DianTool.showTextToast(this,"请检查网络");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
