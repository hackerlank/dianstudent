package com.dingli.diandians.newProject.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.LoginInvalidDialog;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>Title: BaseFragment<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2016/11/2
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder mUnbinder;

    private LoadDataView mLoadView;

    private AlertDialog loginInvalidDialog = null;

    protected abstract int layoutId();

    protected abstract void initView();

    protected abstract void getLoadView(LoadDataView mLoadView);

    protected abstract void initData();

    protected abstract void initPresenter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        try{
            if(null==mLoadView){
                View view = inflater.inflate(layoutId(), container, false);
                mUnbinder = ButterKnife.bind(this, view);
                mLoadView = new LoadDataView(getActivity(), view);
            }
        }catch (Exception e){
            if(null!=mLoadView){
                ((ViewGroup)mLoadView.getParent()).removeView(mLoadView);
                View view = inflater.inflate(layoutId(), container, false);
                mUnbinder = ButterKnife.bind(this, view);
                mLoadView = new LoadDataView(getActivity(), view);
            }
        }
        return mLoadView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getLoadView(mLoadView);
        initPresenter();
        initData();
    }

    @Override
    public void onDestroyView() {
        if(null!=mLoadView){
            if(null!=((ViewGroup)mLoadView.getParent())){
                ((ViewGroup)mLoadView.getParent()).removeView(mLoadView);
            }
            mLoadView=null;
        }
        if(null!=mUnbinder){
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    /**
     * 用户登录令牌失效提示
     */
    protected void showLoginOther(String message) {
        this.showLoginOther(message, null);
    }

    protected void showLoginOther(String message, OnCallback onCallback) {
        try {
            this.mOnCallback = onCallback;
            if (null == loginInvalidDialog) {
                loginInvalidDialog = new LoginInvalidDialog(getActivity(), () -> {
                    if (mOnCallback != null)
                        mOnCallback.onLoginInvalidClick();
                }).create();
            }
            if (!loginInvalidDialog.isShowing()) {
                loginInvalidDialog.setMessage(message);
                loginInvalidDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.LOGIN_OUT)
    public void onLoginOut(Object o) {
        if (loginInvalidDialog.isShowing()) {
            loginInvalidDialog.dismiss();
        }
    }

    private OnCallback mOnCallback;

    public interface OnCallback {
        void onLoginInvalidClick();
    }
}
