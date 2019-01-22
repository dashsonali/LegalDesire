package com.example.user.legaldesire.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.user.legaldesire.fragments.BookmarkFragment;
import com.example.user.legaldesire.fragments.HomeFragment;
import com.example.user.legaldesire.fragments.LawyerRecycler;
import com.example.user.legaldesire.fragments.LearnLaw;
import com.example.user.legaldesire.fragments.UserMenuFragment;
import com.example.user.legaldesire.fragments.UserProfileFrag;

import java.util.HashMap;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final HashMap<String,Fragment> fragmentHashMap = new HashMap<>();
    @Override
    public Fragment getItem(int position) {
       Fragment selectedFragment = null;

       if(position == 0)
       {
           Log.e("Position:",String.valueOf(position));
           selectedFragment = fragmentHashMap.get("user_menu");
           if(selectedFragment==null)
           {
               selectedFragment = new UserMenuFragment();
               fragmentHashMap.put("user_menu",selectedFragment);

           }
       }else if(position == 1){
           Log.e("Position:",String.valueOf(position));
           selectedFragment = fragmentHashMap.get("bookmarks");
           if(selectedFragment==null){
               selectedFragment = new BookmarkFragment();
               fragmentHashMap.put("bookmarks",selectedFragment);
           }

       }else if(position == 2)
       {
           Log.e("Position:",String.valueOf(position));
           selectedFragment = fragmentHashMap.get("lawyer_recycler");
           if(selectedFragment==null){
               selectedFragment = new LawyerRecycler();
               fragmentHashMap.put("lawyer_recycler",selectedFragment);
           }

       }
       return  selectedFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "test";
        if(position == 0)
        {
           title = "Menu";
        }else if(position == 1){
            title = "Bookmarks";
        }else if(position == 2)
        {
            title = "Favorite Lawyers";
        }

        return title;
    }
}
