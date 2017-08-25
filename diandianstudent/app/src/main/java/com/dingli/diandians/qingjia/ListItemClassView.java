package com.dingli.diandians.qingjia;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Course;

/**
 * Created by dingliyuangong on 2016/8/2.
 */
public class ListItemClassView extends RelativeLayout{
   TextView classnamed;
   ImageView selected;
    private onCancelCollectListener mOnCancelInterface;
    public ListItemClassView(Context context) {
        super(context);
    }

    public ListItemClassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemClassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        classnamed=getView(R.id.classnamed);
        selected=getView(R.id.selected);
    }
    public void setNumClass(Course course){
        classnamed.setText(course.name);
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
    public void setmOnCancelInterface(onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
    public interface onCancelCollectListener {
        void onCancelCollects(int position,String name,int id);
//        void checkButton(int id);
//        void onCancelCollect(View v,int  id,String type);
    }
}
