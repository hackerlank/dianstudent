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
public class TongyiDialog extends Dialog {

    TextView tvshetime,tvdijie,shili,contentqing;
    TextView mBtnSelectDialogCancel;
    TextView mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;
    String shijian;
    String jieshu;
    String shilis;
    public TongyiDialog(Context context) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public TongyiDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public TongyiDialog(Context context, String shijian,String jieshu,String shili,String content, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
        this.shijian=shijian;
        this.jieshu=jieshu;
        this.shilis=shili;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_tongyi, null);
        setContentView(container);
        initview(container);
        initListener();
        tvshetime.setText(shijian);
        if(shilis.equals("true")){
            shili.setText("是");
        }else{
            shili.setText("否");
        }
        if(jieshu.equals("")){
            tvdijie.setVisibility(View.GONE);
        }else{
            tvdijie.setVisibility(View.VISIBLE);
            tvdijie.setText(jieshu);
        }
        contentqing.setText(content);
//        mTvSelectDialogTitle.setText(content);
    }
   void initview(View v){
       mBtnSelectDialogCancel=(TextView)v.findViewById(R.id.btnTongyiDialogCancel);
       mBtnSelectDialogDetermine=(TextView)v.findViewById(R.id.btnTongyiDialogDetermine);
       tvshetime=(TextView)v.findViewById(R.id.tvshetime);
       tvdijie=(TextView)v.findViewById(R.id.tvdijie);
       shili=(TextView)v.findViewById(R.id.shili);
       contentqing=(TextView)v.findViewById(R.id.contentqing);
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
                case R.id.btnTongyiDialogCancel:
                    listener.checkButton(R.id.btnTongyiDialogCancel);
                    dismiss();
                    break;

                case R.id.btnTongyiDialogDetermine:
                    listener.checkButton(R.id.btnTongyiDialogDetermine);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        public void checkButton(int id);
    }
}
