package com.dingli.diandians.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.RefreshHeaderLayout;
import com.dingli.diandians.R;

/**
 * Created by dingliyuangong on 2017/6/7.
 */

public class FoundAndInforFrag extends Fragment implements View.OnClickListener{
    private ViewPager foundinforview;
    private RelativeLayout relainfor,relainforno,relacontact,relacontactno;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View views = inflater.inflate(R.layout.fragment_foudandinfo, container, false);
        initView(views);
        return views;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FoundInfoAdapter foundInfoAdapter=new FoundInfoAdapter(getChildFragmentManager());
        foundinforview.setAdapter(foundInfoAdapter);

        foundinforview.setCurrentItem(0);
        relainfor.setVisibility(View.VISIBLE);
        relacontact.setVisibility(View.VISIBLE);
        relainforno.setVisibility(View.GONE);
        relacontactno.setVisibility(View.GONE);
    }
    private void initView(View views) {
        foundinforview=(ViewPager) views.findViewById(R.id.foundinforview);
        relacontact=(RelativeLayout) views.findViewById(R.id.relacontact);
        relainfor=(RelativeLayout) views.findViewById(R.id.relainfor);
        relacontactno=(RelativeLayout) views.findViewById(R.id.relacontactno);
        relainforno=(RelativeLayout) views.findViewById(R.id.relainforno);

        relainfor.setOnClickListener(this);
        relacontact.setOnClickListener(this);
        relainforno.setOnClickListener(this);
        relacontactno.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relainforno:
                relainfor.setVisibility(View.VISIBLE);
                relainforno.setVisibility(View.GONE);
                relacontactno.setVisibility(View.GONE);
                relacontact.setVisibility(View.VISIBLE);
                foundinforview.setCurrentItem(0);
                break;
            case R.id.relacontact:
                relainfor.setVisibility(View.GONE);
                relainforno.setVisibility(View.VISIBLE);
                relacontactno.setVisibility(View.VISIBLE);
                relacontact.setVisibility(View.GONE);
               foundinforview.setCurrentItem(1);
                break;
        }
    }
}
