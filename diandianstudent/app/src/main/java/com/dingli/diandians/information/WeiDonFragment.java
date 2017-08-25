package com.dingli.diandians.information;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.GuanBiDialog;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.MyDialog;
import com.dingli.diandians.common.QingJiaSty;
import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.information.adapter.ListItemWeiDoneView;
import com.dingli.diandians.information.adapter.WeiDoneAdapter;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class WeiDonFragment extends Fragment implements View.OnClickListener,ListItemWeiDoneView.onCancelCollectListener {

    ListView xlvweidone;
    EntrtyActivity parent;
    WeiDoneAdapter adapter;
    List<Course> arr;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout resd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weidone,container,false);
        parent=(EntrtyActivity)getActivity();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview(view);
        resd.setRefreshing(true);
        xlvweidone.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initdata();
    }

    void initdata(){
        if (DianTool.isConnectionNetWork(parent)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/period/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (s.length() != 0) {
                        arr = JSON.parseArray(s, Course.class);
                        showlistweidon();
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    resd.setRefreshing(false);
                    DianTool.response(response,parent);
                }
            });
        }else{
            DianTool.showTextToast(parent,"请检查网络");
        }
    }
    void initview(View v){
        resd=(VerticalSwipeRefreshLayout)v.findViewById(R.id.resd);
        resd.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        resd.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        xlvweidone=(ListView)v.findViewById(R.id.xlvweidone);
        resd.setViewGroup(xlvweidone);
        xlvweidone.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                       if (firstVisibleItem==0){
                           resd.setEnabled(true);
                       }else{
                           resd.setEnabled(false);
                       }
            }
        });
        adapter=new WeiDoneAdapter(parent,this);
    }
    void showlistweidon(){
        if (DianTool.isConnectionNetWork(parent)) {
            final HttpParams params = new HttpParams();
            params.put("status", "request");
            params.put("orderByKey", "createdTime");
            params.put("orderBy", "asc");
            OkGo.get(HostAdress.getRequest("/api/phone/v1/student/getleaverequest")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    resd.setRefreshing(false);
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            String startname = "";
                            String endname = "";
                            if (arr.size() != 0) {
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
                                                arg.get(i).name = startname + "～" + endname;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        List<QingJiaSty> aeg = ShiJiaClass.get(arg);
                        adapter.refreshWeiDone(aeg);
                        adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    resd.setRefreshing(false);
                    DianTool.response(response,parent);
                }
            });
        }else{
            DianTool.showTextToast(parent,"请检查网络");
        }
    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCancelCollect(String jie,final int ids,String url) {
        switch (jie){
            case "jujue":
                GuanBiDialog dialog=new GuanBiDialog(parent, "确认同意撤销请假申请","同意后将撤销请假申请,且该同学请假信息将会作废","","", new GuanBiDialog.SelectDialogButtonListener() {
                    @Override
                    public void checkButton(int id) {
                        switch (id){
                            case R.id.btnSelectDialogDetermineguan:
                                inittime(ids);
                                break;
                        }
                    }
                });
                dialog.show();
                break;
            case "tupian":
                Intent intent1=new Intent(parent,FangDaActivity.class);
                intent1.putExtra(Constant.URL,url);
                intent1.putExtra(Constant.YESHU,0);
                startActivity(intent1);
                parent.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case "tupianone":
                Intent intent2=new Intent(parent,FangDaActivity.class);
                intent2.putExtra(Constant.URL,url);
                intent2.putExtra(Constant.YESHU,1);
                startActivity(intent2);
                parent.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case "tupiantwo":
                Intent intent3=new Intent(parent,FangDaActivity.class);
                intent3.putExtra(Constant.URL,url);
                intent3.putExtra(Constant.YESHU,2);
                startActivity(intent3);
                parent.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
        }
    }
    void inittime(int id){
        DianTool.showMyDialog(getActivity(), "");
        HttpParams params=new HttpParams();
        headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
            params.put("leaveId",id);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/student/cancleleaverequest")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    showlistweidon();
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                    DianTool.response(response,parent);
                }
            });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
