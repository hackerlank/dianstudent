package com.dingli.diandians.newProject.moudle.eye;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dingli.diandians.R;
import com.dingli.diandians.newProject.base.fragment.BaseFragment;
import com.dingli.diandians.newProject.view.LoadDataView;

import butterknife.BindView;

/**
 * Created by lwq on 2017/6/5.
 */

public class JianJieFragment extends BaseFragment {
    @BindView(R.id.recyclerViewJianJie)
    RecyclerView recyclerViewJianJie;
    private  JianJieRecycleAdapter jianJieRecycleAdapter;
    public static JianJieFragment newInstance( Bundle bundle) {
        JianJieFragment fragment = new JianJieFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int layoutId() {
        return R.layout.fragment_jianjie;
    }
    @Override
    protected void initView() {
    }
    @Override
    protected void getLoadView(LoadDataView mLoadView) {
    }
    @Override
    protected void initData() {
        recyclerViewJianJie.setLayoutManager(new GridLayoutManager(getActivity(),1));
        jianJieRecycleAdapter=new JianJieRecycleAdapter(getActivity(),
                getActivity().getWindowManager().getDefaultDisplay().getWidth());
        recyclerViewJianJie.setAdapter(jianJieRecycleAdapter);
        jianJieRecycleAdapter.setData(getArguments().getString("childPic"));

    }
    @Override
    protected void initPresenter() {
    }
}
