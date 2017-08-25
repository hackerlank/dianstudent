package com.dingli.diandians.newProject.moudle.eye.presenter.view;
import com.dingli.diandians.newProject.http.base.view.IBaseView;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.NoliveProtocol;

import java.util.List;

/**
 * Created by lwq on 2017/6/2.
 */

public interface INoLiveView extends IBaseView {
    void getNoLiveInfoSuccess(NoliveProtocol noliveProtocol);
    void getNoLiveInfoFailure(String message);
}
