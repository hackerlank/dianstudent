package com.dingli.diandians.newProject.http.base.view;

/**
 * <p>Title: IBaseView<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author lwq
 * @version 1.0
 * @date 2016/11/3
 */
public interface IBaseView {
    void onLoginInvalid(String message);
   void onNetworkFailure(String message);
}
