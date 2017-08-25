package com.dingli.diandians.qingjia;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.bean.FileStorage;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.GoSetDialog;
import com.dingli.diandians.common.Initoken;
import com.dingli.diandians.common.TongyiDialog;
import com.dingli.diandians.information.EntrtyActivity;
import com.dingli.diandians.personcenter.ActionSheetDialog;
import com.dingli.diandians.qingjia.lib.CalendarSelectorActivity;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.qingjia.picture.GridClose;
import com.dingli.diandians.qingjia.picture.ImgFileListActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class VacateActivity extends BaseActivity implements View.OnClickListener,GridClose{
    ImageView lixiaoliang,lixiaoan,foulixiaoliang,foulixiaoan,ddvacate;
    TextView tvjieshu,kaidate,gongtian,kaienddate,tvkongbai;
    EditText tvlixiaoyuanyin;
    Button bttiti;
    String str;
    SimpleDateFormat matter1;
    boolean lixiao=true;
    long log;
    LinearLayout lijieshu;
    String orderInfoend;
    String orderInfokai;
    View vwjieshu;
    ArrayList<Integer> agr;
    HttpHeaders headers;
    GridView gridView;
    int count;
    Bitmap bmp;
    String imageFilePath;
    File temp;
    ArrayList<String> listfile = new ArrayList<String>();
    ArrayList<String> arrl;
    int zizengs;
    Uri imageUri;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacate);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview();
    }
    void initview(){
        arrl=new ArrayList<>();
        tvkongbai=(TextView)findViewById(R.id.tvkongbai);
        ddvacate=(ImageView)findViewById(R.id.ddvacate);
        ddvacate.setOnClickListener(this);
        kaienddate=(TextView)findViewById(R.id.kaienddate);
        kaienddate.setOnClickListener(this);
       ImageView vacateback=(ImageView)findViewById(R.id.vacateback);
        lixiaoliang=(ImageView)findViewById(R.id.lixiaoliang);
        lixiaoan=(ImageView)findViewById(R.id.lixiaoan);
        foulixiaoliang=(ImageView)findViewById(R.id.foulixiaoliang);
        foulixiaoan=(ImageView)findViewById(R.id.foulixiaoan);
        tvjieshu=(TextView)findViewById(R.id.tvjieshu);
        kaidate=(TextView)findViewById(R.id.kaidate);
        gongtian=(TextView)findViewById(R.id.gongtian);
        tvlixiaoyuanyin=(EditText)findViewById(R.id.tvlixiaoyuanyin);
        lijieshu=(LinearLayout)findViewById(R.id.lijieshu);
        bttiti=(Button)findViewById(R.id.bttiti);
        vwjieshu=findViewById(R.id.vwjieshu);
        vacateback.setOnClickListener(this);
        lixiaoliang.setOnClickListener(this);
        lixiaoan.setOnClickListener(this);
        foulixiaoliang.setOnClickListener(this);
        foulixiaoan.setOnClickListener(this);
        tvjieshu.setOnClickListener(this);
        kaidate.setOnClickListener(this);
        bttiti.setOnClickListener(this);
        tvkongbai.setOnClickListener(this);
        Date d=new Date();
         matter1=new SimpleDateFormat("yyyy-MM-dd");
         str=matter1.format(d);
        kaidate.setText(str);
        kaienddate.setText(str);
        gridView = (GridView) findViewById(R.id.noScrollgridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_focused);
        count = 1;
        GridViewAdapter adapter = new GridViewAdapter(VacateActivity.this, bmp,
                count);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new GridViewItemOnClick2());
    }
    private static String getPath(String path){
        File f=new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }
    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
      switch (v.getId()){
          case R.id.vacateback:
              this.finish();
              arrl.clear();
              if(DianApplication.user.alist!=null) {
                  DianApplication.user.alist.clear();
              }
              overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
              break;
          case R.id.lixiaoliang:
              lixiao=true;
              lixiaoliang.setVisibility(View.VISIBLE);
              lixiaoan.setVisibility(View.GONE);
              foulixiaoliang.setVisibility(View.GONE);
              foulixiaoan.setVisibility(View.VISIBLE);
              break;
          case R.id.lixiaoan:
              lixiao=true;
              lixiaoliang.setVisibility(View.VISIBLE);
              lixiaoan.setVisibility(View.GONE);
              foulixiaoliang.setVisibility(View.GONE);
              foulixiaoan.setVisibility(View.VISIBLE);
              break;
          case R.id.foulixiaoliang:
              lixiao=false;
              lixiaoliang.setVisibility(View.GONE);
              lixiaoan.setVisibility(View.VISIBLE);
              foulixiaoliang.setVisibility(View.VISIBLE);
              foulixiaoan.setVisibility(View.GONE);
              break;
          case R.id.foulixiaoan:
              lixiao=false;
              lixiaoliang.setVisibility(View.GONE);
              lixiaoan.setVisibility(View.VISIBLE);
              foulixiaoliang.setVisibility(View.VISIBLE);
              foulixiaoan.setVisibility(View.GONE);
              break;
          case R.id.tvjieshu:
              Intent intente = new Intent(VacateActivity.this, KeChenJieShuActivity.class);
              if(DianApplication.user.alist!=null) {
                  DianApplication.user.alist.clear();
              }
              startActivityForResult(intente, 2);
              overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
              break;
          case R.id.tvkongbai:
              Intent intents = new Intent(VacateActivity.this, KeChenJieShuActivity.class);
              if(DianApplication.user.alist!=null) {
                  DianApplication.user.alist.clear();
              }
              startActivityForResult(intents, 2);
              overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
              break;
          case R.id.kaidate:
                  intent.setClass(VacateActivity.this, CalendarSelectorActivity.class);
              intent.putExtra("startdate","1");
              startActivityForResult(intent, 1);
              overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
              break;
          case R.id.kaienddate:
              intent.setClass(VacateActivity.this, CalendarSelectorActivity.class);
              intent.putExtra("startdate","3");
              startActivityForResult(intent, 3);
              overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
              break;
          case R.id.bttiti:
              String zong="";
              String jieshu="";
              String shili="";
              String kai=kaidate.getText().toString().trim();
              String end=kaienddate.getText().toString().trim();
              String gongtians=gongtian.getText().toString().trim();
              jieshu = tvjieshu.getText().toString();
              Date date=null;
              Date date1=null;
              try{
                  date=matter1.parse(kai);
                  date1=matter1.parse(end);
              }catch (Exception e){
                  e.printStackTrace();
              }

              if (date.getTime()-date1.getTime()>0){
                  DianTool.showTextToast(this,"请填写正确时间");
              }else {
                  if (lixiao == true) {
                      shili = "true";
                  } else {
                      shili = "false";
                  }
                  if (log > 0) {
                      if (lijieshu.getVisibility()==View.GONE) {
                          zong = kai + "--" + end;
                          jieshu = "";
                          String cases = tvlixiaoyuanyin.getText().toString().trim();
                          TongyiDialog dialog = new TongyiDialog(this, zong, jieshu, shili, cases, new TongyiDialog.SelectDialogButtonListener() {
                              @Override
                              public void checkButton(int id) {
                                  switch (id) {
                                      case R.id.btnTongyiDialogDetermine:
                                          if (arrl.size() > 3) {
                                              DianTool.showTextToast(VacateActivity.this, "图片最多3张");
                                          } else {
                                              if (DianTool.isConnectionNetWork(VacateActivity.this)) {
                                                  bttitijiao();
                                              } else {
                                                  DianTool.showTextToast(VacateActivity.this, "请检查网络");
                                              }
                                          }
                                          break;
                                  }
                              }
                          });
                          dialog.show();
                      }else{
                          zong = kai;
                          jieshu = tvjieshu.getText().toString();
                          String cases = tvlixiaoyuanyin.getText().toString().trim();
                          if (!jieshu.equals("请选择节数")) {
                              TongyiDialog dialog = new TongyiDialog(this, zong, jieshu, shili, cases, new TongyiDialog.SelectDialogButtonListener() {
                                  @Override
                                  public void checkButton(int id) {
                                      switch (id) {
                                          case R.id.btnTongyiDialogDetermine:
                                              if (arrl.size() > 3) {
                                                  DianTool.showTextToast(VacateActivity.this, "图片最多3张");
                                              } else {
                                                  if (DianTool.isConnectionNetWork(VacateActivity.this)) {
                                                      bttitijiao();
                                                  } else {
                                                      DianTool.showTextToast(VacateActivity.this, "请检查网络");
                                                  }
                                              }
                                              break;
                                      }
                                  }
                              });
                              dialog.show();
                          } else {
                              DianTool.showTextToast(VacateActivity.this, "请填写完整信息后提交");
                          }
                      }
                  } else if (log == 0) {
                      zong = kai;
                      jieshu = tvjieshu.getText().toString();
                      String cases = tvlixiaoyuanyin.getText().toString().trim();
                      if (!jieshu.equals("请选择节数")) {
                          TongyiDialog dialog = new TongyiDialog(this, zong, jieshu, shili, cases, new TongyiDialog.SelectDialogButtonListener() {
                              @Override
                              public void checkButton(int id) {
                                  switch (id) {
                                      case R.id.btnTongyiDialogDetermine:
                                          if (arrl.size() > 3) {
                                              DianTool.showTextToast(VacateActivity.this, "图片最多3张");
                                          } else {
                                              if (DianTool.isConnectionNetWork(VacateActivity.this)) {
                                                  bttitijiao();
                                              } else {
                                                  DianTool.showTextToast(VacateActivity.this, "请检查网络");
                                              }
                                          }
                                          break;
                                  }
                              }
                          });
                          dialog.show();
                      } else {
                          DianTool.showTextToast(VacateActivity.this, "请填写完整信息后提交");
                      }
                  } else {
                      DianTool.showTextToast(VacateActivity.this, "请添加正常的日期时间");
                  }
              }
              break;
      }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            orderInfokai = data.getStringExtra(CalendarSelectorActivity.ORDER_DAY);
            kaidate.setText(orderInfokai);
            tvjieshu.setText("请选择节数");
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            ArrayList<String> arg= data.getStringArrayListExtra("names");
             agr=data.getIntegerArrayListExtra("ids");
            StringBuffer stringBuffer=new StringBuffer();
            for (int i=0;i<arg.size();i++){
                stringBuffer.append(arg.get(i)+" ");
            }
            if(arg.size()==0){
                tvjieshu.setText("请选择节数");
            }else {
                tvjieshu.setText(stringBuffer);
            }
        }else if(requestCode == 3 && resultCode == RESULT_OK){
            tvjieshu.setText("请选择节数");
            orderInfoend = data.getStringExtra("enddate");
            kaienddate.setText(orderInfoend);
        }else if (resultCode == RESULT_OK && requestCode == 102) {
            if (arrl.size()>=3){
                DianTool.showTextToast(this,"图片不能超过3张");
            }else {
                arrl.add(imageFilePath);
                count = arrl.size() + 1;
                GridViewAdapter adapter = new GridViewAdapter(this,
                        arrl, count, bmp, this);
                gridView.setAdapter(adapter);
            }
        }else if (requestCode == 101 &&resultCode == RESULT_OK){
            crop();
        }else if(requestCode == 103 && data != null){
            if (arrl.size()>=3){
                DianTool.showTextToast(this,"图片不能超过3张");
            }else {
                arrl.add(file.getPath());
                count = arrl.size() + 1;
                GridViewAdapter adapter = new GridViewAdapter(this,
                        arrl, count, bmp, this);
                gridView.setAdapter(adapter);
            }
        }
        if (!kaidate.getText().toString().equals(str)){
            if(!TextUtils.isEmpty(orderInfoend)){
                gongtian.setVisibility(View.VISIBLE);
                try {
                    Date date=matter1.parse(orderInfoend);
                    Date date1=matter1.parse(orderInfokai);
                    long lo=date.getTime()-date1.getTime();
                    if(lo>0) {
                        log = lo / 3600000 / 24;
                        long lgo=log+1;
                        gongtian.setText("共" + lgo + "天");
                        if(log>0){
                            lijieshu.setVisibility(View.GONE);
                            vwjieshu.setVisibility(View.GONE);
                        }else{
                            lijieshu.setVisibility(View.VISIBLE);
                            vwjieshu.setVisibility(View.VISIBLE);
                        }
                    }else if(lo==0){
                        gongtian.setText("共1天");
                        lijieshu.setVisibility(View.VISIBLE);
                        vwjieshu.setVisibility(View.VISIBLE);
                    }else {
                        DianTool.showTextToast(this, "请正确填写日期");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Date date=matter1.parse(str);
                    Date date1=matter1.parse(orderInfokai);
                    long lo=date.getTime()-date1.getTime();
                    if(lo>0) {
                        log = lo / 3600000 / 24;
                        long lgo=log+1;
                        gongtian.setText("共" + lgo + "天");
                        if(log>0){
                            lijieshu.setVisibility(View.GONE);
                            vwjieshu.setVisibility(View.GONE);
                        }else{
                            lijieshu.setVisibility(View.VISIBLE);
                            vwjieshu.setVisibility(View.VISIBLE);
                        }
                    }else if(lo==0){
                        gongtian.setText("共1天");
                        lijieshu.setVisibility(View.VISIBLE);
                        vwjieshu.setVisibility(View.VISIBLE);
                    }else {
                        DianTool.showTextToast(this, "请正确填写日期");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if(!TextUtils.isEmpty(orderInfoend)){
                gongtian.setVisibility(View.VISIBLE);
                try {
                    Date date=matter1.parse(orderInfoend);
                    Date date1=matter1.parse(str);
                    long lo=date.getTime()-date1.getTime();
                    if(lo>0) {
                        log = lo / 3600000 / 24;
                        long lgo=log+1;
                        gongtian.setText("共" + lgo + "天");
                        if(log>0){
                            lijieshu.setVisibility(View.GONE);
                            vwjieshu.setVisibility(View.GONE);
                        }else{
                            lijieshu.setVisibility(View.VISIBLE);
                            vwjieshu.setVisibility(View.VISIBLE);
                        }
                    }else if(lo==0){
                        gongtian.setText("共1天");
                        lijieshu.setVisibility(View.VISIBLE);
                        vwjieshu.setVisibility(View.VISIBLE);
                    }else {
                        DianTool.showTextToast(this, "请正确填写日期");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!kaienddate.getText().toString().equals(str)){
            if(!TextUtils.isEmpty(orderInfokai)){
                try {
                    Date date=matter1.parse(orderInfoend);
                    Date date1=matter1.parse(orderInfokai);
                    long lo=date.getTime()-date1.getTime();
                    if(lo>0) {
                        log = lo / 3600000 / 24;
                        long lgo=log+1;
                        gongtian.setText("共" + lgo + "天");
                        if(log>0){
                            lijieshu.setVisibility(View.GONE);
                            vwjieshu.setVisibility(View.GONE);
                        }else{
                            lijieshu.setVisibility(View.VISIBLE);
                            vwjieshu.setVisibility(View.VISIBLE);
                        }
                    }else if(lo==0){
                        gongtian.setText("共1天");
                        lijieshu.setVisibility(View.VISIBLE);
                        vwjieshu.setVisibility(View.VISIBLE);
                    }else{
                        DianTool.showTextToast(this, "请正确填写日期");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void crop(){
        Intent intent = new Intent("com.android.camera.action.CROP");
        file = new FileStorage().createCropFile();
        Uri outputUri = Uri.fromFile(file);//缩略图保存地址
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 5);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 500);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, 103);

    }
    private File getFile(String path){
        File f=new File(path);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
    void  bttitijiao(){
        HttpParams requestParams=new HttpParams();
        List<File> list = new ArrayList<>();
        DianTool.showMyDialog(this, "");
        if(arrl.size()!=0) {
            for (int i = 0; i < arrl.size(); i++) {
                Bitmap bp=DianTool.getDiskBitmap(arrl.get(i));
                File file = getFile(arrl.get(i));
                DianTool.saveBitmapFile(bp,file);
                list.add(file);
            }
            requestParams.putFileParams("file", list);
        requestParams.put("isLeaveSchoole", lixiao);
            if(log>0){
                if (lijieshu.getVisibility()==View.GONE) {
                    requestParams.put("requestType", "day");
                    requestParams.put("startDate", kaidate.getText().toString());
                    requestParams.put("endDate", kaienddate.getText().toString());
                    String content = tvlixiaoyuanyin.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        requestParams.put("content", content);
                    } else {
                        requestParams.put("content", "");
                    }
                    headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                    OkGo.post(HostAdress.getZheRe("/api/phone/v1/students/requestLeaveAddPic")).tag(this)
                            .headers(headers).params(requestParams).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this, "提交成功");
                            Intent intent = new Intent(VacateActivity.this, EntrtyActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this, "提交失败,请重新提交");
                            DianTool.response(response, VacateActivity.this);
                        }
                    });
                }else{
                    requestParams.put("requestType", "period");
                    if (tvjieshu.getText().toString().equals("请选择节数")) {
                        DianTool.dismissProgressDialog();
                        DianTool.showTextToast(this, "请填写完整信息后提交");
                    } else {
                        int[] idsd = new int[agr.size()];
                        for (int i = 0; i < agr.size(); i++) {
                            idsd[i] = agr.get(i);
                        }
                        Arrays.sort(idsd);
                        requestParams.put("startPeriodId", idsd[0]);
                        requestParams.put("endPeriodId", idsd[idsd.length - 1]);
                        requestParams.put("startDate", kaidate.getText().toString());
                        requestParams.put("endDate", kaienddate.getText().toString());
                        String content = tvlixiaoyuanyin.getText().toString().trim();
                        if (!TextUtils.isEmpty(content)) {
                            requestParams.put("content", content);
                        } else {
                            requestParams.put("content", "");
                        }
                        OkGo.post(HostAdress.getZheRe("/api/phone/v1/students/requestLeaveAddPic")).tag(this)
                                .headers(headers).params(requestParams).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(VacateActivity.this, "提交成功");
                                Intent intent=new Intent(VacateActivity.this, EntrtyActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(VacateActivity.this,"提交失败,请重新提交");
                                DianTool.response(response,VacateActivity.this);
                            }
                        });
                    }
                }
            } else {
                requestParams.put("requestType", "period");
                if (tvjieshu.getText().toString().equals("请选择节数")) {
                    DianTool.dismissProgressDialog();
                    DianTool.showTextToast(this, "请填写完整信息后提交");
                } else {
                    int[] idsd = new int[agr.size()];
                    for (int i = 0; i < agr.size(); i++) {
                        idsd[i] = agr.get(i);
                    }
                    Arrays.sort(idsd);
                    requestParams.put("startPeriodId", idsd[0]);
                    requestParams.put("endPeriodId", idsd[idsd.length - 1]);
                    requestParams.put("startDate", kaidate.getText().toString());
                    requestParams.put("endDate", kaienddate.getText().toString());
                    String content = tvlixiaoyuanyin.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        requestParams.put("content", content);
                    } else {
                        requestParams.put("content", "");
                    }
                    OkGo.post(HostAdress.getZheRe("/api/phone/v1/students/requestLeaveAddPic")).tag(this)
                            .headers(headers).params(requestParams).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this, "提交成功");
                            Intent intent=new Intent(VacateActivity.this, EntrtyActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this,"提交失败,请重新提交");
                            DianTool.response(response,VacateActivity.this);
                        }
                    });
                }
            }
        }else{
            requestParams.put("isLeaveSchoole", lixiao);
            if(log>0){
                if (lijieshu.getVisibility()==View.GONE) {
                    requestParams.put("requestType", "day");
                    requestParams.put("startDate", kaidate.getText().toString());
                    requestParams.put("endDate", kaienddate.getText().toString());
                    String content = tvlixiaoyuanyin.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        requestParams.put("content", content);
                    } else {
                        requestParams.put("content", "");
                    }
                    headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                    OkGo.post(HostAdress.getZheRe("/api/phone/v1/students/requestleave")).tag(this)
                            .headers(headers).params(requestParams).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this, "提交成功");
                            Intent intent = new Intent(VacateActivity.this, EntrtyActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this, "提交失败,请重新提交");
                            DianTool.response(response, VacateActivity.this);
                        }
                    });
                }else{
                    requestParams.put("requestType", "period");
                    if (tvjieshu.getText().toString().equals("请选择节数")) {
                        DianTool.dismissProgressDialog();
                        DianTool.showTextToast(this, "请填写完整信息后提交");
                    } else {
                        int[] idsd = new int[agr.size()];
                        for (int i = 0; i < agr.size(); i++) {
                            idsd[i] = agr.get(i);
                        }
                        Arrays.sort(idsd);
                        requestParams.put("startPeriodId", idsd[0]);
                        requestParams.put("endPeriodId", idsd[idsd.length - 1]);
                        requestParams.put("startDate", kaidate.getText().toString());
                        requestParams.put("endDate", kaienddate.getText().toString());
                        String content = tvlixiaoyuanyin.getText().toString().trim();
                        if (!TextUtils.isEmpty(content)) {
                            requestParams.put("content", content);
                        } else {
                            requestParams.put("content", "");
                        }
                        OkGo.post(HostAdress.getZheRe("/api/phone/v1/students/requestleave")).tag(this)
                                .headers(headers).params(requestParams).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(VacateActivity.this, "提交成功");
                                Intent intent=new Intent(VacateActivity.this, EntrtyActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(VacateActivity.this,"提交失败,请重新提交");
                                DianTool.response(response,VacateActivity.this);
                            }
                        });
                    }
                }
            } else {
                requestParams.put("requestType", "period");
                if (tvjieshu.getText().toString().equals("请选择节数")) {
                    DianTool.dismissProgressDialog();
                    DianTool.showTextToast(this, "请填写完整信息后提交");
                } else {
                    int[] idsd = new int[agr.size()];
                    for (int i = 0; i < agr.size(); i++) {
                        idsd[i] = agr.get(i);
                    }
                    Arrays.sort(idsd);
                    requestParams.put("startPeriodId", idsd[0]);
                    requestParams.put("endPeriodId", idsd[idsd.length - 1]);
                    requestParams.put("startDate", kaidate.getText().toString());
                    requestParams.put("endDate", kaienddate.getText().toString());
                    String content = tvlixiaoyuanyin.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        requestParams.put("content", content);
                    } else {
                        requestParams.put("content", "");
                    }
                    OkGo.post(HostAdress.getZheRe("/api/phone/v1/students/requestleave")).tag(this)
                            .headers(headers).params(requestParams).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this, "提交成功");
                            Intent intent=new Intent(VacateActivity.this, EntrtyActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            DianTool.showTextToast(VacateActivity.this,"提交失败,请重新提交");
                            DianTool.response(response,VacateActivity.this);
                        }
                    });
                }
            }
        }
    }
   void showDailog(){
       new ActionSheetDialog(this)
               .builder()
               .setCancelable(false)
               .setCanceledOnTouchOutside(true)
               .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Green,
                       new ActionSheetDialog.OnSheetItemClickListener() {
                           @Override
                           public void onClick(int which) {
                               photo();
                           }
                       })
               .addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Green,
                       new ActionSheetDialog.OnSheetItemClickListener() {
                           @Override
                           public void onClick(int which) {
                               gallery();
                           }
                       })
               .show();

   }
    @TargetApi(Build.VERSION_CODES.M)
    void photo(){
        if (DianTool.getsdkbanbe()>22){
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasWriteContactsPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED||
                    hasWriteContactsPermissions!= PackageManager.PERMISSION_GRANTED) {
                VacateActivity activity=this;
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1001);
                return;
            }
        }
        if (DianTool.getsdkbanbe()<24) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            imageFilePath = Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
            temp = new File(imageFilePath);
            Uri imageFileUri = Uri.fromFile(temp);// 获取文件的Uri
            Intent it = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
            it.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(it, 102);
        }else{
            openCamera();
        }
    }
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
            imageUri = FileProvider.getUriForFile(this, "com.dingli.diandians.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, 101);
    }
    @TargetApi(Build.VERSION_CODES.M)
    void gallery(){
        if (DianTool.getsdkbanbe()>22){
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            VacateActivity activity=this;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
            return;
        }
        }
        Intent intent = new Intent(this,
                ImgFileListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意使用write
                Intent intent = new Intent(this,
                                 ImgFileListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }else{
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储");
            }
        }else if(requestCode==1001){
            if (permissions[0].equals(Manifest.permission.CAMERA)&&grantResults[0]==PackageManager.PERMISSION_GRANTED
                    &&permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                if (DianTool.getsdkbanbe()<24){
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        return;
                    }
                    imageFilePath = Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath()
                            + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                    temp = new File(imageFilePath);
                    Uri imageFileUri = Uri.fromFile(temp);// 获取文件的Uri
                    Intent it = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
                    it.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
                    startActivityForResult(it, 102);
                }else{
                    openCamera();
                }
            }else{
                quanxian(Manifest.permission.CAMERA,"摄像头");
            }
        }
    }
    void quanxian(String string,String s){
        if (zizengs==0) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, string)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("该功能需要赋予访问" + s + "的权限，不开启将无法正常工作！")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                zizengs=1;
                return;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
            if (DianApplication.user.alist != null) {
                listfile.addAll(DianApplication.user.alist);
                for (int i=0;i<listfile.size();i++){
                    if(!arrl.contains(listfile.get(i))){
                        arrl.add(listfile.get(i));
                    }else{
                        listfile.remove(i);
                    }
                }
                if (arrl.size() > 3) {
                    DianTool.showTextToast(this, "图片只能选3张");
                    if(DianApplication.user.alist!=null){
                        DianApplication.user.alist.clear();
                    }
                    arrl.removeAll(listfile);
                    if(listfile.size()!=0)listfile.clear();
                } else {
                    count = arrl.size() + 1;
                    GridViewAdapter adapters = new GridViewAdapter(
                            VacateActivity.this, arrl, count, bmp, this);
                    gridView.setAdapter(adapters);
                }
            }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    @Override
    public void close(int position) {
        arrl.remove(position);
        listfile.clear();
        if(DianApplication.user.alist!=null){
            DianApplication.user.alist.clear();
        }
        count=count-1;
        GridViewAdapter  adapters = new GridViewAdapter(
                VacateActivity.this,arrl, count, bmp,this);
        gridView.setAdapter(adapters);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
            arrl.clear();
            if(DianApplication.user.alist!=null) {
                DianApplication.user.alist.clear();
            }
            finish();
            overridePendingTransition(R.anim.activity_vertical_enter,R.anim.activity_vertical_exit);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class GridViewItemOnClick2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            if (position + 1 == count) {
                   listfile.clear();
                    showDailog();
            }
        }
    }
}
