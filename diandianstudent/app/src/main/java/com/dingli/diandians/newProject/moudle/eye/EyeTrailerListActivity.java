package com.dingli.diandians.newProject.moudle.eye;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.dingli.diandians.R;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.newProject.base.activity.BaseActivity;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.constants.ViewStatus;
import com.dingli.diandians.newProject.moudle.eye.presenter.EyePresenter;
import com.dingli.diandians.newProject.moudle.eye.presenter.LiveSubscriptionPresenter;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.IEyeView;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.ILiveSubscriptionView;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollantHollandReportsProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdProtocol;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.BottomRecyclerView;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.BKToolbar;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * Created by lwq on 2017/6/2.
 */
public class EyeTrailerListActivity extends BaseActivity  implements IEyeView,ILiveSubscriptionView,EyeTrailersRecycleAdapter.LiveSubscriptionInterface, BottomRecyclerView.OnBottomListener{
    @BindView(R.id.tbBKToolbar)
    BKToolbar tbBKToolbar;
    @BindView(R.id.linLoading)
    LinearLayout linLoading;
    @BindView(R.id.swipeRefreshLayout)
    VerticalSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.yugaoListRecyclerView)
    BottomRecyclerView yugaoListRecyclerView;

    private EyeTrailersRecycleAdapter eyeTrailersRecycleAdapter;
    private LoadDataView loadDataView;
    private EyePresenter eyePresenter;
    private LiveSubscriptionPresenter liveSubscriptionPresenter;
    private int page = 1,limit=20;
    private boolean isLoding; //    是不是需要加载更多
    public List<HrdProtocol>   hrdProtocolList=new ArrayList<HrdProtocol>();

    @Override
    protected int layoutId() {
        return R.layout.activity_yugao_list;
    }

    @Override
    protected ViewGroup loadDataViewLayout() {
        return linLoading;
    }

    @Override
    protected void initView() {
        tbBKToolbar.getBtnLeft().setOnClickListener(view -> this.finish());
        yugaoListRecyclerView.setOnBottomListener(this);
        DianTool.refresh(swipeRefreshLayout, this);
        swipeRefreshLayout.setOnRefreshListener(() -> {//下来刷新
            page = 1;
            getLiveList(page);
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        yugaoListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        eyeTrailersRecycleAdapter =new EyeTrailersRecycleAdapter(this, getWindowManager().getDefaultDisplay().getWidth());
        yugaoListRecyclerView.setAdapter(eyeTrailersRecycleAdapter);
        eyeTrailersRecycleAdapter.setLiveSubscriptionInterface(this);

        loadDataView.changeStatusView(ViewStatus.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLoding=false;
        if(page!=1){
            page=1;
            yugaoListRecyclerView.smoothScrollToPosition(0);
        }
        getLiveList(page);
    }
    @Override
    protected void getLoadView(LoadDataView loadView) {
        loadDataView=loadView;
        loadDataView.setErrorListner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataView.changeStatusView(ViewStatus.START);
                page=1;
                getLiveList(page);
            }
        });
    }

    @Override
    protected void initPresenter() {
        eyePresenter=new EyePresenter(this);
        liveSubscriptionPresenter=new LiveSubscriptionPresenter(this);
    }
    public void getLiveList(int _page){//获取职播列表
        HashMap<String,String> map=new HashMap<>();
        map.put("pageNumber",page+"");
        map.put("pageSize",limit+"");
        map.put("status",1+"");
        if(null!=eyePresenter) {
            eyePresenter.getLiveList(map);
        }
    }

    @Override
    public void OnBottom() {
        if (isLoding) {
            page++;
            getLiveList(page);
        }
    }

    @Override
    public void getHrdListSuccess(HrdListProtocol hrdListProtocol) {
        loadDataView.changeStatusView(ViewStatus.SUCCESS);
        swipeRefreshLayout.setRefreshing(false);
        if(page==1){
            hrdProtocolList.clear();
        }
        if(!hrdListProtocol.data.isEmpty()){
            hrdProtocolList.addAll(hrdListProtocol.data);
        }
        isLoding=!(page== hrdListProtocol.pageCount);
        eyeTrailersRecycleAdapter.setData(isLoding,hrdProtocolList);
        if(hrdProtocolList.size()==0){
            loadDataView.isFirstLoad=true;
            loadDataView.getLoadingEmptyBtn().setVisibility(View.GONE);
            loadDataView.getLoadingEmptyTv().setVisibility(View.GONE);
            loadDataView.getLoadingEmptyTvTop().setText("暂无预告");
            loadDataView.getLoadingEmptyImageView().setImageResource(R.mipmap.icon_empty_data);
            loadDataView.changeStatusView(ViewStatus.EMPTY);
        }

    }

    @Override
    public void getHrdListFailure(String message) {
        loadDataView.changeStatusView(ViewStatus.FAILURE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoginInvalid(String message) {
        isSubscriptionIng=true;
        loadDataView.changeStatusView(ViewStatus.SUCCESS);
        swipeRefreshLayout.setRefreshing(false);
        showLoginOther(message);
    }

    @Override
    public void onNetworkFailure(String message) {
        isSubscriptionIng=true;
        loadDataView.changeStatusView(ViewStatus.NOTNETWORK);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getArticleListSuccess(List<ArticleProtocol> articleProtocolList) {}
    @Override
    public void getArticleListFailure(String message) {}

    @Override
    public void getHollandJoinMBTISuccess(HollandJoinMBTIProtocol hollandJoinMBTIProtocol) {
    }
    @Override
    public void getHollandJoinMBTIFailure(String message) {
    }
    @Override
    public void getHollandReportsSuccess(HollantHollandReportsProtocol hollantHollandReportsProtocol) {
    }
    @Override
    public void getHollandReportsFailure(String message) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(null!=eyePresenter){
            eyePresenter.destroy();
        }
        if(null!=liveSubscriptionPresenter) {
            liveSubscriptionPresenter.destroy();
        }
    }

    public  boolean isSubscriptionIng=true;
    @Override
    public void cancelSubscription(int position) {//取消订阅
        Video video=new Video();
        video.userId=hrdProtocolList.get(position).userId;
        video.videoId=hrdProtocolList.get(position).id;
        if(isSubscriptionIng){
            isSubscriptionIng=false;
            if(null!=liveSubscriptionPresenter){
                liveSubscriptionPresenter.cancelLiveSubscription(new Gson().toJson(video));
            }
        }

    }

    @Override
    public void doSubscription(int position) {//订阅
        Video video=new Video();
        video.userId=hrdProtocolList.get(position).userId;
        video.videoId=hrdProtocolList.get(position).id;
        if(isSubscriptionIng) {
            isSubscriptionIng=false;
            if(null!=liveSubscriptionPresenter) {
                liveSubscriptionPresenter.saveLiveSubscription(new Gson().toJson(video));
            }
        }
    }

    @Override
    public void doSubscriptionSuccess(String message) {
        isSubscriptionIng=true;
        //   ToastUtils.showShort(this,"预约成功");
    }

    @Override
    public void doSubscriptionFailure(String message) {
        isSubscriptionIng=true;
        //  ToastUtils.showShort(this,"预约失败");

    }

    @Override
    public void cancleSubscriptionSuccess(String message) {
        isSubscriptionIng=true;
        // ToastUtils.showShort(this,"取消预约成功");
    }

    @Override
    public void cancleSubscriptionFailure(String message) {
        isSubscriptionIng=true;
        // ToastUtils.showShort(this,"取消预约失败");
    }


    public  class Video{
        public  String userId;
        public  int videoId;
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.TRAILERSCLOSE)
    public void onEventTrailersClose (Object o) {
        finish();
    }
}
