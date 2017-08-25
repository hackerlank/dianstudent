package com.dingli.diandians.weipingjiao;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.MyDialog;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.firstpage.ListItemFirstPageView;
import com.dingli.diandians.view.XListView;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

public class WeiPJActivity extends BaseActivity implements XListView.IXListViewListener,
        View.OnClickListener{

    private ImageView ddwipj;
    private WeiPJAdapter adapter;
    private XListView xlvWeipingjiao;
    private RelativeLayout rlxian;
    HttpHeaders headers;
    private WeiPJ weiPJ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_pj);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
        xlvWeipingjiao.setAdapter(adapter);
        xlvWeipingjiao.setPullLoadEnable(true);
        xlvWeipingjiao.setPullRefreshEnable(true);
        xlvWeipingjiao.setXListViewListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestList(1, Constant.REFRESH);
    }

    private void initView() {
        ddwipj=(ImageView)findViewById(R.id.ddwipj);
        ddwipj.setOnClickListener(this);
        ImageView wpjback = ((ImageView) findViewById(R.id.wpjback));
        xlvWeipingjiao = ((XListView) findViewById(R.id.xlvWeipingjiao));
        adapter = new WeiPJAdapter(this);
        rlxian = (RelativeLayout) findViewById(R.id.rlxian);
        wpjback.setOnClickListener(this);
        xlvWeipingjiao.setXListViewListener(this);
    }


    @Override
    public void onRefresh() {
        requestList(1, Constant.REFRESH);
    }

    @Override
    public void onLoadMore() {
        xlvWeipingjiao.stopLoadMore();
    }

    private void requestList(final int i, int refresh) {
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.showMyDialog(this, "加载中...");
            HttpParams params = new HttpParams();
            params.put("offset", i);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getZheRe("/api/phone/v1/students/notassess")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    xlvWeipingjiao.stopLoadMore();
                    xlvWeipingjiao.stopRefresh();
                    if (!TextUtils.isEmpty(s)) {
                        weiPJ = JSON.parseObject(s, WeiPJ.class);
                        if (weiPJ == null || weiPJ.getData() == null || weiPJ.getData().size() == 0) {
                            rlxian.setVisibility(View.VISIBLE);
                            xlvWeipingjiao.setVisibility(View.GONE);
                        } else {
                            rlxian.setVisibility(View.GONE);
                            xlvWeipingjiao.setVisibility(View.VISIBLE);
                            adapter.refresh(i == 1, weiPJ.getData());
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                        DianTool.response(response,WeiPJActivity.this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wpjback:
                DianApplication.user.libiao="mine";
                finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }
}