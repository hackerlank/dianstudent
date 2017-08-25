package com.dingli.diandians.information.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.QingJiaSty;


/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class ListItemInSurveyView extends RelativeLayout{
    TextView tvtiwan,couretvzi,tvkeclasse,tvstumings,tvfapoples,tvendriqis;
    public ListItemInSurveyView(Context context) {
        super(context);
    }
    public ListItemInSurveyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListItemInSurveyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvtiwan=getView(R.id.tvtiin);
        couretvzi=getView(R.id.couretvziin);
        tvkeclasse=getView(R.id.tvkeclassin);
        tvstumings=getView(R.id.tvstumingin);
        tvfapoples=getView(R.id.tvfapoplein);
        tvendriqis=getView(R.id.tvendriqiin);
    }
    public void setInSuvey(QingJiaSty resultInfo){
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
