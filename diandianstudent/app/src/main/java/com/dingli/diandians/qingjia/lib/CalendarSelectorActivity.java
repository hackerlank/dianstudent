package com.dingli.diandians.qingjia.lib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.DianTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description 预订日选择
 */
public class CalendarSelectorActivity extends BaseActivity implements View.OnClickListener{

	public static final String ORDER_DAY = "order_day";
	private int daysOfSelect;
	private ListView listView;
	ImageView calenseback;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_selector);
		calenseback=(ImageView)findViewById(R.id.calenseback);
		calenseback.setOnClickListener(this);
		final String xulie=getIntent().getStringExtra("startdate");
		Date d1=new Date();
		long thereBefore=d1.getTime()-72*60*60*1000;
		Date d=new Date(thereBefore);
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
		String str=matter1.format(d);
		int years=Integer.parseInt(str.split("-")[0]);
//		Calendar calendar=Calendar.getInstance();
//		calendar.setTime(d);
//		int monthe=calendar.get(Calendar.MONTH)+1;
//		calendar.add(Calendar.MONTH,-2);
//		String json=matter1.format(calendar.getTime());
//		DianTool.showTextToast(this,monthe+"");
		if(years%4==0&&years%100!=0){
			daysOfSelect =366;
		}else{
			daysOfSelect =365;
		}
		listView = (ListView) findViewById(R.id.lv_calendar);

		CalendarListAdapter adapter = new CalendarListAdapter(this, daysOfSelect, str,d);
		listView.setAdapter(adapter);

		adapter.setOnCalendarOrderListener(new CalendarListAdapter.OnCalendarOrderListener() {
			@Override
			public void onOrder(String orderInfo) {
				if(xulie.equals("1")) {
					Intent result = new Intent();
					result.putExtra(ORDER_DAY, orderInfo);
					setResult(RESULT_OK, result);
				}else{
					Intent result = new Intent();
					result.putExtra("enddate", orderInfo);
					setResult(RESULT_OK, result);
				}
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.calenseback:
				this.finish();
				overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
				break;
		}
	}
}
