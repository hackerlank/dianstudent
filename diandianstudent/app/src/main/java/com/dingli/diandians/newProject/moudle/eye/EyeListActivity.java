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
import com.dingli.diandians.newProject.moudle.eye.presenter.view.IEyeView;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.INoLiveView;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollantHollandReportsProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.NoliveProtocol;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.BottomRecyclerView;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.BKToolbar;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;

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
public class EyeListActivity extends BaseActivity  implements IEyeView,INoLiveView, BottomRecyclerView.OnBottomListener{
    @BindView(R.id.tbBKToolbar)
    BKToolbar tbBKToolbar;
    @BindView(R.id.swipeRefreshLayout)
    VerticalSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.eyeListRecyclerView)
    BottomRecyclerView eyeListRecyclerView;
    @BindView(R.id.linLoading)
    LinearLayout linLoading;

    private LoadDataView loadDataView;
    private   EyeListRecycleAdapter eyeListRecycleAdapter;
    private EyePresenter eyePresenter;
    private int page = 1,limit=20;
    private boolean isLoding; //    是不是需要加载更多
    public List<HrdProtocol>   hrdProtocolList=new ArrayList<HrdProtocol>();
    @Override
    protected int layoutId() {
        return R.layout.activity_eye_list;
    }

    @Override
    protected ViewGroup loadDataViewLayout() {
        return linLoading;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        tbBKToolbar.getBtnLeft().setOnClickListener(view -> this.finish());
        eyeListRecyclerView.setOnBottomListener(this);
        DianTool.refresh(swipeRefreshLayout, this);
        swipeRefreshLayout.setOnRefreshListener(() -> {//下来刷新
            page = 1;
            eyePresenter.recentlyNoLive(this);
            getLiveList(page);
        });
    }

    @Override
    protected void initData() {
        eyeListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        eyeListRecycleAdapter=new EyeListRecycleAdapter(this, getWindowManager().getDefaultDisplay().getWidth());
        eyeListRecyclerView.setAdapter(eyeListRecycleAdapter);

        loadDataView.changeStatusView(ViewStatus.START);
        page = 1;
        eyePresenter.recentlyNoLive(this);
        getLiveList(page);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        eyePresenter.recentlyNoLive(this);
//        isLoding=false;
//        if(page!=1){
//            page=1;
//            eyeListRecyclerView.smoothScrollToPosition(0);
//        }
//        getLiveList(page);
    }

    @Override
    protected void getLoadView(LoadDataView loadView) {
        this.loadDataView=loadView;
        loadDataView.setErrorListner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataView.changeStatusView(ViewStatus.START);
                page = 1;
                eyePresenter.recentlyNoLive(EyeListActivity.this);
                getLiveList(page);
            }
        });
    }

    @Override
    protected void initPresenter() {
        eyePresenter=new EyePresenter(this);
    }
    public void getLiveList(int _page){//获取职播列表
        HashMap<String,String> map=new HashMap<>();
        map.put("pageNumber",page+"");
        map.put("pageSize",limit+"");
        map.put("status",3+"");
        eyePresenter.getLiveList(map);
    }

    @Override
    public void OnBottom() {//加载更多
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
        eyeListRecycleAdapter.setData(isLoding,hrdProtocolList);
        if(hrdProtocolList.size()==0){
            //     loadDataView.isFirstLoad=true;
//            loadDataView.changeStatusView(ViewStatus.EMPTY);
//            loadDataView.getLoadingEmptyTv().setVisibility(View.GONE);
        }
    }

    @Override
    public void getHrdListFailure(String message) {
        loadDataView.changeStatusView(ViewStatus.FAILURE);
        ToastUtils.showShort(EyeListActivity.this,message);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoginInvalid(String message) {
        loadDataView.changeStatusView(ViewStatus.SUCCESS);
        swipeRefreshLayout.setRefreshing(false);
        //  showLoginOther(message);
    }

    @Override
    public void onNetworkFailure(String message) {
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
        eyePresenter.destroy();
    }

    @Override
    public void getNoLiveInfoSuccess(NoliveProtocol noliveProtocol) {
        String noLiveValue="";
        try{
            if(null!=noliveProtocol.data&&noliveProtocol.data.size()>0){
                for(NoliveProtocol.NoHrdProtocol n:noliveProtocol.data){
                    noLiveValue=noLiveValue+n.title+"                              ";
                }
            }
        }catch (Exception e){
            noLiveValue="";
            e.printStackTrace();
        }
        eyeListRecycleAdapter.setData(noLiveValue,noliveProtocol.count);
    }

    @Override
    public void getNoLiveInfoFailure(String message) {
        ToastUtils.showShort(EyeListActivity.this,message);
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.HRDLISTREFSH)
    public void onHrdlistRerfsh (Object o) {
        eyePresenter.recentlyNoLive(this);
        if(page!=1){
            page=1;
            eyeListRecyclerView.smoothScrollToPosition(0);
        }
        getLiveList(page);
    }
}
