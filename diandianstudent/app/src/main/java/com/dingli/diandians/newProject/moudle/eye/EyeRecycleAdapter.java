package com.dingli.diandians.newProject.moudle.eye;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.firstpage.WebViewActivity;
import com.dingli.diandians.firstpage.WebViewHyActivity;
import com.dingli.diandians.firstpage.WebViewsActivity;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.ViedoProtocol;
import com.dingli.diandians.newProject.moudle.hrd.HuifangVideoActicity;
import com.dingli.diandians.newProject.moudle.hrd.OnLineVideoActicity;
import com.dingli.diandians.newProject.utils.TimeUtil;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.KeywordsFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lwq
 */
public class EyeRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String[] keywords = { "产品经理", "人力资源专员", "程序员", "工程师", "行政人员",
            "公关" };
    private Context context;
    private List<HrdProtocol>  hrdList = new ArrayList<HrdProtocol>();
    private List<ArticleProtocol> articleProtocolList= new ArrayList<ArticleProtocol>();
    private LayoutInflater   inflater;
    public List<HollandJoinMBTIProtocol.Job> mJobSet=new ArrayList<HollandJoinMBTIProtocol.Job>();
    //type
    public static final int TYPE_SLIDER_TOP = 0xff01;//轮播图
    public static final int TYPE_HRD= 0xff04;
    public static final int TYPE_MIND_HEAD= 0xff13;
    public static final int TYPE_MIND= 0xff07;
    public static final int TYPE_TOOL= 0xff08;
    private int width;
    public EyeRecycleAdapter(Context context, int width) {
        this.context = context;
        this.width=width;
        inflater = LayoutInflater.from(context);
    }
    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }
    }

    public void setHLDMBTIISTest(){
        notifyDataSetChanged();
    }
    public void setHollandJoinMBTIValue(List<HollandJoinMBTIProtocol.Job> jobSet){
        mJobSet=jobSet;
        notifyDataSetChanged();
    }
    public void setData( List<HrdProtocol>  hrdList ,List<ArticleProtocol> articleProtocolList){
        this.hrdList.clear();
        this.articleProtocolList.clear();

        this.hrdList.addAll(hrdList);
        this.articleProtocolList.addAll(articleProtocolList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_SLIDER_TOP:
                return new SliderHolder(inflater.inflate(R.layout.item_eye_top, parent, false));
            case TYPE_HRD:
                return new HrdHolder(inflater.inflate(R.layout.item_eye, parent, false));
            case TYPE_MIND_HEAD:
                return new MindHeadHolder(inflater.inflate(R.layout.item_mind_head, parent, false));
            case TYPE_MIND:
                return new MindHolder(inflater.inflate(R.layout.item_mind, parent, false));
            case TYPE_TOOL:
                return new ToolHolder(inflater.inflate(R.layout.item_tool, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SliderHolder){
            ViewGroup.LayoutParams layoutParams = ((SliderHolder) holder).imageViewTop.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width * (272 * 1000 / 720) / 1000;
            ((SliderHolder) holder).imageViewTop.setLayoutParams(layoutParams);
            //测评职位
            if(null!=mJobSet&&mJobSet.size()>0){
                ((SliderHolder) holder).tvValue.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.tjzw),mJobSet.get(0).name,mJobSet.get(0).name)));
                switch (mJobSet.size()){
                    case 1:
                        ((SliderHolder) holder).tvJobOne.setText(mJobSet.get(0).name);
                        ((SliderHolder) holder).tvJobTwo.setText("");
                        ((SliderHolder) holder).tvJobThree.setText("");
                        ((SliderHolder) holder).tvJobFour.setText("");
                        ((SliderHolder) holder).tvJobFive.setText("");
                        ((SliderHolder) holder).tvJobSix.setText("");
                        break;
                    case 2:
                        ((SliderHolder) holder).tvJobOne.setText(mJobSet.get(0).name);
                        ((SliderHolder) holder).tvJobTwo.setText(mJobSet.get(1).name);
                        ((SliderHolder) holder).tvJobThree.setText("");
                        ((SliderHolder) holder).tvJobFour.setText("");
                        ((SliderHolder) holder).tvJobFive.setText("");
                        ((SliderHolder) holder).tvJobSix.setText("");
                        break;
                    case 3:
                        ((SliderHolder) holder).tvJobOne.setText(mJobSet.get(0).name);
                        ((SliderHolder) holder).tvJobTwo.setText(mJobSet.get(1).name);
                        ((SliderHolder) holder).tvJobThree.setText(mJobSet.get(2).name);
                        ((SliderHolder) holder).tvJobFour.setText("");
                        ((SliderHolder) holder).tvJobFive.setText("");
                        ((SliderHolder) holder).tvJobSix.setText("");
                        break;
                    case 4:
                        ((SliderHolder) holder).tvJobOne.setText(mJobSet.get(0).name);
                        ((SliderHolder) holder).tvJobTwo.setText(mJobSet.get(1).name);
                        ((SliderHolder) holder).tvJobThree.setText(mJobSet.get(2).name);
                        ((SliderHolder) holder).tvJobFour.setText(mJobSet.get(3).name);
                        ((SliderHolder) holder).tvJobFive.setText("");
                        ((SliderHolder) holder).tvJobSix.setText("");
                        break;
                    case 5:
                        ((SliderHolder) holder).tvJobOne.setText(mJobSet.get(0).name);
                        ((SliderHolder) holder).tvJobTwo.setText(mJobSet.get(1).name);
                        ((SliderHolder) holder).tvJobThree.setText(mJobSet.get(2).name);
                        ((SliderHolder) holder).tvJobFour.setText(mJobSet.get(3).name);
                        ((SliderHolder) holder).tvJobFive.setText(mJobSet.get(4).name);
                        ((SliderHolder) holder).tvJobSix.setText("");
                        break;
                    case 6:
                        ((SliderHolder) holder).tvJobOne.setText(mJobSet.get(0).name);
                        ((SliderHolder) holder).tvJobTwo.setText(mJobSet.get(1).name);
                        ((SliderHolder) holder).tvJobThree.setText(mJobSet.get(2).name);
                        ((SliderHolder) holder).tvJobFour.setText(mJobSet.get(3).name);
                        ((SliderHolder) holder).tvJobFive.setText(mJobSet.get(4).name);
                        ((SliderHolder) holder).tvJobSix.setText(mJobSet.get(5).name);
                        break;
                }
            }else{
                ((SliderHolder) holder).tvValue.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.tjzw),"?","?")));
                ((SliderHolder) holder).tvJobOne.setText(keywords[0]);
                ((SliderHolder) holder).tvJobTwo.setText(keywords[1]);
                ((SliderHolder) holder).tvJobThree.setText(keywords[2]);
                ((SliderHolder) holder).tvJobFour.setText(keywords[3]);
                ((SliderHolder) holder).tvJobFive.setText(keywords[4]);
                ((SliderHolder) holder).tvJobSix.setText(keywords[5]);
            }

            //测评按钮选中与否
            if(BKConstant.HLD_TEST_COUNT>0){
                ((SliderHolder) holder).imageHLD.setImageResource(R.mipmap.icon_xq_selected);
            }else {
                ((SliderHolder) holder).imageHLD.setImageResource(R.mipmap.icon_xq_unselect);
            }
            if(BKConstant.MBTI_TEST_COUNT>0){
                ((SliderHolder) holder).imageViewMBT.setImageResource(R.mipmap.icon_mbt_selected);
            }else {
                ((SliderHolder) holder).imageViewMBT.setImageResource(R.mipmap.icon_mbt_unselect);
            }


            ((SliderHolder) holder).relativeLayoutMore.setOnClickListener(view -> {
                context.startActivity(new Intent(context,EyeListActivity.class));
            });
            ((SliderHolder) holder).imageHLD.setOnClickListener(view -> {//霍兰德测试
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                }else {
                    intent.setClass(context, WebViewHyActivity.class);
                    intent.putExtra("title","霍兰德职业兴趣测试");
                    intent.putExtra("url", "/mobileui/?id=1");
                }
                context.startActivity(intent);
            });
            ((SliderHolder) holder).imageViewMBT.setOnClickListener(view -> {//MBT测试
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                }else {
                    intent.setClass(context, WebViewHyActivity.class);
                    intent.putExtra("title","MBTI职业性格测试");
                    intent.putExtra("url", "/mobileui/?id=2");
                }
                context.startActivity(intent);

            });

        }  else if(holder instanceof HrdHolder){
            HrdProtocol hrdProtocol=hrdList.get(position-1);
            if(null!=hrdProtocol){
                if("0".equals(hrdProtocol.liveStatus)){// 0 回放 1 预告 2  直播
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
            if(position==hrdList.size()){
                ((HrdHolder) holder).viewLine.setVisibility(View.GONE);
            }else {
                ((HrdHolder) holder).viewLine.setVisibility(View.VISIBLE);
            }
            ((HrdHolder) holder).eyeImage.setOnClickListener(view -> {
                ViedoProtocol viedoProtocol= hrdProtocol.getViedoProtocol();
                if(null==viedoProtocol||TextUtils.isEmpty(viedoProtocol.vid)){
                    ToastUtils.showShort(context,"视频不存在！");
                    return;
                }
//                String vid="e1510bdd3af754efd0d24e83208cacc1_e";
                if(hrdProtocol.liveStatus.equals("2")){
                    OnLineVideoActicity.intentTo(context, OnLineVideoActicity.PlayMode.portrait, OnLineVideoActicity.PlayType.vid, viedoProtocol.vid,
                            false,hrdProtocol.publishTime,viedoProtocol.duration,hrdProtocol.onlineNumber,hrdProtocol.childPic,hrdProtocol.id+"",hrdProtocol.name);
                }else {
                    HuifangVideoActicity.intentTo(context, HuifangVideoActicity.PlayMode.portrait, HuifangVideoActicity.PlayType.vid, viedoProtocol.vid,
                            false,hrdProtocol.childPic,hrdProtocol.id+"",hrdProtocol.name);
                }

            });
        }else if(holder instanceof MindHeadHolder){
            ((MindHeadHolder) holder).relativeLayout.setOnClickListener(view -> {
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                }else {
                    intent.setClass(context, WebViewsActivity.class);
                    intent.putExtra("url", "/mobileui/allArticle");
                }
                context.startActivity(intent);
            });
        } else if(holder instanceof MindHolder){
            ArticleProtocol articleProtocol=articleProtocolList.get(position-hrdList.size()-2);
            if(null!=articleProtocol){
                Glide.with(context).load(articleProtocol.picUrl)
                        .centerCrop()
                        .into(((MindHolder) holder).mindImage);
                if(!TextUtils.isEmpty(articleProtocol.title)){
                    ((MindHolder) holder).tvTitle.setText(articleProtocol.title);
                }else {
                    ((MindHolder) holder).tvTitle.setText("");
                }
                if(!TextUtils.isEmpty(articleProtocol.hitCount)){
                    ((MindHolder) holder).tvChakan.setText(articleProtocol.hitCount);
                }else {
                    ((MindHolder) holder).tvChakan.setText("0");
                }
                if(!TextUtils.isEmpty(articleProtocol.praiseCount)){
                    ((MindHolder) holder).tvDianZan.setText(articleProtocol.praiseCount);
                }else {
                    ((MindHolder) holder).tvDianZan.setText("0");
                }
            }
            holder.itemView.setOnClickListener(view -> {
                if(null==articleProtocol||TextUtils.isEmpty(articleProtocol.id)){
                    return;
                }
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                Bundle bundle=new Bundle();
                intent.setClass(context, WebViewActivity.class);
                bundle.putString("url", "/mobileui/article?"+articleProtocol.id);
                bundle.putString("id", articleProtocol.id);
                bundle.putString("list","listv2");
                intent.putExtras(bundle);
                context.startActivity(intent);
            });

        } else if(holder instanceof ToolHolder){
            ((ToolHolder) holder).imageViewZYNL.setOnClickListener(view -> {//能力测试
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                }else {
                    intent.setClass(context, WebViewHyActivity.class);
                    intent.putExtra("title","职业能力测试");
                    intent.putExtra("url", "/mobileui/?id=4");
                }
                context.startActivity(intent);
            });
            ((ToolHolder) holder).imageViewJCPHD.setOnClickListener(view -> {//决策平衡单
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                }else {
                    intent.setClass(context, WebViewHyActivity.class);
                    intent.putExtra("title","决策平衡单");
                    intent.putExtra("url", "/mobileui/?id=5");
                }
                context.startActivity(intent);
            });
            ((ToolHolder) holder).imageViewJZG.setOnClickListener(view -> {//价值观测试
                Intent intent=new Intent();
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(context, LoginActivity.class);
                }else {
                    intent.setClass(context, WebViewHyActivity.class);
                    intent.putExtra("title","价值观测试");
                    intent.putExtra("url", "/mobileui/?id=3");
                }
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return hrdList.size()+articleProtocolList.size()+3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_SLIDER_TOP;
        }
        if(position>=1&&position<=hrdList.size()){
            return TYPE_HRD;
        }
        if(position==hrdList.size()+1){
            return TYPE_MIND_HEAD;
        }
        if(position>hrdList.size()+1&&position<=hrdList.size()+articleProtocolList.size()+1){
            return TYPE_MIND;
        }
        if(position==hrdList.size()+articleProtocolList.size()+2){
            return TYPE_TOOL;
        }
        return TYPE_TOOL;
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
                        case TYPE_SLIDER_TOP:
                        case TYPE_HRD:
                        case TYPE_MIND_HEAD:
                        case TYPE_MIND:
                        case TYPE_TOOL:
                            return gridManager.getSpanCount();
                        default:
                            return gridManager.getSpanCount();
                    }
                }
            });
        }
    }

    public class SliderHolder extends RecyclerView.ViewHolder {//首页binner
        ImageView imageViewTop,imageHLD,imageViewMBT,imageviewLD;
        TextView tvMore;
        TextView tvValue;
        TextView tvJobOne,tvJobTwo,tvJobThree,tvJobFour,tvJobFive,tvJobSix;
        RelativeLayout relativeLayoutMore;
        KeywordsFlow keywordsFlow;
        LinearLayout linBackGround;
        public SliderHolder(View itemView) {
            super(itemView);
            imageViewTop = (ImageView) itemView.findViewById(R.id.imageViewTop);
            tvMore= (TextView) itemView.findViewById(R.id.tvMore);
            tvValue=(TextView) itemView.findViewById(R.id.tvValue);
            relativeLayoutMore= (RelativeLayout) itemView.findViewById(R.id.relativeLayoutMore);
            imageHLD= (ImageView) itemView.findViewById(R.id.imageHLD);
            imageViewMBT= (ImageView) itemView.findViewById(R.id.imageViewMBT);
            tvJobOne= (TextView) itemView.findViewById(R.id.tvJobOne);
            tvJobTwo= (TextView) itemView.findViewById(R.id.tvJobTwo);
            tvJobThree= (TextView) itemView.findViewById(R.id.tvJobThree);
            tvJobFour= (TextView) itemView.findViewById(R.id.tvJobFour);
            tvJobFive= (TextView) itemView.findViewById(R.id.tvJobFive);
            tvJobSix= (TextView) itemView.findViewById(R.id.tvJobSix);
            imageviewLD= (ImageView) itemView.findViewById(R.id.imageviewLD);

            ViewGroup.LayoutParams layoutParams1 =  imageviewLD.getLayoutParams();
            layoutParams1.width=width;
            layoutParams1.height = width * (446*1000/720)/1000;
            imageviewLD.setLayoutParams(layoutParams1);
            Glide.with(context).load(R.mipmap.icon_ld_backgroud).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate() //去掉显示动画
                    .into(imageviewLD);
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
            viewLine= itemView.findViewById(R.id.viewLine);
        }
    }
    class MindHeadHolder extends RecyclerView.ViewHolder {//就业心理头部
        TextView  tvMore;
        RelativeLayout relativeLayout;
        public MindHeadHolder(View itemView) {
            super(itemView);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.relativeLayoutMind);
        }
    }
    class MindHolder extends RecyclerView.ViewHolder {//就业心理
        ImageView  mindImage;
        TextView  tvTitle;
        TextView tvChakan;
        TextView tvDianZan;
        View viewLine;
        public MindHolder(View itemView) {
            super(itemView);
            mindImage = (ImageView) itemView.findViewById(R.id.mindImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvChakan = (TextView) itemView.findViewById(R.id.tvChakan);
            tvDianZan = (TextView) itemView.findViewById(R.id.tvDianZan);
            viewLine=  itemView.findViewById(R.id.viewLine);
        }
    }

    class ToolHolder extends RecyclerView.ViewHolder {//小工具
        private ImageView imageViewJZG;//价值观
        private ImageView imageViewJCPHD ;//决策
        private ImageView imageViewZYNL;
        public ToolHolder(View itemView) {
            super(itemView);
            imageViewJZG = (ImageView) itemView.findViewById(R.id.imageJZG);
            imageViewJCPHD = (ImageView) itemView.findViewById(R.id.imageJCPHD);
            imageViewZYNL = (ImageView) itemView.findViewById(R.id.imageViewZYNL);
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
