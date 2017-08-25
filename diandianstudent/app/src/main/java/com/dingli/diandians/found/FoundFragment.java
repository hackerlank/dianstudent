package com.dingli.diandians.found;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.MainActivity;
import com.dingli.diandians.R;
import com.dingli.diandians.bean.CourseData;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.Course;
import com.dingli.diandians.common.Coursecenter;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.Result;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.common.ResultInfoCall;
import com.dingli.diandians.personcenter.ActionSheetDialog;
import com.dingli.diandians.survey.WebViewSurveyActivity;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.dingli.diandians.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static anetwork.channel.http.NetworkSdkSetting.context;

/**
 * Created by dingliyuangong on 2016/6/12.
 */
public class FoundFragment extends Fragment implements XListView.IXListViewListener,ListContactView.ContactListener{

    ContactAdapter adapter;
    int currentpage=1;
    VerticalSwipeRefreshLayout reffound;
    int zizengs;
    String phones,names,stuId;
    XListView xlvcontact;
   int call,contact,resume;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View foundview=inflater.inflate(R.layout.fragment_found,container,false);
        initview(foundview);
        reffound.setRefreshing(true);
        initdata(1, Constant.REFRESH);
        return foundview;
    }
    @TargetApi(Build.VERSION_CODES.M)
    void initview(View v){
        reffound=(VerticalSwipeRefreshLayout) v.findViewById(R.id.reffound);
        reffound.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        xlvcontact=(XListView)v.findViewById(R.id.xlvcontact);
        adapter=new ContactAdapter(getActivity(),this);
        xlvcontact.setAdapter(adapter);
        xlvcontact.setXListViewListener(this);
        xlvcontact.setPullRefreshEnable(true);
        xlvcontact.setPullLoadEnable(true);
    }
    void initdata(int page, final int style){
        if (DianTool.isConnectionNetWork(getActivity())){
            HttpHeaders headers=new HttpHeaders();
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            HttpParams params=new HttpParams();
            params.put("offset",page);
            currentpage=page;
            OkGo.get(HostAdress.getRequest("/api/phone/v1/student/class/getPhone")).headers(headers).params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            onnorefresh();
                            xlvcontact.stopLoadMore();
                            xlvcontact.stopRefresh();
                            if (!TextUtils.isEmpty(s)){
                                if (!s.equals("{}")) {
                                    Coursecenter result = JSON.parseObject(s, Coursecenter.class);
                                    if (result.data.size() != 0) {
                                        switch (style) {
                                            case Constant.REFRESH:
                                                adapter.refreshListContact(result.data);
                                                adapter.notifyDataSetChanged();
                                                break;
                                            case Constant.LOAD_MORE:
                                                adapter.addListContact(result.data);
                                                adapter.notifyDataSetChanged();
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            onnorefresh();
                            xlvcontact.stopLoadMore();
                            xlvcontact.stopRefresh();
                        }
                    });
        }else{
            onnorefresh();
            xlvcontact.stopLoadMore();
            xlvcontact.stopRefresh();
            DianTool.showTextToast(getActivity(),"请检查网络");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onRefresh() {
        onnorefresh();
        initdata(1, Constant.REFRESH);
    }
   void onnorefresh(){
       reffound.setRefreshing(false);
       reffound.setEnabled(false);
   }
    @Override
    public void onLoadMore() {
        onnorefresh();
        initdata(++currentpage, Constant.LOAD_MORE);
    }

    @Override
    public void onContactListener(String tv,final String phone,final String name,final String stuIds) {
        if (!TextUtils.isEmpty(tv)) {
            phones = phone;
            names = name;
            stuId = stuIds;
            new ActionSheetDialog(getActivity())
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拨打电话", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    call = 1;
                                    callphone(phone, stuIds);
                                }
                            })
                    .addSheetItem("保存到手机通讯录", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    contact = 1;
                                    baocuncontact(phone);
                                }
                            })
                    .addSheetItem("复制", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboardManager.setText(phone);
                                }
                            })
                    .show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
//        if (resume!=1) {
//            if (call == 1) {
//                contact = 0;
//                call = 0;
//                callphone(phones, stuId);
//            }
//        }
//            if (contact == 1) {
//                contact = 0;
//                call = 0;
//                baocuncontact(phones);
//            }
    }

    void zhiding(){
        Intent addIntent = new Intent(Intent.ACTION_INSERT,Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
        addIntent.setType("vnd.android.cursor.dir/person");
        addIntent.setType("vnd.android.cursor.dir/contact");
        addIntent.setType("vnd.android.cursor.dir/raw_contact");
        addIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phones);
        addIntent.putExtra(ContactsContract.Intents.Insert.NAME, names);
        startActivity(addIntent);
    }
    void baoda(String phone,String stuIds){
        if (!phone.equals("null")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
            startActivity(intent);
            if (DianTool.isConnectionNetWork(getActivity())){
                JSONObject jsonObject=new JSONObject();
                HttpHeaders headers=new HttpHeaders();
                DianTool.huoqutoken();
                headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                try {
                    jsonObject.put("calledPhone",phone);
                    jsonObject.put("calledStudentId",stuIds);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (DianTool.isConnectionNetWork(getActivity())) {
                    OkGo.post(HostAdress.getRequest("/api/phone/v1/student/callRecords")).headers(headers).tag(this).upJson(jsonObject)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                }
                            });
                }
            }
        }
    }
        @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002){
            if (permissions[0].equals(Manifest.permission.WRITE_CONTACTS)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&permissions[1].equals(Manifest.permission.READ_CONTACTS)
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED){
                if (!phones.equals("null")) {
                        zhiding();
                    }
            }else{
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_CONTACTS,"保存到通讯录且读取通讯录");
            }
        }else if (requestCode==1004){
            if (permissions[0].equals(Manifest.permission.CALL_PHONE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (!phones.equals("null")) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones));
                        startActivity(intent);
                    }
            }else{
                quanxian(Manifest.permission.CALL_PHONE,"打电话");
            }
        }
    }
    void quanxian(String quanxian,String s){
        if (zizengs==0) {
            if (!shouldShowRequestPermissionRationale(quanxian)) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("该功能需要赋予调用" + s + "的权限，不开启将无法正常工作！")
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
    @TargetApi(Build.VERSION_CODES.M)
    void callphone(String phone, String stuIds){
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        1004);
                return;
            }
        }
        resume=1;
                baoda(phone, stuIds);
    }
    void baocuncontact(String phone){
        if (DianTool.getsdkbanbe()>22){
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_CONTACTS);
            int hasWriteContactsPermissions = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED||
                    hasWriteContactsPermissions != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,
                        },
                        1002);
                return;
            }
        }
        if (!phone.equals("null")) {
            zhiding();
        }
    }
}
