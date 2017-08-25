package com.dingli.diandians.firstpage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.dingli.diandians.login.LoginActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.KeyEvent.KEYCODE_BACK;


public class WebViewTwoActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    LinearLayout weblltwo;
    int izd;
    JCVideoPlayerStandard webvieod;
    JCVideoPlayerStandard webVieo;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    webView.loadUrl("javascript:callWebAppGoBack()");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewtwo);
        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBartwo);
        initView();
        webView.addJavascriptInterface(new JCCallBack(), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress==100) {
                    bar.setVisibility(View.GONE);
                    weblltwo.setVisibility(View.GONE);
                }else{
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
        registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
        registerReceiver(myReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewtwo);
        ImageView remenback=(ImageView) findViewById(R.id.remenbacktwo);
        TextView tvtoutiao=(TextView)findViewById(R.id.tvtoutiaotwo);
        weblltwo=(LinearLayout) findViewById(R.id.weblltwo);
        weblltwo.setVisibility(View.VISIBLE);
        remenback.setOnClickListener(this);
        String url=getIntent().getStringExtra("targetUrl");
        String targetTitle=getIntent().getStringExtra("targetTitle");
        if (!TextUtils.isEmpty(targetTitle)){
            tvtoutiao.setText(targetTitle);
        }
        webView.loadUrl(url);
        setWebViewNEW();
    }
    public void setWebViewNEW() {
          webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
          webView.setWebViewClient(new WebViewClient(){
              @Override
              public boolean shouldOverrideUrlLoading(WebView view, String url) {
                  view.loadUrl(url);
                  return true;
              }
          });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        unregisterReceiver(myReceiver);
    }
    public class JCCallBack {
        @JavascriptInterface
        public String accessTokenAddTokenType(){
            DianTool.huoqutoken();
            return DianApplication.user.token;
        }
        @JavascriptInterface
        public String  getNetwork(){
          if (DianTool.isConnectionNetWork(WebViewTwoActivity.this)){
              return "online";
          }else{
              return "offline";
          }
        }
        @JavascriptInterface
        public String getUserInfo(){
            return DianApplication.sharedPreferences.getStringValue(Constant.INFO);
        }
        @JavascriptInterface
        public void backNative(){
            WebViewTwoActivity.this.finish();
            overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
        }
        @JavascriptInterface
        public void errorHandle(){
            Intent intent=new Intent(WebViewTwoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
        }
        @JavascriptInterface
        public void errorHandle(String str){
            Intent intent=new Intent(WebViewTwoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
        }
        @JavascriptInterface
        public void androidPlayVideo(final String url,final String name) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DianTool.isConnectionNetWork(WebViewTwoActivity.this)) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        if (webVieo != null) {
                            webView.removeView(webVieo);
                        }
                        izd = 1;
                        webVieo = new JCVideoPlayerStandard(WebViewTwoActivity.this);
                        webvieod = webVieo;
                        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                        webVieo.setUp(url,
                                JCVideoPlayer.SCREEN_LAYOUT_NORMAL, name, webVieo);
                        webView.addView(webVieo, layoutParams);
                        webVieo.startVideo();
                    }else{
                        DianTool.showTextToast(WebViewTwoActivity.this,"请检查网络");
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbacktwo:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode) {
            if (webVieo!=null){
                if (webVieo.currentState!=webVieo.CURRENT_STATE_ERROR){
                    JCMediaManager.instance().mediaPlayer.pause();
                }
                webVieo.setVisibility(View.GONE);
                webVieo.clearFloatScreen();
                webVieo=null;
            }else {
             handler.sendEmptyMessage(1);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()) ) {//当按下电源键，屏幕亮起的时候
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                if (webVieo!=null) {
                    webVieo.startVideo();
                }
            }
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) ) {/// /当按下电源键，屏幕变黑的时候
                if (webVieo!=null) {
                    JCMediaManager.instance().mediaPlayer.pause();
                }
            }
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()) ) {//当解除锁屏的时候
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                if (webVieo!=null) {
                    webVieo.startVideo();
                }
            }
            String SYSTEM_REASON = "reason";
            String SYSTEM_HOME_KEY = "homekey";
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    if (webVieo!=null) {
                        JCMediaManager.instance().mediaPlayer.pause();
                    }
                }
            }
            }
    };
}


