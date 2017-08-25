package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.QingJiaSty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class ListItemQanDaoView extends RelativeLayout{
    TextView tvqiandao,tvqiandaocon,tvqiandaodi;
    public ListItemQanDaoView(Context context) {
        super(context);
    }
    public ListItemQanDaoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemQanDaoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvqiandaodi=getView(R.id.tvqiandaodi);
        tvqiandaocon=getView(R.id.tvqiandaocon);
        tvqiandao=getView(R.id.tvqiandao);

    }
    public void setQanDao(Course resultInfo){
        tvqiandao.setText(resultInfo.pushTime);
        tvqiandaocon.setText(resultInfo.content);
        tvqiandaodi.setText(resultInfo.businessContent);

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
