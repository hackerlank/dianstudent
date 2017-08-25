package com.dingli.diandians.newProject.moudle.eye.presenter.view;
import com.dingli.diandians.newProject.http.base.view.IBaseView;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollantHollandReportsProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;

import java.util.List;

/**
 * Created by lwq on 2017/6/2.
 */

public interface IEyeView extends IBaseView {
    void getHrdListSuccess(HrdListProtocol hrdListProtocol);
    void getHrdListFailure(String message);
    void getArticleListSuccess(List<ArticleProtocol> articleProtocolList);
    void getArticleListFailure(String message);
    void getHollandJoinMBTISuccess(HollandJoinMBTIProtocol hollandJoinMBTIProtocol);
    void getHollandJoinMBTIFailure(String message);
    void getHollandReportsSuccess(HollantHollandReportsProtocol hollantHollandReportsProtocol);
    void getHollandReportsFailure(String message);
}
