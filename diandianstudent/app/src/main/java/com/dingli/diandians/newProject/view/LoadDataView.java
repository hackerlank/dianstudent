package com.dingli.diandians.newProject.view;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.newProject.constants.ViewStatus;


/**
 * <p>Title: UserPresenter<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2016/11/2
 */
public class LoadDataView extends FrameLayout {

	/** 数据加载异常 */
	private final View errorView;
	/** 没有数据 */
	public final View noDataView;
	/** 数据 */
	private final View dataView;
	/** 加载数据中 */
	private final View loadingView;
	/** 网络连接失败 */
	private final View netErrorView;
	private final LayoutInflater inflater;
	//private final Animation mImageViewAnimation;
	private Button loadingEmptyBtn;
	public TextView loadingEmptyTv,netErrorTv;
	private ImageView loadingEmptyImageView,loading_imagview;
	public volatile boolean isFirstLoad = true;
	private AnimationDrawable animationDrawable;
	private ProgressBar progressBar;
	public LoadDataView(Context context, View view) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		dataView = view;
		inflater = LayoutInflater.from(context);
		netErrorView = inflater.inflate(R.layout.layout_net_error, null);
		errorView = inflater.inflate(R.layout.layout_data_error, null);
		noDataView = inflater.inflate(R.layout.layout_data_empty, null);
		loadingView = inflater.inflate(R.layout.layout_data_loading, null);
		loading_imagview= (ImageView) loadingView.findViewById(R.id.loading_imagview);
		loadingEmptyBtn = (Button) noDataView.findViewById(R.id.data_error_button);
		loadingEmptyTv = (TextView) noDataView.findViewById(R.id.data_loading_empty_textview);
		netErrorTv= (TextView) noDataView.findViewById(R.id.netErrorTv);
		loadingEmptyImageView = (ImageView) noDataView.findViewById(R.id.no_data_loading_img);
		progressBar= (ProgressBar) findViewById(R.id.progressBar);

//		mImageViewAnimation = AnimationUtils.loadAnimation(context, R.anim.rote);
//		mImageViewAnimation.setInterpolator(new LinearInterpolator());
//		mImageViewAnimation.setDuration(2000);
//		mImageViewAnimation.setRepeatCount(Animation.INFINITE);
//		mImageViewAnimation.setRepeatMode(Animation.RESTART);
		initViews();
	}

	private void initViews() {
		if (null != dataView) {
			addView(dataView);
		}
		if (null != errorView) {
			addView(errorView);
			errorView.setVisibility(View.GONE);
		}
		if (null != netErrorView) {
			addView(netErrorView);
			netErrorView.setVisibility(View.GONE);
		}
		if (null != loadingView) {
			addView(loadingView);
			loadingView.setVisibility(View.GONE);
		}
		if (null != noDataView) {
			addView(noDataView);
			noDataView.setVisibility(View.GONE);
		}
	}

	private void stop() {
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
//		if (null != loading_imagview) {
//			loading_imagview.clearAnimation();
//		}

//		if (animationDrawable != null && animationDrawable.isRunning()) {
//			animationDrawable.stop();
//		}
	}

	private void start() {
//		stop();
//		if (null != loading_imagview) {
//			loading_imagview.startAnimation(mImageViewAnimation);
//		}

//		if (loading_imagview != null) {
//			loading_imagview.setVisibility(View.VISIBLE);
//		}
//		if (animationDrawable == null) {
//			loading_imagview.setImageResource(R.drawable.animstion_push);
//			animationDrawable = (AnimationDrawable) loading_imagview.getDrawable();
//		}
//
//		animationDrawable.start();
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
 	}


	/**
	 * 开始加载
	 *
	 * <br>
	 */
	private void loadStart() {
		if (null != dataView && dataView.getVisibility() != View.GONE) {
			dataView.setVisibility(View.GONE);
		}
		if (null != errorView && errorView.getVisibility() != View.GONE) {
			errorView.setVisibility(View.GONE);
		}
		if (null != netErrorView && netErrorView.getVisibility() != View.GONE) {
			netErrorView.setVisibility(View.GONE);
		}
		if (null != noDataView && noDataView.getVisibility() != View.GONE) {
			noDataView.setVisibility(View.GONE);
		}

		if (null != loadingView && loadingView.getVisibility() != View.VISIBLE) {
			start();
			loadingView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 加载成功
	 *
	 * <br>
	 */
	private void loadSuccess() {
		stop();
		if (null != dataView && dataView.getVisibility() != View.VISIBLE) {
			dataView.setVisibility(View.VISIBLE);
		}
		if (null != errorView && errorView.getVisibility() != View.GONE) {
			errorView.setVisibility(View.GONE);
		}
		if (null != netErrorView && netErrorView.getVisibility() != View.GONE) {
			netErrorView.setVisibility(View.GONE);
		}
		if (null != noDataView && noDataView.getVisibility() != View.GONE) {
			noDataView.setVisibility(View.GONE);
		}

		if (null != loadingView && loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
	}

	/**
	 * 加载失败
	 *
	 */
	private void loadError() {
		stop();
		if (null != dataView && dataView.getVisibility() != View.GONE) {
			dataView.setVisibility(View.GONE);
		}
		if (null != errorView && errorView.getVisibility() != View.VISIBLE) {
			errorView.setVisibility(View.VISIBLE);
		}
		if (null != netErrorView && netErrorView.getVisibility() != View.GONE) {
			netErrorView.setVisibility(View.GONE);
		}
		if (null != noDataView && noDataView.getVisibility() != View.GONE) {
			noDataView.setVisibility(View.GONE);
		}

		if (null != loadingView && loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
	}

	/**
	 * 加载成功，但无数据
	 *
	 * <br>
	 */
	private void loadNoData() {
		stop();
		if (null != dataView && dataView.getVisibility() != View.GONE) {
			dataView.setVisibility(View.GONE);
		}
		if (null != errorView && errorView.getVisibility() != View.GONE) {
				errorView.setVisibility(View.GONE);
		}
		if (null != netErrorView && netErrorView.getVisibility() != View.GONE) {
			netErrorView.setVisibility(View.GONE);
		}
		if (null != noDataView && noDataView.getVisibility() != View.VISIBLE) {
			noDataView.setVisibility(View.VISIBLE);
			loadingEmptyTv.setVisibility(View.GONE);

		}
		if (null != loadingView && loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
	}

	/**
	 * 网络连接问题，加载异常，检查网络，点击屏幕重新连接
	 *
	 */
	private void loadNotNetwork() {
		stop();

		if (null != dataView && dataView.getVisibility() != View.GONE) {
			dataView.setVisibility(View.GONE);
		}
		if (null != errorView && errorView.getVisibility() != View.GONE) {
			errorView.setVisibility(View.GONE);
		}
		if (null != netErrorView && netErrorView.getVisibility() != View.VISIBLE) {
			netErrorView.setVisibility(View.VISIBLE);
		}
		if (null != noDataView && noDataView.getVisibility() != View.GONE) {
			noDataView.setVisibility(View.GONE);
		}

		if (null != loadingView && loadingView.getVisibility() != View.GONE) {
			loadingView.setVisibility(View.GONE);
		}
	}

	public void setErrorListner(OnClickListener listener) {
		if (null == listener) {
			return;
		}
		if (null != errorView) {

			errorView.findViewById(R.id.data_error_button).setOnClickListener(listener);
		}
		if (null != netErrorView) {

			netErrorView.findViewById(R.id.netBT).setOnClickListener(listener);
		}
		if (null != noDataView) {

			noDataView.findViewById(R.id.data_error_button).setOnClickListener(listener);
		}

	}

	public void changeStatusView(ViewStatus status) {
		if (isFirstLoad) {
			switch (status) {
			case START:
				loadStart();
				break;
			case SUCCESS:
				isFirstLoad = false;
				loadSuccess();
				break;
			case FAILURE:
				loadError();
				break;
			case EMPTY:
				loadNoData();
				break;
			case NOTNETWORK:
				loadNotNetwork();
				break;
			}
		}
	}
	public void setFirstLoad(){
		isFirstLoad = true;
	}
	public TextView getLoadingEmptyTv(){
		return loadingEmptyTv;
	}
	public TextView getLoadingEmptyTvTop(){
		return netErrorTv;
	}
	public void setLoadingEmptyTv(String value){
		 if(loadingEmptyTv!=null){
			 loadingEmptyTv.setText(value);
		 }
	}
	public Button getLoadingEmptyBtn(){
		return loadingEmptyBtn;
	}
	public ImageView getLoadingEmptyImageView(){
	return loadingEmptyImageView;}
}
