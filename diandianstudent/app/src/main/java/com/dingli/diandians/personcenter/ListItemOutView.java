package com.dingli.diandians.personcenter;

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
 * Created by dingliyuangong on 2016/3/15.
 */
public class ListItemOutView extends RelativeLayout {
    TextView classwenzi,classtea,zhengchu,yichu;
    public ListItemOutView(Context context) {
        super(context);
    }
    public ListItemOutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemOutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        classwenzi=getView(R.id.classwenzi);
        classtea=getView(R.id.classtea);
        zhengchu=getView(R.id.zhengchu);
        yichu=getView(R.id.yichu);
    }
  public void setOutList(Course course){
      classwenzi.setText(course.courseName);
      classtea.setText(course.teacherName);
      zhengchu.setText(String.valueOf(course.normal));
      yichu.setText(String.valueOf(course.abnormal));
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
