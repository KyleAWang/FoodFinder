package com.wang.kyle.foodfinder.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wang.kyle.foodfinder.fragment.HomeFragment;
import com.wang.kyle.foodfinder.fragment.MyFragment;

/**
 * Created by Kyle on 5/2/2016.
 */
public class MainPageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    public MainPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                MyFragment myFragment = new MyFragment();
                return myFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
