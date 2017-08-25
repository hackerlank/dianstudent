package com.dingli.diandians.information;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.QingJiaSty;
import com.dingli.diandians.common.ResultInfoCall;
import com.dingli.diandians.information.adapter.InfomationAdapter;
import com.dingli.diandians.information.instructor.InsLocationActivity;
import com.dingli.diandians.information.instructor.SignNoteActivity;
import com.dingli.diandians.survey.WebViewSurveyActivity;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by dingliyuangong on 2016/6/14.
 */
public class InformationFragment extends Fragment implements View.OnClickListener{

  RelativeLayout rlqingjia,rlsurvey,rlinstructor;
    TextView tvshuzi,tvweitongzhi,tvtion,tvcell,tvcoll;
    TextView tvtzsj,tvinstructor,tvshuinstructor,tvtimeinstructor;
    HttpHeaders headers;
    ImageView ivqj;
    String dianjiId;
    VerticalSwipeRefreshLayout slayout;
    int refr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View informationView=inflater.inflate(R.layout.fragment_information,container,false);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant. APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview(informationView);
        return informationView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            if (refr==0) {
                refr=1;
                slayout.setRefreshing(true);
            }
            initdata();
        }
    }

    void initview(View v){
        slayout=(VerticalSwipeRefreshLayout)v.findViewById(R.id.slayout);
        slayout.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        slayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        ivqj=(ImageView)v.findViewById(R.id.ivqj);
        tvweitongzhi=(TextView)v.findViewById(R.id.tvweitongzhi);
        rlqingjia=(RelativeLayout)v.findViewById(R.id.rlqingjia);
        rlsurvey=(RelativeLayout)v.findViewById(R.id.rlsurvey);
        rlinstructor=(RelativeLayout) v.findViewById(R.id.rlinstructor);
        tvshuzi=(TextView)v.findViewById(R.id.tvshuzi);
        tvtzsj=(TextView)v.findViewById(R.id.tvtzsj);
        tvtion=(TextView)v.findViewById(R.id.tvtion);
        tvcell=(TextView)v.findViewById(R.id.tvcell);
        tvcoll=(TextView)v.findViewById(R.id.tvcoll);
        tvinstructor=(TextView) v.findViewById(R.id.tvinstructor);
        tvshuinstructor=(TextView) v.findViewById(R.id.tvshuinstructor);
        tvtimeinstructor=(TextView) v.findViewById(R.id.tvtimeinstructor);
        rlqingjia.setOnClickListener(this);
        rlinstructor.setOnClickListener(this);
        rlsurvey.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlqingjia:
                rlqingjia.setClickable(false);
                v.clearAnimation();
                dianjiId="qingjia";
                initdatas();
                break;
            case R.id.rlsurvey:
                dianjiId="survey";
                rlsurvey.setClickable(false);
                initdatas();
                break;
            case R.id.rlinstructor:
                dianjiId="rollcallever";
                rlinstructor.setClickable(false);
                initdatas();
                break;
        }
    }
    void  initdata(){
        if(DianTool.isConnectionNetWork(getActivity())){
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/getstatus")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    slayout.setRefreshing(false);
                    rlqingjia.setClickable(true);
                    rlsurvey.setClickable(true);
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            if (!TextUtils.isEmpty(arg.get(i).module)) {
                                if (arg.get(i).module.equals("leave")) {
                                    if (arg.get(i).function.equals("student_notice")) {
                                        if (arg.get(i).notRead != 0) {
                                            tvshuzi.setVisibility(View.VISIBLE);
                                            tvshuzi.setText(String.valueOf(arg.get(i).notRead));
                                            tvshuzi.setBackgroundResource(R.drawable.circle_shuzi);
                                            tvweitongzhi.setText("你有" + arg.get(i).notRead + "条未阅读的请假通知");
                                        } else {
                                            tvshuzi.setVisibility(View.GONE);
                                            tvweitongzhi.setText(R.string.wutis);
                                        }
                                        tvtzsj.setText(arg.get(i).lastPushTime);
                                    }
                                }
                                if (arg.get(i).module.equals("questionnaire")) {
                                    if (arg.get(i).function.equals("que_student_notice")) {
                                        if (arg.get(i).notRead != 0) {
                                            tvtion.setVisibility(View.VISIBLE);
                                            tvtion.setText(String.valueOf(arg.get(i).notRead));
                                            tvtion.setBackgroundResource(R.drawable.circle_shuzi);
                                            tvcell.setText("你有" + arg.get(i).notRead + "条调查问卷通知");
                                        } else {
                                            tvtion.setVisibility(View.GONE);
                                            tvcell.setText(R.string.ninyous);
                                        }
                                        tvcoll.setText(arg.get(i).lastPushTime);
                                    }
                                }
                                if (arg.get(i).module.equals("rollcallever")) {
                                    if (arg.get(i).function.equals("rollCallEver_student_notice")) {
                                        if (arg.get(i).notRead != 0) {
                                            tvinstructor.setVisibility(View.VISIBLE);
                                            tvinstructor.setText(String.valueOf(arg.get(i).notRead));
                                            tvinstructor.setBackgroundResource(R.drawable.circle_shuzi);
                                            tvshuinstructor.setText("你有" + arg.get(i).notRead + "条辅导员点名通知");
                                        } else {
                                            tvinstructor.setVisibility(View.GONE);
                                            tvshuinstructor.setText(R.string.lingtiao);
                                        }
                                        tvtimeinstructor.setText(arg.get(i).lastPushTime);
                                    }
                                }
                            } else {
                                tvshuzi.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    slayout.setRefreshing(false);
                    rlqingjia.setClickable(true);
                    rlsurvey.setClickable(true);
                    DianTool.response(response,getActivity());
                }
            });
        } else {
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }
    void  initdatas(){
        if(DianTool.isConnectionNetWork(getActivity())){
            DianTool.showMyDialog(getActivity(),"");
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            HttpParams params=new HttpParams();
            if (dianjiId.equals("qingjia")){
                params.put("module","leave");
                params.put("function","student_notice");
            }else if (dianjiId.equals("survey")){
                params.put("module","questionnaire");
                params.put("function","que_student_notice");
            }else if (dianjiId.equals("rollcallever")){
                params.put("module","rollcallever");
                params.put("function","rollCallEver_student_notice");
            }
            OkGo.get(HostAdress.getRequest("/api/phone/v1/get")).tag(this).params(params)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    successorfail();
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    successorfail();
                }
            });
        } else {
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    void successorfail(){
        DianTool.dissMyDialog();
        rlqingjia.setClickable(true);
        rlsurvey.setClickable(true);
        rlinstructor.setClickable(true);
        Intent intent=new Intent();
        if (dianjiId.equals("qingjia")) {
            tvshuzi.setVisibility(View.GONE);
            tvshuzi.setBackgroundResource(R.drawable.circle_shuziwu);
            tvweitongzhi.setText(R.string.wutis);
            intent.setClass(getActivity(), QingJiaMessActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }else if (dianjiId.equals("survey")){
            tvtion.setVisibility(View.GONE);
            tvtion.setBackgroundResource(R.drawable.circle_shuziwu);
            tvcell.setText(R.string.ninyous);
            intent.setClass(getActivity(), WebViewSurveyActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("url",Constant.webdiaocha+"/mobileui/questatuslist");
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }else{
            suisign();
        }
    }
    void suisign(){
        OkGo.get(HostAdress.getRequest("/api/phone/v1/students/getRollCallEver")).tag(this)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                List<ResultInfoCall> resultInfoCall= JSON.parseArray(s,ResultInfoCall.class);
                Intent intent=new Intent();
                if (tvinstructor.getVisibility()==View.VISIBLE) {
                    if (resultInfoCall.size()!=0) {
                        if (resultInfoCall.get(0).status == true) {
                            intent.setClass(getActivity(), InsLocationActivity.class);
                            intent.putExtra(Constant.SUISIGN, resultInfoCall.get(0).id);
                            intent.putExtra(Constant.SUISICI, "1");
                        } else {
                            intent.setClass(getActivity(), SignNoteActivity.class);
                        }
                    }else{
                        intent.setClass(getActivity(), SignNoteActivity.class);
                    }
                }else{
                    intent.setClass(getActivity(), SignNoteActivity.class);
                }
                tvinstructor.setVisibility(View.GONE);
                tvinstructor.setBackgroundResource(R.drawable.circle_shuziwu);
                tvshuinstructor.setText(R.string.lingtiao);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.response(response,getActivity());
            }
        });
    }
}