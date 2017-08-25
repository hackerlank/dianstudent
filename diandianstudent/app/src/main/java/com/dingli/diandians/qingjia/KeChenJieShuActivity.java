package com.dingli.diandians.qingjia;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.MyDialog;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.numbertest.NumberTestActivity;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class KeChenJieShuActivity extends BaseActivity implements View.OnClickListener{
    ScrollDisabledListView kejielist;
    NumClassAdapter adapter;
    boolean firstIn = true;
    ImageView ddkechenjie;
    ArrayList<String> lis;
    ArrayList<Integer> lst;
    ArrayList<Integer> arl;
    Map<Integer,String> map;
    String str;
    VerticalSwipeRefreshLayout refjieshu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kechenjieshu);
        initview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refjieshu.setRefreshing(true);
        initnow();
    }

    void initview(){
        map=new HashMap<>();
        refjieshu=(VerticalSwipeRefreshLayout)findViewById(R.id.refjieshu);
        refjieshu.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refjieshu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initnow();
            }
        });
       ImageView kechenjieshuback=(ImageView)findViewById(R.id.kechenjieshuback);
        ddkechenjie=(ImageView)findViewById(R.id.ddkechenjie);
        ddkechenjie.setOnClickListener(this);
        kechenjieshuback.setOnClickListener(this);
        kejielist=(ScrollDisabledListView)findViewById(R.id.kejielist);
       adapter=new NumClassAdapter(this);
        kejielist.setAdapter(adapter);
        lis=new ArrayList<>();
        lst=new ArrayList<>();
        arl=new ArrayList<>();
    }
    void initnow(){
        if(DianTool.isConnectionNetWork(this)) {
            if (firstIn) {
                firstIn=false;
            }
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/user/getnow")).tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (!TextUtils.isEmpty(s)) {
                                if (!s.equals("{}")) {
                                    Result result = JSON.parseObject(s, Result.class);
                                    String strs = result.time;
                                    str = strs.split(" ")[0];
                                    initdata();
                                }
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            refjieshu.setRefreshing(false);
                        }
                    });
        }else{
            DianTool.showTextToast(KeChenJieShuActivity.this, "请检查网络");
        }
    }
    void initdata(){
        if(DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            HttpParams params=new HttpParams();
            HttpHeaders headers=new HttpHeaders();
            params.put("startDay",str);
            headers.put("Content-Type", Constant.APPLICATION_FORMURL);
            headers.put("Encoding", "UTF-8");
           headers.put("Accept", Constant.APPLICATION_JSON);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/period/getbyteachday")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refjieshu.setRefreshing(false);
                    if (s.length() != 0) {
                        final List<Course> arr = JSON.parseArray(s, Course.class);
                        adapter.refreshNumClass(arr);
                        adapter.notifyDataSetChanged();
                        kejielist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (int i = 0; i < arr.size(); i++) {
                                    if (i == (int) id) {
                                        ImageView c = (ImageView) kejielist.getChildAt((int) id).findViewById(R.id.selected);
                                        if (c.isShown() == true) {
                                            Collections.sort(arl);
                                            if (position == arl.get(arl.size() - 1)) {
                                                c.setVisibility(View.GONE);
                                                String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                for (int e = 0; e < lis.size(); e++) {
                                                    if (lis.get(e).equals(name)) {
                                                        lis.remove(lis.get(e));
                                                    }
                                                }
                                                for (int e = 0; e < lst.size(); e++) {
                                                    if (lst.get(e).equals(idd)) {
                                                        lst.remove(lst.get(e));
                                                    }
                                                }
                                                for (int e = 0; e < arl.size(); e++) {
                                                    if (position == arl.get(e)) {
                                                        arl.remove(arl.get(e));
                                                    }
                                                }
                                            } else if (position == arl.get(0)) {
                                                c.setVisibility(View.GONE);
                                                String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                for (int e = 0; e < lis.size(); e++) {
                                                    if (lis.get(e).equals(name)) {
                                                        lis.remove(lis.get(e));
                                                    }
                                                }
                                                for (int e = 0; e < lst.size(); e++) {
                                                    if (lst.get(e).equals(idd)) {
                                                        lst.remove(lst.get(e));
                                                    }
                                                }
                                                for (int e = 0; e < arl.size(); e++) {
                                                    if (position == arl.get(e)) {
                                                        arl.remove(arl.get(e));
                                                    }
                                                }
                                            } else {
                                                DianTool.showTextLongToast(KeChenJieShuActivity.this, "只能顺序取消");
                                            }
                                        } else {
                                            if (arl.size() == 0) {
                                                arl.add((int) id);
                                                c.setVisibility(View.VISIBLE);
                                                String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                lis.add(name);
                                                lst.add(idd);
                                            } else if (arl.size() == 1) {
                                                if (position - arl.get(0) == 1) {
                                                    arl.add((int) id);
                                                    c.setVisibility(View.VISIBLE);
                                                    String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                    int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                    lis.add(name);
                                                    lst.add(idd);
                                                    Collections.sort(arl);
                                                } else if (id - arl.get(0) == -1) {
                                                    arl.add((int) id);
                                                    c.setVisibility(View.VISIBLE);
                                                    String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                    int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                    lis.add(name);
                                                    lst.add(idd);
                                                    Collections.sort(arl);
                                                } else {
                                                    DianTool.showTextToast(KeChenJieShuActivity.this, "按顺序点击0");
                                                }
                                            } else {
                                                if (id - arl.get(0) == -1) {
                                                    arl.add((int) id);
                                                    c.setVisibility(View.VISIBLE);
                                                    String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                    int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                    lis.add(name);
                                                    lst.add(idd);
                                                    Collections.sort(arl);
                                                } else if (id - arl.get(arl.size() - 1) == 1) {
                                                    arl.add((int) id);
                                                    c.setVisibility(View.VISIBLE);
                                                    String name = ((Course) parent.getItemAtPosition((int) id)).name;
                                                    int idd = ((Course) parent.getItemAtPosition((int) id)).id;
                                                    lis.add(name);
                                                    lst.add(idd);
                                                    Collections.sort(arl);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refjieshu.setRefreshing(false);
                    DianTool.response(response,KeChenJieShuActivity.this);
                }
            });
        } else {
            DianTool.showTextToast(this,"请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kechenjieshuback:
                Intent result = new Intent();
                result.putStringArrayListExtra("names",lis);
                result.putIntegerArrayListExtra("ids",lst);
                setResult(RESULT_OK, result);
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
            Intent result = new Intent();
            result.putStringArrayListExtra("names",lis);
            result.putIntegerArrayListExtra("ids",lst);
            setResult(RESULT_OK, result);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

//    @Override
//    public void onCancelCollects(int position, String name, int id) {
//    }
}
