package com.dingli.diandians.view;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * <p>Title: PtrClassicRefreshView<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author wei-qiang.liu
 * @version 1.0
 * @date 2016/8/10
 */
public class PtrClassicRefreshView extends PtrClassicFrameLayout {

    public PtrClassicRefreshView(Context context) {
        super(context);
        initView();
    }

    public PtrClassicRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrClassicRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void initView() {
        PtrHeader ptrHeader = new PtrHeader(getContext());
        setHeaderView(ptrHeader);
        setLoadingMinTime(800);
        addPtrUIHandler(ptrHeader);
        setDurationToCloseHeader(200);
    }

}
