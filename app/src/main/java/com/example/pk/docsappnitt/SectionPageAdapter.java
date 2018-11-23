package com.example.pk.docsappnitt;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentPagerAdapter{
    private final List<Fragment>mFragmentList=new ArrayList<>();
    private final List<String>mFragmentTitleList=new ArrayList<>();


    public void addFragment(Fragment fragment, String Title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(Title);

    }
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
