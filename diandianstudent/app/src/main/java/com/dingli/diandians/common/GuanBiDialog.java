package com.dingli.diandians.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandians.R;

/**
 * Created by lpf on 2014/9/23 0023.
 */
public class  GuanBiDialog extends Dialog {

    TextView mTvSelectDialogTitle,tvSelectDialogTitleguan,tvneicontent;
    Button mBtnSelectDialogCancel;
    Button mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;
    String title;
    String zuocontent;
    String youcontent;
    public GuanBiDialog(Context context) {
        super(context);
        this.context = context;
    }

    public GuanBiDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public GuanBiDialog(Context context, String title,String content,String zuocontent,String youcontent, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
        this.title=title;
        this.zuocontent=zuocontent;
        this.youcontent=youcontent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_guanbi, null);
        setContentView(container);
        initview(container);
        initListener();
    }
   void initview(View v){
       tvneicontent=(TextView)v.findViewById(R.id.tvneicontent);
       tvSelectDialogTitleguan=(TextView)v.findViewById(R.id.tvSelectDialogTitleguan);
       mBtnSelectDialogCancel=(Button)v.findViewById(R.id.btnSelectDialogCancelguan);
       mBtnSelectDialogDetermine=(Button)v.findViewById(R.id.btnSelectDialogDetermineguan);
       if (TextUtils.isEmpty(content)){
           tvneicontent.setVisibility(View.GONE);
       }else{
           tvneicontent.setVisibility(View.VISIBLE);
           tvneicontent.setText(content);
       }
       if (TextUtils.isEmpty(title)){
           tvSelectDialogTitleguan.setVisibility(View.GONE);
       }else{
           tvSelectDialogTitleguan.setText(title);
           tvSelectDialogTitleguan.setVisibility(View.VISIBLE);
       }
       if (TextUtils.isEmpty(zuocontent)){
           mBtnSelectDialogCancel.setText("取消");
           mBtnSelectDialogCancel.setTextColor(context.getResources().getColor(R.color.xiangqin));
       } else{
           mBtnSelectDialogCancel.setText(zuocontent);
           mBtnSelectDialogCancel.setTextColor(context.getResources().getColor(R.color.qianblue));
       }
       if (TextUtils.isEmpty(youcontent)){
           mBtnSelectDialogDetermine.setText("确定");
       }else{
           mBtnSelectDialogDetermine.setText(youcontent);
       }
   }
    private void initListener() {
        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
        mBtnSelectDialogCancel.setOnClickListener(buttonOnClickListener);
        mBtnSelectDialogDetermine.setOnClickListener(buttonOnClickListener);
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnSelectDialogCancelguan:
                    listener.checkButton(R.id.btnSelectDialogCancelguan);
                    dismiss();
                    break;

                case R.id.btnSelectDialogDetermineguan:
                    listener.checkButton(R.id.btnSelectDialogDetermineguan);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        void checkButton(int id);
    }
}
