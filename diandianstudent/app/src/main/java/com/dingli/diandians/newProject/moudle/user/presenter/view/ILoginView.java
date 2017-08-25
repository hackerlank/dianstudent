package com.dingli.diandians.newProject.moudle.user.presenter.view;

import com.dingli.diandians.newProject.http.base.view.IBaseView;

/**
 * <p>Title: ILoginView<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2017/6/1
 */
public interface ILoginView extends IBaseView {
    void loginSuccess(String message);

    void loginFailure(String message);

}
