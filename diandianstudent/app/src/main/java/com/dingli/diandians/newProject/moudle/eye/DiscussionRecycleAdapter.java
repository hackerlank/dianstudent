package com.dingli.diandians.newProject.moudle.eye;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dingli.diandians.R;
import com.dingli.diandians.newProject.moudle.eye.protocol.CommentProtocol;

import java.util.LinkedList;
import java.util.List;
/**
 * Created by lwq
 */
public class DiscussionRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LinkedList<CommentProtocol> discussionList = new LinkedList<CommentProtocol>();
    private LayoutInflater   inflater;
    //typ
    public static final int TYPE_JIAOLIU= 0xff02;
    private int width;
    public DiscussionRecycleAdapter(Context context, int width) {
        this.context = context;
        this.width=width;
        inflater = LayoutInflater.from(context);
    }

    public void setData(boolean isLoadMore,List<CommentProtocol>  discussionList){
        this.discussionList.clear();
        this.discussionList.addAll(discussionList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_JIAOLIU:
                return new CommunionHolder(inflater.inflate(R.layout.item_jiaoliu, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentProtocol commentProtocol =discussionList.get(position);
        ((CommunionHolder) holder).tvName.setText(commentProtocol.name+"：");
        ((CommunionHolder) holder).tvContent.setText(commentProtocol.text+"");

    }

    @Override
    public int getItemCount() {
        return discussionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_JIAOLIU;
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
                        case TYPE_JIAOLIU:
                            return gridManager.getSpanCount();
                        default:
                            return gridManager.getSpanCount();
                    }
                }
            });
        }
    }

    class CommunionHeadHolder extends RecyclerView.ViewHolder {//
        public CommunionHeadHolder(View itemView) {
            super(itemView);
        }
    }
    class CommunionHolder extends RecyclerView.ViewHolder {//Hrd视频
        TextView tvName,tvContent;
        public CommunionHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
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
}
