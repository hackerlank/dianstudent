package com.dingli.diandians.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;


public class DaemonService extends Service {
    private AMapLocationClient mLocationClient;
    private AMapLocation aMapLocations;
    public static final int SERVICE_ID = 9510;
    int zizen;
    /**
     */
    private final static long WAKE_INTERVAL = 15 * 60 * 1000;
    @Override
    public void onCreate() {
        super.onCreate();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        String deviceId=mPushAgent.getRegistrationId();
        DianApplication.sharedPreferences.saveString(Constant.DEVICEID,deviceId);
        int timer=DianApplication.sharedPreferences.getIntValue(Constant.TIMER);
        int timers=timer*1000;
        Timer timersch=new Timer();
        if (timer>0) {
//            DianApplication.handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startLocation();
//                    DianApplication.handler.postDelayed(this, timers);
//                }
//            }, timers);
            TimerTask timerTask=new TimerTask() {
                @Override
                public void run() {
                    startLocation();
                }
            };
            timersch.schedule(timerTask,0,timers);
        }
        BroadcastReceiver receiver = new ScreenReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(receiver, intentFilter);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (zizen!=2){
            startLocation();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(SERVICE_ID, new Notification());
        } else {
            startForeground(SERVICE_ID, new Notification());
            Intent sendIntend = new Intent(this, ChannelService.class);
            startService(sendIntend);
        }
        try {
            //定时检查 WorkService 是否在运行，如果不在运行就把它拉起来
            //Android 5.0+ 使用 JobScheduler，效果比 AlarmManager 好
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Log.i(TAG, "开启 JobService 定时");
                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.cancelAll();
                JobInfo.Builder builder = new JobInfo.Builder(1024, new ComponentName(getPackageName(), ScheduleService.class.getName()));
                builder.setPeriodic(WAKE_INTERVAL);
//                builder.setPeriodic(DianApplication.sharedPreferences.getIntValue(Constant.TIMER));
                builder.setPersisted(true);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                int schedule = jobScheduler.schedule(builder.build());
                if (schedule <= 0) {
//                    Log.w(TAG, "schedule error！");
                }
            } else {
                //Android 4.4- 使用 AlarmManager
//                Log.i(TAG, "开启 AlarmManager 定时");
                Intent alarmIntent = new Intent("LOCATION_CLOCK");
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//                Intent alarmIntent = new Intent(getApplication(), DaemonService.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, 1024, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pendingIntent);
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), WAKE_INTERVAL, pendingIntent);
//                am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + WAKE_INTERVAL, WAKE_INTERVAL, pendingIntent);
            }
        } catch (Exception e) {
//            Log.e(TAG, "e:" + e);
        }
        //简单守护开机广播
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(getPackageName(), DaemonService.class.getName()),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d(TAG, "onDestroy()");
        Intent intent = new Intent(this, DaemonService.class);
        startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();
        if(null == mLocationClient){
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 每2秒定位一次
        mLocationOption.setInterval(200);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        //低耗
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            if (aMapLocations != null) {
                if (aMapLocations.getLongitude() != 0.0) {
                    final JSONObject object = new JSONObject();
                    if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.DEVICEID))) {
                        if (DianTool.isConnectionNetWork(DaemonService.this)) {
                            DianTool.huoqutoken();
                            HttpHeaders headers = new HttpHeaders();
                            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                            try {
                                object.put("address", aMapLocations.getAddress());
                                object.put("lltude", aMapLocations.getLatitude() + "-" + aMapLocations.getLongitude());
                                if (aMapLocations.getLocationType() != 1) {
                                    if (!TextUtils.isEmpty(DianApplication.user.strNetworkType)) {
                                        object.put("connectWay", DianApplication.user.strNetworkType);
                                        DianApplication.user.gpstype = DianApplication.user.strNetworkType;
                                    } else {
                                        if (aMapLocations.getLocationType() == 5) {
                                            object.put("connectWay", "wifi");
                                            DianApplication.user.gpstype = "wifi";
                                        } else if (aMapLocations.getLocationType() == 1) {
                                            object.put("connectWay", "gps");
                                            DianApplication.user.gpstype = "gps";
                                        } else if (aMapLocations.getLocationType() == 6) {
                                            object.put("connectWay", "4G");
                                            DianApplication.user.gpstype = "4G";
                                        } else {
                                            object.put("connectWay", DianApplication.user.gpstype);
                                        }
                                    }
                                } else {
                                    object.put("connectWay", "gps");
                                    DianApplication.user.gpstype = "gps";
                                }
                                object.put("equipmentCode", DianApplication.sharedPreferences.getStringValue(Constant.DEVICEID));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            OkGo.post(HostAdress.getRequest("/api/phone/v1/saveGPS")).headers(headers)
                                    .upJson(object).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    zizen=2;
                                }
                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    if (response.code() == 401) {
                                        Initoken.initoken(getApplicationContext());
                                    }
                                }
                            });
                        }
                    } else{
                    }
                } else{
                }
            } else{
            }
        }
    }
    /**
     * 停止定位
     */
    void stopLocation(){
        if(null != mLocationClient){
            mLocationClient.stopLocation();
        }
    }
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            aMapLocations=aMapLocation;
        }
    };
}
