package com.dingli.diandians.common;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;

/**
 * Created by Administrator on 2015/5/27.
 */
public class MyDialog extends AlertDialog{

//	AlertDialog alertDialog = null;
	MyDialog alertDialog;
	Context context = null;
    String content;
	public MyDialog(Context context) {
		super(context);
	}
	public MyDialog(Context context,String content) {
		super(context);
		this.context = context;
		this.content=content;
	}
//	public void dismiss(){
//		alertDialog.dismiss();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_loading);
		((TextView)findViewById(R.id.textwenzi)).setText(content);
	}

	// waiting
	public void waiting(String text, Boolean cancelAble) {
//		Dialog dialog=new Dialog(context);
//		dialog.setContentView(dialog_view,new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//		dialog.show();
//		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
//		Window window = alertDialog.getWindow();
//		window.setContentView(dialog_view);
//		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
}
