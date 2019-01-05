package com.example.user.legaldesire.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.user.legaldesire.fragments.HomeFragment;
import com.example.user.legaldesire.fragments.LawyerRecycler;
import com.example.user.legaldesire.fragments.LearnLaw;
import com.example.user.legaldesire.fragments.UserMenuFragment;
import com.example.user.legaldesire.fragments.UserProfileFrag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final HashMap<String,Fragment> fragmentHashMap = new HashMap<>();
    private final List<Fragment>  mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    @Override
    public Fragment getItem(int position) {



       return  mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {


        return mFragmentTitleList.get(position);
    }
}
