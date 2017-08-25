package com.dingli.diandians.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.bean.PaiXuFa;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.firstpage.submit.SubminActivity;
import com.dingli.diandians.numbertest.NumberTestActivity;
import com.dingli.diandians.setting.HelpActivity;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.alibaba.fastjson.JSON;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/3/7.
 */
public class FirstPageFragment extends BaseActivity implements
        View.OnClickListener,ListItemFirstPageView.onCancelCollectListener{
    RelativeLayout rlxian;
    ListView xlvFirstPages;
    TextView firstpageriqi,firstpageweek;
    boolean firstIn = true;
    ZhuanYeAdapter adapter;
    FirstPageFragment parent;
    ResultInfo resultInfo;
    TextView dijizhou;
    LinearLayout firstPagenavigation;
    HttpHeaders headers;
    String stritem;
    ImageView firstback;
    VerticalSwipeRefreshLayout refreshLayout;
    DateFormat formatter;
    Date date;
    int indexpage;
    List<Course> listcourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_firstpage);
        parent=this;
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        init();
        xlvFirstPages.setAdapter(adapter);
        xlvFirstPages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    refreshLayout.setEnabled(true);
                }else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
    }
   public void init(){
       listcourse=new ArrayList<>();
       refreshLayout=(VerticalSwipeRefreshLayout)findViewById(R.id.refreshLayout);
       refreshLayout.setRefreshing(true);
       refreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
               getResources().getColor(R.color.holo_green_light),
               getResources().getColor(R.color.holo_orange_light),
               getResources().getColor(R.color.holo_red_light));
      formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       adapter = new ZhuanYeAdapter(this,this);
       firstback=(ImageView)findViewById(R.id.firstback);
       firstback.setOnClickListener(this);
       ImageView kebiaoiv=(ImageView)findViewById(R.id.kebiaoiv);
         kebiaoiv.setOnClickListener(this);
       rlxian=(RelativeLayout)findViewById(R.id.rlxian);
       xlvFirstPages=(ListView)findViewById(R.id.xlvFirstPages);
       refreshLayout.setViewGroup(xlvFirstPages);
       firstpageweek=(TextView)findViewById(R.id.firstpageweek);
       firstpageriqi=(TextView)findViewById(R.id.firstpageriqi);
        dijizhou=(TextView)findViewById(R.id.dijizhou);
       firstPagenavigation=(LinearLayout)findViewById(R.id.firstPagenavigation);
       MaterialSpinner spinner=(MaterialSpinner)findViewById(R.id.spinnere);
       List<String> list=new ArrayList<>();
       date=new Date();
       for (int i=0;i<7;i++){
           long longs=date.getTime()-(i*24*60*60*1000);
           Date date1=new Date(longs);
           String strs1=formatter.format(date1);
           String[] strings=strs1.split(" ");
           list.add(strings[0]);
       }
       spinner.setItems(list);
       spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
               firstIn=true;
               stritem=String.valueOf(item);
               showlistfirst(1,stritem);
           }
       });
   }
   public void showlistfirst(int page,String ttime){
       if (DianTool.isConnectionNetWork(this)) {
           indexpage=0;
           if(listcourse.size()!=0) {
               listcourse.clear();
           }
           if (firstIn) {
               firstIn=false;
           }
           DianTool.huoqutoken();
           HttpParams params = new HttpParams();
           params.put("offset", page);
           params.put("teachTime",ttime);
           params.put("limit",500);
           headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
           OkGo.get(HostAdress.getZheRe("/api/phone/v1/students/courselist/get")).tag(this)
                   .headers(headers).params(params).execute(new StringCallback() {
               @Override
               public void onSuccess(String s, Call call, Response response) {
                   refreshLayout.setRefreshing(false);
                   if (!TextUtils.isEmpty(s)) {
                       if (!s.equals("{}")) {
                           resultInfo = JSON.parseObject(s, ResultInfo.class);
                           if (resultInfo.data[0].weekName != null) {
                               dijizhou.setText("第" + resultInfo.data[0].weekName + "周");
                           }
                           firstpageriqi.setText(resultInfo.data[0].teach_time);
                           if (resultInfo.data[0].dayOfWeek.equals("4")) {
                               firstpageweek.setText("星期四");
                           } else if (resultInfo.data[0].dayOfWeek.equals("1")) {
                               firstpageweek.setText("星期一");
                           } else if (resultInfo.data[0].dayOfWeek.equals("2")) {
                               firstpageweek.setText("星期二");
                           } else if (resultInfo.data[0].dayOfWeek.equals("3")) {
                               firstpageweek.setText("星期三");
                           } else if (resultInfo.data[0].dayOfWeek.equals("5")) {
                               firstpageweek.setText("星期五");
                           } else if (resultInfo.data[0].dayOfWeek.equals("6")) {
                               firstpageweek.setText("星期六");
                           } else if (resultInfo.data[0].dayOfWeek.equals("7")) {
                               firstpageweek.setText("星期日");
                           }
                           if (resultInfo.data[0].courseList == null || resultInfo.data[0].courseList.size() == 0) {
                               rlxian.setVisibility(View.VISIBLE);
                               xlvFirstPages.setVisibility(View.GONE);
                           } else {
                               rlxian.setVisibility(View.GONE);
                               xlvFirstPages.setVisibility(View.VISIBLE);
                               List<Course> lsd = resultInfo.data[0].courseList;
                               for (int i = 0; i < lsd.size(); i++) {
                                   dateCompare(lsd.get(i).teach_time + " " + lsd.get(i).classEndTime, lsd.get(i).teach_time + " " + lsd.get(i).classBeginTime);
                               }
                               for (int i = indexpage - 1; i >= 0; i--) {
                                   listcourse.add(lsd.get(i));
                               }
                               adapter.refreshFirstlist(listcourse);
                               adapter.notifyDataSetChanged();
                           }
                       }
                   }
               }
               @Override
               public void onError(Call call, Response response, Exception e) {
                   refreshLayout.setRefreshing(false);
                   DianTool.response(response,FirstPageFragment.this);
               }
           });
       }else{
           DianTool.showTextToast(this, "请检查网络");
       }
   }
    void dateCompare(String end,String begin){
        Date currenttime=new Date();
        try {
            Date dateend=formatter.parse(end);
            Date datebegin=formatter.parse(begin);
            if (dateend.getTime()-currenttime.getTime()<0){
                indexpage++;
            }else{
                if (datebegin.getTime()-4*60*1000-currenttime.getTime()<0){
                    indexpage++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kebiaoiv:
                Intent intent=new Intent(parent, HelpActivity.class);
                startActivity(intent);
                parent.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.firstback:
                firstback.setClickable(false);
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    @Override
    public void onCancelCollect(String courseName,String rollcallType,String type,String rollcallendtime,String auto,int id,boolean be,String location,int position) {
                       if(type.equals("4")){
                           DianTool.showTextToast(parent,"你已请假,不能签到");
                       }else{
                           if (!TextUtils.isEmpty(rollcallType)) {
                               if (rollcallType.equals("automatic")) {
                                   Intent intent = new Intent(parent, SubminActivity.class);
                                   intent.putExtra(Constant.COURSENAME, courseName);
                                   intent.putExtra(Constant.KE_ID, id);
                                   intent.putExtra(Constant.ROLLCALLTYPE, rollcallType);
                                   startActivity(intent);
                               } else if (rollcallType.equals("digital")) {
                                   Intent intent = new Intent(parent, NumberTestActivity.class);
                                   intent.putExtra("schedu", id);
                                   intent.putExtra("location", location);
                                   intent.putExtra(Constant.COURSENAME, courseName);
                                   startActivity(intent);
                               }
                           }
                       }
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    @Override
    protected void onResume() {
        super.onResume();
        showlistfirst(1, stritem);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showlistfirst(1, stritem);
            }
        });
    }
}