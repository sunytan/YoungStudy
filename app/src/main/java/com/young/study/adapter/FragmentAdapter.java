package com.young.study.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by edz on 2017/7/21.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    public List<Fragment> fragmentList;
    private List<String> titleList;


    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments,List<String> title) {
        super(fm);
        this.fragmentList = fragments;
        this.titleList = title;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return super.getItemPosition(object);
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  titleList.get(position);
    }
}
