package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.AppointmentParentPagerAdapter;
import com.example.user.legaldesire.adapters.UseraAppointmentParentPagerAdapter;

public class UserAppointmetnParentFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private UseraAppointmentParentPagerAdapter appointmentParentPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_appointment_parent, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        appointmentParentPagerAdapter = new UseraAppointmentParentPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(appointmentParentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Toast.makeText(getContext(),"IN HERE",Toast.LENGTH_SHORT).show();
        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
