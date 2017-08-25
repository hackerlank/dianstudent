package com.dingli.diandians.newProject.moudle.eye;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.dingli.diandians.R;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.newProject.base.fragment.BaseFragment;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.moudle.eye.presenter.EyePresenter;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.IEyeView;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollantHollandReportsProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdProtocol;
import com.dingli.diandians.newProject.utils.ToastUtils;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * Created by lwq on 2017/5/31.
 */

public class EyeFragment extends BaseFragment implements IEyeView {
    @BindView(R.id.eyeRecyclerView)
    RecyclerView eyeRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    VerticalSwipeRefreshLayout swipeRefreshLayout;

    private EyeRecycleAdapter eyeRecycleAdapter;
    private EyePresenter eyePresenter;
    private List<HrdProtocol> hrdProtocolList=new ArrayList<HrdProtocol>();
    private   List<ArticleProtocol> mArticleProtocolList=new ArrayList<ArticleProtocol>();
    private LoadDataView loadDataView;
    public static EyeFragment newInstance(boolean showBack) {
        EyeFragment fragment = new EyeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int layoutId() {
        return  R.layout.fragment_eye;
    }

    @Override
    protected void initView() {
        DianTool.refresh(swipeRefreshLayout, getActivity());
        swipeRefreshLayout.setOnRefreshListener(() -> {//下来刷新
            getLiveList();
            eyePresenter.getHollandJoinMBTI();
            eyePresenter.getHollandReports("1","2");
        });
    }
    @Override
    protected void getLoadView(LoadDataView mLoadView) {
        loadDataView=mLoadView;
    }
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        eyeRecyclerView.setLayoutManager(new GridLayoutManager(eyeRecyclerView.getContext(), 2, GridLayoutManager.VERTICAL, false));
        eyeRecycleAdapter=new EyeRecycleAdapter(getActivity(),getActivity().getWindowManager().getDefaultDisplay().getWidth());
        eyeRecyclerView.setAdapter(eyeRecycleAdapter);
    }

    @Override
    protected void initPresenter() {
        eyePresenter=new EyePresenter(this);
    }

    public void getLiveList(){//获取职播列表
        HashMap<String,String> map=new HashMap<>();
        map.put("pageNumber",0+"");
        map.put("pageSize",3+"");
        map.put("status",3+"");
        if(null!=eyePresenter){
            eyePresenter.getLiveList(map);
        }
    }


    @Override
    public void getHrdListSuccess(HrdListProtocol hrdListProtocol) {//职播列表的成功回调
        swipeRefreshLayout.setRefreshing(false);
        hrdProtocolList.clear();
       if(!hrdListProtocol.data.isEmpty()){
            hrdProtocolList.addAll(hrdListProtocol.data);
        }
        if(null!=eyePresenter) {
            eyePresenter.getArticleList();
        }
    }

    @Override
    public void getHrdListFailure(String message) {//职播列表的失败回调
        swipeRefreshLayout.setRefreshing(false);
        ToastUtils.showShort(getActivity(),message);
        eyePresenter.getArticleList();
    }

    @Override
    public void getArticleListSuccess(List<ArticleProtocol> articleProtocolList) {//就业心理列表的成功回调
        swipeRefreshLayout.setRefreshing(false);
        if(null!=articleProtocolList&&!articleProtocolList.isEmpty()){
            mArticleProtocolList.clear();
            mArticleProtocolList.addAll(articleProtocolList);
        }
        eyeRecycleAdapter.setData(hrdProtocolList,mArticleProtocolList);
    }

    @Override
    public void getArticleListFailure(String message) {////就业心理列表的失败回调
        swipeRefreshLayout.setRefreshing(false);
        ToastUtils.showShort(getActivity(),message);
        eyeRecycleAdapter.setData(hrdProtocolList,mArticleProtocolList);
    }

    @Override
    public void getHollandJoinMBTISuccess(HollandJoinMBTIProtocol hollandJoinMBTIProtocol) {
        if(null!=hollandJoinMBTIProtocol.jobSet){
            eyeRecycleAdapter.setHollandJoinMBTIValue(hollandJoinMBTIProtocol.jobSet);
        }
    }

    @Override
    public void getHollandJoinMBTIFailure(String message) {

    }

    @Override
    public void getHollandReportsSuccess(HollantHollandReportsProtocol hollantHollandReportsProtocol) {
        eyeRecycleAdapter.setHLDMBTIISTest();
    }

    @Override
    public void getHollandReportsFailure(String message) {

    }

    @Override
    public void onLoginInvalid(String message) {//过期
        swipeRefreshLayout.setRefreshing(false);
      //  showLoginOther(message);
    }

    @Override
    public void onNetworkFailure(String message) {//无网络
        swipeRefreshLayout.setRefreshing(false);
        ToastUtils.showShort(getActivity(),message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if(null!=eyePresenter){
            eyePresenter.destroy();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            if(null!=eyePresenter) {
                getLiveList();
                eyePresenter.getHollandJoinMBTI();
                eyePresenter.getHollandReports("1", "2");
            }
        }
    }
    @Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.JOBREFSH)
    public void onFresh(Object o) {
        eyePresenter.getHollandJoinMBTI();
        eyePresenter.getHollandReports("1", "2");
    }
}
