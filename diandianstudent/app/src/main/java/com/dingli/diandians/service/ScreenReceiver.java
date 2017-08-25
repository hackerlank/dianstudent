package com.dingli.diandians.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dingli.diandians.common.DianApplication;


public class ScreenReceiver extends BroadcastReceiver {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private CheckTopTask mCheckTopTask = new CheckTopTask(DianApplication.getAppContext());

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        // 这里可以启动一些服务
        try {
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
//                Log.i(TAG, "锁屏开启一像素");
                CheckTopTask.startForeground(context);
                mHandler.postDelayed(mCheckTopTask, 3000);
            } else if ("android.intent.action.USER_PRESENT".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
//                Log.i(TAG, "开屏关闭一像素");
                OnepxActivity onePxActivity = OnepxActivity.instance != null ? OnepxActivity.instance.get() : null;
                if (onePxActivity != null) {
                    onePxActivity.finishSelf();
                }
                mHandler.removeCallbacks(mCheckTopTask);
            }
        } catch (Exception e) {
        }
    }
}
