package com.dingli.diandians.newProject.moudle.eye.presenter;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.presenter.BasePresenter;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.subscriber.BKSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lwq on 2017/6/12
 */

public class LineNumberPresenter extends BasePresenter {
    public void onlineNumber(String liveId) {
        subscribe(utouuHttp(api().onlineNumber(liveId), HttpRequestURL.ONLINENUMBER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<Object>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<Object> result) {

                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                }));
    }

}
