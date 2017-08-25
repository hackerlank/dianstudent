package com.dingli.diandians.yichangnv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.Data;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.MyDialog;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.common.ResultOne;
import com.dingli.diandians.login.LoginActivity;
import com.dingli.diandians.personcenter.OutQingAdapter;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.dingli.diandians.common.DianApplication.data;

/**
 * Created by dingliyuangong on 2016/6/14.
 */
public class YiChangNvActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener{

    OutQingAdapter adapter;
    int currentPage=1,totalPage;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout refyichang;
    MaterialSpinner recordspinner;
    PieChart mChartfenrecord;
    List<Integer> listid;
    XListView lvzhuanye;
    int larrvied,llate,lbeg,learly,lcrunk;
    TextView tvzan;
    int ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichangnv);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview();
        refyichang.setRefreshing(true);
        initdata();
    }
    void initview(){
        listid=new ArrayList<>();
        refyichang=(VerticalSwipeRefreshLayout)findViewById(R.id.refyichang);
        refyichang.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
       ImageView yichangback=(ImageView)findViewById(R.id.yichangback);
        lvzhuanye=(XListView) findViewById(R.id.lvzhuanye);
        lvzhuanye.setPullRefreshEnable(true);
        lvzhuanye.setXListViewListener(this);
        lvzhuanye.setPullLoadEnable(true);
        lvzhuanye.addHeaderView(getView());
        yichangback.setOnClickListener(this);
        adapter = new OutQingAdapter(this);
        lvzhuanye.setAdapter(adapter);
    }
    void initdata(){
                if(DianTool.isConnectionNetWork(this)) {
                    DianTool.huoqutoken();
                    headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                    OkGo.get(HostAdress.getZheRe("/api/phone/v1/student/semester/get")).tag(this)
                            .headers(headers).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (TextUtils.isEmpty(s)) {
                                DianTool.showTextToast(YiChangNvActivity.this, getResources().getString(R.string.wushuju));
                            } else {
                                List<ResultOne> resultOne=JSON.parseArray(s,ResultOne.class);
                                List<String> list=new ArrayList<String>();
                                for (int i=0;i<resultOne.size();i++){
                                    list.add(resultOne.get(i).name);
                                    listid.add(resultOne.get(i).id);
                                }
                                recordspinner.setItems(list);
                                DianApplication.user.semesterId=listid.get(0);
                                showlistfirst(listid.get(0));
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            refyichang.setRefreshing(false);
                            DianTool.response(response,YiChangNvActivity.this);
                        }
                    });
                }else{
                    DianTool.showTextToast(this,"请检查网络");
                }
    }
   public View getView(){
        View view= LayoutInflater.from(this).inflate(R.layout.hearder_record,null);
       recordspinner=(MaterialSpinner) view.findViewById(R.id.recordspinner);
       recordspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
               DianApplication.user.semesterId=listid.get(position);
               DianTool.showMyDialog(YiChangNvActivity.this, "");
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       DianTool.dissMyDialog();
                   }
               },3000);
               showlistfirst(listid.get(position));
           }
       });
       TextView detailrecord=(TextView) view.findViewById(R.id.detailrecord);
       TextView recordyidao=(TextView) view.findViewById(R.id.recordyidao);
       TextView recordchidao=(TextView) view.findViewById(R.id.recordchidao);
       TextView recordzaotui=(TextView) view.findViewById(R.id.recordzaotui);
       TextView recordkuangke=(TextView) view.findViewById(R.id.recordkuangke);
       TextView recordqingjia=(TextView) view.findViewById(R.id.recordqingjia);
       tvzan=(TextView) view.findViewById(R.id.tvzan);
       detailrecord.setOnClickListener(this);
       recordyidao.setOnClickListener(this);
       recordchidao.setOnClickListener(this);
       recordkuangke.setOnClickListener(this);
       recordzaotui.setOnClickListener(this);
       recordqingjia.setOnClickListener(this);
       mChartfenrecord=(PieChart) view.findViewById(R.id.mChartfenrecord);
       Description description=new Description();
       description.setText("");
       mChartfenrecord.setDescription(description);
       mChartfenrecord.setHoleColor(Color.TRANSPARENT);
       mChartfenrecord.setHoleRadius(0);  //半径
       mChartfenrecord.setTransparentCircleRadius(0f); // 半透明圈
       mChartfenrecord.setDrawCenterText(false);  //饼状图中间不可以添加文字
       mChartfenrecord.setDrawHoleEnabled(true);
       mChartfenrecord.setRotationEnabled(false);// 不可以手动旋转
       mChartfenrecord.setTouchEnabled(false);
       mChartfenrecord.setDrawEntryLabels(true);
       return view;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
         switch (v.getId()){
             case R.id.recordqingjia:
                 intent.setClass(this,RecordDetailActivity.class);
                 intent.putExtra(Constant.TYPEID,4);
                 startActivity(intent);
                 break;
             case R.id.recordkuangke:
                 intent.setClass(this,RecordDetailActivity.class);
                 intent.putExtra(Constant.TYPEID,2);
                 startActivity(intent);
                 break;
             case R.id.recordzaotui:
                 intent.setClass(this,RecordDetailActivity.class);
                 intent.putExtra(Constant.TYPEID,5);
                 startActivity(intent);
                 break;
             case R.id.recordchidao:
                 intent.setClass(this,RecordDetailActivity.class);
                 intent.putExtra(Constant.TYPEID,3);
                 startActivity(intent);
                 break;
             case R.id.recordyidao:
                 intent.setClass(this,RecordDetailActivity.class);
                 intent.putExtra(Constant.TYPEID,1);
                 startActivity(intent);
                 break;
             case R.id.detailrecord:
                 intent.setClass(this,RecordDetailActivity.class);
                 intent.putExtra(Constant.TYPEID,0);
                 startActivity(intent);
                 break;
             case R.id.yichangback:
                 DianApplication.user.libiao="mine";
                 this.finish();
                 overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                 break;
         }
    }
    void showlistfirst(final int id){
        if (DianTool.isConnectionNetWork(this)){
          HttpParams params=new HttpParams();
            params.put("semesterId",id);
            ids=id;
            OkGo.get(HostAdress.getRequest("/api/phone/v1/student/attendance/getCount")).headers(headers)
                    .params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (!TextUtils.isEmpty(s)){
                        ResultInfo resultInfo=JSON.parseObject(s,ResultInfo.class);
                               pie(resultInfo.data);
                        showlistsign(1,Constant.REFRESH,id);
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {

                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    void pie(Data[] datas){
        int cishuone=0;
        if (datas[0].arrvied!=0){
            cishuone+=1;
        }
        if (datas[0].crunk!=0){
            cishuone+=1;
        }
        if (datas[0].late!=0){
            cishuone+=1;
        }
        if (datas[0].beg!=0){
            cishuone+=1;
        }
        if (datas[0].early!=0){
            cishuone+=1;
        }
        if (cishuone==0){
            tvzan.setVisibility(View.VISIBLE);
        }else{
            tvzan.setVisibility(View.GONE);
        }
        PieData pieData = getPieData(cishuone, datas);
        //设置数据
        mChartfenrecord.setData(pieData);
        Legend mLegend = mChartfenrecord.getLegend();  //设置比例图
        mLegend.setEnabled(false);// 不展示比例图
        mChartfenrecord.animateXY(1000, 1000);  //设置动画
    }
    private PieData getPieData(int cishu,Data[] datas){
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        ArrayList<Integer> colors = new ArrayList<Integer>();
        List<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据
        for (int i = 0; i < cishu; i++) {
            xValues.add("");
        }
        if (datas[0].arrvied!=0){
            float as = datas[0].arrvied * 100/ (datas[0].late+datas[0].beg+ datas[0].early+ datas[0].crunk+ datas[0].arrvied);
            larrvied = (int) as;
                yValues.add(new PieEntry(larrvied, ""));
            colors.add(Color.rgb(131,223,182));
        }
        if (datas[0].late!=0){
            float as = datas[0].late * 100/ (datas[0].late+datas[0].beg+ datas[0].early+ datas[0].crunk+ datas[0].arrvied);
            llate = (int) as;
                yValues.add(new PieEntry(llate, ""));
            colors.add(Color.rgb(250,214,128));
        }
        if (datas[0].beg!=0){
            float as = datas[0].beg * 100/ (datas[0].late+datas[0].beg+ datas[0].early+ datas[0].crunk+ datas[0].arrvied);
            lbeg = (int) as;
                yValues.add(new PieEntry(lbeg, ""));
            colors.add(Color.rgb(142,205,246));
        }
        if (datas[0].early!=0){
            float as = datas[0].early * 100/ (datas[0].late+datas[0].beg+ datas[0].early+ datas[0].crunk+ datas[0].arrvied);
            learly = (int) as;
                yValues.add(new PieEntry(learly, ""));
            colors.add(Color.rgb(247,150,121));
        }
        if (datas[0].crunk!=0) {
            float as = datas[0].crunk * 100/ (datas[0].late+datas[0].beg+ datas[0].early+ datas[0].crunk+ datas[0].arrvied);
            lcrunk = (int) as;
                yValues.add(new PieEntry(lcrunk, ""));
            colors.add(Color.rgb(227, 207, 174));
        }
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "Election Results"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentYiChangFormatter());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        return pieData;
    }
    void showlistsign(int page, final int type, final int id){
        if (DianTool.isConnectionNetWork(this)){
            HttpParams params=new HttpParams();
            currentPage=page;
            params.put("semesterId",id);
            params.put("offset",page);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/student/attendance/getcourseCount"))
                    .params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    refyichang.setRefreshing(false);
                    refyichang.setEnabled(false);
                    lvzhuanye.stopLoadMore();
                    lvzhuanye.stopRefresh();
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("{}")) {
                            ResultOne resultOne = JSON.parseObject(s, ResultOne.class);
                            switch (type) {
                                case Constant.LOAD_MORE:
                                    adapter.addOutlist(resultOne.data);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case Constant.REFRESH:
                                    adapter.refreshOutlist(resultOne.data);
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    @Override
    public void onRefresh() {
       lvzhuanye.stopRefresh();
    }

    @Override
    public void onLoadMore() {
       showlistsign(++currentPage,Constant.LOAD_MORE,ids);
    }
}
