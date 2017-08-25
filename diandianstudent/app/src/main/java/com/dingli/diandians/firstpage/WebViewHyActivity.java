package com.dingli.diandians.firstpage;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.dingli.diandians.firstpage.hybrid.JsInterface;
import com.dingli.diandians.lostproperty.InJavaScript;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.utils.DisplayUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.simple.eventbus.EventBus;

import static android.view.KeyEvent.KEYCODE_BACK;


public class WebViewHyActivity extends Activity implements InJavaScript.UpPhone, View.OnClickListener{

    private X5WebView webView;
    private String url="";
    private ImageView remenback;
    private TextView tvtoutiao;
    private String toutiao;
    private  String id;
    private  String  title;
    private String weburl;
    private int type;
    private LinearLayout webone,lineaone;
    private View viewaone;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl("javascript:accessTokenAddTokenType()");
                        webView.loadUrl("javascript:getClientType()");
                        webView.loadUrl("javascript:BundleShortVersion()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_hy);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBarone);
        initView();
        getData();
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(new InJavaScript(WebViewHyActivity.this, this,webView), "aizhixin");
        fillWebView();
        webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                if (i == 0) {
                    handler.sendEmptyMessage(1);
                }
                if (i == 100) {
                    bar.setVisibility(View.GONE);
                    webone.setVisibility(View.INVISIBLE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                weburl=s;
                if (s!=null){
                    webView.loadUrl(s);
                }
                return true;
            }
        });
    }
    private void fillWebView() {
        webView.loadUrl(Constant.JYhy+url);
    }
    private void getData() {
        url=getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
        type=getIntent().getIntExtra("type",0);
//        if(!TextUtils.isEmpty(title)){
//            tvtoutiao.setText(title);
//        }
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewone);
        remenback=(ImageView) findViewById(R.id.remenbackone);
        tvtoutiao=(TextView)findViewById(R.id.tvtoutiaoone);
        remenback.setOnClickListener(this);
        webone=(LinearLayout) findViewById(R.id.webone);
        lineaone=(LinearLayout) findViewById(R.id.lineaone);
        viewaone= findViewById(R.id.viewaone);
        ViewGroup.LayoutParams layoutParams = lineaone.getLayoutParams();
        layoutParams.width    =getWindowManager().getDefaultDisplay().getWidth();
        layoutParams.height =DisplayUtil.dip2px(this,43,48);
        Log.d("dasd",layoutParams.height+"");
        lineaone.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams1 = viewaone.getLayoutParams();
        layoutParams1.height =DisplayUtil.dip2px(this,19,22);
        viewaone.setLayoutParams(layoutParams1);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post("", BKConstant.EventBus.JOBREFSH);
        webView.setVisibility(View.GONE);
        webView.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.loadUrl("about:blank");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackone:
                if (!TextUtils.isEmpty(weburl)){
                    webView.goBack();
                    weburl="";
                }else {
                    this.finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void upphone(String name, String str) {


    }
}


