package com.dingli.diandians.information.instructor;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.ResultInfoCall;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/4/13.
 */

public class SignNoteActivity extends BaseActivity implements View.OnClickListener{

    private ImageView signnoteback;
    private ListView xlvsignnote;
    private VerticalSwipeRefreshLayout resignnote;
    SignNoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signnote);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resignnote.setRefreshing(true);
        initdata();
    }

    private void initView() {
        signnoteback = (ImageView) findViewById(R.id.signnoteback);
        xlvsignnote = (ListView) findViewById(R.id.xlvsignnote);
        resignnote = (VerticalSwipeRefreshLayout) findViewById(R.id.resignnote);
        signnoteback.setOnClickListener(this);
        adapter=new SignNoteAdapter(this);
        xlvsignnote.setAdapter(adapter);
        DianTool.refresh(resignnote,this);
        resignnote.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signnoteback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            HttpHeaders headers=new HttpHeaders();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/students/getRollCallEver")).tag(this).headers(headers)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            resignnote.setRefreshing(false);
                            resignnote.setEnabled(false);
                            if (!TextUtils.isEmpty(s)) {
                                if (!s.equals("[]")) {
                                    List<ResultInfoCall> resultInfoCalls = JSON.parseArray(s, ResultInfoCall.class);
                                    adapter.refresh(resultInfoCalls);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            resignnote.setRefreshing(false);
                            DianTool.response(response,SignNoteActivity.this);
                        }
                    });

        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
