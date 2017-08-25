package com.dingli.diandians.newProject.moudle.eye.presenter.view;
import com.dingli.diandians.newProject.http.base.view.IBaseView;
/**
 * Created by lwq on 2017/6/2.
 */

public interface ILiveSubscriptionView extends IBaseView {
    void doSubscriptionSuccess(String message);
    void doSubscriptionFailure(String message);
    void cancleSubscriptionSuccess(String message);
    void cancleSubscriptionFailure(String message);
}
