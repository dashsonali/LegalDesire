package com.example.user.legaldesire.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.legaldesire.fragments.LawyerAppointmentFragment;
import com.example.user.legaldesire.fragments.LawyerCasesFragment;

import java.util.HashMap;

public class AppointmentParentPagerAdapter extends FragmentPagerAdapter {
    private final HashMap<String,Fragment> fragmentHashMap = new HashMap<>();
    public AppointmentParentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment = null;
        switch (position)
        {
            case 0:
                selectedFragment = fragmentHashMap.get("lawyer_appointment");
                if(selectedFragment==null)
                {
                    selectedFragment = new LawyerAppointmentFragment();
                    fragmentHashMap.put("lawyer_appointment",selectedFragment);

                }
                break;
            case 1:
                selectedFragment = fragmentHashMap.get("lawyer_cases");
                if(selectedFragment==null)
                {
                    selectedFragment = new LawyerCasesFragment();
                    fragmentHashMap.put("lawyer_cases",selectedFragment);

                }
                break;
        }

        return selectedFragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position==0)
        {
            title = "Requests";
        }else {
            title = "My Cases";
        }
        return title;
    }
}
