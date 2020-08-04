package com.mrwhitehat.compcode;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class Contests extends Fragment {

    TabLayout tab_nav;
    ViewPager pager;

    List<Contests_api> upcoming_contests;
    List<Contests_api> ongoing_contests;
    List<Contests_api> long_contests;

    public Contests(List<Contests_api> ongoing_contests, List<Contests_api> upcoming_contests, List<Contests_api> long_contests) {
        this.ongoing_contests = ongoing_contests;
        this.upcoming_contests = upcoming_contests;
        this.long_contests = long_contests;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contests, container, false);

        pager = view.findViewById(R.id.pager);
        tab_nav = view.findViewById(R.id.tab_nav);
        pager.setAdapter(new TabAdapter(getChildFragmentManager()));
        tab_nav.setupWithViewPager(pager);
        pager.setCurrentItem(1);

        return view;
    }

    class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new Ongoing(ongoing_contests);
                case 1:
                    return new Upcoming(upcoming_contests);
                case 2:
                    return new Long(long_contests);
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Ongoing";
                case 1:
                    return "Upcoming";
                case 2:
                    return "Long";
            }

            return super.getPageTitle(position);
        }
    }

}