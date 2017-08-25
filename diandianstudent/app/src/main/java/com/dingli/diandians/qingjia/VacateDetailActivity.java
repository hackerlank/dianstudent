package com.dingli.diandians.qingjia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.DianTool;

import java.util.ArrayList;

/**
 * Created by dingliyuangong on 2016/10/24.
 */
public class VacateDetailActivity extends BaseActivity {

//    private ViewPager vpdetail;
     private ImageView iview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacatedetail);
        String path=getIntent().getStringExtra("listfile");
        initView(path);
    }

    private void initView(String path) {
//        vpdetail = (ViewPager) findViewById(R.id.vpdetail);
//        VacateDetailAdapter adapter=new VacateDetailAdapter(getSupportFragmentManager(),alistfile);
//        vpdetail.setAdapter(adapter);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
       Bitmap bm = BitmapFactory.decodeFile(path,options);
        iview=(ImageView)findViewById(R.id.iview);
        iview.setImageBitmap(bm);

    }
}
