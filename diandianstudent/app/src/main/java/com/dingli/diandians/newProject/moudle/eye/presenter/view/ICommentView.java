package com.dingli.diandians.newProject.moudle.eye.presenter.view;
import com.dingli.diandians.newProject.http.base.view.IBaseView;
import com.dingli.diandians.newProject.moudle.eye.protocol.MessageCommentListProtocl;
/**
 * Created by lwq on 2017/6/2.
 */

public interface ICommentView extends IBaseView {
    void getCommentListSuccess(MessageCommentListProtocl messageCommentListProtocl);
    void getCommentListFailure(String message);
    void sendCommentSuccess(String message);
    void sendCommentFailure(String message);
}
