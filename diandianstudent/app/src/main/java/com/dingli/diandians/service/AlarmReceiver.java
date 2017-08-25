package com.dingli.diandians.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/1/4.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private PowerManager powerManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("LOCATION_CLOCK")) {
            if (powerManager == null) {
//                //针对熄屏后cpu休眠导致的无法联网、定位失败问题,通过定期点亮屏幕实现联网,本操作会导致cpu无法休眠耗电量增加,谨慎使用
                powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "bright");
                wl.acquire(60*1000);
                Intent locationIntent = new Intent(context, DaemonService.class);
                context.startService(locationIntent);
                //点亮屏幕
                wl.release();
//                //释放
            }
        }
    }
}
