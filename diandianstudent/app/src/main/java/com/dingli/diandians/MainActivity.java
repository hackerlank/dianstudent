package com.dingli.diandians;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.QingJiaSty;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.firstpage.HybridShouyeFragment;
import com.dingli.diandians.found.FoundFragment;
import com.dingli.diandians.information.InformationFragment;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.personcenter.PersoncenterFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.umeng.message.PushAgent;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final String KEY_FRAGMENT_TAG = "fragment_tag";
    private static final String FRAGMENT_TAG_HOME = "home";
    private static final String FRAGMENT_TAG_PRODUCT = "product";
    private static final String FRAGMENT_TAG_MY_ASSERT = "my_assert";
    private static final String FRAGMENT_TAG_FOUND = "found";
    private String[] mFragmentTags = new String[]{FRAGMENT_TAG_HOME, FRAGMENT_TAG_PRODUCT,FRAGMENT_TAG_FOUND, FRAGMENT_TAG_MY_ASSERT};
    private String mFragmentCurrentTag = FRAGMENT_TAG_HOME;
    private LinearLayout[] mLayouts = null;
    private int[] NORMAL_BACKGROUD = new int[]{R.mipmap.firstpagexuan,R.mipmap.xiaoxian,R.mipmap.contact,R.mipmap.mine};
    private SparseIntArray mImgBgMap = new SparseIntArray(NORMAL_BACKGROUD.length);
//    RelativeLayout vpDianDian;
    public int chooseItem=0;
    long mWaitTime = 2000;
    long mTouchTime = 0;
    HybridShouyeFragment firstPageFragment;
//    SyllFragment syllbusFragment;
    InformationFragment informationFragment;
    FoundFragment foundFragment;
    PersoncenterFragment personcenterFragment;
