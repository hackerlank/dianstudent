package com.dingli.diandians.newProject.view;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Author     : lwq
 */
public class TopRecyclerView extends RecyclerView {

    private OnTopListener onTopListener;
    private boolean isReply = false;

    public TopRecyclerView(Context context) {
        super(context);
    }

    public TopRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnTopListener(final OnTopListener onTopListener) {
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /**
                 *
                 * 如果当前RecyclerView处于最底部，先判断是否已经响应了底部监听事件。如果还未响应则可以对其响应，
                 * 并将响应标识变量 "isReply" 设置为 "true"，防止连续多次响应。如果当前RecyclerView并未处于
                 * 最底部，则需将响应标识变量 "isReply" 设置为 "true"，以保证当再次滑动到页面地板时可以正常响应监听事件
                 *
                 */
                if (((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {
                    if (!isReply) {
                        if (getLayoutManager().getItemCount() >9) {
                            onTopListener.OnTop();
                            isReply = true;
                        } else {
                            isReply = true;
                        }

                    }
                } else {
                    isReply = false;
                }
            }
        });
        this.onTopListener = onTopListener;
    }

    public interface OnTopListener {
        void OnTop();
    }
}

