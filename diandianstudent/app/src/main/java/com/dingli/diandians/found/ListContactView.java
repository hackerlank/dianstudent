package com.dingli.diandians.found;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Coursecenter;
import com.dingli.diandians.login.CircleImageView;

/**
 * Created by dingliyuangong on 2017/3/17.
 */

public class ListContactView extends LinearLayout{

    ImageView contactxin;
    CircleImageView imgcenter;
    TextView contactren,contactzhi,contactphone;
    private ContactListener contactListener;
    public ListContactView(Context context) {
        super(context);
    }

    public ListContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imgcenter=getView(R.id.imgcenter);
        contactxin=getView(R.id.contactxin);
        contactren=getView(R.id.contactren);
        contactzhi=getView(R.id.contactzhi);
        contactphone=getView(R.id.contactphone);
    }
  public void setContactView(Coursecenter coursecenter){
      contactphone.setText("");
      contactren.setText(coursecenter.name);
      if (!TextUtils.isEmpty(coursecenter.avatar)){
          if (!coursecenter.avatar.equals("null")) {
              Glide.with(getContext()).load(coursecenter.avatar).into(imgcenter);
          }else {
              imgcenter.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.cerg));
          }
      }
      if (!TextUtils.isEmpty(coursecenter.phone)){
          if (!coursecenter.phone.equals("null")) {
              contactphone.setText(coursecenter.phone);
          }else{
              contactphone.setText("");
          }
      }
      if (coursecenter.role.equals("3")){
          contactzhi.setText("辅导员");
      }else{
         contactzhi.setText("学号:"+coursecenter.stuId);
      }
      contactxin.setVisibility(GONE);
      if (!TextUtils.isEmpty(coursecenter.sex)){
          if (coursecenter.sex.equals("female")){
              contactxin.setVisibility(VISIBLE);
              contactxin.setBackgroundResource(R.mipmap.icon_woman);
          }else if (coursecenter.sex.equals("male")){
              contactxin.setVisibility(VISIBLE);
              contactxin.setBackgroundResource(R.mipmap.icon_man);
          }
      }
  }
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
    public void setContactListener(ContactListener listenr){
        this.contactListener=listenr;
    }
    public interface ContactListener{
        void onContactListener(String tv,String phone,String name,String stuId);
    }
}
