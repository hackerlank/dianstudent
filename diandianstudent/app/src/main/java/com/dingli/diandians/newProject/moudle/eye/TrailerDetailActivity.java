package com.dingli.diandians.newProject.moudle.eye;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.newProject.base.activity.BaseActivity;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.constants.ViewStatus;
import com.dingli.diandians.newProject.moudle.eye.presenter.LiveSubscriptionPresenter;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.ILiveSubscriptionView;
import com.dingli.diandians.newProject.moudle.hrd.OnLineVideoActicity;
import com.dingli.diandians.newProject.utils.DisplayUtil;
import com.dingli.diandians.newProject.utils.TimeUtil;
import com.dingli.diandians.newProject.utils.TimerUtils;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.CustomDialog;
import com.dingli.diandians.newProject.widget.MikyouCountDownTimer;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lwq on 2017/6/6.
 */

public class TrailerDetailActivity extends BaseActivity implements ILiveSubscriptionView, View.OnClickListener {
    @BindView(R.id.imageDetail)
    ImageView imageDetail;
    @BindView(R.id.imageJianJie)
    ImageView imageJianJie;
    @BindView(R.id.imageYuYue)
    ImageView imageYuYue;
    @BindView(R.id.tvStartYuYue)
    TextView tvStartYuYue;
    @BindView(R.id.linTime)
    LinearLayout linTime;
    @BindView(R.id.linLoading)
    LinearLayout linLoading;
    private LoadDataView loadDataView;
    private CustomDialog.Builder ibuilder;
    private  CustomDialog customDialog;
    private Context context;
    private String vid,coverPic,childPic,subscriptionStatus,ptime ,userId, duration,uerName;
    private long _time;
    private int onLineMan,id;
    LiveSubscriptionPresenter liveSubscriptionPresenter;
    @Override
    protected int layoutId() {
        return R.layout.activity_yugao_detail;
    }

    @Override
    protected ViewGroup loadDataViewLayout() {
        return linLoading;
    }

    @Override
    protected void initView() {

    }
    MikyouCountDownTimer timertTxtView;
    @Override
    protected void initData() {
        context=this;
        vid=getIntent().getStringExtra("vid");
        id=getIntent().getIntExtra("id",0);
        userId=getIntent().getStringExtra("userId");
        coverPic=getIntent().getStringExtra("coverPic");
        childPic=getIntent().getStringExtra("childPic");
        subscriptionStatus=getIntent().getStringExtra("subscriptionStatus");
        ptime=getIntent().getStringExtra("ptime");
        duration=getIntent().getStringExtra("duration");
        onLineMan=getIntent().getIntExtra("onlineNumber",0);
        uerName=getIntent().getStringExtra("uerName");

        _time=Long.parseLong(ptime)-TimeUtil.getNowMillisecond();

//        loadDataView.changeStatusView(ViewStatus.START);
        Glide.with(this).load(coverPic)
                .centerCrop()
                .into(imageDetail);
        Glide.with(this).load(childPic)
                .fitCenter()
                .into(imageJianJie);
        if(("1").equals(subscriptionStatus)){
            imageYuYue.setVisibility(View.VISIBLE);
            tvStartYuYue.setText("已预约");
        }else {
            imageYuYue.setVisibility(View.GONE);
            tvStartYuYue.setText("开启预约");
        }

        try{
            timertTxtView= TimerUtils.getTimer(TimerUtils.VIP_STYLE,this,_time,TimerUtils.TIME_STYLE_TWO,R.drawable.timer_shape2)
                    .setTimerPadding(DisplayUtil.dip2px(this,3),DisplayUtil.dip2px(this,4),DisplayUtil.dip2px(this,3),DisplayUtil.dip2px(this,4))
                    .setTimerTextColor(Color.WHITE)
                    .setTimerTextSize(DisplayUtil.dip2px(this,8))
                    .setTimerGapColor(getResources().getColor(R.color.background))
                    .setOnEndLister(() -> {
                        timertTxtView.getmDateTv().setText("职播已经开始");
                        if(null==context){
                            return;
                        }
                        ibuilder   = new CustomDialog.Builder(context);
                        ibuilder.setTitle("温馨提示");
                        ibuilder.setMessage("职播已经开始，是否立即进去观看！");
                        ibuilder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                            customDialog.dismiss();
                            if(TextUtils.isEmpty(vid)){
                                return;
                            }
                            OnLineVideoActicity.intentTo(context, OnLineVideoActicity.PlayMode.portrait, OnLineVideoActicity.PlayType.vid, vid,
                                    false,ptime,duration,onLineMan,coverPic,id+"",uerName);
                            finish();
                        });
                        ibuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                            customDialog.dismiss();
                            EventBus.getDefault().post("", BKConstant.EventBus.TRAILERSCLOSE);
                            EventBus.getDefault().post("", BKConstant.EventBus.HRDLISTREFSH);
                            finish();
                        });
                        customDialog=ibuilder.create();
                        customDialog.show();
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
        TextView textView=  timertTxtView.getmDateTv() ;
        linTime.addView(textView);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
        textView.setLayoutParams(params);
        timertTxtView.startTimer();
    }

    @Override
    protected void getLoadView(LoadDataView loadView) {
        loadDataView=loadView;
    }

    @Override
    protected void initPresenter() {
        liveSubscriptionPresenter=new LiveSubscriptionPresenter(this);
    }

    @Override
    public void finish() {
        super.finish();
        timertTxtView.cancelTimer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timertTxtView.cancelTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.linClose,R.id.tvStartYuYue})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linClose:
                finish();
                break;
            case R.id.tvStartYuYue:
                if(("0").equals(subscriptionStatus)){
                    Video video=new  Video();
                    video.userId=userId;
                    video.videoId=id;
                    if(isSubscriptionIng){
                        isSubscriptionIng=false;
                        liveSubscriptionPresenter.saveLiveSubscription(new Gson().toJson(video));
                    }

                }else {
                    Video video=new  Video();
                    video.userId=userId;
                    video.videoId=id;
                    if(isSubscriptionIng){
                        isSubscriptionIng=false;
                        liveSubscriptionPresenter.cancelLiveSubscription(new Gson().toJson(video));
                    }
                }
                break;
        }
    }

    public  boolean isSubscriptionIng=true;
    @Override
    public void doSubscriptionSuccess(String message) {
        subscriptionStatus="1";
        isSubscriptionIng=true;
        //  ToastUtils.showShort(this,"预约成功");
        imageYuYue.setVisibility(View.VISIBLE);
        tvStartYuYue.setText("已预约");
    }

    @Override
    public void doSubscriptionFailure(String message) {
        isSubscriptionIng=true;
        //  ToastUtils.showShort(this,"预约失败");
    }

    @Override
    public void cancleSubscriptionSuccess(String message) {
        subscriptionStatus="0";
        isSubscriptionIng=true;
        //  ToastUtils.showShort(this,"取消预约成功");
        imageYuYue.setVisibility(View.GONE);
        tvStartYuYue.setText("开启预约");
    }

    @Override
    public void cancleSubscriptionFailure(String message) {
        isSubscriptionIng=true;
        //  ToastUtils.showShort(this,"取消预约失败");
    }

    @Override
    public void onLoginInvalid(String message) {
        isSubscriptionIng=true;
    }

    @Override
    public void onNetworkFailure(String message) {
        isSubscriptionIng=true;
    }
    public  class Video{
        public  String userId;
        public  int videoId;
    }
}
