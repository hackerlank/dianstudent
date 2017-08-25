package com.dingli.diandians.newProject.http.base.presenter;
import com.dingli.diandians.newProject.http.HttpPresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>Title: BasePresenter<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2016/11/2
 */
public abstract class BasePresenter extends HttpPresenter {

    private CompositeSubscription mSubscription = new CompositeSubscription();

    protected void subscribe(Subscription subscription) {
        this.mSubscription.add(subscription);
    }

    protected void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.clear();
        }
    }
    public void resume() {
    }
    public void pause() {
    }
    public void destroy() {
        unsubscribe();
    }

}