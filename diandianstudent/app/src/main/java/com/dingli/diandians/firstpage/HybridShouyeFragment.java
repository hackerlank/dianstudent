package com.dingli.diandians.firstpage;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.NetUtil;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.dingli.diandians.firstpage.hybrid.JsInterface;
import com.dingli.diandians.view.OnRefreshWebViewListener;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.dingli.diandians.view.YTWebView;

import java.util.Timer;
import java.util.TimerTask;


public class HybridShouyeFragment extends Fragment{
    YTWebView wbv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        wbv.getWebView().loadUrl("javascript:getClientType()");
                        wbv.getWebView().loadUrl("javascript:BundleShortVersion()");
                        break;
                    case 2:
                        if(mFailingUrl !=null){
                            wbv.getWebView().loadUrl(mFailingUrl);
                        }
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    GifView img;
    public HybridShouyeFragment() {
    }
    LinearLayout tvduanwang;
    private String mFailingUrl = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hybrid, container, false);
        tvduanwang = (LinearLayout) v.findViewById(R.id.tvduanwang);
        TextView textnames=(TextView)v.findViewById(R.id.textnames);
        wbv = (YTWebView) v.findViewById(R.id.wbv);
        img=(GifView)v.findViewById(R.id.ivg);
        img.setGifImage(R.mipmap.gifpigeimg);
        img.setGifImageType(GifView.GifImageType.COVER);
        wbv.setOnRefreshWebViewListener(new OnRefreshWebViewListener() {
            @Override
            public void onRefresh() {
                wbv.getWebView().loadUrl(Constant.webshouye);
                tvduanwang.setVisibility(View.GONE);
            }
        });
        if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.ORGAINER))){
            textnames.setText("首页");
        }else{
            textnames.setText(DianApplication.sharedPreferences.getStringValue(Constant.ORGAINER));
        }
        initdata();
        return v;
    }
    void initdata(){
        DianTool.getWebViewSet(wbv.getWebView());
        wbv.getWebView().addJavascriptInterface(new AndroidObjectInJavascripts(getActivity()), "aizhixin");
        wbv.getWebView().addJavascriptInterface(new JsInterface(handler), "retry");
        wbv.getWebView().loadUrl(Constant.webshouye);
        wbv.getWebView().setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 0) {
                    handler.sendEmptyMessage(1);
                }
                if (progress == 100) {
                    img.setVisibility(View.GONE);
                    wbv.setRefreshSuccess();
                }
                if (DianTool.isConnectionNetWork(getActivity())){
                    tvduanwang.setVisibility(View.GONE);
                }else {
                    tvduanwang.setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, progress);
            }
        });
        wbv.getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                if (DianTool.isConnectionNetWork(getActivity())) {
                    //加载出错的自定义界面
                    tvduanwang.setVisibility(View.GONE);
                } else {
                    tvduanwang.setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);
                }
                view.loadUrl("file:///android_asset/error.html");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(Constant.webshouye);
                return true;
            }
        });
    }
}

