package com.dingli.diandians.common;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.dingli.diandians.R;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class DianTool {

    private static Toast toast = null;
    private static ProgressDialog progressDialog;
    private static MyDialog myDialog;
    public static void showProgressDialog(Context context, int msg) {
        progressDialog = new ProgressDialog(context, msg);
        progressDialog.show();
    }
    public static boolean isConnectionNetWork(Context context){
        if (context==null)
            return false;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        } else {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
               DianApplication.user.strNetworkType = "wifi";
            }else{
                DianApplication.user.strNetworkType = "4G";
            }
            DianApplication.sharedPreferences.saveString(Constant.NETWORKTYPE,DianApplication.user.strNetworkType);
            return info.isAvailable();
        }
    }
    public static  boolean iswificonnection(Context context){
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWiFiNetworkInfo!=null){
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }
    public static void showTextToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    public static void showTextLongToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static boolean isPhoneFormat(String number) {
        String format = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(format, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    public static Bitmap readBitMap(Context context,int resId){
        BitmapFactory.Options opt=new BitmapFactory.Options();
        opt.inPreferredConfig=Bitmap.Config.RGB_565;
        opt.inPurgeable=true;
        opt.inInputShareable=true;
        InputStream is=context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
    public static void showMyDialog(Context context,String msg){
//        if (myDialog==null) {
            myDialog = new MyDialog(context,msg);
            myDialog.setCancelable(false);
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.show();
//        }
//        myDialog.waiting(msg,true);
    }
    public static void dissMyDialog(){
        if(myDialog!=null){
            myDialog.dismiss();
            myDialog=null;
        }
    }
    /**
     * 字符串为数字
     */
    public static boolean isNumber(String number) {
        if (TextUtils.isEmpty(number))
            return false;
        else {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(number);
            if (m.matches())
                return true;
            else
                return false;
        }
    }
    /**
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }
    public static boolean isFirstEnter(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))return false;
        String mResultStr = context.getSharedPreferences(Constant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(Constant.KEY_GUIDE_ACTIVITY, "");
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }
    public static void share(Context context){
        SharedPreferences settings = context.getSharedPreferences(Constant.SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.KEY_GUIDE_ACTIVITY, "false");
        editor.commit();
    }
    public static void shareno(Context context){
        SharedPreferences settings = context.getSharedPreferences(Constant.SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.KEY_GUIDE_ACTIVITY, "true");
        editor.commit();
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isduxie(Activity activity,Context context){
        int hasWriteContactsPermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
            return true;
        }
        return  false;
    }
    public static  int getsdkbanbe(){
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
    public static void getWebViewSet(WebView wbv){
        WebSettings webSettings = wbv.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setSupportZoom(false);
        webSettings.setTextZoom(100);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings .setDomStorageEnabled(true);
        webSettings .setAppCacheMaxSize(1024*1024*8);
        webSettings .setAllowFileAccess(true);
        webSettings .setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }
    public static void kongtoken(){
        DianApplication.user.token = "";
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITONE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, "");
    }
    public static void huoqutoken(){
        DianApplication.user.token_type=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
        DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE)+"-"+
                DianApplication.sharedPreferences.getStringValue(Constant.SPLITTWO)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITTHREE)
                +"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFOUR)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFIVE);
    }
    public static int suijishu(){
        int chars = 0;
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        char[] c = s.toCharArray();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            chars = random.nextInt(c.length);
        }
        return  chars;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static void genxin(Context context){
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("https://www.pgyer.com/amCc");
        intent.setData(content_url);
        context.startActivity(intent);
    }
    public static void response(Response response,Context context){
        if (response!=null) {
            if (response.code() == 401) {
                Initoken.initoken(context);
            }
        }
    }
    public static void refresh(VerticalSwipeRefreshLayout refresh,Context context){
        refresh.setColorSchemeColors(context.getResources().getColor(R.color.holo_blue_bright),
                context.getResources().getColor(R.color.holo_green_light),
                context.getResources().getColor(R.color.holo_orange_light),
                context.getResources().getColor(R.color.holo_red_light));
    }
    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        Bitmap bMapRotate = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                opt.inTempStorage = new byte[1024 * 1024 * 10];
                long length = file.length();
                if (length / (1024 * 1024) > 4) {
                    opt.inSampleSize = 16;
                } else if (length / (1024 * 1024) >= 1) {
                    opt.inSampleSize = 8;
                } else if (length / (1024 * 512) >= 1) {
                    opt.inSampleSize = 4;
                } else if (length / (1024 * 256) >= 1) {
                    opt.inSampleSize = 2;
                } else {
                    opt.inSampleSize = 1;
                }
                bitmap = BitmapFactory.decodeFile(pathString, opt);
                int orientation = readPictureDegree(pathString);
                if (orientation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);
                    bMapRotate = Bitmap
                            .createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                    bitmap.getHeight(), matrix, true);
                } else {
                    bMapRotate = Bitmap.createScaledBitmap(bitmap,
                            bitmap.getWidth(), bitmap.getHeight(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bMapRotate != null) {
            return bMapRotate;
        }
        return bitmap;
    }
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public  static void saveBitmapFile(Bitmap bitmap,File file){
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static List<String> weekday(){
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            List<String> list=new ArrayList<>();
            Calendar c = Calendar.getInstance();
            // 今天是一周中的第几天
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK );

            if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            // 计算一周开始的日期
            c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

            for (int i=1;i<=7;i++) {
                c.add(Calendar.DAY_OF_MONTH, 1);
               list.add(sdf.format(c.getTime()));
            }
            return list;
        }
    public  static List<String> getLastTimeInterval(int date) {
        List<String> list=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
       for (int i=1;i<=7;i++){
           Calendar calendar1 = Calendar.getInstance();
           int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
            int offset=i-dayOfWeek;
            calendar1.add(Calendar.DATE,offset-(7*date));
            list.add(sdf.format(calendar1.getTime()));
        }
        return list;
    }
    public  static List<String> getNextTimeInterval(int date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        List<String> list=new ArrayList<>();
        for (int i=1;i<=7;i++){
            Calendar calendar1 = Calendar.getInstance();
            int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
            int offset=i-dayOfWeek;
            calendar1.add(Calendar.DATE, offset +(7*date));
            list.add(sdf.format(calendar1.getTime()));
        }
        return list;
    }
    /**
     * @param path
     * @return
     */
    public static String getPath(String path){
        File f=new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }

}
