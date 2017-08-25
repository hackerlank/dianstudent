package com.dingli.diandians.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by dingliyuangong on 2017/2/23.
 */

public class Service2 extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1=new Intent(this,DaemonService.class);
        startService(intent1);
        return super.onStartCommand(intent, flags, startId);
    }
}
