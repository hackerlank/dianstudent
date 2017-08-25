package com.dingli.diandians.yichangnv;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.syllabus.CourseActivity;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/3/14.
 */

public class RecordDetailActivity extends BaseActivity implements View.OnClickListener,ViewHolder.onCancelCollectListener,XListView.IXListViewListener {

    VerticalSwipeRefreshLayout refrecord;
    TextView recordshaixuan,recordshaixuans,recquanbu,recchidao,reczaotui,reckuangke,recqingjia;
    int typeId;
    String courseName;
    ListFirstRecordAdapter adapter;
    int currentpage=1;
    XListView lvrecord;
    PopupWindow pop;
    LinearLayout lldeta,llwulu;
    int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorddetail);
        initview();
        popuwindow();
        refrecord.setRefreshing(true);
        showlistdetail(1,Constant.REFRESH,typeId);
    }

    void initview(){
        typeId=getIntent().getIntExtra(Constant.TYPEID,0);
        courseName=getIntent().getStringExtra(Constant.COURSENAME);
        refrecord=(VerticalSwipeRefreshLayout)findViewById(R.id.refrecord);
        TextView tvderecord=(TextView) findViewById(R.id.tvderecord);
        if (TextUtils.isEmpty(courseName)){
            tvderecord.setText(R.string.outyichtitles);
        }else{
            tvderecord.setText("课程考勤记录");
        }
        refrecord.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        lvrecord=(XListView) findViewById(R.id.lvrecord);
        adapter=new ListFirstRecordAdapter(this,this);
        lvrecord.setAdapter(adapter);
        lvrecord.setPullRefreshEnable(true);
        lvrecord.setXListViewListener(this);
        lvrecord.setPullLoadEnable(true);
        ImageView recordback=(ImageView) findViewById(R.id.recordback);
        recordshaixuan=(TextView) findViewById(R.id.recordshaixuan);
        recordshaixuans=(TextView) findViewById(R.id.recordshaixuans);
        llwulu=(LinearLayout) findViewById(R.id.llwulu);
        lldeta=(LinearLayout) findViewById(R.id.lldeta);
        recordback.setOnClickListener(this);
        recordshaixuan.setOnClickListener(this);
        recordshaixuans.setOnClickListener(this);
    }
    void popuwindow(){
        View view= LayoutInflater.from(this).inflate(R.layout.popuwindow_record,null);
       recquanbu=(TextView) view.findViewById(R.id.recquanbu);
        recchidao=(TextView) view.findViewById(R.id.recchidao);
        reczaotui=(TextView) view.findViewById(R.id.reczaotui);
        reckuangke=(TextView) view.findViewById(R.id.reckuangke);
        recqingjia=(TextView) view.findViewById(R.id.recqingjia);
        View viewdetail=view.findViewById(R.id.viewdetail);
        viewdetail.setOnClickListener(this);
        recquanbu.setOnClickListener(this);
        recchidao.setOnClickListener(this);
        reczaotui.setOnClickListener(this);
        reckuangke.setOnClickListener(this);
        recqingjia.setOnClickListener(this);
        pop=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        view.getBackground().setAlpha(110);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        pop.update();
        pop.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pop.dismiss();
                    return true;
                }
                recordshaixuans.setVisibility(View.GONE);
                recordshaixuan.setVisibility(View.VISIBLE);
                return false;
            }
        });
            switch (typeId){
                case 0:
                    xianshi(1);
                    recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                    recquanbu.setBackgroundResource(R.mipmap.icon_z);
                    recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                    reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                    reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                    break;
                case 1:
                    xianshi(1);
                    recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                    recquanbu.setBackgroundResource(R.mipmap.icon_z);
                    recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                    reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                    reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                    break;
                case 2:
                    xianshi(2);
                    recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                    recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                    recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                    reckuangke.setBackgroundResource(R.mipmap.icon_z);
                    reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                    break;
                case 3:
                    xianshi(3);
                    recchidao.setBackgroundResource(R.mipmap.icon_z);
                    recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                    recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                    reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                    reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                    break;
                case 4:
                    xianshi(4);
                    recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                    recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                    recqingjia.setBackgroundResource(R.mipmap.icon_z);
                    reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                    reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                    break;
                case 5:
                    xianshi(5);
                    recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                    recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                    recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                    reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                    reczaotui.setBackgroundResource(R.mipmap.icon_z);
                    break;
            }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.viewdetail:
                pop.dismiss();
                xianshi(0);
                break;
            case R.id.recchidao:
                xianshi(3);
                recchidao.setBackgroundResource(R.mipmap.icon_z);
                recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                type=3;
                pop.dismiss();
                showlistdetail(1, Constant.REFRESH, 3);
                break;
            case R.id.recquanbu:
                xianshi(1);
                recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                recquanbu.setBackgroundResource(R.mipmap.icon_z);
                recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                type=1;
                pop.dismiss();
                showlistdetail(1, Constant.REFRESH, 1);
                break;
            case R.id.recqingjia:
                xianshi(4);
                recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                recqingjia.setBackgroundResource(R.mipmap.icon_z);
                reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                type=4;
                pop.dismiss();
                showlistdetail(1, Constant.REFRESH, 4);
                break;
            case R.id.reczaotui:
                xianshi(5);
                recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                reckuangke.setBackgroundResource(R.mipmap.icon_weiz);
                reczaotui.setBackgroundResource(R.mipmap.icon_z);
                type=5;
                pop.dismiss();
                showlistdetail(1, Constant.REFRESH, 5);
                break;
            case R.id.reckuangke:
                xianshi(2);
                recchidao.setBackgroundResource(R.mipmap.icon_weiz);
                recquanbu.setBackgroundResource(R.mipmap.icon_weiz);
                recqingjia.setBackgroundResource(R.mipmap.icon_weiz);
                reckuangke.setBackgroundResource(R.mipmap.icon_z);
                reczaotui.setBackgroundResource(R.mipmap.icon_weiz);
                type=2;
                pop.dismiss();
                showlistdetail(1, Constant.REFRESH, 2);
                break;
            case R.id.recordback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.recordshaixuan:
                pop.showAsDropDown(lldeta);
                recordshaixuans.setVisibility(View.VISIBLE);
                recordshaixuan.setVisibility(View.GONE);
                break;
        }
    }
    void showlistdetail(int page, final int style, int typeIds){
       if (DianTool.isConnectionNetWork(this)){
           HttpHeaders headers=new HttpHeaders();
           DianTool.huoqutoken();
           headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
           HttpParams params=new HttpParams();
           params.put("semesterId",DianApplication.user.semesterId);
           currentpage = page;
           if (!TextUtils.isEmpty(courseName)){
               if (type==0) {
                       params.put("courseName", courseName);
                       params.put("offset", page);
               }else if (type==1){
                   params.put("courseName", courseName);
                   params.put("offset", page);
               }else{
                   params.put("typeId", type);
                   params.put("offset", page);
               }
           }
           if (typeId!=0){
               if (type==0) {
                       params.put("typeId", typeId);
                       params.put("offset", page);
               }else if (type==1){
                   params.put("typeId", typeId);
                   params.put("offset", page);
               }else{
                   params.put("typeId", type);
                   params.put("offset", page);
               }
           }else{
               if (type==0) {
                   params.put("offset", page);
               }else if (type==1){
//                   params.put("typeId", typeId);
                   params.put("offset", page);
               }else{
                   params.put("typeId", type);
                   params.put("offset", page);
               }
           }
           OkGo.get(HostAdress.getRequest("/api/phone/v1/student/Schedule/get")).params(params).headers(headers)
                   .execute(new StringCallback() {
                       @Override
                       public void onSuccess(String s, Call call, Response response) {
                           llwulu.setVisibility(View.GONE);
                           refrecord.setRefreshing(false);
                           refrecord.setEnabled(false);
                           lvrecord.stopRefresh();
                           lvrecord.stopLoadMore();
                           if (!TextUtils.isEmpty(s)) {
                               if (!s.equals("{}")) {
                                   Result resultInfo = JSON.parseObject(s, Result.class);
                                   if (resultInfo.data.size() != 0) {
                                       switch (style) {
                                           case Constant.REFRESH:
                                               adapter.refreshRecord(resultInfo.data);
                                               adapter.notifyDataSetChanged();
                                               break;
                                           case Constant.LOAD_MORE:
                                               adapter.addRecord(resultInfo.data);
                                               adapter.notifyDataSetChanged();
                                               break;
                                       }
                                   } else {
                                       if (currentpage == 1) {
                                           adapter.refreshRecord(resultInfo.data);
                                           adapter.notifyDataSetChanged();
                                           llwulu.setVisibility(View.VISIBLE);
                                       }
                                   }
                               }
                           }
                       }
                       @Override
                       public void onError(Call call, Response response, Exception e) {
                           DianTool.response(response,RecordDetailActivity.this);
                       }
                   });
       }else{
           DianTool.showTextToast(this,"请检查网络");
       }
    }
    @Override
    public void onRefresh() {
        showlistdetail(1,Constant.REFRESH,typeId);
    }

    @Override
    public void onLoadMore() {
         showlistdetail(++currentpage,Constant.LOAD_MORE,typeId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    void xianshi(int types){
        recordshaixuans.setVisibility(View.GONE);
        recordshaixuan.setVisibility(View.VISIBLE);
        switch (types){
            case 1:
                recordshaixuans.setText("全部");
                recordshaixuan.setText("全部");
                break;
            case 2:
                recordshaixuans.setText("旷课");
                recordshaixuan.setText("旷课");
                break;
            case 3:
                recordshaixuans.setText("迟到");
                recordshaixuan.setText("迟到");
                break;
            case 4:
                recordshaixuans.setText("请假");
                recordshaixuan.setText("请假");
                break;
            case 5:
                recordshaixuans.setText("早退");
                recordshaixuan.setText("早退");
                break;
        }
    }
    @Override
    public void onRecordListener(String scheduleId) {
        int courid=Integer.parseInt(scheduleId);
        Intent intent=new Intent(this, CourseActivity.class);
        intent.putExtra(Constant.KE_ID,courid);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
}
