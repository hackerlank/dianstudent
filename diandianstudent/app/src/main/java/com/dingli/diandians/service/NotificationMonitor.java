
package com.dingli.diandians.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

public class NotificationMonitor extends NotificationListenerService {
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation aMapLocations;


    private static final String TAG = "SevenNLS";
    private static final String TAG_PRE = "[" + NotificationMonitor.class.getSimpleName() + "] ";
    private static final int EVENT_UPDATE_CURRENT_NOS = 0;
    public static final String ACTION_NLS_CONTROL = "com.seven.notificationlistenerdemo.NLSCONTROL";
    public static List<StatusBarNotification[]> mCurrentNotifications = new ArrayList<StatusBarNotification[]>();
    public static int mCurrentNotificationsCounts = 0;
    public static StatusBarNotification mPostedNotification;
    public static StatusBarNotification mRemovedNotification;
    public static final int NOTIFICATION_ID=0x11;
    private CancelNotificationReceiver mReceiver = new CancelNotificationReceiver();
    TimerTask timerTask;
    private Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_UPDATE_CURRENT_NOS:
                    updateCurrentNotifications();
                    break;
                case 1:
                    startLocation();
                    break;
                default:
                    break;
            }
        }
    };
    class CancelNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent != null && intent.getAction() != null) {
                action = intent.getAction();
                if (action.equals(ACTION_NLS_CONTROL)) {
                    String command = intent.getStringExtra("command");
                    if (TextUtils.equals(command, "cancel_last")) {
                        if (mCurrentNotifications != null && mCurrentNotificationsCounts >= 1) {
                            StatusBarNotification sbnn = getCurrentNotifications()[mCurrentNotificationsCounts - 1];
                            cancelNotification(sbnn.getPackageName(), sbnn.getTag(), sbnn.getId());
                        }
                    } else if (TextUtils.equals(command, "cancel_all")) {
                        cancelAllNotifications();
                    }
                }
            }
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NLS_CONTROL);
        registerReceiver(mReceiver, filter);
        mMonitorHandler.sendMessage(mMonitorHandler.obtainMessage(EVENT_UPDATE_CURRENT_NOS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // a.equals("b");
//        logNLS("onBind...");
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocation();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                Message message=Message.obtain();
                message.what=1;
                mMonitorHandler.sendMessage(message);
            }
        };
        return START_REDELIVER_INTENT;
//        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        updateCurrentNotifications();
//        logNLS("onNotificationPosted...");
//        logNLS("have " + mCurrentNotificationsCounts + " active notifications");
        mPostedNotification = sbn;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        updateCurrentNotifications();
//        logNLS("removed...");
//        logNLS("have " + mCurrentNotificationsCounts + " active notifications");
        mRemovedNotification = sbn;
    }

    private void updateCurrentNotifications() {
        try {
            StatusBarNotification[] activeNos = getActiveNotifications();
            if (mCurrentNotifications.size() == 0) {
                mCurrentNotifications.add(null);
            }
            mCurrentNotifications.set(0, activeNos);
            mCurrentNotificationsCounts = activeNos.length;
        } catch (Exception e) {
//            logNLS("Should not be here!!");
            e.printStackTrace();
        }
    }

    public static StatusBarNotification[] getCurrentNotifications() {
        if (mCurrentNotifications.size() == 0) {
//            logNLS("mCurrentNotifications size is ZERO!!");
            return null;
        }
        return mCurrentNotifications.get(0);
    }

//    private static void logNLS(Object object) {
//        Log.i(TAG, TAG_PRE + object);
//    }



    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();
        if(null == mLocationClient){
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }
        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(true);
        // 每10秒定位一次
        mLocationOption.setInterval(200);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        //低耗
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
        if (aMapLocations!=null) {
            if (aMapLocations.getLongitude() != 0.0) {
                final JSONObject object = new JSONObject();
                if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.DEVICEID))) {
                    if (DianTool.isConnectionNetWork(NotificationMonitor.this)) {
                        DianTool.huoqutoken();
                        HttpHeaders headers = new HttpHeaders();
                        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                        try {
                            object.put("address", aMapLocations.getAddress());
                            object.put("lltude", aMapLocations.getLatitude() + "-" + aMapLocations.getLongitude());
                            if (aMapLocations.getLocationType()!=1){
                                if (!TextUtils.isEmpty(DianApplication.user.strNetworkType)){
                                    object.put("connectWay", DianApplication.user.strNetworkType);
                                    DianApplication.user.gpstype = DianApplication.user.strNetworkType;
                                }else{
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
                            }else{
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
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                if (response.code()==401){
                                    Initoken.initoken(getApplicationContext());
                                }
                            }
                        });
                    }
                }
            }
        }else{
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
