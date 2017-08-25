package com.dingli.diandians.information;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.MyDialog;
import com.dingli.diandians.common.QingJiaSty;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.information.adapter.QingJiaMessAdapter;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class QingJiaMessActivity extends BaseActivity implements View.OnClickListener{

    ImageView QingJiaMessback;
    ListView listviews;
    QingJiaMessAdapter adapter;
    List<Course> arr;
    TextView tvstud,tvxuehao;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingjiamessage);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
        ref.setRefreshing(true);
        initqingjia();
        ref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initqingjia();
            }
        });
    }
    void initView(){
        ref=(VerticalSwipeRefreshLayout)findViewById(R.id.ref);
        ref.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        tvstud=(TextView)findViewById(R.id.tvstud);
        tvxuehao=(TextView)findViewById(R.id.tvxuehao);
        QingJiaMessback=(ImageView)findViewById(R.id.QingJiaMessback);
        listviews=(ListView)findViewById(R.id.listviews);
        ref.setViewGroup(listviews);
        QingJiaMessback.setOnClickListener(this);
        adapter=new QingJiaMessAdapter(this);
        listviews.setAdapter(adapter);
    }
    void initqingjia(){
        if(DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/period/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (s.length() != 0) {
                        arr = JSON.parseArray(s, Course.class);
                        initname();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    ref.setRefreshing(false);
                    DianTool.response(response,QingJiaMessActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this, "网络出错,请检查网络");
        }
    }
    void initname(){
        OkGo.get(HostAdress.getRequest("/api/phone/v1/user/info")).tag(this)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (TextUtils.isEmpty(s)) {
                } else {
                    ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                    tvstud.setText(resultInfo.name);
                    tvxuehao.setText(resultInfo.personId);
                    initdata();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                ref.setRefreshing(false);
                DianTool.response(response,QingJiaMessActivity.this);
            }
        });
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(this)){
            HttpParams params=new HttpParams();
            params.put("status", "processed");
            params.put("orderByKey","lastModifiedTime");
            params.put("orderBy","asc");
            OkGo.get(HostAdress.getRequest("/api/phone/v1/student/getleaverequest")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ref.setRefreshing(false);
                    if (s.length()!=0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            String startname = "";
                            String endname = "";
                            if(arr.size()!=0){
                                for (int j = 0; j < arr.size(); j++) {
                                    if (arg.get(i).startPeriodId != 0) {
                                        if (arg.get(i).endPeriodId != 0) {
                                            if (arg.get(i).startPeriodId == arr.get(j).id) {
                                                startname = arr.get(j).name;
                                            }
                                            if (arg.get(i).endPeriodId == arr.get(j).id) {
                                                endname = arr.get(j).name;
                                            }
                                            if (startname.equals(endname)) {
                                                arg.get(i).name = startname;
                                            } else {
                                                arg.get(i).name = startname + " " + endname;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        List<QingJiaSty> aeg = ShiJiaClass.get(arg);
                        adapter.refreshQingJia(aeg);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    ref.setRefreshing(false);
                    DianTool.response(response,QingJiaMessActivity.this);
                }
            });
        }else{
          DianTool.showTextToast(QingJiaMessActivity.this,"网络出错,请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.QingJiaMessback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
}
