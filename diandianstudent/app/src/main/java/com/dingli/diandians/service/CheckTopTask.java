package com.dingli.diandians.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by Silence on 2016.12.31
 */
public class CheckTopTask implements Runnable {
    private Context context;

    public CheckTopTask(Context context) {
        this.context = context;
    }

    public static void startForeground(Context context) {
        try {
            Intent intent = new Intent(context, OnepxActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        boolean foreground = isForeground(context);
        if (!foreground) {
            startForeground(context);
        }
    }

    private boolean isForeground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses != null) {
                int myPid = android.os.Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo.pid == myPid) {
                        return runningAppProcessInfo.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

}