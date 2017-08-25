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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.ViedoProtocol;
import com.dingli.diandians.newProject.moudle.hrd.HuifangVideoActicity;
import com.dingli.diandians.newProject.moudle.hrd.OnLineVideoActicity;
import com.dingli.diandians.newProject.utils.TimeUtil;
import com.dingli.diandians.newProject.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq
 */
public class EyeListRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HrdProtocol>  hrdList = new ArrayList<HrdProtocol>();
    private LayoutInflater   inflater;
    //type
    public static final int TYPE_HRD_HEAD = 0xff01;//预告文字提示
    public static final int TYPE_HRD= 0xff02;
    public static final int TYPE_MORE= 0xf03;//加载更多
    public static final int TYPE_NOMORE= 0xff04;//无加载更多
    private boolean isLoadMore=true;
    private  String[] images={"http://7xpscc.com1.z0.glb.clouddn.com/862e71e4-55a0-4fe7-b0af-e7eef4df52ae.jpg","http://7xpscc.com1.z0.glb.clouddn.com/f2eac51d-e8ca-489d-aa8d-dbc17df0f6b0.jpg","http://7xpscc.com1.z0.glb.clouddn.com/d1d0e68a-0b04-4633-a576-63687e316c2a.png"};
    private int width;
    private  String  noLiveValue;
    private int     changCount;
    private  TextView _tvValue,_tvRight;
    public EyeListRecycleAdapter(Context context, int width) {
        this.context = context;
        this.width=width;
        inflater = LayoutInflater.from(context);
    }
    public void setData(String  noLiveValue,int changCount){
        this.noLiveValue=noLiveValue;
        this.changCount=changCount;
        notifyDataSetChanged();
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
            case TYPE_HRD_HEAD:
                return new HrdHeadHolder(inflater.inflate(R.layout.item_eyelist_top, parent, false));
            case TYPE_HRD:
                return new HrdHolder(inflater.inflate(R.layout.item_eyelist_hrd, parent, false));
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
        if (holder instanceof HrdHeadHolder){//预告信息
            if(null!=noLiveValue){
                if(changCount==0){
                    ((HrdHeadHolder) holder).tvValue.setText(noLiveValue+"                                "+noLiveValue);
                }else {
                    ((HrdHeadHolder) holder).tvValue.setText(noLiveValue);
                }
            }
            ((HrdHeadHolder) holder).tvRight.setText(changCount+"");
            if(changCount==0){
                ((HrdHeadHolder) holder).linTop.setVisibility(View.GONE);
            }else {
                ((HrdHeadHolder) holder).linTop.setVisibility(View.VISIBLE);
            }
            ((HrdHeadHolder) holder).linTop.setOnClickListener(view -> {
                context.startActivity(new Intent(context,EyeTrailerListActivity.class));
            });
        }  else if(holder instanceof HrdHolder){//职播列表
            HrdProtocol hrdProtocol=hrdList.get(position-1);
            if(null!=hrdProtocol){
                if("0".equals(hrdProtocol.liveStatus)){
                    ((HrdHolder) holder).tvHrdType.setBackgroundResource( R.mipmap.icon_huifang);
                    ((HrdHolder) holder).tvHrdCount.setBackgroundResource( R.mipmap.icon_time);
                    ((HrdHolder) holder).tvHrdType.setText("回放");
                    ((HrdHolder) holder).tvHrdCount.setText(TimeUtil.getLongAgo("",Long.parseLong(hrdProtocol.publishTime)));
                }else {
                    ((HrdHolder) holder).tvHrdType.setBackgroundResource( R.mipmap.icon_live);
                    ((HrdHolder) holder).tvHrdCount.setBackgroundResource( R.mipmap.icon_pepole_count);
                    ((HrdHolder) holder).tvHrdType.setText("");
                    ((HrdHolder) holder).tvHrdCount.setText(hrdProtocol.onlineNumber+"人");
                }
                if(!TextUtils.isEmpty(hrdProtocol.title)){
                    ((HrdHolder) holder).tvHrdTitle.setText(hrdProtocol.title);
                }else {
                    ((HrdHolder) holder).tvHrdTitle.setText("暂无标题");
                }
                Glide.with(context).load(hrdProtocol.coverPic)
                        .centerCrop()
                        .into( ((HrdHolder) holder).eyeImage);
            }
            ((HrdHolder) holder).eyeImage.setOnClickListener(view -> {
                //  String vid="sl8da4jjbx94bf53d252ae2802a6ef4f_s";
                ViedoProtocol viedoProtocol= hrdProtocol.getViedoProtocol();
                if(null==viedoProtocol||TextUtils.isEmpty(viedoProtocol.vid)){
                    ToastUtils.showShort(context,"视频不存在！");
                    return;
                }
                // String vid="sl8da4jjbx94bf53d252ae2802a6ef4f_s";
                String vid="e1510bdd3af754efd0d24e83208cacc1_e";
                if(hrdProtocol.liveStatus.equals("2")){
                    OnLineVideoActicity.intentTo(context, OnLineVideoActicity.PlayMode.portrait, OnLineVideoActicity.PlayType.vid,viedoProtocol.vid,
                            false,hrdProtocol.publishTime,viedoProtocol.duration,hrdProtocol.onlineNumber,hrdProtocol.childPic,hrdProtocol.id+"",hrdProtocol.name);
                }else {
                    HuifangVideoActicity.intentTo(context, HuifangVideoActicity.PlayMode.portrait, HuifangVideoActicity.PlayType.vid, viedoProtocol.vid,
                            false,hrdProtocol.childPic,hrdProtocol.id+"",hrdProtocol.name);
                }
            });
            if(position==hrdList.size()&&!isLoadMore&&hrdList.size()<=6){
                ((HrdHolder) holder).viewLine.setVisibility(View.VISIBLE);
            }else {
                ((HrdHolder) holder).viewLine.setVisibility(View.GONE);
            }
        }else if(holder instanceof  NOMoreHolder){//无更多
            if(hrdList.size()>6){
                (( NOMoreHolder) holder).goods_recover_sorry.setVisibility(View.VISIBLE);
            }else {
                ((NOMoreHolder) holder).goods_recover_sorry.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hrdList.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HRD_HEAD;
        }
//        if(position>=1&&position<=hrdList.size()){
//            return TYPE_HRD;
//        }
        if(position>=hrdList.size()+1){
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
                        case TYPE_HRD_HEAD:
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

    class HrdHeadHolder extends RecyclerView.ViewHolder {//
        TextView tvValue,tvRight;
        RelativeLayout linTop;
        public HrdHeadHolder(View itemView) {
            super(itemView);
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
            tvRight = (TextView) itemView.findViewById(R.id.tvRight);
            linTop = (RelativeLayout) itemView.findViewById(R.id.linTop);
            _tvValue=tvValue;
            _tvRight=tvRight;
        }
    }

    class HrdHolder extends RecyclerView.ViewHolder {//Hrd视频
        ImageView eyeImage;
        TextView tvHrdType,tvHrdCount,tvHrdTitle;
        View viewLine;
        public HrdHolder(View itemView) {
            super(itemView);
            eyeImage = (ImageView) itemView.findViewById(R.id.eyeImage);
            tvHrdType = (TextView) itemView.findViewById(R.id.tvHrdType);
            tvHrdCount = (TextView) itemView.findViewById(R.id.tvHrdCount);
            tvHrdTitle = (TextView) itemView.findViewById(R.id.tvHrdTitle);
            viewLine=   itemView.findViewById(R.id.viewLine);
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
}
