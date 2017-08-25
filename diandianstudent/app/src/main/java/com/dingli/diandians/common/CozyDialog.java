package com.dingli.diandians.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandians.R;

/**
 * Created by lpf on 2014/9/23 0023.
 */
public class CozyDialog extends Dialog {

    Button btcheat;
    SelectDialogButtonListener listener;
    Context context;
    public CozyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CozyDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public CozyDialog(Context context, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_cozy, null);
        setContentView(container);
        initview(container);
        initListener();
    }
   void initview(View v){
       btcheat=(Button)v.findViewById(R.id.btcheat);
       TextView tvcheat=(TextView) v.findViewById(R.id.tvcheat);
   }
    private void initListener() {
        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
        btcheat.setOnClickListener(buttonOnClickListener);
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btcheat:
                    listener.checkButton(R.id.btcheat);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        void checkButton(int id);
    }
}
