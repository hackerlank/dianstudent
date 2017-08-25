package com.dingli.diandians.newProject.moudle.eye.presenter;
import com.dingli.diandians.newProject.constants.BKConstant;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.presenter.BasePresenter;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.subscriber.BKSubscriber;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.IEyeView;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.INoLiveView;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollantHollandReportsProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.NoliveProtocol;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lwq on 2017/6/2.
 */

public class EyePresenter extends BasePresenter {
    private IEyeView iEyeView;
    public EyePresenter(IEyeView iEyeView){
        this.iEyeView=iEyeView;
    }
    public void getLiveList(HashMap<String, String> map) {
        subscribe(utouuHttp(api().queryLiveList(map), HttpRequestURL.QUERYLIVELIST_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<HrdListProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<HrdListProtocol> result) {
                        if(null!=iEyeView){
                            iEyeView.getHrdListSuccess(result.data);
                        }
                    }

                    @Override
                    protected void onFailure(String message) {
                        if(null!=iEyeView){
                            iEyeView.getHrdListFailure(message);
                        }

                    }

                    @Override
                    protected void onNetworkFailure(String message) {
                        if(null!=iEyeView) {
                            iEyeView.onNetworkFailure(message);
                        }
                    }
                    @Override
                    protected void onLoginInvalid(String message) {
                        if(null!=iEyeView) {
                            iEyeView.onLoginInvalid(message);
                        }

                    }
                }));
    }


    public void getArticleList() {//文章
        subscribe(utouuHttp(api().queryArticleList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<List<ArticleProtocol>>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<List<ArticleProtocol>> result) {
                        if(null!=iEyeView) {
                            iEyeView.getArticleListSuccess(result.data);
                        }
                    }
                    @Override
                    protected void onFailure(String message) {
                        if(null!=iEyeView) {
                            iEyeView.getArticleListFailure(message);
                        }
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                        if(null!=iEyeView) {
                            iEyeView.onNetworkFailure(message);
                        }
                    }
                    @Override
                    protected void onLoginInvalid(String message) {
                        if(null!=iEyeView) {
                            iEyeView.onLoginInvalid(message);
                        }
                    }
                }));
    }

    public void recentlyNoLive(INoLiveView iNoLiveView) {//预告信息
        subscribe(utouuHttp(api().recentlyNoLive())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<NoliveProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<NoliveProtocol> result) {
                        iNoLiveView.getNoLiveInfoSuccess(result.data);
                    }

                    @Override
                    protected void onFailure(String message) {
                        iNoLiveView.getNoLiveInfoFailure(message);
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                        if(null!=iNoLiveView) {
                            iNoLiveView.onNetworkFailure(message);
                        }
                    }
                    @Override
                    protected void onLoginInvalid(String message) {
                    }
                }));
    }

    public void getHollandJoinMBTI() {//Holland和MBTI推荐共同推荐岗位
        subscribe(utouuHttp(api().getHollandJoinMBTI())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<HollandJoinMBTIProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<HollandJoinMBTIProtocol> result) {
                        if(null!=iEyeView) {
                            iEyeView.getHollandJoinMBTISuccess(result.data);
                        }
                    }
                    @Override
                    protected void onFailure(String message) {
                        if(null!=iEyeView) {
                            iEyeView.getHollandJoinMBTIFailure(message);
                        }
                    }
                    @Override
                    protected void onLoginInvalid(String message) {
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                    }
                }));
    }
    public void getHollandReports(String evaluationHLDId,String evaluationMTIId) {//判断是否有测试报告
        subscribe(utouuHttp(api().getHollandReports(evaluationHLDId))
                .flatMap(new Func1<BaseProtocol<HollantHollandReportsProtocol>, Observable<BaseProtocol<HollantHollandReportsProtocol>>>() {
                    @Override
                    public Observable<BaseProtocol<HollantHollandReportsProtocol>> call(BaseProtocol<HollantHollandReportsProtocol> hollantHollandReportsProtocolBaseProtocol) {
                        BKConstant.HLD_TEST_COUNT=hollantHollandReportsProtocolBaseProtocol.data.times;
                        return (utouuHttp(api().getHollandReports(evaluationMTIId)));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<HollantHollandReportsProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<HollantHollandReportsProtocol> result) {
                        BKConstant.MBTI_TEST_COUNT=result.data.times;
                        if(null!=iEyeView) {
                            iEyeView.getHollandReportsSuccess(result.data);
                        }

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    protected void onFailure(String message) {
                        if(null!=iEyeView) {
                            iEyeView.getHollandReportsFailure(message);
                        }
                    }
                    @Override
                    protected void onLoginInvalid(String message) {
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                    }
                }));
    }
}
