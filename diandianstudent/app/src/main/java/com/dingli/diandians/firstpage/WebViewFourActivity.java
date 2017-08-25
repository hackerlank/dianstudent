package com.dingli.diandians.firstpage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebViewFourActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    private ImageView remenback;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewfour);
        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBarfour);
        initView();
        webView.addJavascriptInterface(new AndroidObjectInJavascripts(this), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewfour);
        remenback=(ImageView) findViewById(R.id.remenbackfour);
        TextView tvtoutiao=(TextView)findViewById(R.id.tvtoutiaofour);
        remenback.setOnClickListener(this);
        String url=getIntent().getStringExtra("targetUrl");
        String targetTitle=getIntent().getStringExtra("targetTitle");
        LinearLayout webllfour=(LinearLayout) findViewById(R.id.webllfour);
        if (!TextUtils.isEmpty(targetTitle)){
            webllfour.setVisibility(View.VISIBLE);
            tvtoutiao.setText(targetTitle);
        }else{
            webllfour.setVisibility(View.GONE);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackfour:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
        }
    }
}


