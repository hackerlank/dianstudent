package com.dingli.diandians.newProject.base.activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.view.LoadDataView;
import com.dingli.diandians.newProject.widget.LoginInvalidDialog;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.ButterKnife;


/**
 * <p>Title: BaseActivity<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2016/11/2
 */
public abstract class BaseActivity extends AppCompatActivity {

    private AlertDialog loginInvalidDialog = null;

    protected abstract int layoutId();

    protected abstract ViewGroup loadDataViewLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void getLoadView(LoadDataView loadView);

    protected abstract void initPresenter();

    private LoadDataView mLoadView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
        initView();
        initViewGroup();
        initPresenter();
        initData();
        initCrash();
        context = this;
    }

    public void initCrash() {
       // CrashManager.getInstance(PApplication.getIns()).setMActivity(this);//注册代码
    }
    protected void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
                loginInvalidDialog = new LoginInvalidDialog(this, () -> {
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
    private OnCallback mOnCallback;
    public interface OnCallback {
        void onLoginInvalidClick();
    }
    /**
     * 嵌入loaddataview
     */
    private void initViewGroup() {
        ViewGroup view = loadDataViewLayout();
        if (null != view) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
                this.mLoadView = new LoadDataView(this, view);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0, 0, 0, 0);
                viewGroup.addView(this.mLoadView, params);
                getLoadView(this.mLoadView);
            }
        }
    }
    @Subscriber(mode = ThreadMode.MAIN, tag = BKConstant.EventBus.LOGIN_OUT)
    public void onLoginOut(Object o) {
        if (loginInvalidDialog.isShowing()) {
            loginInvalidDialog.dismiss();
        }
    }
}
