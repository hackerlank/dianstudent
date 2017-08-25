package com.dingli.diandians.newProject.moudle.eye.presenter;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.presenter.BasePresenter;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.subscriber.BKSubscriber;
import com.dingli.diandians.newProject.moudle.eye.presenter.view.ICommentView;
import com.dingli.diandians.newProject.moudle.eye.protocol.MessageCommentListProtocl;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lwq on 2017/6/12
 */

public class CommentPresenter extends BasePresenter {
    private ICommentView iCommentView;
    public CommentPresenter(ICommentView iCommentView){
        this.iCommentView=iCommentView;
    }
    public void ueryMessageCommentList(String pager,String pagerSize,String liveId) {
        subscribe(utouuHttp(api().queryMessageCommentList(pager,pagerSize,liveId), HttpRequestURL.QUERYMESSAGECOMMENTLIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<MessageCommentListProtocl>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<MessageCommentListProtocl> result) {
                        iCommentView.getCommentListSuccess(result.data);
                    }
                    @Override
                    protected void onFailure(String message) {
                        iCommentView.getCommentListFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iCommentView.onLoginInvalid(message);
                    }

                    @Override
                    protected void onNetworkFailure(String message) {
                        iCommentView.onNetworkFailure(message);
                    }
                }));
    }
    public void sendMessageComment(String data) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),data);
        subscribe(utouuHttp(api().saveMessageComment(body), HttpRequestURL.SAVEMESSAGECOMMENT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<Object>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<Object> result) {
                        iCommentView.sendCommentSuccess("");
                    }

                    @Override
                    protected void onFailure(String message) {
                        iCommentView.sendCommentFailure(message);
                    }
                    @Override
                    protected void onLoginInvalid(String message) {
                        iCommentView.onLoginInvalid(message);
                    }

                    @Override
                    protected void onNetworkFailure(String message) {
                        iCommentView.onNetworkFailure(message);
                    }
                }));
    }
}
