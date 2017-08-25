package com.dingli.diandians.common;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.dingli.diandians.newProject.constants.BKPreference;
import com.dingli.diandians.newProject.moudle.hrd.PolyvDemoService;
import com.dingli.diandians.newProject.utils.SPUtils;
import com.dingli.diandians.service.AlarmReceiver;
import com.dingli.diandians.service.DaemonService;
import com.dingli.diandians.service.Receiver2;
import com.dingli.diandians.service.Service2;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.lzy.okgo.OkGo;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Administrator on 2016/3/4.
 */
public class DianApplication extends Application {
    //DaemonApplication
    public static DianSharedPreferences sharedPreferences;
    private static DianApplication instance;
    public static UserInfo user;
    public static Data data;
    public static int mNetWorkState;
    public static List<Activity> activityList = new LinkedList<Activity>();
    public static List<Activity> alist=new LinkedList<>();
    PushAgent mPushAgent;
    public static String code;
    private static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        initYaoData();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        //开机启动
        mPushAgent.onAppStart();
        mNetWorkState=NetUtil.getNetworkState(this);
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }
            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                super.dealWithCustomAction(context, uMessage);
            }
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                DianApplication.sharedPreferences.saveString(Constant.KEY,"infor");
                super.launchApp(context, uMessage);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        OkGo.init(this);
        try {
            OkGo.getInstance()
                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo")
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS);   //全局的写入超时时间
//                    .setCacheMode(CacheMode.NO_CACHE)
//                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
            }
            @Override
            public void onFailure(String s, String s1) {
            }
        });
        code=getVersionCode(this);
        initPolyvCilent();
    }
    private void initYaoData() {
        user = new UserInfo();
        data=new Data();
        sharedPreferences = new DianSharedPreferences();
    }
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    public void remove(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }
    public String getVersionCode(Context context) {
        return getPackageInfo(context).versionName;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
    public static  DianApplication getInstance(){
        return instance;
    }


    //保利威视初始化
    ServiceStartErrorBroadcastReceiver serviceStartErrorBroadcastReceiver;
    /** 加密秘钥 */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /** 加密向量 */
    private String iv = "2u9gDPKdX6GyQJKU";
    public void initPolyvCilent() {
        //OPPO手机自动熄屏一段时间后，会启用系统自带的电量优化管理，禁止一切自启动的APP（用户设置的自启动白名单除外）。
        //如果startService异常，就会发送消息上来提醒异常了
        //如不需要额外处理，也可不接收此信息
//        IntentFilter statusIntentFilter = new IntentFilter(AndroidService.SERVICE_START_ERROR_BROADCAST_ACTION);
//        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
//        serviceStartErrorBroadcastReceiver = new ServiceStartErrorBroadcastReceiver();
//        LocalBroadcastManager.getInstance(this).registerReceiver(serviceStartErrorBroadcastReceiver, statusIntentFilter);

        //网络方式取得SDK加密串，（推荐）
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        // client.setConfig("iPGXfu3KLEOeCW4KXzkWGl1UYgrJP7hRxUfsJGldI6DEWJpYfhaXvMA+32YIYqAOocWd051v5XUAU17LoVlgZCSEVNkx11g7CxYadcFPYPozslnQhFjkxzzjOt7lUPsWF/CO2xt5xZemQCBkkSKLGA==", aeskey, iv, getApplicationContext());
        //userid：e1510bdd3a   writetoken：3934b9a2-351e-4063-9fd1-6e60e380638f
        // readtoken：14ee1a08-294b-4e1d-8f9e-b23842471b8a   secretkey：E8vnZopmkt

        client.setConfig("e1510bdd3a" , "3934b9a2-351e-4063-9fd1-6e60e380638f", "14ee1a08-294b-4e1d-8f9e-b23842471b8a", "E8vnZopmkt");
        //初始化数据库服务
        client.initDatabaseService(this);
        //启动服务
        client.startService(getApplicationContext(), PolyvDemoService.class);
        //启动Bugly
//		client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        //获取SD卡信息
//        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {
//
//            @Override
//            public void callback() {
//                if (PolyvDevMountInfo.getInstance().isSDCardAvaiable() == false) {
//                    Log.e(TAG, "没有可用的存储设备");
//                    return;
//                }
//                StringBuilder dirPath = new StringBuilder();
//                dirPath.append(PolyvDevMountInfo.getInstance().getSDCardPath()).append(File.separator).append("polyvdownload");
//                File saveDir = new File(dirPath.toString());
//                if (saveDir.exists() == false) {
//                    saveDir.mkdir();
//                }
//
//                //如果生成不了文件夹，可能是外部SD卡需要写入特定目录/storage/sdcard1/Android/data/包名/
//                if (saveDir.exists() == false) {
//                    dirPath.delete(0, dirPath.length());
//                    dirPath.append(PolyvDevMountInfo.getInstance().getSDCardPath()).append(File.separator).append("Android").append(File.separator).append("data")
//                            .append(File.separator).append(getPackageName()).append(File.separator).append("polyvdownload");
//                    saveDir = new File(dirPath.toString());
//                    getExternalFilesDir(null); // 生成包名目录
//                    saveDir.mkdirs();
//                }
//
//                PolyvSDKClient.getInstance().setDownloadDir(saveDir);
//            }
//        });
    }
    private class ServiceStartErrorBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
        }
    }
    //用户相关
    public String getAuthorization() {
        if (TextUtils.isEmpty((String) SPUtils.get(this, BKPreference.KEY_BASIC_AO, ""))) {
            DianApplication.user.token_type=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
            DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.DATA_TOKEN);
            SPUtils.put(this, BKPreference.KEY_BASIC_AO, DianApplication.user.token_type + "" + DianApplication.user.token);
            return DianApplication.user.token_type + "" + DianApplication.user.token;
        } else {
            return (String) SPUtils.get(this, BKPreference.KEY_BASIC_AO, BKPreference.KEY_BASIC_AO_RESET);
        }
    }
    public void setAuthorization(String ao) {

        SPUtils.put(this, BKPreference.KEY_BASIC_AO, ao);
    }
    public void setRefreshToken(String refresh_token) {
        SPUtils.put(this, BKPreference.REFRESHED, refresh_token);
    }
    public String getRefreshToken() {
        if (TextUtils.isEmpty((String) SPUtils.get(this, BKPreference.REFRESHED, ""))) {
            DianApplication.user.refresh_token=DianApplication.sharedPreferences.getStringValue(Constant.REFRESHED);
            SPUtils.put(this, BKPreference.REFRESHED, DianApplication.user.refresh_token);
            return DianApplication.user.refresh_token;
        }else {
            return (String) SPUtils.get(this, BKPreference.REFRESHED, "");}
    }
    public void setUserInfo( UserInfo user) {
        if(null!=user){
            if(!TextUtils.isEmpty(user.account)){
                SPUtils.put(this, BKPreference.ACCOUNT, user.account);
            }
            if(!TextUtils.isEmpty(user.password)){
                SPUtils.put(this, BKPreference.PASSWORD, user.password);
            } if(!TextUtils.isEmpty(user.orgainname)){
                SPUtils.put(this, BKPreference.NAME, user.orgainname);
            }
        }
    }
    /**
     * 因为API数超过了64K，用的MultiDex库,application中没有重写attachBaseContext方法。导致找不到类库的方法；
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
