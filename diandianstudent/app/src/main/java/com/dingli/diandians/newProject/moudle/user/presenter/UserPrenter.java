package com.dingli.diandians.newProject.moudle.user.presenter;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.presenter.BasePresenter;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.subscriber.BKSubscriber;
import com.dingli.diandians.newProject.moudle.user.presenter.view.ILoginView;
import com.dingli.diandians.newProject.moudle.user.protocol.LoginResultProtocol;
import com.dingli.diandians.newProject.moudle.user.protocol.UserProtocol;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lwq on 2017/6/1.
 */

public class UserPrenter extends BasePresenter {

    /**
     * 获取tgt登陆获取信息
     */
    public void getTgt(HashMap<String, String> map, ILoginView iLoginView) {
        subscribe(utouuHttp(api().getLoginToken(map))
                .flatMap(new Func1<BaseProtocol<LoginResultProtocol>, Observable<BaseProtocol<UserProtocol>>>() {
                    @Override
                    public Observable<BaseProtocol<UserProtocol>> call(BaseProtocol<LoginResultProtocol> loginProtocol) {
                        DianApplication.getInstance().setAuthorization(loginProtocol.data.token_type + "" + loginProtocol.data.access_token);//保存tgt
                        return utouuHttp(api().getUserInfo(), HttpRequestURL.ACCOUNT_INFO_URL);//获取用户信息
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<UserProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<UserProtocol> result) {
                        iLoginView.loginSuccess("登陆成功");

                    }

                    @Override
                    protected void onFailure(String message) {
                        iLoginView.loginFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iLoginView.onLoginInvalid(message);
                    }

                    @Override
                    protected void onNetworkFailure(String message) {
                        iLoginView.onNetworkFailure(message);
                    }
                }));
    }
}
