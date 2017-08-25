package com.dingli.diandians.firstpage;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ant.liao.GifView;
import com.bumptech.glide.Glide;
import com.dingli.diandians.BuildConfig;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.Coursecenter;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.NetUtil;
import com.dingli.diandians.common.ResultOne;
import com.dingli.diandians.firstpage.adapter.GlideImageLoader;
import com.dingli.diandians.firstpage.adapter.ShouYeAdapter;
import com.dingli.diandians.firstpage.adapter.ShouYeGridAdapter;
import com.dingli.diandians.firstpage.school.SchoolWebActivity;
import com.dingli.diandians.found.WebViewLianxiActivity;
import com.dingli.diandians.information.instructor.SignNoteActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.lostproperty.WebViewLostActivity;
import com.dingli.diandians.newProject.moudle.camera.CaptureActivity;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.qingjia.VacateActivity;
import com.dingli.diandians.schedule.SyllFragment;
import com.dingli.diandians.setting.HelpActivity;
import com.dingli.diandians.survey.WebViewSurveyActivity;
import com.dingli.diandians.view.MyGrideView;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class ShouyeFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks{

    private List<ResultOne> listid;
    private ScheduledExecutorService scheduledExecutorService;
    private ListView listView;
    private TextView tvSaoSao;
    ShouYeAdapter shouyeadapter;
    VerticalSwipeRefreshLayout refreshouye;
    MyGrideView myshouye;
    LinearLayout lineaone,lineatwo;
    LinearLayout tvduanwang;
    LinearLayout[] liall;
    Banner banner;
    ResultOne resultOne;
    public static ShouyeFragment newInstance(){
        ShouyeFragment shouyeFragment=new ShouyeFragment();
        return  shouyeFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_blank, container, false);
        initviews(v);
        refreshouye.setRefreshing(true);
        initbanner();
        initdata();
        return  v;
    }
    void initviews(View v){
        listView= (ListView) v.findViewById(R.id.listView);
        tvSaoSao= (TextView) v.findViewById(R.id.tvSaoSao);
        refreshouye=(VerticalSwipeRefreshLayout) v.findViewById(R.id.refreshouye);
        DianTool.refresh(refreshouye,getActivity());
        listView.addHeaderView(getViews());
        shouyeadapter=new ShouYeAdapter();
        listView.setAdapter(shouyeadapter);
        tvduanwang=(LinearLayout)v.findViewById(R.id.tvduanwang);
        TextView textnames=(TextView)v.findViewById(R.id.tvblank);
        if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.ORGAINER))){
            textnames.setText("首页");
        }else{
            textnames.setText(DianApplication.sharedPreferences.getStringValue(Constant.ORGAINER));
        }
        refreshouye.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DianApplication.sharedPreferences.saveString("homepage","");
                    initbanner();
                initdata();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (BuildConfig.DIANSTUDENT_LIVE) {
//            tvSaoSao.setVisibility(View.GONE);
//        }else {
//            tvSaoSao.setVisibility(View.VISIBLE);
//        }
        tvSaoSao.setOnClickListener(view -> {
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
               Intent intent=new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                return;
            }
            if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
                startActivity(new Intent(getActivity(), CaptureActivity.class));
            } else {
                EasyPermissions.requestPermissions(getActivity(), getString(R.string.permission_hint),
                        CAMERA_PERMISSIONS, Manifest.permission.CAMERA);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.dianming:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                    getActivity().startActivity(intent);
                    startintent(intent);
                }else {
                    Initoken.signId(getActivity());
                }
                break;
            case R.id.suill:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), VacateActivity.class);
                }
                startintent(intent);
                break;
            case R.id.veryll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), SyllFragment.class);
                }
                startintent(intent);
                break;
            case R.id.chakanll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), FirstPageFragment.class);
                }
                startintent(intent);
                break;
            case R.id.kebiao:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), SignNoteActivity.class);
                }
                startintent(intent);
                break;
            case R.id.daochu_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewLianxiActivity.class);
                    bundle.putString("url", Constant.weblianxi+"/mobileui/");
                    intent.putExtras(bundle);
                }
                startintent(intent);
                break;
            case R.id.school_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), SchoolWebActivity.class);
                    intent.putExtra("url", "/mobileui/schoolNews");
                    intent.putExtra("title", "校园动态");
                    intent.putExtra("isRefresh", false);
                    intent.putExtra("isStatusBar", true);
                    intent.putExtra("domainName", "dd_mobile");
                }
                startintent(intent);
                break;
            case R.id.zhiye_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(),LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewSurveyActivity.class);
                    bundle.putString("url", Constant.webdiaocha + "/mobileui/questatuslist");
                    intent.putExtras(bundle);
                }
                startintent(intent);
                break;
            case R.id.diandian_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewOneActivity.class);
                    intent.putExtra("url","/mobileui_hy");
                    intent.putExtra("title","");
                }
                startintent(intent);
                break;
            case R.id.rollcall_ll:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewsActivity.class);
                    intent.putExtra("url", "/mobileui/allArticle");
                }
                startintent(intent);
                break;
            case R.id.hyall:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent.setClass(getActivity(), WebViewsActivity.class);
                    intent.putExtra("url", "/mobileui/allArticle");
                }
                startintent(intent);
                break;
            default:
                DianTool.showTextToast(getActivity(),"当前版本不支持此功能，请升级最新版本");
                break;
        }
    }
    void startintent(Intent intent){
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    private View getViews(){
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shouye,null);
        LinearLayout hyall=(LinearLayout) v.findViewById(R.id.hyall);
        TextView tvmachine=(TextView) v.findViewById(R.id.tvmachine);
        TextView tvsuill=(TextView) v.findViewById(R.id.tvsuill);
        TextView tvverd=(TextView) v.findViewById(R.id.tvverd);
        TextView tvchake=(TextView) v.findViewById(R.id.tvchake);
        TextView tvkenumb=(TextView) v.findViewById(R.id.tvkenumb);
        TextView tvdaochull=(TextView) v.findViewById(R.id.tvdaochull);
        TextView tvschool=(TextView) v.findViewById(R.id.tvschool);
        TextView tvzhiye=(TextView) v.findViewById(R.id.tvzhiye);
        TextView tvdianxin=(TextView) v.findViewById(R.id.tvdianxin);
        TextView tvrollca=(TextView) v.findViewById(R.id.tvrollca);
        LinearLayout suill=(LinearLayout) v.findViewById(R.id.suill);
        LinearLayout veryll=(LinearLayout) v.findViewById(R.id.veryll);
        LinearLayout chakanll=(LinearLayout) v.findViewById(R.id.chakanll);
        LinearLayout dianming= (LinearLayout) v.findViewById(R.id.dianming);
        LinearLayout kebiao= (LinearLayout) v.findViewById(R.id.kebiao);
        LinearLayout daochu_ll= (LinearLayout) v.findViewById(R.id.daochu_ll);
        final LinearLayout school_ll= (LinearLayout) v.findViewById(R.id.school_ll);
        LinearLayout zhiye_ll= (LinearLayout) v.findViewById(R.id.zhiye_ll);
        LinearLayout diandian_ll= (LinearLayout) v.findViewById(R.id.diandian_ll);
        LinearLayout rollcall_ll= (LinearLayout) v.findViewById(R.id.rollcall_ll);
        ImageView ivsuill=(ImageView) v.findViewById(R.id.ivsuill);
        ImageView ivverd=(ImageView) v.findViewById(R.id.ivverd);
        ImageView ivchake=(ImageView) v.findViewById(R.id.ivchake);
        ImageView ivkenumb=(ImageView) v.findViewById(R.id.ivkenumb);
        ImageView ivdaochull=(ImageView) v.findViewById(R.id.ivdaochull);
        ImageView ivschool=(ImageView) v.findViewById(R.id.ivschool);
        ImageView ivzhiye=(ImageView) v.findViewById(R.id.ivzhiye);
        ImageView ivdianxin=(ImageView) v.findViewById(R.id.ivdianxin);
        ImageView ivrollcall=(ImageView) v.findViewById(R.id.ivrollcall);
        myshouye=(MyGrideView) v.findViewById(R.id.myshouye);
        lineaone=(LinearLayout) v.findViewById(R.id.lineaone);
        lineatwo=(LinearLayout) v.findViewById(R.id.lineatwo);
        banner = (Banner) v.findViewById(R.id.myviewPager);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent=new Intent();
                if (resultOne!=null){
                    if (resultOne.banner.length!=0){
                        for (int i=0;i<resultOne.banner.length;i++){
                            String targetTitle=resultOne.banner[i].targetTitle;
                            String targetUrl=resultOne.banner[i].targetUrl.trim();
                            boolean isNeedLogin=resultOne.banner[i].isNeedLogin;
                            if (position==i){
                                if (targetUrl.contains("helperCenter")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        intent.setClass(getActivity(), HelpActivity.class);
                                    }
                                }else if (targetUrl.contains("lostproperty")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        intent.setClass(getActivity(), WebViewLostActivity.class);
                                    }
                                }else if (targetUrl.contains("article?")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        String id = targetUrl.substring(targetUrl.indexOf("?") + 1);
                                        intent.setClass(getActivity(), WebViewFiveActivity.class);
                                        intent.putExtra("targetTitle", targetTitle);
                                        intent.putExtra("targetUrl", targetUrl);
                                        intent.putExtra("id", id);
                                    }
                                }else if (targetUrl.contains("allArticle")){
                                    if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                        intent.setClass(getActivity(), LoginActivity.class);
                                    }else {
                                        intent.setClass(getActivity(), WebViewsActivity.class);
                                        intent.putExtra("url", "/mobileui/allArticle");
                                    }
                                }else{
                                    intent.setClass(getActivity(), WebViewFourActivity.class);
                                    intent.putExtra("targetTitle", targetTitle);
                                    intent.putExtra("targetUrl", targetUrl);
                                }
                                getActivity().startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }
                        }
                    }
                }
            }
        });
        liall=new LinearLayout[]{zhiye_ll,diandian_ll,rollcall_ll,veryll,chakanll,kebiao,daochu_ll,school_ll,suill,dianming};
        hyall.setOnClickListener(this);
        for (int i=0;i<liall.length;i++){
            liall[i].setOnClickListener(this);
        }
        listid=new ArrayList<>();
        return v;
    }
    void trueclick(){
        for (int i=0;i<liall.length;i++){
            liall[i].setClickable(true);
        }
    }
    void falseclick(){
        for (int i=0;i<liall.length;i++){
            liall[i].setClickable(false);
        }
    }
    void initbanner(){
        String json=DianApplication.sharedPreferences.getStringValue("homepage");
        if (TextUtils.isEmpty(json)) {
            if (DianTool.isConnectionNetWork(getActivity())) {
                tvduanwang.setVisibility(View.GONE);
                falseclick();
                HttpParams params = new HttpParams();
                params.put("role", "student");
                params.put("version","V2");
                OkGo.get(HostAdress.getRequest("/api/phone/v1/listHomePageV2")).tag(this).params(params).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        DianApplication.sharedPreferences.saveString("homepage",s);
                        initnorefresh();
                        if (!TextUtils.isEmpty(s)) {
                            resshouye(s);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        initnorefresh();
                        visible();
                        trueclick();
                    }
                });
            } else {
                initnorefresh();
                tvduanwang.setVisibility(View.VISIBLE);
                DianTool.showTextToast(getActivity(), "请检查网络");
            }
        }else{
            initnorefresh();
            resshouye(json);
        }
    }
    void resshouye(String s){
        listid.clear();
        List<String> imgUrl=new ArrayList<>();
        resultOne = JSON.parseObject(s, ResultOne.class);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        for (int i = 0; i < resultOne.banner.length; i++) {
            listid.add(resultOne.banner[i]);
            imgUrl.add(resultOne.banner[i].iconUrl);
        }
        //设置图片集合
        banner.setImages(imgUrl);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        if (resultOne.menu.length != 0) {
            myshouye.setVisibility(View.VISIBLE);
            ShouYeGridAdapter grideadapter = new ShouYeGridAdapter(resultOne.menu);
            myshouye.setAdapter(grideadapter);
            lineaone.setVisibility(View.GONE);
            lineatwo.setVisibility(View.GONE);
        } else {
            visible();
            trueclick();
        }
    }
    void visible(){
        myshouye.setVisibility(View.GONE);
        lineaone.setVisibility(View.VISIBLE);
        lineatwo.setVisibility(View.VISIBLE);
    }
    void initnorefresh(){
        refreshouye.setRefreshing(false);
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(getActivity())){
            OkGo.get(HostAdress.getWenZhang("/api/web/v1/articleManagement/articleManagementShow/articleList4")).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    initnorefresh();
                    DianApplication.sharedPreferences.saveString("listbiao",s);
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("[]")) {
                            List<Course> coursecenter = JSON.parseArray(s, Course.class);
                            shouyeadapter.refreshShouYe(coursecenter);
                            shouyeadapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    initnorefresh();
                    if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue("listbiao"))){
                        String s=DianApplication.sharedPreferences.getStringValue("listbiao");
                        if (!TextUtils.isEmpty(s)) {
                            if (!s.equals("[]")) {
                                List<Course> coursecenter = JSON.parseArray(s, Course.class);
                                shouyeadapter.refreshShouYe(coursecenter);
                                shouyeadapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }else{
            initnorefresh();
            DianTool.showTextToast(getActivity(),"请检查网络");
        }
    }


    //以下为相机权限管理
    private static final int CAMERA_PERMISSIONS = 0x001;
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            startActivity(new Intent(getActivity(), CaptureActivity.class));
        } else {
            ToastUtils.showLong(getContext(), R.string.permission_hint_content);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == CAMERA_PERMISSIONS) {
            ToastUtils.showLong(getContext(), R.string.permission_hint_content);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

