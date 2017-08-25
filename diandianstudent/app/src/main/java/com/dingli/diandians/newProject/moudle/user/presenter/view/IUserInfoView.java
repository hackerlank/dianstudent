package com.dingli.diandians.newProject.moudle.user.presenter.view;


import com.dingli.diandians.newProject.http.base.view.IBaseView;
import com.dingli.diandians.newProject.moudle.user.protocol.UserProtocol;

/**
 * <p>Title: ILoginView<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2016/11/2
 */
public interface IUserInfoView extends IBaseView {
    void getUserInfoSuccess(UserProtocol userProtocol);

    void getUserInfoFailure(String message);
}
