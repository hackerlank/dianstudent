package com.dingli.diandians.information.instructor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.ResultInfoCall;

/**
 * Created by dingliyuangong on 2017/4/13.
 */

public class ListNoteView extends RelativeLayout{
    RelativeLayout rlwdone;
    TextView tvti,opentv,tvrq,vtlix,signtai;
    ImageView ivsigntu;
    Button vtjujuew;
    public ListNoteView(Context context) {
        super(context);
    }
    public ListNoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListNoteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlwdone=getView(R.id.rlwdone);
        opentv=getView(R.id.opentv);
        tvrq=getView(R.id.tvrq);
        vtlix=getView(R.id.vtlix);
        signtai=getView(R.id.signtai);
        ivsigntu=getView(R.id.ivsigntu);
        vtjujuew=getView(R.id.vtjujuew);
    }
    public void setNote(ResultInfoCall resultInfoCall){
        opentv.setText(resultInfoCall.openTime);
        tvrq.setText(resultInfoCall.signTime);
        if (resultInfoCall.haveReport==true){
            ivsigntu.setBackgroundResource(R.mipmap.icon_signok);
            rlwdone.setBackgroundResource(R.color.lixiaos);
        }else{
            ivsigntu.setBackgroundResource(R.mipmap.icon_signno);
            rlwdone.setBackgroundResource(R.color.bg_White);
        }
        if (resultInfoCall.status==true){
            signtai.setText("进行中");
            signtai.setTextColor(getResources().getColor(R.color.qianblue));
            vtjujuew.setVisibility(VISIBLE);
            rlwdone.setBackgroundResource(R.color.lixiaos);
        }else{
            vtjujuew.setVisibility(GONE);
            signtai.setTextColor(getResources().getColor(R.color.notetv));
            signtai.setText("已关闭");
        }
        vtlix.setText(resultInfoCall.gpsDetail);
    }
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(ex.toString(), "Could not cast View to concrete class");
            throw ex;
        }
    }
}
