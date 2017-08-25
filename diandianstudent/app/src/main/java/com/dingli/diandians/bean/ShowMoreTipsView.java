package com.dingli.diandians.bean;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dingli.diandians.R;


/**
 * 更多提示引导View
 */
public class ShowMoreTipsView extends BaseTipsView {
    private static final String TAG = ShowMoreTipsView.class.getSimpleName();
    private static final String UNIQUE_KEY = TAG;
//    private ImageView mImgClose;
//    private Button mBtnSure;
//    private ImageView mImgArrow;

    public ShowMoreTipsView(Context context) {
        super(context);
        init();
    }

    public ShowMoreTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowMoreTipsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public String getUniquekey() {
        return UNIQUE_KEY;
    }

    private void init() {
        initLayoutParams();
        addStatusBarView();
        addContentView();
    }

    private void initLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams. MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setOrientation(VERTICAL);
        setLayoutParams(lp);
    }
    private void addContentView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_show_more_tips, this, false);
         ImageView ivdakaji=(ImageView) view.findViewById(R.id.ivdakaji);
//        mImgClose = (ImageView) view.findViewById(R.id.img_close);
//        mBtnSure = (Button) view.findViewById(R.id.btn_sure);
//        mImgArrow = (ImageView) view.findViewById(R.id.img_arrow);
//        changeImageArrowMargin();
//        mImgClose.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnCloseListener != null) {
//                    mOnCloseListener.onClose(ShowMoreTipsView.this);
//                }
//            }
//        });
        ivdakaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSureListener != null) {
                    mOnSureListener.onSure(ShowMoreTipsView.this);
                }
            }
        });
        addView(view);
    }

    /**
     * 改变ImgArrow的margin
     */
//    private void changeImageArrowMargin() {
//        LayoutParams lp = (LayoutParams) mImgArrow.getLayoutParams();
//        DisplayMetrics dm =getResources().getDisplayMetrics();
//        int screen = dm.widthPixels;
//        int moreViewWidth = screen / 4;
//        lp.rightMargin = moreViewWidth / 2 + 10;
//        mImgArrow.setLayoutParams(lp);
//    }

}
