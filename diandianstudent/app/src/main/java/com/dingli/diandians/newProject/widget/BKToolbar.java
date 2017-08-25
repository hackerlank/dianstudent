package com.dingli.diandians.newProject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;

/**
 * <p>Title: BKToolbar<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: zhixin<／p>
 *
 * @author lwq
 * @version 1.0
 * @date 2017/6/1
 */
public class BKToolbar extends LinearLayout {

    private Context mContext;

    private ImageView mBtnLeft;
    private ImageView mBtnRight;
    private TextView mTvTitle;
    private TextView mTvLeft;
    private TextView mTvRight;

    public ImageView getBtnLeft() {
        return mBtnLeft;
    }

    public ImageView getBtnRight() {
        return mBtnRight;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public TextView getTvLeft() {
        return mTvLeft;
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    public BKToolbar(Context context) {
        this(context, null);
    }

    public BKToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BKToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(Color.rgb(41, 193, 146));


        final TypedArray customAttrs = context.obtainStyledAttributes(attrs,
                R.styleable.BKToolbar);
        int titleTxtAttr = customAttrs.getResourceId(R.styleable.BKToolbar_titleTxt, 0);
        int leftImgAttr = customAttrs.getResourceId(R.styleable.BKToolbar_leftImg, 0);
        int rightImgAttr = customAttrs.getResourceId(R.styleable.BKToolbar_rightImg, 0);
        boolean showLeftImgAttr = customAttrs.getBoolean(R.styleable.BKToolbar_showLeftImg, false);
        boolean showRightImgAttr = customAttrs.getBoolean(R.styleable.BKToolbar_showRightImg, false);
        boolean showLeftTvAttr = customAttrs.getBoolean(R.styleable.BKToolbar_showLeftTv, false);
        boolean showRightTvAttr = customAttrs.getBoolean(R.styleable.BKToolbar_showRightTv, false);
        int leftTxtAttr = customAttrs.getResourceId(R.styleable.BKToolbar_leftTxt, 0);
        int rightTxtAttr = customAttrs.getResourceId(R.styleable.BKToolbar_rightTxt, 0);

        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, 48)));

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams leftImgParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftImgParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftImgParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        RelativeLayout.LayoutParams rightImgParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightImgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightImgParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams leftTvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftTvParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftTvParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftTvParams.setMargins(dp2px(context, 10), 0, 0, 0);
        RelativeLayout.LayoutParams rightTvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightTvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightTvParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightTvParams.setMargins(0, 0, dp2px(context, 10), 0);

        /*左按钮*/
        mBtnLeft = new ImageView(context);
        mBtnLeft.setPadding(dp2px(context, 15), dp2px(context, 2), dp2px(context, 15), dp2px(context, 2));
        mBtnLeft.setScaleType(ImageView.ScaleType.CENTER);
        mBtnLeft.setLayoutParams(leftImgParams);
        if (leftImgAttr > 0) {
            mBtnLeft.setImageResource(leftImgAttr);
        } else {
            mBtnLeft.setImageResource(R.mipmap.backwhites);
        }
        mBtnLeft.setVisibility(showLeftImgAttr ? VISIBLE : GONE);
        layout.addView(mBtnLeft);

         /*左侧文本*/
        mTvLeft = new TextView(context);
        mTvLeft.setLayoutParams(leftTvParams);
        mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mTvLeft.setGravity(Gravity.CENTER);
        mTvLeft.setVisibility(showLeftTvAttr ? VISIBLE : GONE);
        mTvLeft.setText(leftTxtAttr > 0 ? customAttrs.getResources().getText(leftTxtAttr) : customAttrs.getString(R
                .styleable.BKToolbar_leftTxt));
        layout.addView(mTvLeft);

        /*标题*/
        mTvTitle = new TextView(context);
        mTvTitle.setLayoutParams(titleParams);
        mTvTitle.setTextColor(Color.rgb(255, 255, 255));
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        mTvTitle.setPadding(dp2px(context, 45), dp2px(context, 0), dp2px(context, 45), dp2px(context, 0));
        mTvTitle.setSingleLine();
        mTvTitle.setGravity(Gravity.CENTER);
        mTvTitle.setText(titleTxtAttr > 0 ? customAttrs.getResources().getText(titleTxtAttr) : customAttrs.getString(R
                .styleable.BKToolbar_titleTxt));
        layout.addView(mTvTitle);

        /*右按钮*/
        mBtnRight = new ImageView(context);
        mBtnRight.setPadding(dp2px(context, 1), dp2px(context, 2), dp2px(context, 15), dp2px(context, 2));
        mBtnRight.setLayoutParams(rightImgParams);
        mBtnRight.setScaleType(ImageView.ScaleType.CENTER);
        if (rightImgAttr > 0) {
            mBtnRight.setImageResource(rightImgAttr);
        }
        mBtnRight.setVisibility(showRightImgAttr ? VISIBLE : GONE);
        layout.addView(mBtnRight);

        /*右侧文本*/
        mTvRight = new TextView(context);
        mTvRight.setLayoutParams(rightTvParams);
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mTvRight.setGravity(Gravity.CENTER_VERTICAL);
        mTvRight.setVisibility(showRightTvAttr ? VISIBLE : GONE);
        mTvRight.setText(rightTxtAttr > 0 ? customAttrs.getResources().getText(rightTxtAttr) : customAttrs.getString(R
                .styleable.BKToolbar_rightTxt));
        layout.addView(mTvRight);

        addView(layout);

        /*底边线*/
        View line = new View(context);
        line.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        line.setBackgroundColor(Color.rgb(204, 204, 204));

        addView(line);

        customAttrs.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, dp2px(mContext, 48));
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
