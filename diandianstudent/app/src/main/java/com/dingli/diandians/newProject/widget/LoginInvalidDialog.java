package com.dingli.diandians.newProject.widget;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.newProject.constants.BKConstant;


import org.simple.eventbus.EventBus;

public class LoginInvalidDialog extends AlertDialog.Builder {

    private OnCallback mOnCallback;
    public LoginInvalidDialog(@NonNull Context context) {
        this(context, null);
    }

    public LoginInvalidDialog(@NonNull Context context, OnCallback onCallback) {
        super(context);
        this.mOnCallback = onCallback;
        setTitle("身份令牌过期");
        setCancelable(false);
        setPositiveButton("确定", (dialog, which) -> okClick());
    }

    private void okClick() {
        EventBus.getDefault().post("", BKConstant.EventBus.LOGIN_OUT);
        getContext().startActivity(new Intent(getContext(), LoginActivity.class));
        if (mOnCallback != null)
            mOnCallback.onClick();
    }

    public interface OnCallback {
        void onClick();
    }

}
