package com.dingli.diandians.personcenter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandians.R;
import com.dingli.diandians.bean.FileStorage;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.HostAdress;
import com.dingli.diandians.common.ResultInfo;
import com.dingli.diandians.information.EntrtyActivity;
import com.dingli.diandians.login.CircleImageView;
import com.dingli.diandians.setting.HelpActivity;
import com.dingli.diandians.setting.SettingActivity;
import com.dingli.diandians.survey.WebViewSurveyActivity;
import com.dingli.diandians.yichangnv.YiChangNvActivity;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/3/9.
 */
public class PersoncenterFragment extends Fragment implements View.OnClickListener{

    private static final String PHOTO_FILE_NAME = "photo.jpg";
    private static final String PHOTO_FILE_PATH = getPath(Environment.getExternalStorageDirectory()+"/"+"diandian");
    File tempFile;
    private View personcenterView;
    CircleImageView personci;
    TextView tvmingzi;
    ImageView rlson;
    Paint paint;
    TextView tvmingzix;
    HttpHeaders headers;
    int zizengs;
    int photo,phone;
    int chenggong;
    Uri imageUri;
    File file;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        personcenterView=inflater.inflate(R.layout.activity_personcenter,container,false);
        paint = new Paint();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        init(personcenterView);
        tempFile = getFile(PHOTO_FILE_PATH+"/"+PHOTO_FILE_NAME);
        return personcenterView;
    }
    void incenter(){
        if(DianTool.isConnectionNetWork(getActivity())) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/user/info")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (TextUtils.isEmpty(s)) {
                        DianTool.showTextToast(getActivity(), getResources().getString(R.string.wushuju));
                    } else {
                        if (!s.equals("{}")) {
                            ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                            tvmingzi.setText(resultInfo.name);
                            tvmingzix.setText(resultInfo.personId);
                            if (!TextUtils.isEmpty(resultInfo.avatar)) {
                                if (!resultInfo.avatar.equals("null")) {
                                    Glide.with(getActivity()).load(resultInfo.avatar).into(personci);
                                    DianApplication.sharedPreferences.saveString("phone", resultInfo.avatar);
                                }
                            }
                            DianApplication.sharedPreferences.saveString("personId", resultInfo.personId);
                            DianApplication.sharedPreferences.saveString("mingName", resultInfo.name);
                        }
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.response(response,getActivity());
                }
            });
        }else{
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }
    public void init(View v){
        tvmingzix=(TextView)v.findViewById(R.id.tvmingzix);
        rlson=(ImageView)v.findViewById(R.id.rlson);
        tvmingzi=(TextView)v.findViewById(R.id.tvmingzi);
        LinearLayout llwoqing=(LinearLayout)v.findViewById(R.id.llwoqing);
        LinearLayout llwoyichang=(LinearLayout)v.findViewById(R.id.llwoyichang);
        LinearLayout llsurvey=(LinearLayout)v.findViewById(R.id.llsurvey);
        llwoqing.setOnClickListener(this);
        llwoyichang.setOnClickListener(this);
        llsurvey.setOnClickListener(this);
        LinearLayout ll_sett=(LinearLayout)v.findViewById(R.id.ll_sett);
        personci=(CircleImageView)v.findViewById(R.id.personci);
        ll_sett.setOnClickListener(this);
        LinearLayout ll_help=(LinearLayout)v.findViewById(R.id.ll_help);
        ll_help.setOnClickListener(this);
        personci.setOnClickListener(this);
    }

    /**
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void gallery() {
        if (DianTool.getsdkbanbe()>22){
            chenggong=1;
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1002);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1002){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意使用write
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Constant.PHOTO_REQUEST_GALLERY);
            }else{
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储");
            }
        }else if (requestCode==1001){
            if (permissions[0].equals(Manifest.permission.CAMERA)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                if (DianTool.getsdkbanbe()<24) {
                    if (DianTool.hasSdcard()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
                    }
                }else{
                    openCamera();
                }
            }else{
                quanxian(Manifest.permission.CAMERA,"摄像头");
            }
        }
    }
    void  quanxian(String string,String s){
        if (zizengs==0) {
            if (!shouldShowRequestPermissionRationale(string)) {
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
    @Override
    public void onResume() {
        super.onResume();
        if (chenggong!=1) {
            if (photo == 1) {
                photo = 0;
                phone = 0;
                photo();
            }
            if (phone == 1) {
                photo = 0;
                phone = 0;
                gallery();
            }
        }
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue("mingName"))){
            if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue("phone"))) {
                Glide.with(DianApplication.getInstance()).load(DianApplication.sharedPreferences.getStringValue("phone")).into(personci);
            }
            if(null!=tvmingzi){
                tvmingzi.setText(DianApplication.sharedPreferences.getStringValue("mingName"));
            }
            if(null!=tvmingzix){
                tvmingzix.setText(DianApplication.sharedPreferences.getStringValue("personId"));
            }
        }
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            incenter();
        }
    }
    /**
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void photo() {
        if (DianTool.getsdkbanbe()>22) {
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
            int hasWriteContactsPermissions = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED||
                    hasWriteContactsPermissions!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1001);
                return;
            }
        }
        chenggong=1;
        if (DianTool.getsdkbanbe()<24) {
            if (DianTool.hasSdcard()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
            }
        }else{
            openCamera();
        }
    }
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
            imageUri = FileProvider.getUriForFile(getActivity(), "com.dingli.diandians.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, Constant.PHOTO_REQUEST_CAMERA);
    }
    /**
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constant.PHOTO_REQUEST_GALLERY && data != null){
            crop(data.getData(), Uri.fromFile(tempFile));
        }else if(requestCode == Constant.PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK ){
            crop(Uri.fromFile(tempFile), Uri.fromFile(tempFile));
        }else if(requestCode == Constant.PHOTO_REQUEST_CUT && data != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                personci.setImageBitmap(bitmap);
                upfile(file);
            }else {
                Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(tempFile));//decode bitmap
//             Bitmap bitmap2 = BlurUtils.decodeBitmapToBlur(getActivity(), bitmap, 20);
//            rlson.setImageBitmap(bitmap2);
                personci.setImageBitmap(bitmap);
                upfile(tempFile);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    void upfile(final File file){
        List<File> list=new ArrayList<>();
        list.add(file);
        HttpParams params=new HttpParams();
        params.putFileParams("file", list);
        if (DianTool.isConnectionNetWork(getActivity())) {
            OkGo.post(HostAdress.getRequest("/api/phone/v1/user/avatar")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.showTextToast(getActivity(), "上传成功");
                    incenter();
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                }
            });
        }else{
            DianTool.showTextToast(getActivity(),"请检查网络");
        }
    }
    /**
     */
    private void crop(Uri uri,Uri cutImgUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            file = new FileStorage().createCropFile();
            Uri outputUri = Uri.fromFile(file);//缩略图保存地址
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }else {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cutImgUri);
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 5);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 500);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, Constant.PHOTO_REQUEST_CUT);
    }
    /**
     * @param path
     * @return
     */
    private static String getPath(String path){
        File f=new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }
    /**
     * @param path
     * @return
     */
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
    /**
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llwoqing:
                Intent intentt=new Intent(getActivity(), EntrtyActivity.class);
                startActivity(intentt);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.llwoyichang:
                Intent intent=new Intent(getActivity(), YiChangNvActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.llsurvey:
                Intent intent1= new Intent(getActivity(), WebViewSurveyActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url",Constant.webdiaocha+"/mobileui/questatuslist");
                intent1.putExtras(bundle);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.ll_sett:
                Intent intents = new Intent(getActivity(), SettingActivity.class);
                startActivity(intents);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.ll_help:
                Intent intented = new Intent(getActivity(), HelpActivity.class);
                startActivity(intented);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.personci:
                new ActionSheetDialog(getActivity())
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        photo=1;
                                        photo();
                                    }
                                })
                        .addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        phone=1;
                                        gallery();
                                    }
                                })
                        .show();
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
