package com.dingli.diandians.bean;

import android.webkit.JavascriptInterface;

/**
 * Created by dingliyuangong on 2017/5/31.
 */

public interface InJavaScriptInterface {
    @JavascriptInterface
    void addEventListener(String eventName, String handler, String args);
    @JavascriptInterface
    void removeEventListener(String name);
}
