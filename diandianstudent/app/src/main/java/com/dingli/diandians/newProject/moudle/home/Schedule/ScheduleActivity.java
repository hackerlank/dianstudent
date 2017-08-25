package com.dingli.diandians.newProject.moudle.home.Schedule;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.newProject.base.activity.BaseActivity;
import com.dingli.diandians.newProject.constants.ViewStatus;
import com.dingli.diandians.newProject.moudle.home.Schedule.presenter.SchedulePresenter;
import com.dingli.diandians.newProject.moudle.home.Schedule.presenter.view.IScheduleView;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CourseListProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CourseProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CurrentWeekProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.WeekListProtocol;
import com.dingli.diandians.newProject.utils.AnimationUtil;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.progress.JHProgressDialog;
import com.dingli.diandians.schedule.ScheduleContainerScrollView;
import com.dingli.diandians.schedule.SyFormActivity;
import com.dingli.diandians.syllabus.CourseActivity;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lwq on 2017/7/6.
 * 首页课程表
 */

public class ScheduleActivity extends BaseActivity implements View.OnClickListener, IScheduleView,NewSchedule.OnCourseClickListener {
    @BindView(R.id.linLoading)
    LinearLayout linLoading;
    @BindView(R.id.schedulef)
    NewSchedule schedule;
    @BindView(R.id.tabContainerf)
    LinearLayout tabContainer;
    @BindView(R.id.container_mainf)
    LinearLayout container_mainf;
    @BindView(R.id.tvbenzhou)
    TextView tvbenzhou;
    @BindView(R.id.tvbenzhous)
    TextView tvbenzhous;
    @BindView(R.id.tvbenzhoud)
    TextView tvbenzhoud;
    @BindView(R.id.ivdowa)
    ImageView ivdowa;
    @BindView(R.id.llcurrent)
    LinearLayout llcurrent;
    @BindView(R.id.schedback)
    ImageView schedback;
    @BindView(R.id.ddschedul)
    ImageView ddschedul;
    @BindView(R.id.swipe_containersch)
    VerticalSwipeRefreshLayout swipeLayout;
    @BindView(R.id.llform)
    LinearLayout llform;
    @BindView(R.id.tvmonth)
    TextView tvmonth;
    @BindView(R.id.scroview)
    ScheduleContainerScrollView scroview;
    @BindView(R.id.lletv)
    LinearLayout lletv;
    List<CurrentWeekProtocol> weekList=new ArrayList<>();
    private LoadDataView loadDataView;
    private JHProgressDialog jhProgressDialog;

    private  String[] zhouyi={"周一","周二","周三","周四","周五","周六","周日"};
    private CurrentWeekProtocol resultInfoweek;


    List<String> listdate;
    String mWay;
    String weekName;
    List<Integer> lisnid=new ArrayList<>();
    List<String> listed=new ArrayList<>();
    List<TextView> listtv=new ArrayList<>();
    String name;
    int index;
    int detance;

    private SchedulePresenter schedulePresenter;
    @Override
    protected int layoutId() {
        return R.layout.activity_schedules;
    }

    @Override
    protected ViewGroup loadDataViewLayout() {
        return linLoading;
    }

    @Override
    protected void initView() {//初始化布局
        swipeLayout.setRefreshing(false);
        swipeLayout.setEnabled(false);
        schedule.setOnCourseClickListener(this);
        jhProgressDialog = new JHProgressDialog(this);
        jhProgressDialog.setShowBackground(false);
        ivdowa.setBackgroundResource(R.mipmap.sanjiao);
        final Calendar c = Calendar.getInstance();
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        listdate=DianTool.weekday();
        forearcher(Integer.parseInt(DianTool.getLastTimeInterval(1).get(0).split("-")[0])+"",mWay,tvmonth);
    }

    @Override
    protected void initData() {//获取数据
        loadDataView.changeStatusView(ViewStatus.START);
        schedulePresenter.getCurrentWeek();
        schedulePresenter.getWeekList();
    }

    @Override
    protected void getLoadView(LoadDataView loadView) {
        this.loadDataView=loadView;
        loadDataView.setErrorListner(view -> {
            loadDataView.changeStatusView(ViewStatus.START);
            schedulePresenter.getCurrentWeek();
            schedulePresenter.getWeekList();
        });
    }

    @Override
    protected void initPresenter() {
        if(null==schedulePresenter){
            schedulePresenter=new SchedulePresenter(this);
        }
    }

