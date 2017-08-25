package com.dingli.diandians.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MonitorReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent target = new Intent(context, DaemonService.class);
        context.startService(target);
    }
}
