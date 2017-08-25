package com.dingli.diandians.found;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.dingli.diandians.information.InformationFragment;

/**
 * Created by dingliyuangong on 2017/6/7.
 */

public class FoundInfoAdapter extends FragmentPagerAdapter{
    public FoundInfoAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                InformationFragment informationFragment=new InformationFragment();
                return  informationFragment;
            case 1:
                FoundFragment foundFragment=new FoundFragment();
                return foundFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
