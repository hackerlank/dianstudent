package com.dingli.diandians.newProject.moudle.eye;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.newProject.moudle.eye.protocol.BinnerProtocol;

import java.util.ArrayList;
import java.util.List;

/**
 *Created by lwq
 */

public class TopBannerAdapter extends PagerAdapter {
    private Context                  context;
    private List<BinnerProtocol> eyeBannerProtocols=new ArrayList<>();
    private int                      width;

    public TopBannerAdapter(Context context, List<BinnerProtocol> eyeBannerProtocols, int width) {
        this.context = context;
        this.eyeBannerProtocols = eyeBannerProtocols;
        this.width = width;

    }

    @Override
    public int getCount() {
        return eyeBannerProtocols != null ? eyeBannerProtocols.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topbanner_eye, null);
        ImageView mView = (ImageView) view.findViewById(R.id.eyeImage);

        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width * (290 * 1000 / 750) / 1000;
        mView.setLayoutParams(layoutParams);
//        Glide.with(context).load(eyeBannerProtocols.get(position).iconUrl)
//                .centerCrop()
//                .into(mView);
//        container.addView(view);
        mView.setImageResource(R.mipmap.icon_eye_top);
        mView.setOnClickListener(view1 -> {

        });
        return view;
    }
}
