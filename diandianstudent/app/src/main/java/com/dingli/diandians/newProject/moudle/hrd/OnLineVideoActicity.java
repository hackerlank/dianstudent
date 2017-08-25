package com.dingli.diandians.newProject.moudle.hrd;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dingli.diandians.R;
import com.dingli.diandians.newProject.base.activity.BaseActivity;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.constants.ViewStatus;
import com.dingli.diandians.newProject.moudle.eye.JianJieFragment;
import com.dingli.diandians.newProject.moudle.eye.JiaoLiuFragment;
import com.dingli.diandians.newProject.moudle.eye.TabAdapter;
import com.dingli.diandians.newProject.moudle.eye.presenter.LineNumberPresenter;
import com.dingli.diandians.newProject.utils.NetUtils;
import com.dingli.diandians.newProject.utils.TimeUtil;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.CustomDialog;
import com.easefun.polyvsdk.Video;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.util.ScreenTool;
import com.easefun.polyvsdk.video.PolyvVideoView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class OnLineVideoActicity extends BaseActivity {
	private static final String TAG = "OnLineVideoActicity";
	@BindView(R.id.videoview)
	IjkVideoView videoView;
	@BindView(R.id.rl)
	VideoViewContainer rl; 	// videoview的容器
	@BindView(R.id.container)
	RelativeLayout container;
	@BindView(R.id.count_down)
	TextView videoAdCountDown;
	@BindView(R.id.logo)
	TextView logo;

	@BindView(R.id.tabLayoutColumn)
	TabLayout tabLayoutColumn;
	@BindView(R.id.tvJianJIE)
	TextView tvJianJIE;
	@BindView(R.id.tvJiaoLiu)
	TextView tvJiaoLiu;
	@BindView(R.id.viewPagerColumn)
	ViewPager viewPagerColumn;
	private LoadDataView loadDataView;

	private PolyvPlayerAdvertisementView adView = null;
	private MediaController mediaController = null;
	private PolyvPlayerFirstStartView playerFirstStartView = null;
	private int w = 0, h = 0, adjusted_h = 0;
	private boolean startNow = false;
	private	String ptime,  duration,childPic,videoId,userName;
	private int  onLineMan;
	private String vid;
	private int fastForwardPos = 0;
	private boolean isPlay = false;/** 是否在播放中，用于锁频后返回继续播放 */
	private boolean isPause = false;	/** 是否暂停中，用于home键切出去回来后暂停播放 */
	private CustomDialog.Builder ibuilder;
	private LineNumberPresenter lineNumberPresenter;
	private CustomDialog customDialog = null;
	private Video.HlsSpeedType hlsSpeedType;
	private	  String value;
	private	CustomDialog dialog=null;

	private Handler handler = new Handler() {//错误提示框
		@Override
		public void handleMessage(Message msg) {
		}
	};
	public static void intentTo(Context context, PlayMode playMode, PlayType playType, String value, boolean startNow,String ptime,String duration,int onLineMan,String childPic,String videoId,String userName) {
		context.startActivity(newIntent(context, playMode, playType, value,startNow,ptime,duration,onLineMan,childPic,videoId,userName));
	}
	public static Intent newIntent(Context context, PlayMode playMode, PlayType playType, String value,
								   boolean startNow,String ptime,String duration,int onLineMan,String childPic,String videoId,String userName) {
		Intent intent = new Intent(context, OnLineVideoActicity.class);
		intent.putExtra("playMode", playMode.getCode());
		intent.putExtra("playType", playType.getCode());
		intent.putExtra("value", value);
		intent.putExtra("startNow", startNow);
		intent.putExtra("ptime", ptime);
		intent.putExtra("duration", duration);
		intent.putExtra("onLineMan", onLineMan);
		intent.putExtra("childPic", childPic);
		intent.putExtra("videoId", videoId);
		intent.putExtra("userName", userName);
		return intent;
	}
	@Override
	protected int layoutId() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		return R.layout.video_small2;
	}

	@Override
	protected ViewGroup loadDataViewLayout() {
		return container;
	}

	@Override
	protected void initView() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		initSensor();
		EventBus.getDefault().register(this);
		int playModeCode = getIntent().getIntExtra("playMode", 0);
		OnLineVideoActicity.PlayMode playMode = OnLineVideoActicity.PlayMode.getPlayMode(playModeCode);
		int playTypeCode = getIntent().getIntExtra("playType", 0);
		final OnLineVideoActicity.PlayType playType = OnLineVideoActicity.PlayType.getPlayType(playTypeCode);
		value = getIntent().getStringExtra("value");
		hlsSpeedType = Video.HlsSpeedType
				.getHlsSpeedType(getIntent().getStringExtra("hlsSpeedType"));
		if (hlsSpeedType == null)
			hlsSpeedType = Video.HlsSpeedType.SPEED_1X;
		startNow = getIntent().getBooleanExtra("startNow", false);
		ptime= getIntent().getStringExtra("ptime");
		duration= getIntent().getStringExtra("duration");
		childPic= getIntent().getStringExtra("childPic");
		onLineMan= getIntent().getIntExtra("onLineMan",0);
		videoId= getIntent().getStringExtra("videoId");
		userName= getIntent().getStringExtra("userName");
		startNow=true;
		if (playMode == null || playType == null || TextUtils.isEmpty(value)) {
			Log.e(TAG, "Null Data Source");
			finish();
			return;
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		int[] wh = ScreenTool.getNormalWH(this);
		w = wh[0];
		h = wh[1];
		// 小窗口的比例
		float ratio = (float) 4 / 3;
		adjusted_h = (int) (w*1000*0.512/1000);
		rl = (VideoViewContainer) findViewById(R.id.rl);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));
		videoView= (IjkVideoView) findViewById(R.id.videoview);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
		videoAdCountDown = (TextView) findViewById(R.id.count_down);
		logo = (TextView) findViewById(R.id.logo);
		// 在缓冲时出现的loading
		videoView.setMediaBufferingIndicator(progressBar);
		videoView.setOpenTeaser(true);
		videoView.setOpenAd(true);
		videoView.setOpenQuestion(true);
		videoView.setOpenSRT(true);
		videoView.setNeedGestureDetector(true);
		videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		videoView.setAutoContinue(true);
		videoView.setOnPreparedListener(mp -> {
			videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
			if (startNow == false) {
				videoView.pause(true);
				if (playType == OnLineVideoActicity.PlayType.vid) {
					playerFirstStartView.show(rl, videoView.getVideo().getVid());
				}
			}

			if (isPause) {
				videoView.pause(true);
			}

		});
		videoView.setOnVideoPlayErrorLisener(new PolyvVideoView.OnVideoPlayErrorLisener() {
			@Override
			public boolean onVideoPlayError(IjkVideoView.ErrorReason errorReason) {


				switch (errorReason.getType()) {
					case BITRATE_ERROR:
						OnLineVideoActicity.this.sendMessage("设置的码率错误");
						break;
					case CAN_NOT_CHANGE_BITRATE:
						OnLineVideoActicity.this.sendMessage("未开始播放视频不能切换码率");
						break;
					case CAN_NOT_CHANGE_HLS_SPEED:
						OnLineVideoActicity.this.sendMessage("未开始播放视频不能切换播放速度");
						break;
					case CHANGE_EQUAL_BITRATE:
						OnLineVideoActicity.this.sendMessage("切换码率相同");
						break;
					case CHANGE_EQUAL_HLS_SPEED:
						OnLineVideoActicity.this.sendMessage("切换播放速度相同");
						break;
					case HLS_15X_URL_ERROR:
						OnLineVideoActicity.this.sendMessage("1.5倍当前码率视频正在编码中");
						break;
					case HLS_15X_ERROR:
						OnLineVideoActicity.this.sendMessage("视频不支持1.5倍当前码率播放");
						break;
					case HLS_15X_INDEX_EMPTY:
						OnLineVideoActicity.this.sendMessage("视频不支持1.5倍自动码率播放");
						break;
					case HLS_SPEED_TYPE_NULL:
						OnLineVideoActicity.this.sendMessage("请设置播放速度");
						break;
					case LOCAL_VIEWO_ERROR:
						OnLineVideoActicity.this.sendMessage("本地视频文件损坏");
						break;
					case M3U8_15X_LINK_NUM_ERROR:
						OnLineVideoActicity.this.sendMessage("HLS 1.5倍播放地址服务器数据错误");
						break;
					case M3U8_LINK_NUM_ERROR:
						OnLineVideoActicity.this.sendMessage("HLS 播放地址服务器数据错误");
						break;
					case MP4_LINK_NUM_ERROR:
						OnLineVideoActicity.this.sendMessage("MP4 播放地址服务器数据错误");
						break;
					case NETWORK_DENIED:
						OnLineVideoActicity.this.sendMessage("无法连接网络");
						break;
					case NOT_LOCAL_VIDEO:
						OnLineVideoActicity.this.sendMessage("找不到本地下载的视频文件，请连网后重新下载或播放");
						break;
					case NOT_PERMISSION:
						OnLineVideoActicity.this.sendMessage("没有权限，不能播放该视频");
						break;
					case OUT_FLOW:
						OnLineVideoActicity.this.sendMessage("流量超标");
						break;
					case QUESTION_JSON_ERROR:
						OnLineVideoActicity.this.sendMessage("问答数据加载为空");
						break;
					case QUESTION_JSON_PARSE_ERROR:
						OnLineVideoActicity.this.sendMessage("问答数据格式化错误");
						break;
					case LOADING_VIDEO_ERROR:
						OnLineVideoActicity.this.sendMessage("视频信息加载过程中出错");
						break;
					case START_ERROR:
						OnLineVideoActicity.this.sendMessage("开始播放视频错误，请重试");
						break;
					case TIMEOUT_FLOW:
						OnLineVideoActicity.this.sendMessage("账号过期");
						break;
					case USER_TOKEN_ERROR:
						OnLineVideoActicity.this.sendMessage("没有设置用户数据");
						break;
					case VIDEO_NULL:
						OnLineVideoActicity.this.sendMessage("视频信息为空");
						break;
					case VIDEO_STATUS_ERROR:
						// 	OnLineVideoActicity.this.sendMessage("视频状态错误");
						break;
					case VID_ERROR:
						OnLineVideoActicity.this.sendMessage("设置的vid错误");
						break;
					default:
						break;

				}
				// 返回 false，sdk中会弹出一个默认的错误提示框
				// 返回 true，sdk中不会弹出一个错误提示框
				return true;
			}
		});

		videoView.setOnErrorListener((arg0, arg1, arg2) -> {
			sendMessage("播放异常，请稍后再试");
			// 返回 false，sdk中会弹出一个默认的错误提示框
			return true;
		});

		videoView.setOnAdvertisementOutListener(adMatter -> {
			if (adView == null) {
				adView = new PolyvPlayerAdvertisementView(OnLineVideoActicity.this);
				adView.setIjkVideoView(videoView);
			}

			adView.show(rl, adMatter);
		});

		videoView.setOnAdvertisementCountDownListener(new IjkVideoView.OnAdvertisementCountDownListener() {

			@Override
			public void onEnd() {
				videoAdCountDown.setVisibility(View.GONE);
			}

			@Override
			public void onCountDown(int num) {
				videoAdCountDown.setText(String.format("广告也精彩：%d秒", num));
				videoAdCountDown.setVisibility(View.VISIBLE);
			}
		});
		videoView.setOnPlayPauseListener(new IjkVideoView.OnPlayPauseListener() {

			@Override
			public void onPlay() {
				isPause = false;
			}

			@Override
			public void onPause() {
				isPause = true;
			}

			@Override
			public void onCompletion() {
				ibuilder = new CustomDialog.Builder(OnLineVideoActicity.this);
				ibuilder.setTitle("温馨提示");
				ibuilder.setMessage("职播已经结束，是否立即进入回放！");
				ibuilder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
					dialog.dismiss();
					HuifangVideoActicity.intentTo(OnLineVideoActicity.this, HuifangVideoActicity.PlayMode.portrait, HuifangVideoActicity.PlayType.vid, vid,
							false,childPic,videoId,userName);
					finish();
				});
				ibuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    dialog.dismiss();
                    onBackPressed();
                });
				dialog=ibuilder.create();
				dialog.show();
			}
		});

		videoView.setClick((start, end) -> mediaController.toggleVisiblity());

		// 设置缓存监听
		videoView.setOnInfoListener((arg0, arg1, arg2) -> {
			switch (arg1) {
				case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
					break;

				case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
					// 恢复
					// 把缓冲后的时间设置给控制器
					mediaController.setNewtime(videoView.getCurrentPosition());
					break;
			}
			return false;
		});

		mediaController = new MediaController(this, false);
		mediaController.setIjkVideoView(videoView);
		mediaController.setAnchorView(videoView);
		mediaController.setInstantSeeking(false);
		videoView.setMediaController(mediaController);
		long mduration=0;
		try{
			if(!TextUtils.isEmpty(ptime)){
				mduration=TimeUtil.getNowMillisecond()-Long.parseLong(ptime);
			}else {
				mduration=0;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		mediaController.setOnLinPosition(mduration,onLineMan+1);

		// 设置切屏事件
		mediaController.setOnBoardChangeListener(new MediaController.OnBoardChangeListener() {

			@Override
			public void onPortrait() {
				stretch_flag=true;
				sm.unregisterListener(listener);
				changeToLandscape();
			}

			@Override
			public void onLandscape() {
				stretch_flag=false;
				sm.unregisterListener(listener);
				changeToPortrait();
			}
		});

		mediaController.setOnResetViewListener(() -> {

		});

		mediaController.setOnUpdateStartNow(isStartNow -> startNow = isStartNow);

		switch (playMode) {
			case landScape:
				stretch_flag=false;
				changeToLandscape();
				break;

			case portrait:
				stretch_flag=true;
				changeToPortrait();
				break;
		}

	}
	public void startVideo(){
		if(!NetUtils.isConnected(getApplicationContext())){
			return;
		}
		loadDataView.changeStatusView(ViewStatus.SUCCESS);
		if(!NetUtils.isWifi(getApplicationContext())){
			CustomDialog.Builder ibuilder = new CustomDialog.Builder(OnLineVideoActicity.this);
			ibuilder.setTitle("温馨提示");
			ibuilder.setMessage("当前网络不是WiFi，是否继续观看！");
			ibuilder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
				customDialog.dismiss();
				videoView.setVid(value, false, hlsSpeedType);
			});
			ibuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
				customDialog.dismiss();
				finish();
			});
			customDialog=ibuilder.create();
			customDialog.show();
			return;
		}else {
			videoView.setVid(value, false, hlsSpeedType);
			//				videoView.setVid(value,0);
		}

		// 由videoview的容器处理videoview之外的触摸事件
		rl.setVideoView(videoView);
		// 设置隐藏状态栏的监听器
		ScreenTool.setHideStatusBarListener(this, 2000);
		// 弹幕
		vid = value;

		// 这段代码要放到初始化弹幕view的后面
		videoView.start();
		videoView.seekTo(500);
	}
	private TabAdapter tabAdapter;
	private ArrayList<Fragment> mFragmnetList = new ArrayList<>();
	private int tabPostion=0 ,currentTabPostion=0;
	@Override
	protected void initData() {
		if(NetUtils.isConnected(getApplicationContext())){
			startVideo();
		}else {
			loadDataView.changeStatusView(ViewStatus.NOTNETWORK);
		}
		lineNumberPresenter.onlineNumber(videoId);
		tabLayoutColumn.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				tabPostion = tab.getPosition();
				if (currentTabPostion != tabPostion) {
					changeTabSelect(tabPostion);
				}
				currentTabPostion = tabPostion;
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				changeTabNormal(tab.getPosition());
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
		Bundle bundle=new Bundle();
		bundle.putString("childPic",childPic);
		bundle.putString("videoId",videoId);
		bundle.putString("userName",userName);
		mFragmnetList.clear();
		mFragmnetList.add(JianJieFragment.newInstance(bundle));
		mFragmnetList.add(new JiaoLiuFragment().newInstance(bundle));
		//viewPagerColumn.setOffscreenPageLimit(1);
		tabAdapter = new TabAdapter(getSupportFragmentManager(), mFragmnetList);
		viewPagerColumn.setAdapter(tabAdapter);
		tabLayoutColumn.setupWithViewPager(viewPagerColumn);
		viewPagerColumn.setCurrentItem(0);

		tvJianJIE.setOnClickListener(view -> {
			viewPagerColumn.setCurrentItem(0);
		});
		tvJiaoLiu.setOnClickListener(view -> {
			viewPagerColumn.setCurrentItem(1);
		});
	}

	@Override
	protected void getLoadView(LoadDataView loadView) {
		this.loadDataView=loadView;
		loadDataView.setErrorListner(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startVideo();
				tabAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void initPresenter() {
		lineNumberPresenter=new LineNumberPresenter();
	}
	private void changeTabSelect(int position) {
		if(position==0){
			tvJianJIE.setTextColor(getResources().getColor(R.color.qianblue));
		}
		if(position==1){
			tvJiaoLiu.setTextColor(getResources().getColor(R.color.qianblue));
		}
	}
	private void changeTabNormal( int position) {
		if(position==0){
			tvJianJIE.setTextColor(getResources().getColor(R.color.text_color_404040));
		}
		if(position==1){
			tvJiaoLiu.setTextColor(getResources().getColor(R.color.text_color_404040));
		}
	}
	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

		//锁频回来继续播放
		if (videoView != null && videoView.isPausState() && isPlay) {
			if (adView != null) {
				adView.hide();
			}
			videoView.start();
		}
	}

	/**
	 * 切换到横屏
	 */
	public void changeToLandscape() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// 由于切换到横屏获取到的宽高可能和竖屏的不一样，所以需要重新获取宽高
		int[] wh = ScreenTool.getNormalWH(this);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(wh[0], wh[1]);
		rl.setLayoutParams(p);
	}

	/**
	 * 切换到竖屏
	 */
	public void changeToPortrait() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, adjusted_h);
		rl.setLayoutParams(p);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// 隐藏或显示状态栏
		ScreenTool.reSetStatusBar(this);
	}

	// 配置文件设置congfigchange 切屏调用一次该方法，hide()之后再次show才会出现在正确位置
	@Override
	public void onConfigurationChanged(Configuration arg0) {
		super.onConfigurationChanged(arg0);
		videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		if (arg0.orientation == Configuration.ORIENTATION_LANDSCAPE){
			// 由于切换到横屏获取到的宽高可能和竖屏的不一样，所以需要重新获取宽高
			int[] wh = ScreenTool.getNormalWH(this);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(wh[0], wh[1]);
			rl.setLayoutParams(p);
		} else {
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, adjusted_h);
			rl.setLayoutParams(p);
		}
		mediaController.hide();
		// 隐藏或显示状态栏
		ScreenTool.reSetStatusBar(this);
		if (playerFirstStartView != null && playerFirstStartView.isShowing()) {
			playerFirstStartView.hide();
			playerFirstStartView.refresh();
		}
		if (adView != null && adView.isShowing()) {
			adView.hide();
			adView.refresh();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean value = mediaController.dispatchKeyEvent(event);
		if (value)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	int seek=0;
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (videoView != null) {
			videoView.destroy();
		}
		if (playerFirstStartView != null) {
			playerFirstStartView.hide();
		}

		if (adView != null) {
			adView.hide();
		}
		seek=-1;
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//锁频的时候出去暂停播放
		if (videoView != null && videoView.isPlayState()) {
			isPlay = true;
			videoView.pause();
		} else {
			isPlay = false;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (videoView != null) {
			videoView.destroy();
		}
		if (playerFirstStartView != null) {
			playerFirstStartView.hide();
		}
		if (adView != null) {
			adView.hide();
		}
		try{
			if(null!=sm){
				sm.unregisterListener(listener);
			}
			if(null!=sm1){
				sm1.unregisterListener(listener1);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		EventBus.getDefault().unregister(this);
	};

	/**
	 * 播放类型
	 *
	 * @author TanQu
	 */
	public enum PlayType {
		/** 使用vid播放 */
		vid(1),
		/** 使用url播放 */
		url(2);

		private final int code;

		private PlayType(int code) {
			this.code = code;
		}

		/**
		 * 取得类型对应的code
		 *
		 * @return
		 */
		public int getCode() {
			return code;
		}

		public static PlayType getPlayType(int code) {
			switch (code) {
				case 1:
					return vid;
				case 2:
					return url;
			}

			return null;
		}
	}

	/**
	 * 播放模式
	 *
	 * @author TanQu
	 */
	public enum PlayMode {
		/** 横屏 */
		landScape(3),
		/** 竖屏 */
		portrait(4);

		private final int code;

		private PlayMode(int code) {
			this.code = code;
		}

		/**
		 * 取得类型对应的code
		 *
		 * @return
		 */
		public int getCode() {
			return code;
		}

		public static PlayMode getPlayMode(int code) {
			switch (code) {
				case 3:
					return landScape;
				case 4:
					return portrait;
			}

			return null;
		}
	}
	private void sendMessage(String info) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("msg", info);
		msg.setData(data);
		handler.sendMessage(msg);
	}
	private int screenWidth;
	private int screenHeight;
	private boolean sensor_flag=true;
	private boolean stretch_flag=true;
	private SensorManager sm;
	private  OrientationSensorListener listener;
	private Sensor sensor;
	private SensorManager sm1;
	private Sensor sensor1;
	private OrientationSensorListener2 listener1;
	public void initSensor(){

		//注册重力感应器  屏幕旋转
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		listener = new  OrientationSensorListener(handler1);
		sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);


		//根据  旋转之后 点击 符合之后 激活sm
		sm1 = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensor1 = sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		listener1 = new OrientationSensorListener2();
		sm1.registerListener(listener1, sensor1, SensorManager.SENSOR_DELAY_UI);
	}
	/**
	 * 重力感应监听者
	 */
	public class OrientationSensorListener implements SensorEventListener {
		private static final int _DATA_X = 0;
		private static final int _DATA_Y = 1;
		private static final int _DATA_Z = 2;

		public static final int ORIENTATION_UNKNOWN = -1;

		private Handler rotateHandler;

		public OrientationSensorListener(Handler handler) {
			rotateHandler = handler;
		}

		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		public void onSensorChanged(SensorEvent event) {

			if(sensor_flag!=stretch_flag)  //只有两个不相同才开始监听行为
			{
				float[] values = event.values;
				int orientation = ORIENTATION_UNKNOWN;
				float X = -values[_DATA_X];
				float Y = -values[_DATA_Y];
				float Z = -values[_DATA_Z];
				float magnitude = X*X + Y*Y;
				// Don't trust the angle if the magnitude is small compared to the y value
				if (magnitude * 4 >= Z*Z) {
					//屏幕旋转时
					float OneEightyOverPi = 57.29577957855f;
					float angle = (float)Math.atan2(-Y, X) * OneEightyOverPi;
					orientation = 90 - (int)Math.round(angle);
					// normalize to 0 - 359 range
					while (orientation >= 360) {
						orientation -= 360;
					}
					while (orientation < 0) {
						orientation += 360;
					}
				}
				if (rotateHandler!=null) {
					rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
				}

			}
		}
	}


	public class OrientationSensorListener2 implements SensorEventListener {
		private static final int _DATA_X = 0;
		private static final int _DATA_Y = 1;
		private static final int _DATA_Z = 2;

		public static final int ORIENTATION_UNKNOWN = -1;

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent event) {

			float[] values = event.values;

			int orientation = ORIENTATION_UNKNOWN;
			float X = -values[_DATA_X];
			float Y = -values[_DATA_Y];
			float Z = -values[_DATA_Z];

			/**
			 * 这一段据说是 android源码里面拿出来的计算 屏幕旋转的 不懂 先留着 万一以后懂了呢
			 */
			float magnitude = X*X + Y*Y;
			// Don't trust the angle if the magnitude is small compared to the y value
			if (magnitude * 4 >= Z*Z) {
				//屏幕旋转时
				float OneEightyOverPi = 57.29577957855f;
				float angle = (float)Math.atan2(-Y, X) * OneEightyOverPi;
				orientation = 90 - (int)Math.round(angle);
				// normalize to 0 - 359 range
				while (orientation >= 360) {
					orientation -= 360;
				}
				while (orientation < 0) {
					orientation += 360;
				}
			}

			if (orientation>200&&orientation<290){  //横屏
				sensor_flag = false;
			}else if ((orientation>300&&orientation<360)||(orientation>0&&orientation<45)){  //竖屏
				sensor_flag = true;
			}

			if(stretch_flag== sensor_flag){  //点击变成横屏  屏幕 也转横屏 激活
				System.out.println("激活");
				sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);

			}
		}
	}
	private Handler handler1 = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 888:
					int orientation = msg.arg1;
					if (orientation>45&&orientation<135) {

					}else if (orientation>135&&orientation<200){

					}else if (orientation>200&&orientation<290){
						//System.out.println("切换成横屏");
						OnLineVideoActicity.this.setRequestedOrientation(0);
						//changeToLandscape();
						sensor_flag = false;
						stretch_flag=false;

					}else if ((orientation>300&&orientation<360)||(orientation>0&&orientation<45)){
						//	System.out.println("切换成竖屏");
						OnLineVideoActicity.this.setRequestedOrientation(1);
						//	changeToPortrait();
						sensor_flag = true;
						stretch_flag=true;

					}
					break;
				default:
					break;
			}

		};
	};
	@Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.HRDCLOSE)
	public void onEventShopCartCount(Object o) {
		onBackPressed();
	}

	@Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.WIFINETWORK)
	public void onEventNetChange(int flag) {//0无网络,1wifi,2手机
		switch (flag){
			case 0:
				ToastUtils.showShort(OnLineVideoActicity.this,"网络异常，请检查您的网络...");
				break;
			case 1:
				// 网络是wifi，开始
				if (videoView != null && videoView.isPausState()) {
					if (adView != null) {
						adView.hide();
					}
					videoView.start();
				}
				if(null!=customDialog){
					customDialog.dismiss();
				}
				ToastUtils.showShort(OnLineVideoActicity.this,"当前网络切换到WiFi");
				break;
			case 2:
				// 网络不是wifi，停止
				if (videoView != null && videoView.isPlayState()) {
					isPlay = true;
					videoView.pause();
				} else {
					isPlay = false;
				}
				getDilag();
				if(null!=customDialog&&!customDialog.isShowing()){
					customDialog.show();
				}
			//	ToastUtils.showShort(OnLineVideoActicity.this,"当前网络切换到手机");
				break;
		}
	}

	public CustomDialog getDilag(){
		if(null!=customDialog){
			return customDialog;
		}
		CustomDialog.Builder ibuilder = new CustomDialog.Builder(OnLineVideoActicity.this);
		ibuilder.setTitle("温馨提示");
		ibuilder.setMessage("当前网络不是WiFi，是否继续观看！");
		ibuilder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
			customDialog.dismiss();
			//锁频回来继续播放
			if (videoView != null && videoView.isPausState() ) {
				if (adView != null) {
					adView.hide();
				}
				videoView.start();
			}
		});
		ibuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
			customDialog.dismiss();
			finish();
		});
		customDialog=ibuilder.create();
		return  customDialog;
	}
}