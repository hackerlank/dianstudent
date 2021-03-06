package com.dingli.diandians.newProject.moudle.hrd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.easefun.polyvsdk.Video.ADMatter;
import com.easefun.polyvsdk.ijk.IjkVideoView;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * 广告视图，只针对图片广告做处理，视频广告在IjkVideoView中已经处理
 * @author TanQu 2016-3-3
 */
public class PolyvPlayerAdvertisementView extends RelativeLayout {
	private Context mContext = null;
	private PopupWindow popupWindow = null;
	private IjkVideoView mIjkVideoView = null;
	private ImageView mAdvertisementImage = null;
	private ImageButton mStartBtn = null;
	private TextView mCountDown = null;
	private View mAnchorView = null;
	private ADMatter mADMatter = null;
	private int countDownNum = 0;
	private static final int COUNT_DOWN = 1;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			countDownNum--;
			mCountDown.setText(String.valueOf(countDownNum));
			if (countDownNum <= 0) {
				//重要，调用此方法才能继续播放
				if (ADMatter.LOCATION_FIRST.equals(mADMatter.getLocation())
						|| ADMatter.LOCATION_LAST.equals(mADMatter.getLocation())) {
					mIjkVideoView.playNext();
				}
				
				hide();
			} else {
				handler.sendEmptyMessageDelayed(COUNT_DOWN, 1000);
			}
		}
	};

    public PolyvPlayerAdvertisementView(Context context) {
        super(context);
        mContext = context;
        initViews();
    }
    
    public PolyvPlayerAdvertisementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }
    
    public PolyvPlayerAdvertisementView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViews();
    }
    
    public void setIjkVideoView(IjkVideoView ijkVideoView) {
    	mIjkVideoView = ijkVideoView;
    }
    
    private void initViews() {
    	LayoutInflater.from(getContext()).inflate(R.layout.polyv_player_advertisement_view, this);
    	mAdvertisementImage = (ImageView) findViewById(R.id.advertisement_image);
    	mAdvertisementImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = mADMatter.getAddrUrl();
				if (TextUtils.isEmpty(path) == false) {
					try {
						new URL(path);
					} catch (MalformedURLException e) {
						return;
					}

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(path));
					mContext.startActivity(intent);
				}
			}
		});

    	mCountDown = (TextView) findViewById(R.id.count_down);

    	mStartBtn = (ImageButton) findViewById(R.id.advertisement_start_btn);
    	mStartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mIjkVideoView.start();
				hide();
			}
		});

    	if (popupWindow == null) {
    		popupWindow = new PopupWindow(mContext);
    		popupWindow.setContentView(this);
    	}
    }
    
    /**
     * 设置图片并显示
     * @param anchorView
     * @param adMatter
     */
    public void show(View anchorView, ADMatter adMatter) {
    	mAnchorView = anchorView;
    	mADMatter = adMatter;
    	countDownNum = adMatter.getTimeSize();
    	refresh();
    	
    	//暂停图片广告不需要倒计时，是点击开始按钮继续
    	if (ADMatter.LOCATION_PAUSE.equals(adMatter.getLocation())) {
    		mStartBtn.setVisibility(View.VISIBLE);
    		mCountDown.setVisibility(View.GONE);
    	} else {
    		mStartBtn.setVisibility(View.GONE);
    		mCountDown.setVisibility(View.VISIBLE);
    		handler.removeMessages(COUNT_DOWN);
        	handler.sendEmptyMessageDelayed(COUNT_DOWN, 1000);
    	}
    }
    
    /**
	 * 重新设置控件
	 */
	public void refresh() {
		int[] location = new int[2];
		mAnchorView.getLocationInWindow(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0] + mAnchorView.getWidth(), location[1] + mAnchorView.getHeight());
		popupWindow.setWidth(mAnchorView.getWidth());
		popupWindow.setHeight(mAnchorView.getHeight());
		popupWindow.showAtLocation(mAnchorView, Gravity.NO_GRAVITY, 0, anchorRect.top);

		Glide.with(mContext).load(mADMatter.getMatterUrl() )
				.centerCrop()
				.into(mAdvertisementImage);
		//ImageLoader.getInstance().displayImage(mADMatter.getMatterUrl(), mAdvertisementImage, mOptions, new AnimateFirstDisplayListener());
		mCountDown.setText(String.valueOf(countDownNum));
	}
    
	/**
	 * 是否在显示中
	 * @return
	 */
	public boolean isShowing() {
		return popupWindow.isShowing();
	}
	
    /**
     * 隐藏
     */
    public void hide() {
    	Drawable drawable = mAdvertisementImage.getDrawable();
    	if (drawable != null && drawable instanceof BitmapDrawable) {
    		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
    		if (bitmap != null && bitmap.isRecycled() == false) {
    			bitmap.recycle();
    			bitmap = null;
    		}
    	}
    	
    	mAdvertisementImage.setImageBitmap(null);
    	popupWindow.dismiss();
    	System.gc();
    	
    	if (handler != null) {
    		handler.removeMessages(COUNT_DOWN);
    	}
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
		return true;
	}
}
