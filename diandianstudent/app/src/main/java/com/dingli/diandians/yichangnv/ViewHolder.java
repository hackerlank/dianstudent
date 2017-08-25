package com.dingli.diandians.yichangnv;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IViewHolder;
import com.dingli.diandians.R;
import com.dingli.diandians.view.NoScrollListView;

/**
 * Created by dingliyuangong on 2016/3/25.
 */
public class ViewHolder extends IViewHolder {
    TextView benyue,lvyue,secondzhuangtai,recorddetail,recordriqi,recordjieshu
            ,recordtea,recordweek;
    LinearLayout llsecordre;
    NoScrollListView  lvs;
    private onCancelCollectListener mOnCancelInterface;

    public ViewHolder(View itemView) {
        super(itemView);
        benyue=(TextView) itemView.findViewById(R.id.benyue);
        lvs=(NoScrollListView) itemView.findViewById(R.id.lvs);
        lvyue=(TextView) itemView.findViewById(R.id.lvyue);
    }
    public interface onCancelCollectListener {
        void onRecordListener(String courseId);
    }
    public void setmOnCancelInterface(onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
}
