package com.example.user.legaldesire.adapters;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.legaldesire.fragments.LawyerAppointmentFragment;
import com.example.user.legaldesire.fragments.LawyerCasesFragment;
import com.example.user.legaldesire.fragments.LawyerManualCasesFragment;
import com.example.user.legaldesire.fragments.UserAppointmentFragment;
import com.example.user.legaldesire.fragments.UserCasesFragment;

import java.util.HashMap;

public class UseraAppointmentParentPagerAdapter extends FragmentPagerAdapter {
    private final HashMap<String,Fragment> fragmentHashMap = new HashMap<>();
    public UseraAppointmentParentPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment = null;
        switch (position)
        {
            case 0:
                selectedFragment = fragmentHashMap.get("user_appointments");
                if(selectedFragment==null)
                {
                    selectedFragment = new UserAppointmentFragment();
                    fragmentHashMap.put("user_appointments",selectedFragment);

                }
                break;
            case 1:
                selectedFragment = fragmentHashMap.get("user_cases");
                if(selectedFragment==null)
                {
                    selectedFragment = new UserCasesFragment();
                    fragmentHashMap.put("user_cases",selectedFragment);

                }
                break;
        }

        return selectedFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position==0)
        {
            title = "Appointments";
        }else if(position==1) {
            title = "Cases";
        }

        return  title;

    }
}