//    FragmentTransaction ft;
LinearLayout home_tab_main,home_tab_wo,home_tab_found,home_tab_kebiao;
    ImageView ivfirstpage,ivkebiao,ivmine,ivfound;
    TextView tvmine,tvfirstpage,tvkebiao,tvfound,tvinfom;
    String deviceId;
    HttpHeaders headers;
    int studeshu,qiandaoshu;
    int zione=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreFragments();
            mFragmentCurrentTag =  savedInstanceState.getString(KEY_FRAGMENT_TAG);
        }
        setContentView(R.layout.activity_main);
        headers = new HttpHeaders();
        DianApplication.user.main=this;
        DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
        boolean mFirst = isFirstEnter(MainActivity.this, MainActivity.this.getClass().getName());
        if(mFirst) {
            Intent mIntent = new Intent();
            mIntent.setClass(MainActivity.this, YiDaoTuActivity.class);
           this.startActivity(mIntent);
            this.finish();
            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
        }else {
            init();
            setListener();
        }
    }
    private void initUMeng() {
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                do {
                    deviceId =mPushAgent.getRegistrationId();
                    if(!TextUtils.isEmpty(DianApplication.user.token)){
                        inithttp();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (TextUtils.isEmpty(deviceId));
            }
        }, 500);
    }
    void initoken(){
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITONE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, "");
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, "");
        DianApplication.user.token="";
        DianApplication.user.token_type="";
        if (DianTool.isConnectionNetWork(this)){
            DianApplication.user.account=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNTS);
            DianApplication.user.password=DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORDS);
            if (!TextUtils.isEmpty(DianApplication.user.account)) {
                HttpParams params1 = new HttpParams();
                headers.put("Content-Type", Constant.APPLICATION_FORMURL);
                headers.put("Encoding", "UTF-8");
                headers.put("Accept", Constant.APPLICATION_JSON);
                headers.put("Authorization", "Basic ZGxlZHVBcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==");
                params1.put("username", DianApplication.user.account);
                params1.put("password", DianApplication.user.password);
                params1.put("grant_type", "password");
                params1.put("scope", "read write");
                params1.put("client_secret", "mySecretOAuthSecret");
                params1.put("client_id", "dleduApp");
                OkGo.getInstance().addCommonHeaders(headers);
                OkGo.post(HostAdress.getRequestUrl("/oauth/token"))     // 请求方式和请求url
                        .tag(this).params(params1)                       // 请求的 tag, 主要用于取消对应的请求
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                // s 即为所需要的结果
                                ResultInfo resultInfotoken = JSON.parseObject(s, ResultInfo.class);
                                DianApplication.user.token = resultInfotoken.access_token;
                                DianApplication.user.token_type = resultInfotoken.token_type;
                                DianApplication.user.refresh_token = resultInfotoken.refresh_token;
                                DianApplication.user.strtoken = resultInfotoken.access_token.split("-");
                                DianApplication.sharedPreferences.saveString(Constant.SPLITONE, DianApplication.user.strtoken[0]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, DianApplication.user.strtoken[1]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, DianApplication.user.strtoken[2]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, DianApplication.user.strtoken[3]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, DianApplication.user.strtoken[4]);
                                DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, resultInfotoken.access_token);
                                DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, resultInfotoken.token_type);
                                DianApplication.sharedPreferences.saveString(Constant.REFRESHED, resultInfotoken.refresh_token);
                                initforma();
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                            }
                        });
            }
        }else{
            DianTool.dissMyDialog();
            DianTool.showTextToast(MainActivity.this,"请检查网络");
        }
    }
    void inithttp() {
        //教师端的推送(请假)
        HttpParams params=new HttpParams();
        params.put("android", deviceId);
        headers.put("Authorization", DianApplication.user.token_type + " " + DianApplication.user.token);
        OkGo.getInstance().addCommonHeaders(headers);
        OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/postdevicetoken")).tag(this).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                DianTool.dissMyDialog();
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.dissMyDialog();
                if (response.code()==401){
                    Initoken.initoken(MainActivity.this);
                }
            }
        });
    }
    void initforma(){
        if(DianTool.isConnectionNetWork(this)){
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/getstatus")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    zione=1;
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            if (!TextUtils.isEmpty(arg.get(i).module)) {
                                if (arg.get(i).module.equals("leave")) {
                                    if (arg.get(i).function.equals("student_notice")) {
                                        studeshu=arg.get(i).notRead;
                                    }
                                }
                                if (arg.get(i).module.equals("questionnaire")) {
                                    if (arg.get(i).function.equals("que_student_notice")) {
                                        if (arg.get(i).notRead != 0) {
                                            qiandaoshu=arg.get(i).notRead;
                                        }
                                    }
                                }
                                if(studeshu+qiandaoshu!=0) {
                                    tvinfom.setVisibility(View.VISIBLE);
                                    tvinfom.setText(String.valueOf(studeshu+qiandaoshu));
                                }
                            } else {
                                tvinfom.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    if (response.code()==401) {
                        Initoken.initoken(MainActivity.this);
                    }
                }
            });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    private boolean isFirstEnter(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))return false;
        String mResultStr = context.getSharedPreferences(Constant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(Constant.KEY_GUIDE_ACTIVITY, "");
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }
    void  restoreFragments(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        firstPageFragment= (HybridShouyeFragment) manager.findFragmentByTag(FRAGMENT_TAG_HOME);
        transaction.hide(firstPageFragment);
        informationFragment = (InformationFragment) manager.findFragmentByTag(FRAGMENT_TAG_PRODUCT);
        transaction.hide(informationFragment);
//        foundFragment = (FoundFragment) manager.findFragmentByTag(FRAGMENT_TAG_FOUND);
//        transaction.hide(foundFragment);
        personcenterFragment = (PersoncenterFragment) manager.findFragmentByTag(FRAGMENT_TAG_MY_ASSERT);
        transaction.hide(personcenterFragment);
        transaction.commit();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_FRAGMENT_TAG, mFragmentCurrentTag);
        super.onSaveInstanceState(outState);
    }
    public void init(){
//        vpDianDian=(RelativeLayout)findViewById(R.id.vpDianDian);
        home_tab_main=(LinearLayout)findViewById(R.id.home_tab_main);
        home_tab_kebiao=(LinearLayout)findViewById(R.id.home_tab_kebiao);
        home_tab_found=(LinearLayout)findViewById(R.id.home_tab_found);
        home_tab_wo=(LinearLayout)findViewById(R.id.home_tab_wo);
        ivfirstpage=(ImageView)findViewById(R.id.ivfirstpage);
        ivkebiao=(ImageView)findViewById(R.id.ivkebiao);
        ivfound=(ImageView)findViewById(R.id.ivfound);
        ivmine=(ImageView)findViewById(R.id.ivmine);
        tvfirstpage=(TextView)findViewById(R.id.tvfirstpage);
        tvkebiao=(TextView)findViewById(R.id.tvkebiao);
        tvinfom=(TextView)findViewById(R.id.tvinfom);
        tvfound=(TextView)findViewById(R.id.tvfound);
        tvmine=(TextView)findViewById(R.id.tvmine);
        home_tab_main.setTag(new TabViewHolder(ivfirstpage, tvfirstpage));
        home_tab_kebiao.setTag(new TabViewHolder(ivkebiao, tvkebiao));
        home_tab_wo.setTag(new TabViewHolder(ivmine, tvmine));
        home_tab_found.setTag(new TabViewHolder(ivfound,tvfound));
        mLayouts = new LinearLayout[]{home_tab_main,home_tab_kebiao,home_tab_found, home_tab_wo};
        mImgBgMap.put(R.id.ivfirstpage,R.mipmap.firstpage);
        mImgBgMap.put(R.id.ivkebiao,R.mipmap.xiaoxiliang);
        mImgBgMap.put(R.id.ivfound,R.mipmap.contact_click);
        mImgBgMap.put(R.id.ivmine,R.mipmap.minexuan);
    }
    /**
     * 先全部隐藏
     * @param fragmentManager
     * @param transaction
     */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < mFragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(mFragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }
    private void setListener() {
        for (int i = 0; i < mLayouts.length; i++) {
            mLayouts[i].setOnClickListener(this);
        }
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.KEY))){
            mFragmentCurrentTag=FRAGMENT_TAG_PRODUCT;
            DianApplication.sharedPreferences.saveString(Constant.KEY,"");
        }
        if (TextUtils.equals(FRAGMENT_TAG_HOME, mFragmentCurrentTag)) {
            home_tab_main.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_PRODUCT, mFragmentCurrentTag)) {
            home_tab_kebiao.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_FOUND, mFragmentCurrentTag)) {
        home_tab_found.performClick();
       }else if (TextUtils.equals(FRAGMENT_TAG_MY_ASSERT, mFragmentCurrentTag)) {
            home_tab_wo.performClick();
        }
    }
    /**
     * 设置底部背景为正常状态
     */
    private void setNormalBackgrounds() {
        for (int i = 0; i < mLayouts.length; i++) {
            setTabBackgroud(mLayouts[i], NORMAL_BACKGROUD[i], R.color.qianblue);
        }
    }
    /**
     * 设置底部背景为选中状态
     */
    private void setSelectedBackgroud(LinearLayout linearLayout) {
        TabViewHolder tabViewHolder = (TabViewHolder) linearLayout.getTag();
        int imgResId = mImgBgMap.get(tabViewHolder.img.getId());
        setTabBackgroud(linearLayout, imgResId, R.color.qianblue);
    }
    private void setTabBackgroud(LinearLayout linearLayout, int imgResId, int colorResId) {
        TabViewHolder tabViewHolder = (TabViewHolder) linearLayout.getTag();
        tabViewHolder.img.setImageResource(imgResId);
        tabViewHolder.txt.setTextColor(getResources().getColor(colorResId));
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
            long currentTime=System.currentTimeMillis();
            if((currentTime-mTouchTime)>=mWaitTime){
                DianTool.showTextToast(this, getResources().getString(R.string.zaian));
                mTouchTime=currentTime;
            }else{
                finish();
            }
            DianApplication.user.libiao=null;
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        setNormalBackgrounds();
        switch (v.getId()){
            case R.id.home_tab_main:
                if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    if (zione == 0) {
                        initoken();
                    }
                }
                DianApplication.user.libiao=null;
                setSelectedBackgroud((LinearLayout)v);
                mFragmentCurrentTag = FRAGMENT_TAG_HOME;
                if (firstPageFragment == null) {
                    firstPageFragment = new HybridShouyeFragment();
                    transaction.add(R.id.vpDianDian, firstPageFragment, FRAGMENT_TAG_HOME);
                }
                transaction.show(firstPageFragment);
                break;
            case R.id.home_tab_kebiao:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                }else {
                    DianApplication.user.libiao = "infor";
                    v.clearAnimation();
                    tvinfom.setVisibility(View.GONE);
                    setSelectedBackgroud((LinearLayout) v);
                    mFragmentCurrentTag = FRAGMENT_TAG_PRODUCT;

                    if (informationFragment == null) {
                        informationFragment = new InformationFragment();
                        transaction.add(R.id.vpDianDian, informationFragment, FRAGMENT_TAG_PRODUCT);
                    }
                    transaction.show(informationFragment);
                }
                break;
            case R.id.home_tab_wo:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }else {
                    initforma();
                    DianApplication.user.libiao = "mine";
                    setSelectedBackgroud((LinearLayout) v);
                    mFragmentCurrentTag = FRAGMENT_TAG_MY_ASSERT;
                    if (personcenterFragment == null) {
                        personcenterFragment = new PersoncenterFragment();
                        transaction.add(R.id.vpDianDian, personcenterFragment, FRAGMENT_TAG_MY_ASSERT);
                    }
                    transaction.show(personcenterFragment);
                }
                break;
            case R.id.home_tab_found:
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }else {
                    initforma();
                    DianApplication.user.libiao = "found";
                    setSelectedBackgroud((LinearLayout) v);
                    mFragmentCurrentTag = FRAGMENT_TAG_FOUND;
                    if (foundFragment == null) {
                        foundFragment = new FoundFragment();
                        transaction.add(R.id.vpDianDian, foundFragment, FRAGMENT_TAG_FOUND);
                    }
                    transaction.show(foundFragment);
                }
                break;
        }
        transaction.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            initUMeng();
        }