    @OnClick({R.id.linWeekSelect,R.id.tvqinjia,R.id.schedback})
    @Override
    public void onClick(View view) {//点击事件
        switch (view.getId()){
            case R.id.linWeekSelect:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroview.scrollTo(index*detance,0);
                    }
                },50);
                if (llform.getVisibility()==View.VISIBLE){
                    ivdowa.setBackgroundResource(R.mipmap.sanjiao);
                    llform.setVisibility(View.GONE);
                    llform.setAnimation(AnimationUtil.moveToViewBottom());
                    tvbenzhou.setVisibility(View.VISIBLE);
                    llcurrent.setVisibility(View.GONE);
                    tvbenzhou.setText("第" + resultInfoweek.name+ "周");
                    name="";
                    listdate=DianTool.weekday();
                    forearcher(Integer.parseInt(DianTool.getLastTimeInterval(1).get(0).split("-")[0])+"",mWay,tvmonth);
                    schedulePresenter.getCourseweek(resultInfoweek.id);
                    initwed(weekList);
                }else{
                    llform.setVisibility(View.VISIBLE);
                    llform.setAnimation(AnimationUtil.moveToViewLocation());
                    ivdowa.setBackgroundResource(R.mipmap.icon_uparrows);
                }
                break;
            case R.id.tvqinjia:
                Intent intent=new Intent(this, SyFormActivity.class);
                startActivity(intent);
                break;
            case R.id.schedback:
                this.finish();
                break;
        }
    }


    @Override
    public void getCourseListSuccess(List<CourseListProtocol> courseListProtocols) {
        loadDataView.changeStatusView(ViewStatus.SUCCESS);
        jhProgressDialog.dismiss();
        schedule.fillSchedule(courseListProtocols);
    }

    @Override
    public void getCourseListFailure(String message) {
        loadDataView.changeStatusView(ViewStatus.FAILURE);
        jhProgressDialog.dismiss();
        ToastUtils.showShort(this,message);
    }

    @Override
    public void getCurrentWeekSuccess(CurrentWeekProtocol currentWeekProtocol) {//当前周获取成功
        resultInfoweek=currentWeekProtocol;
        tvbenzhou.setText("第"+resultInfoweek.name+"周");
        weekName=resultInfoweek.name;
    }

    @Override
    public void getCurrentWeekFailure(String message) {
        loadDataView.changeStatusView(ViewStatus.FAILURE);
        ToastUtils.showShort(this,message);
    }

    @Override
    public void getWeekListSuccess(WeekListProtocol weekListProtocol) {
      //  loadDataView.changeStatusView(ViewStatus.SUCCESS);
        initwed(weekListProtocol.weekList);
        weekList.addAll(weekListProtocol.weekList);
    }

    @Override
    public void getWeekListFailure(String message) {
        loadDataView.changeStatusView(ViewStatus.FAILURE);
        ToastUtils.showShort(this,message);
    }

    @Override
    public void onLoginInvalid(String message) {
        loadDataView.changeStatusView(ViewStatus.SUCCESS);
        jhProgressDialog.dismiss();
    }

    @Override
    public void onNetworkFailure(String message) {
        loadDataView.changeStatusView(ViewStatus.NOTNETWORK);
        jhProgressDialog.dismiss();
    }

    void initwed(final List<CurrentWeekProtocol> weeklist){//顶部学期周
        lletv.removeAllViews();
        if (listed.size()!=0){
            listed.clear();
            lisnid.clear();
            listtv.clear();
        }
        for (int i=0;i<weeklist.size();i++){
            lisnid.add(weeklist.get(i).id);
            listed.add(weeklist.get(i).name);
            final TextView textView = new TextView(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(schedule.getColumnWidth()+3, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setText(weeklist.get(i).name);
            textView.setPadding(0,5,0,5);
            if (!TextUtils.isEmpty(weekName)) {
                if (weekName.equals(weeklist.get(i).name)) {
                    index = i;
                    textView.setTextColor(getResources().getColor(R.color.sytv));
                    textView.setTextSize(20);
                    textView.setBackgroundResource(R.drawable.syform_corner);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.sy));
                    textView.setBackgroundResource(R.color.shoutous);
                    textView.setTextSize(15);
                }
            }
            if (!TextUtils.isEmpty(name)&&!name.equals(resultInfoweek.name)){
                if (textView.getText().toString().trim().equals(name)){
                    textView.setTextColor(getResources().getColor(R.color.sys));
                }else{
                    textView.setTextColor(getResources().getColor(R.color.sy));
                }
            }else{
                textView.setTextColor(getResources().getColor(R.color.sy));
            }
            listtv.add(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name=textView.getText().toString().trim();
                    int ids=listed.indexOf(name);
                    if (!name.equals(resultInfoweek.name)) {
                        for (int i=0;i<listtv.size();i++) {
                            if (listtv.get(i).getText().toString().trim().equals(name)) {
                                listtv.get(i).setTextColor(getResources().getColor(R.color.sys));
                            }else{
                                listtv.get(i).setTextColor(getResources().getColor(R.color.sy));
                            }
                        }
                        tvbenzhou.setVisibility(View.GONE);
                        llcurrent.setVisibility(View.VISIBLE);
                        tvbenzhous.setText("第" +name + "周");
                        tvbenzhoud.setText("返回本周");
                    }else{
                        llcurrent.setVisibility(View.GONE);
                        tvbenzhou.setVisibility(View.VISIBLE);
                        tvbenzhou.setText("第" + name + "周");
                    }
                    if (listdate.size()!=0){
                        listdate.clear();
                    }
                    if (!TextUtils.isEmpty(name)) {
                        if (!TextUtils.isEmpty(resultInfoweek.name)) {
                            int nameone=Integer.parseInt(name);
                            int nametwo=Integer.parseInt(resultInfoweek.name);
                            if (nameone > nametwo) {
                                List<String> listred= DianTool.getNextTimeInterval(nameone-nametwo);
                                for (int i=0;i<listred.size();i++){
                                    listdate.add(listred.get(i).split("-")[1]);
                                }
                                forearcher(DianTool.getNextTimeInterval(nameone-nametwo).get(0).split("-")[0],mWay,tvmonth);
                            } else {
                                List<String> listred=DianTool.getLastTimeInterval(nametwo-nameone);
                                for (int i=0;i<listred.size();i++){
                                    listdate.add(listred.get(i).split("-")[1]);
                                }
                                forearcher(listred.get(0).split("-")[0],mWay,tvmonth);
                            }
                        }
                    }
                    jhProgressDialog.show();
                    schedulePresenter.getCourseweek(lisnid.get(ids));
                }
            });
            lletv.addView(textView);
        }
    }
    private void forearcher(String mMonth,String mWay,TextView tvmonth){//初始化本周日期tab
        if (!TextUtils.isEmpty(mMonth)){
            tvmonth.setText(mMonth+"\n"+"月");
        }
        tabContainer.removeAllViews();
        for (int i = 0; i < zhouyi.length; i++) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(schedule.getColumnWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setText(listdate.get(i)+"\n"+zhouyi[i]);
            textView.setTextColor(getResources().getColor(R.color.kb_week_color));
            detance=schedule.getColumnWidth();
            textView.setBackgroundColor(getResources().getColor(R.color.white));
            if("1".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(6)+"\n"+"周日"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==6) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                    // textView.setBackgroundResource(R.mipmap.tvbad);
                }
            }else if("4".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(2)+"\n"+"周三"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==2) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                }
            }else  if("3".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(1)+"\n"+"周二"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==1) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                }
            }else if("2".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(0)+"\n"+"周一"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==0) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                }
            }else if("5".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(3)+"\n"+"周四"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==3) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                }
            }else if("6".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(4)+"\n"+"周五"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==4) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                }
            }else  if("7".equals(mWay)){
                if(textView.getText().toString().equals(listdate.get(5)+"\n"+"周六"))
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                if(i==5) {
                    textView.setBackgroundColor(getResources().getColor(R.color.kb_week_back_color));
                }
            }
            tabContainer.addView(textView);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {//更换皮肤
        super.onResume();
        String str= DianApplication.sharedPreferences.getStringValue("defalt");
        if (!TextUtils.isEmpty(str)){
            switch (str){
                case "1":
                    Bitmap bp1= BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morenone);
                    container_mainf.setBackground(new BitmapDrawable(bp1));
                    break;
                case "2":
                    Bitmap bp2=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morentwo);
                    container_mainf.setBackground(new BitmapDrawable(bp2));
                    break;
                case "3":
                    Bitmap bp3=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morenthree);
                    container_mainf.setBackground(new BitmapDrawable(bp3));
                    break;
                case "4":
                    Bitmap bp4=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_morenfour);
                    container_mainf.setBackground(new BitmapDrawable(bp4));
                    break;
                case "5":
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    Bitmap bm = BitmapFactory.decodeFile(DianApplication.sharedPreferences.getStringValue("fileone"),options);
                    container_mainf.setBackground(new BitmapDrawable(bm));
                    break;
                case "6":
                    Bitmap bp6=BitmapFactory.decodeResource(getResources(),R.mipmap.backgroud_kb);
                    container_mainf.setBackground(new BitmapDrawable(bp6));
                    break;
                default:
                    break;
            }
        }else{
            container_mainf.setBackgroundResource(R.mipmap.backgroud_kb);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        schedulePresenter.destroy();
    }

    @Override
    public void onCourseClick(CourseProtocol model) {
        Intent intent=new Intent(ScheduleActivity.this, CourseActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(Constant.COURSE,model);
        intent.putExtra(Constant.KE_ID,model.id);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
