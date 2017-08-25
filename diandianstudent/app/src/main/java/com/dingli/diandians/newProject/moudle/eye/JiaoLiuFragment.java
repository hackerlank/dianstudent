package com.dingli.diandians.newProject.moudle.eye;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.dingli.diandians.R;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.newProject.base.fragment.BaseFragment;
import com.dingli.diandians.newProject.moudle.eye.presenter.CommentPresenter;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.ICommentView;
import com.dingli.diandians.newProject.moudle.eye.protocol.CommentProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.MessageCommentListProtocl;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.utils.UIUtil;
import com.dingli.diandians.newProject.view.BottomRecyclerView;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.view.TopRecyclerView;
import com.dingli.diandians.view.PtrClassicRefreshView;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by lwq on 2017/6/5.
 */

public class JiaoLiuFragment extends BaseFragment implements ICommentView,View.OnClickListener{
    @BindView(R.id.recyclerViewDiscussion)
    TopRecyclerView recyclerViewDiscussion;
    @BindView(R.id.ptrClassicRefreshView)
    PtrClassicRefreshView ptrClassicRefreshView;
    @BindView(R.id.editTextDiscussion)
    EditText editTextDiscussion;
    @BindView(R.id.tvSend)
    TextView tvSend;
    private String videoId="",uerName;
    private CommentPresenter commentPresenter;
    private LinkedList<CommentProtocol> discussionList = new LinkedList<CommentProtocol>();
    private  DiscussionRecycleAdapter discussionRecycleAdapter;
    private int pager=1,pagerZise=10;
    private boolean isLoding; //    是不是需要加载更多
    public static JiaoLiuFragment newInstance( Bundle bundle) {
        JiaoLiuFragment fragment = new JiaoLiuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int layoutId() {
        return R.layout.fragment_jiaoliu;
    }

    @Override
    protected void initView() {
        if (ptrClassicRefreshView != null) {
            ptrClassicRefreshView.setResistance(1.0f);//粘度
            ptrClassicRefreshView.setRatioOfHeaderHeightToRefresh(1.2f);
            ptrClassicRefreshView.setDurationToClose(200);
            ptrClassicRefreshView.setDurationToCloseHeader(300);
            ptrClassicRefreshView.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return isLoding;
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    pager++;
                    commentPresenter.ueryMessageCommentList(pager+"",pagerZise+"",videoId);
                }
            });

        }
    }

    @Override
    protected void getLoadView(LoadDataView mLoadView) {

    }

    @Override
    protected void initData() {
        videoId=getArguments().getString("videoId");
        uerName=getArguments().getString("uerName");
        recyclerViewDiscussion.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        discussionRecycleAdapter=new DiscussionRecycleAdapter(getActivity(), getActivity().getWindowManager().getDefaultDisplay().getWidth());
        recyclerViewDiscussion.setAdapter(discussionRecycleAdapter);

        commentPresenter.ueryMessageCommentList(pager+"",pagerZise+"",videoId);
    }

    @Override
    protected void initPresenter() {
        commentPresenter=new CommentPresenter(this);
    }

    @OnClick(R.id.tvSend)
    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(editTextDiscussion.getText())){
            String value=editTextDiscussion.getText().toString();

            Message message=new Message();
            message.text=value;
            message.videoId=videoId;
            commentPresenter.sendMessageComment(new Gson().toJson(message));
        }
    }

    @Override
    public void getCommentListSuccess(MessageCommentListProtocl messageCommentListProtocl) {
        if (null!=ptrClassicRefreshView) {
            ptrClassicRefreshView.refreshComplete();
        }
        if(pager==1){
            discussionList.clear();
        }
        try{
            if(null!=messageCommentListProtocl&&messageCommentListProtocl.data.size()>0){
                List<CommentProtocol> listData=messageCommentListProtocl.data;
                Collections.reverse(listData);
                discussionList.addAll(0,listData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        isLoding=!(pager== messageCommentListProtocl.pageCount);
        discussionRecycleAdapter.setData(false,discussionList);
        if(pager==1){
            if(discussionList.size()>0){
                recyclerViewDiscussion.smoothScrollToPosition(discussionRecycleAdapter.getItemCount()-1);
            }
        }
        editTextDiscussion.setText("");
        UIUtil.hideSoftInput(getActivity(),tvSend);
    }

    @Override
    public void getCommentListFailure(String message) {
        if (null!=ptrClassicRefreshView) { ptrClassicRefreshView.refreshComplete();}
        ToastUtils.showShort(getActivity(),message);
    }

    @Override
    public void sendCommentSuccess(String message) {
        pager=1;
        commentPresenter.ueryMessageCommentList(pager+"",pagerZise+"",videoId);
    }

    @Override
    public void sendCommentFailure(String message) {
    }

    @Override
    public void onLoginInvalid(String message) {
        if (null!=ptrClassicRefreshView) { ptrClassicRefreshView.refreshComplete();}
    }

    @Override
    public void onNetworkFailure(String message) {
        if (null!=ptrClassicRefreshView) { ptrClassicRefreshView.refreshComplete();}
        ToastUtils.showShort(getActivity(),message);
    }

    public  class Message{
        public  String text;
        public  String videoId;
    }
}