//        UpdateFunGO.onResume(this);
        if (TextUtils.isEmpty(DianApplication.user.libiao)){
            mFragmentCurrentTag=FRAGMENT_TAG_HOME;
        }else if(DianApplication.user.libiao.equals("infor")){
            mFragmentCurrentTag=FRAGMENT_TAG_PRODUCT;
        }else if(DianApplication.user.libiao.equals("found")){
            mFragmentCurrentTag=FRAGMENT_TAG_FOUND;
        }else if(DianApplication.user.libiao.equals("mine")){
            mFragmentCurrentTag=FRAGMENT_TAG_MY_ASSERT;
        }
        if (TextUtils.equals(FRAGMENT_TAG_HOME, mFragmentCurrentTag)) {
            home_tab_main.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_PRODUCT, mFragmentCurrentTag)) {
            home_tab_kebiao.performClick();
        } else if (TextUtils.equals(FRAGMENT_TAG_MY_ASSERT, mFragmentCurrentTag)) {
            home_tab_wo.performClick();
        }else if (TextUtils.equals(FRAGMENT_TAG_FOUND, mFragmentCurrentTag)) {
            home_tab_found.performClick();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
//        UpdateFunGO.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    private class TabViewHolder {
        public ImageView img;
        public TextView txt;

        public TabViewHolder(ImageView img, TextView txt) {
            this.img = img;
            this.txt = txt;
        }
    }
}
