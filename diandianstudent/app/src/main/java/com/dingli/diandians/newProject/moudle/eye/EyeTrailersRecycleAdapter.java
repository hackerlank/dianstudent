package com.dingli.diandians.newProject.moudle.eye;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.ViedoProtocol;
import com.dingli.diandians.newProject.utils.TimeUtil;
import com.dingli.diandians.newProject.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq
 */
public class EyeTrailersRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HrdProtocol>  hrdList = new ArrayList<HrdProtocol>();
    private LayoutInflater   inflater;
    //type
    public static final int TYPE_HRD= 0xff02;
    public static final int TYPE_MORE= 0xf03;//加载更多
    public static final int TYPE_NOMORE= 0xff04;//无加载更多
    private boolean isLoadMore=true;
    private int width;
    public EyeTrailersRecycleAdapter(Context context, int width) {
        this.context = context;
        this.width=width;
        inflater = LayoutInflater.from(context);
    }

    public void setData(boolean isLoadMore,List<HrdProtocol>  mhrdList){
        this.isLoadMore=isLoadMore;
        this.hrdList.clear();
        this.hrdList.addAll(mhrdList);
        if(this.hrdList.size()==0){
            this.isLoadMore=false;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HRD:
                return new HrdHolder(inflater.inflate(R.layout.item_yugaolist_hrd, parent, false));
            case TYPE_MORE:
                return new MoreHolder(inflater.inflate(R.layout.view_list_no_connect, parent, false));
            case TYPE_NOMORE:
                return new NOMoreHolder(inflater.inflate(R.layout.reclye_foot, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HrdHolder){
            HrdProtocol hrdProtocol=hrdList.get(position);
            if(null!=hrdProtocol) {
                if ("1".equals(hrdProtocol.subscriptionStatus)) {//  1已预约 2未预约
                    ((HrdHolder) holder).imageYuYue.setVisibility(View.VISIBLE);
                    ((HrdHolder) holder).tvRight.setText("已预约");
                    ((HrdHolder) holder).tvTime.setTextColor(context.getResources().getColor(R.color.message_red));
                }else if("0".equals(hrdProtocol.subscriptionStatus)){
                    ((HrdHolder) holder).imageYuYue.setVisibility(View.GONE);
                    ((HrdHolder) holder).tvRight.setText("开启预约");
                    ((HrdHolder) holder).tvTime.setTextColor(context.getResources().getColor(R.color.text_color_666666));
                }
                if(!TextUtils.isEmpty(hrdProtocol.title)){
                    ((HrdHolder) holder).tvHrdTitle.setText(hrdProtocol.title);
                }else {
                    ((HrdHolder) holder).tvHrdTitle.setText("");
                }
                ((HrdHolder) holder).tvTime.setText("开播："+ TimeUtil.getMillisecondTime(hrdProtocol.publishTime));
                Glide.with(context).load(hrdProtocol.coverPic)
                        .centerCrop()
                        .into( ((HrdHolder) holder).eyeImage);
            }
            ((HrdHolder) holder).tvRight.setOnClickListener(view -> {//预约
                if(null==hrdProtocol){
                    return;
                }
                if(("0").equals(hrdProtocol.subscriptionStatus)){
                    mLiveSubscriptionInterface.doSubscription(position);
                    hrdProtocol.subscriptionStatus="1";
                    ((HrdHolder) holder).imageYuYue.setVisibility(View.VISIBLE);
                    ((HrdHolder) holder).tvRight.setText("已预约");
                    ((HrdHolder) holder).tvTime.setTextColor(context.getResources().getColor(R.color.message_red));

                }else {
                    mLiveSubscriptionInterface.cancelSubscription(position);
                    hrdProtocol.subscriptionStatus="0";
                    ((HrdHolder) holder).imageYuYue.setVisibility(View.GONE);
                    ((HrdHolder) holder).tvRight.setText("开启预约");
                    ((HrdHolder) holder).tvTime.setTextColor(context.getResources().getColor(R.color.text_color_666666));
                }
            });

            ((HrdHolder) holder).eyeImage.setOnClickListener(view -> {//进入预约详情
                if(null==hrdProtocol){
                    return;
                }
                ViedoProtocol viedoProtocol= hrdProtocol.getViedoProtocol();
                if(null==viedoProtocol|| TextUtils.isEmpty(viedoProtocol.vid)){
                    ToastUtils.showShort(context,"视频不存在！");
                    return;
                }
                String vid="e1510bdd3af754efd0d24e83208cacc1_e";
                Intent i=new Intent(context, TrailerDetailActivity.class);
                i.putExtra("vid",hrdProtocol.getViedoProtocol().vid);
                i.putExtra("id",hrdProtocol.id);
                i.putExtra("userId",hrdProtocol.userId);
                i.putExtra("subscriptionStatus",hrdProtocol.subscriptionStatus);
                i.putExtra("coverPic",hrdProtocol.coverPic);
                i.putExtra("childPic",hrdProtocol.childPic);
                i.putExtra("onlineNumber",hrdProtocol.onlineNumber);
                i.putExtra("ptime",hrdProtocol.publishTime);
                i.putExtra("duration",hrdProtocol.getViedoProtocol().duration);
                i.putExtra("uerName",hrdProtocol.name);
                context.startActivity(i);
            });
        }else if(holder instanceof NOMoreHolder){
            if(hrdList.size()>6){
                ((NOMoreHolder) holder).goods_recover_sorry.setVisibility(View.VISIBLE);
            }else {
                ((NOMoreHolder) holder).goods_recover_sorry.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hrdList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position>=hrdList.size()){
            if(isLoadMore){
                return TYPE_MORE;
            }else {
                return TYPE_NOMORE;
            }
        }
        return TYPE_HRD;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type){
                        case TYPE_HRD:
                        case TYPE_MORE:
                        case TYPE_NOMORE:
                            return gridManager.getSpanCount();
                        default:
                            return gridManager.getSpanCount();
                    }
                }
            });
        }
    }

    class HrdHolder extends RecyclerView.ViewHolder {//Hrd视频
        ImageView eyeImage,imageYuYue;
        TextView tvGG, tvTime,tvRight,tvHrdTitle;
        public HrdHolder(View itemView) {
            super(itemView);
            eyeImage = (ImageView) itemView.findViewById(R.id.eyeImage);
            tvGG = (TextView) itemView.findViewById(R.id.tvGG);
            imageYuYue = (ImageView) itemView.findViewById(R.id.imageYuYue);
            tvRight = (TextView) itemView.findViewById(R.id.tvRight);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvHrdTitle= (TextView) itemView.findViewById(R.id.tvHrdTitle);
        }
    }
    private class NOMoreHolder extends RecyclerView.ViewHolder {
        LinearLayout goods_recover_sorry;

        public NOMoreHolder(View itemView) {
            super(itemView);
            goods_recover_sorry = (LinearLayout) itemView.findViewById(R.id.goods_recover_sorry);
        }
    }

    private class MoreHolder extends RecyclerView.ViewHolder {

        public MoreHolder(View itemView) {
            super(itemView);
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int top;
        private int lift;
        private int right;
        public SpacesItemDecoration(int top, int lift, int right) {
            this.right = right;
            this.top = top;
            this.lift = lift;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildPosition(view) == 0)
                outRect.top = top;
            outRect.left = lift;
            outRect.right = right;
        }
    }
    public  void setLiveSubscriptionInterface(LiveSubscriptionInterface liveSubscriptionInterface ){
        mLiveSubscriptionInterface=liveSubscriptionInterface;
    }
    public LiveSubscriptionInterface   mLiveSubscriptionInterface;
    public  interface  LiveSubscriptionInterface{
        public void  cancelSubscription(int position);
        public void   doSubscription(int position);
    }
}
