package com.dingli.diandians.newProject.moudle.eye;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
/**
 * LWQ
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private FragmentManager manager;
    //Tab名称集合
    private ArrayList<Fragment>           fragmentList;
    public TabAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.manager = fm;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
