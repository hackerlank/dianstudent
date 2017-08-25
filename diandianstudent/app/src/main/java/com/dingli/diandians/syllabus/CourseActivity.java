package com.dingli.diandians.syllabus;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingli.diandians.R;
import com.dingli.diandians.bean.TableData;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.MyDialog;
import com.dingli.diandians.common.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.newProject.widget.progress.JHProgressDialog;
import com.dingli.diandians.newProject.widget.statedialog.StateDialog;
import com.dingli.diandians.newProject.widget.statedialog.StateView;
import com.dingli.diandians.view.RatingBarView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;


import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class CourseActivity extends BaseActivity implements View.OnClickListener{

    ImageView xianone,xiantwo,xianthree,xianfour,xianfive,ivgaizhangs;
    TextView latertime,tvpands,woveiew,tvtaiming,syllabustv,classroomtv,xianwen,starnub,classroomdatecourse,classroomweekcourse,classroomzhoucourse,classroomjiecourse,classroomtimecourse,classroomjiaocourse;
    int ide;
    Course course;
    EditText etfankui;
    int i=0;
    int e=0;
    int d=0;
    Button bttijiao;
    LinearLayout lifping,ids;
    String wenzi;
    ImageView igzhuangtai,daosanjiao;
    String str;
    ResultInfo resultInfotoken;
    int dao_type;
    SimpleDateFormat    formatter;
    HttpHeaders headers;
    RatingBarView ratingBarView;
    boolean firstIn=true;
    TextView signs,signsds;
    RelativeLayout rlout,rlbttijiao;
    View viewon;
    private TableData.CourseListEntity courseListEntity;
    private JHProgressDialog jhProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        ide= getIntent().getIntExtra(Constant.KE_ID,0);
        courseListEntity= (TableData.CourseListEntity) getIntent().getSerializableExtra(Constant.COURSE);
        init();
        update();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(null==courseListEntity){
            initdata();
            return;
        }
        pinjiao(courseListEntity.id);//获取评教信息
    }
    public void init(){
        jhProgressDialog = new JHProgressDialog(this);
        jhProgressDialog.setShowBackground(false);
        daosanjiao=(ImageView)findViewById(R.id.daosan);
        rlout=(RelativeLayout)findViewById(R.id.rlout);
        rlbttijiao=(RelativeLayout)findViewById(R.id.rlbttijiao);
        viewon=findViewById(R.id.viewon);
        signs=(TextView)findViewById(R.id.signs);
        signsds=(TextView) findViewById(R.id.signsds);
        latertime=(TextView)findViewById(R.id.latertimes);
//        ivgaizhangs=(ImageView)findViewById(R.id.ivgaizhangs);
//        tvpands=(TextView)findViewById(R.id.tvpands);
        woveiew=(TextView)findViewById(R.id.woveiew);
        tvtaiming=(TextView)findViewById(R.id.tvtaiming);
//        igzhuangtai=(ImageView)findViewById(R.id.igzhuangtai);
        classroomtv=(TextView)findViewById(R.id.classroomtvcourse);
        syllabustv=(TextView)findViewById(R.id.syllabustv);
        xianone=(ImageView)findViewById(R.id.xianone);
        xiantwo=(ImageView)findViewById(R.id.xiantwo);
        xianthree=(ImageView)findViewById(R.id.xianthree);
        xianfour=(ImageView)findViewById(R.id.xianfour);
        xianfive=(ImageView)findViewById(R.id.xianfive);
        xianwen=(TextView)findViewById(R.id.xianwen);
        ids=(LinearLayout)findViewById(R.id.ids);
        ids.setVisibility(View.GONE);
        final ImageView courseback=(ImageView)findViewById(R.id.courseback);
        starnub=(TextView)findViewById(R.id.starnub);
        classroomdatecourse=(TextView)findViewById(R.id.classroomdatecourse);
        classroomweekcourse=(TextView)findViewById(R.id.classroomweekcourse);
        classroomzhoucourse=(TextView)findViewById(R.id.classroomzhoucourse);
        classroomjiecourse=(TextView)findViewById(R.id.classroomjiecourse);
        classroomtimecourse=(TextView)findViewById(R.id.classroomtimecourse);
        classroomtimecourse=(TextView)findViewById(R.id.classroomtimecourse);
        classroomjiaocourse=(TextView)findViewById(R.id.classroomjiaocourse);
        ratingBarView = (RatingBarView)findViewById(R.id.starView);
//        ratingBarView.setStar(3);
        ratingBarView.setmClickable(true);
        ratingBarView.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                i=RatingScore;
            }
        });
        lifping=(LinearLayout)findViewById(R.id.lifping);
        bttijiao=(Button)findViewById(R.id.bttijiaocour);
        bttijiao.setClickable(true);
        bttijiao.setBackgroundResource(R.drawable.code_click);
        etfankui=(EditText)findViewById(R.id.etfankui);
        etfankui.setEnabled(true);
        bttijiao.setOnClickListener(this);
        courseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DianApplication.user.libiao=null;
                CourseActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
            }
        });
        formatter    =   new    SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date    curDate    =   new    Date(System.currentTimeMillis());
        str    =    formatter.format(curDate);
        etfankui.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String str=etfankui.getText().toString().trim();
                if(str.getBytes().length>250){
                    DianTool.showTextToast(CourseActivity.this,"字数不能超过80个字");
                    bttijiao.setBackgroundResource(R.color.beijinan);
                    bttijiao.setClickable(false);
                }else{
                    if (null!=courseListEntity) {
                        if ( !courseListEntity.rollcallType.equals("2") && !courseListEntity.rollcallType.equals("4")) {
                            bttijiao.setBackgroundResource(R.drawable.login_selector);
                            bttijiao.setClickable(true);
                        }
                    }else {
                        if ( !course.rollcallType.equals("2") && !course.rollcallType.equals("4")) {
                            bttijiao.setBackgroundResource(R.drawable.login_selector);
                            bttijiao.setClickable(true);
                        }
                    }
                }
            }
        });
    }

    //更新UI
    public void update(){
        if(null==courseListEntity){
            initdata();
            return;
        }
        classroomtv.setText(courseListEntity.teacher);
        syllabustv.setText(courseListEntity.courseName);
        classroomdatecourse.setText(courseListEntity.teach_time);
        classroomzhoucourse.setText(getResources().getString(R.string.di) + courseListEntity.weekName + getResources().getString(R.string.zhou));
        if (courseListEntity.classBeginTime != null) {
            if (courseListEntity.classEndTime != null) {
                classroomtimecourse.setText(courseListEntity.classBeginTime + "-" + courseListEntity.classEndTime);
            }
        }
        if (courseListEntity.lateTime != 0) {
            ids.setVisibility(View.VISIBLE);
            latertime.setText("上课" + courseListEntity.lateTime + "分钟之后");
        }else{
            ids.setVisibility(View.GONE);
        }
        classroomjiecourse.setText(courseListEntity.whichLesson);
        classroomjiaocourse.setText(courseListEntity.classRoom);
        if (!TextUtils.isEmpty(courseListEntity.signTime)) {
            signs.setText(courseListEntity.signTime);
        }
        if (!TextUtils.isEmpty(courseListEntity.address)) {
            signsds.setVisibility(View.VISIBLE);
            signsds.setText(courseListEntity.address);
        }else{
            signsds.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(courseListEntity.rollcallType)) {
            if (courseListEntity.rollcallType.equals("2")) {
                ratingBarView.setmClickable(false);
                tvtaiming.setText(getResources().getString(R.string.kuangke));
                bttijiao.setClickable(false);
                etfankui.setEnabled(false);
                bttijiao.setBackgroundResource(R.drawable.check_btn_not_click);
            } else if (courseListEntity.rollcallType.equals("3")) {
                tvtaiming.setText(getResources().getString(R.string.chidao));
            } else if (courseListEntity.rollcallType.equals("4")) {
                ratingBarView.setmClickable(false);
                bttijiao.setClickable(false);
                etfankui.setEnabled(false);
                bttijiao.setBackgroundResource(R.drawable.check_btn_not_click);
                tvtaiming.setText(getResources().getString(R.string.qingjia));
            } else if (courseListEntity.rollcallType.equals("5")) {
                tvtaiming.setText(getResources().getString(R.string.zaotui));
            } else if (courseListEntity.rollcallType.equals("1")) {
                tvtaiming.setText(getResources().getString(R.string.yidao));
            } else if (courseListEntity.rollcallType.equals("0")) {
                tvtaiming.setText("未点名");
            } else if (courseListEntity.rollcallType.equals("9")) {
                tvtaiming.setText("取消本节课考勤");
            } else if (courseListEntity.rollcallType.equals("7")) {
                tvtaiming.setText("未提交");
            } else if (courseListEntity.rollcallType.equals("6")) {
                tvtaiming.setText("已提交");
            } else {
                tvtaiming.setText("异常");
            }
        } else {
            tvtaiming.setText(getResources().getString(R.string.weiqiandao));
        }
        if (courseListEntity.dayOfWeek.equals("3")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "三");
        } else if (courseListEntity.dayOfWeek.equals("1")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "一");
        } else if (courseListEntity.dayOfWeek.equals("2")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "二");
        } else if (courseListEntity.dayOfWeek.equals("4")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "四");
        } else if (courseListEntity.dayOfWeek.equals("5")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "五");
        } else if (courseListEntity.dayOfWeek.equals("6")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "六");
        } else if (courseListEntity.dayOfWeek.equals("7")) {
            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "日");
        }
    }

    public void initdata(){
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            HttpParams params = new HttpParams();
            params.put("schedule_id", ide);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getZheRe("/api/phone/v1/student/coursedetail/get")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    if (TextUtils.isEmpty(s)) {
                        DianTool.showTextToast(CourseActivity.this, getResources().getString(R.string.wushuju));
                        classroomweekcourse.setText("");
                    } else {
                        course = JSON.parseObject(s, Course.class);
                        classroomtv.setText(course.teacher);
                        syllabustv.setText(course.courseName);
                        classroomdatecourse.setText(course.teach_time);
                        classroomzhoucourse.setText(getResources().getString(R.string.di) + course.weekName + getResources().getString(R.string.zhou));
                        if (course.classBeginTime != null) {
                            if (course.classEndTime != null) {
                                classroomtimecourse.setText(course.classBeginTime + "-" + course.classEndTime);
                            }
                        }
                        if (course.lateTime != 0) {
                            ids.setVisibility(View.VISIBLE);
                            latertime.setText("上课" + course.lateTime + "分钟之后");
                        }else{
                            ids.setVisibility(View.GONE);
                        }
                        classroomjiecourse.setText(course.whichLesson);
                        classroomjiaocourse.setText(course.classRoom);
                        if (!TextUtils.isEmpty(course.signTime)) {
                            signs.setText(course.signTime);
                        }
                        if (!TextUtils.isEmpty(course.address)) {
                            signsds.setVisibility(View.VISIBLE);
                            signsds.setText(course.address);
                        }else{
                            signsds.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(course.rollcallType)) {
                            if (course.rollcallType.equals("2")) {
                                ratingBarView.setmClickable(false);
                                tvtaiming.setText(getResources().getString(R.string.kuangke));
                                bttijiao.setClickable(false);
                                etfankui.setEnabled(false);
                                bttijiao.setBackgroundResource(R.drawable.check_btn_not_click);
                            } else if (course.rollcallType.equals("3")) {
                                tvtaiming.setText(getResources().getString(R.string.chidao));
                            } else if (course.rollcallType.equals("4")) {
                                ratingBarView.setmClickable(false);
                                bttijiao.setClickable(false);
                                etfankui.setEnabled(false);
                                bttijiao.setBackgroundResource(R.drawable.check_btn_not_click);
                                tvtaiming.setText(getResources().getString(R.string.qingjia));
                            } else if (course.rollcallType.equals("5")) {
                                tvtaiming.setText(getResources().getString(R.string.zaotui));
                            } else if (course.rollcallType.equals("1")) {
                                tvtaiming.setText(getResources().getString(R.string.yidao));
                            } else if (course.rollcallType.equals("0")) {
                                tvtaiming.setText("未点名");
                            } else if (course.rollcallType.equals("9")) {
                                tvtaiming.setText("取消本节课考勤");
                            } else if (course.rollcallType.equals("7")) {
                                tvtaiming.setText("未提交");
                            } else if (course.rollcallType.equals("6")) {
                                tvtaiming.setText("已提交");
                            } else {
                                tvtaiming.setText("异常");
                            }
                        } else {
                            tvtaiming.setText(getResources().getString(R.string.weiqiandao));
                        }
                        if (course.dayOfWeek.equals("3")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "三");
                        } else if (course.dayOfWeek.equals("1")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "一");
                        } else if (course.dayOfWeek.equals("2")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "二");
                        } else if (course.dayOfWeek.equals("4")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "四");
                        } else if (course.dayOfWeek.equals("5")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "五");
                        } else if (course.dayOfWeek.equals("6")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "六");
                        } else if (course.dayOfWeek.equals("7")) {
                            classroomweekcourse.setText(getResources().getString(R.string.xiqing) + "日");
                        }
                        pinjiao(course.id);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                    DianTool.response(response,CourseActivity.this);
                }
            });
        } else{
            DianTool.showTextToast(this, "请检查网络");
        }

    }
    void pinjiao(int id){
        DianTool.huoqutoken();
        HttpParams params=new HttpParams();
        params.put("schedule_id", id);
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        OkGo.get(HostAdress.getZheRe("/api/phone/v1/students/course/assess/getmy")).tag(this)
                .headers(headers).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (!s.equals("[]")) {
                    ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                    if (resultInfo.data.length != 0) {
                        rlbttijiao.setVisibility(View.GONE);
                        viewon.setVisibility(View.GONE);
                        etfankui.setVisibility(View.GONE);
                        rlout.setVisibility(View.GONE);
                        daosanjiao.setVisibility(View.GONE);
                        lifping.setVisibility(View.VISIBLE);
                        woveiew.setVisibility(View.VISIBLE);
                        ratingBarView.setVisibility(View.GONE);
                        starnub.setText(resultInfo.data[resultInfo.data.length - 1].score);
                        if (!TextUtils.isEmpty(resultInfo.data[resultInfo.data.length - 1].content)) {
                            xianwen.setText(resultInfo.data[resultInfo.data.length - 1].content);
                        }
                        if (resultInfo.data[resultInfo.data.length - 1].score.equals("1")) {
                            xianone.setVisibility(View.VISIBLE);
                        } else if (resultInfo.data[resultInfo.data.length - 1].score.equals("2")) {
                            xianone.setVisibility(View.VISIBLE);
                            xiantwo.setVisibility(View.VISIBLE);
                        } else if (resultInfo.data[resultInfo.data.length - 1].score.equals("3")) {
                            xianone.setVisibility(View.VISIBLE);
                            xiantwo.setVisibility(View.VISIBLE);
                            xianthree.setVisibility(View.VISIBLE);
                        } else if (resultInfo.data[resultInfo.data.length - 1].score.equals("4")) {
                            xianone.setVisibility(View.VISIBLE);
                            xiantwo.setVisibility(View.VISIBLE);
                            xianthree.setVisibility(View.VISIBLE);
                            xianfour.setVisibility(View.VISIBLE);
                        } else if (resultInfo.data[resultInfo.data.length - 1].score.equals("5")) {
                            xianone.setVisibility(View.VISIBLE);
                            xiantwo.setVisibility(View.VISIBLE);
                            xianthree.setVisibility(View.VISIBLE);
                            xianfour.setVisibility(View.VISIBLE);
                            xianfive.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rlbttijiao.setVisibility(View.VISIBLE);
                        viewon.setVisibility(View.VISIBLE);
                        etfankui.setVisibility(View.VISIBLE);
                        rlout.setVisibility(View.VISIBLE);
                        daosanjiao.setVisibility(View.VISIBLE);
                        lifping.setVisibility(View.GONE);
                        ratingBarView.setVisibility(View.VISIBLE);
                        if(null!=courseListEntity){
                            if (!TextUtils.isEmpty(courseListEntity.teach_time)) {
                                if (courseListEntity.classBeginTime != null) {
                                    dateCompare(courseListEntity.teach_time + " " + courseListEntity.classEndTime, str);
                                } else {
                                    bttijiao.setBackgroundResource(R.color.beijinan);
                                    bttijiao.setClickable(false);
                                }
                            } else {
                                bttijiao.setBackgroundResource(R.color.beijinan);
                                bttijiao.setClickable(false);
                            }

                        }else {
                            if (!TextUtils.isEmpty(course.teach_time)) {
                                if (course.classBeginTime != null) {
                                    dateCompare(course.teach_time + " " + course.classEndTime, str);
                                } else {
                                    bttijiao.setBackgroundResource(R.color.beijinan);
                                    bttijiao.setClickable(false);
                                }
                            } else {
                                bttijiao.setBackgroundResource(R.color.beijinan);
                                bttijiao.setClickable(false);
                            }
                        }

                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.response(response,CourseActivity.this);
            }
        });
    }
    void dateCompare(String one,String two){
        try {
            Date d1 = formatter.parse(one);
            Date d2 = formatter.parse(two);
            if(d1.getTime() - d2.getTime()>0) {
                etfankui.setHint("现在还不能评教哦~");
                bttijiao.setBackgroundResource(R.color.beijinan);
                ratingBarView.setmClickable(false);
                bttijiao.setClickable(false);
                etfankui.setEnabled(false);
                if(d1.getTime() - d2.getTime()>resultInfotoken.expires_in*1000){
                    dao_type=1;
                }else {
                    dao_type=2;
                }
            }else{
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bttijiaocour:
                tijiaodata();
                break;
        }
    }
    void tijiaodata() {
        HttpParams requestParams=new HttpParams();
        wenzi = etfankui.getText().toString().trim();
        if (i != 0) {
            if (!TextUtils.isEmpty(wenzi)) {
//                    String str = new String(wenzi.getBytes("UTF-8"), "8859_1");
                if (wenzi.getBytes().length <= 250) {
                    requestParams.put("content", wenzi);
                } else {
                    DianTool.dissMyDialog();
                    DianTool.showTextToast(CourseActivity.this, "字数不能超过80个字");
                    return;
                }
            }else {
                DianTool.showTextToast(CourseActivity.this, "评教信息不能为空！");
                return;
            }
            if(null!=courseListEntity){
                requestParams.put("schedule_id", courseListEntity.id);
            }else {
                requestParams.put("schedule_id", course.id);
            }
            requestParams.put("score", i);
            //  DianTool.showMyDialog(this,"");
            jhProgressDialog.show();
            OkGo.post(HostAdress.getZheRe("/api/phone/v1/assess/create")).tag(this)
                    .headers(headers).params(requestParams).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    // DianTool.dissMyDialog();
                    jhProgressDialog.dismiss();
                    DianTool.showTextToast(CourseActivity.this, getResources().getString(R.string.pinggong));
                    //  new StateDialog(CourseActivity.this, StateView.State.SUCCESS).setMessage(getString(R.string.pinggong)).show();
                    rlbttijiao.setVisibility(View.GONE);
                    viewon.setVisibility(View.GONE);
                    etfankui.setVisibility(View.GONE);
                    rlout.setVisibility(View.GONE);
                    daosanjiao.setVisibility(View.GONE);
                    lifping.setVisibility(View.VISIBLE);
                    ratingBarView.setVisibility(View.GONE);
                    woveiew.setVisibility(View.VISIBLE);
                    starnub.setText("" + i);
                    if (!TextUtils.isEmpty(wenzi)) {
                        xianwen.setText(wenzi);
                    }
                    if (i == 1) {
                        xianone.setVisibility(View.VISIBLE);
                    } else if (i == 2) {
                        xianone.setVisibility(View.VISIBLE);
                        xiantwo.setVisibility(View.VISIBLE);
                    } else if (i == 3) {
                        xianone.setVisibility(View.VISIBLE);
                        xiantwo.setVisibility(View.VISIBLE);
                        xianthree.setVisibility(View.VISIBLE);
                    } else if (i == 4) {
                        xianone.setVisibility(View.VISIBLE);
                        xiantwo.setVisibility(View.VISIBLE);
                        xianthree.setVisibility(View.VISIBLE);
                        xianfour.setVisibility(View.VISIBLE);
                    }else if(i==5){
                        xianone.setVisibility(View.VISIBLE);
                        xiantwo.setVisibility(View.VISIBLE);
                        xianthree.setVisibility(View.VISIBLE);
                        xianfour.setVisibility(View.VISIBLE);
                        xianfive.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    //  DianTool.dissMyDialog();
                    jhProgressDialog.dismiss();
                    DianTool.showTextToast(CourseActivity.this,"评教失败");
                    //  new StateDialog(CourseActivity.this, StateView.State.FAILUE).setMessage("评教失败").show();
                    DianTool.response(response,CourseActivity.this);
                }
            });
        } else {
            DianTool.dissMyDialog();
            DianTool.showTextToast(CourseActivity.this, getResources().getString(R.string.yixin));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}