package com.dingli.diandians.qingjia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dingli.diandians.common.DianApplication;

/**
 * Created by dingliyuangong on 2016/11/3.
 */
public class BrocastRe extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
            DianApplication.user.alist=null;
        }
        if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
            DianApplication.user.alist=null;
        }
        if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){
            DianApplication.user.alist=null;
        }
    }
}
