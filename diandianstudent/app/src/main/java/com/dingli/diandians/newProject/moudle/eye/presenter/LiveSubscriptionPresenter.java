package com.dingli.diandians.newProject.moudle.eye.presenter;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.presenter.BasePresenter;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.subscriber.BKSubscriber;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.ILiveSubscriptionView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lwq on 2017/6/2.
 */

public class LiveSubscriptionPresenter extends BasePresenter {
    private ILiveSubscriptionView iLiveSubscriptionView;
    public LiveSubscriptionPresenter(ILiveSubscriptionView iLiveSubscriptionView){
        this.iLiveSubscriptionView=iLiveSubscriptionView;
    }
    public void saveLiveSubscription(String data) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),data);
        subscribe(utouuHttp(api().saveLiveSubscription(body), HttpRequestURL.SAVESUBSCRIPTION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<Object>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<Object> result) {
                        iLiveSubscriptionView.doSubscriptionSuccess("");
                    }

                    @Override
                    protected void onFailure(String message) {
                        iLiveSubscriptionView.cancleSubscriptionFailure(message);
                    }

                    @Override
                    protected void onNetworkFailure(String message) {
                        iLiveSubscriptionView.onNetworkFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iLiveSubscriptionView.onLoginInvalid(message);
                    }
                }));
    }
    public void cancelLiveSubscription(String data) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),data);
        subscribe(utouuHttp(api().cancelLiveSubscription(body), HttpRequestURL.CANCELSUBSCRIPTION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<Object>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<Object> result) {
                        iLiveSubscriptionView.cancleSubscriptionSuccess("");
                    }

                    @Override
                    protected void onFailure(String message) {
                        iLiveSubscriptionView.cancleSubscriptionFailure(message);

                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                        iLiveSubscriptionView.onNetworkFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iLiveSubscriptionView.onLoginInvalid(message);
                    }
                }));
    }
}
