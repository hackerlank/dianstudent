package com.dingli.diandians.newProject.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.utils.NetUtils;

import org.simple.eventbus.EventBus;

/**
 * Created by lwq.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//            //获得ConnectivityManager对象
//            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            //获取ConnectivityManager对象对应的NetworkInfo对象
//            //获取WIFI连接的信息
//            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            //获取移动数据连接的信息
//            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//                EventBus.getDefault().post("", BKConstant.EventBus.WIFINETWORK);
//               // Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
//            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//                EventBus.getDefault().post("", BKConstant.EventBus.WIFINETWORK);
//              //  Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
//            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//                EventBus.getDefault().post("", BKConstant.EventBus.DATANETWORK);
//               // Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
//            } else {
//                EventBus.getDefault().post("", BKConstant.EventBus.DATANETWORK);
//               // Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
//            }
//        }else {//API level 大于21
//            //这里的就不写了，前面有写，大同小异
//            //获得ConnectivityManager对象
//            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            //获取所有网络连接的信息
//            Network[] networks = connMgr.getAllNetworks();
//            //用于存放网络连接信息
//            StringBuilder sb = new StringBuilder();
//            //通过循环将网络信息逐个取出来
//            for (int i=0; i < networks.length; i++){
//                //获取ConnectivityManager对象对应的NetworkInfo对象
//                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
//                networkInfo.getType().
//                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
//            }
//          //  Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();
//        }
        if(NetUtils.isConnected(context)){//0无网络，1wifi,2手机
            if(NetUtils.isWifi(context)){
                EventBus.getDefault().post(1, BKConstant.EventBus.WIFINETWORK);
            }else {
                EventBus.getDefault().post(2, BKConstant.EventBus.WIFINETWORK);
            }
        }else {
            EventBus.getDefault().post(0, BKConstant.EventBus.WIFINETWORK);
        }
    }
}
