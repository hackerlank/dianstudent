package com.dingli.diandians.firstpage.hybrid;

import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

/**
 * Created by dingliyuangong on 2017/1/14.
 */
public class JsInterface {
    public  Handler handler;
    public JsInterface(Handler handler){
        this.handler=handler;
    }
    @JavascriptInterface
    public void errorReload() {
        new Thread(){
            public void run() {
                Message msg = Message.obtain();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
