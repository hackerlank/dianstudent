package com.dingli.diandians;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dingli.diandians.firstpage.HybridShouyeFragment;
import com.dingli.diandians.firstpage.ShouyeFragment;
import com.dingli.diandians.found.FoundAndInforFrag;
import com.dingli.diandians.found.FoundFragment;
import com.dingli.diandians.information.InformationFragment;
import com.dingli.diandians.information.WeiDonFragment;
import com.dingli.diandians.information.YiBackFragment;
import com.dingli.diandians.information.YiDoneFragment;
import com.dingli.diandians.newProject.moudle.eye.EyeFragment;
import com.dingli.diandians.personcenter.PersoncenterFragment;


/**
 * Created by dingliyuangong on 2016/8/1.
 */
public class MainAdapter extends FragmentPagerAdapter {
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ShouyeFragment shouyeFragment=ShouyeFragment.newInstance();
                return shouyeFragment;
            case 1:
//                InformationFragment informationFragment=new InformationFragment();
                EyeFragment eyeFragment =   EyeFragment.newInstance(false);
                return  eyeFragment;
            case 2:
                FoundAndInforFrag foundFragment=new FoundAndInforFrag();
                return foundFragment;
            case 3:
                PersoncenterFragment personcenterFragment=new PersoncenterFragment();
                return  personcenterFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
